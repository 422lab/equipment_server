package com.kystu.equipment.servlet;

import com.dxzc.json.ArrayGen;
import com.dxzc.json.ObjectGen;
import com.kystu.equipment.GetPostTools;
import com.kystu.equipment.Tools;
import com.kystu.equipment.dao.DeviceDao;
import com.kystu.equipment.dao.UserDao;
import com.kystu.equipment.model.*;
import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@WebServlet(name = "UserReserveDevice", value = "/userReserveDevice")
public class UserReserveDeviceServlet extends BaseServlet {

    /**
     * 登录成功返回码.
     */
    public static final int SUCCESS = 0;

    /**
     * 登录失败返回码.
     */
    public static final int ERROR = 1;

    /**
     * 登录参数错误返回码.
     */
    public static final int PARAMETER = 2;

    @Override
    protected void doGetOrPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        GetPostTools tools = new GetPostTools(req);
        ObjectGen json = new ObjectGen();
        Timestamp now = Tools.currentTimestamp();
        try {
            String account = tools.getParameter("account");
            String password = tools.getParameter("password");
            if (account == null || password == null) {
                json.number("code", 1);
                json.string("msg", "account or password");
                return;
            }
            UseReserve reserve;
            try {
                reserve = new UseReserve(JsonParser.parseDoc(tools.getParameter("reserves")));
                if (reserve.start.before(now)) {
                    reserve.start = now;
                }
            } catch (RuntimeException e) {
                json.number("code", 1);
                json.string("msg", "json?" + e.toString());
                return;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reserve.start);
            calendar.add(Calendar.MINUTE, 1);
            if (calendar.getTime().after(reserve.end)) {
                json.number("code", 1);
                json.string("msg", "reserve time");
                return;
            }
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            Timestamp dead = new Timestamp(calendar.getTime().getTime());
            if (dead.before(reserve.start)) {
                json.number("code", 1);
                json.string("msg", "start too late");
            }
            if (dead.before(reserve.end)) {
                reserve.end = dead;
            }

            if (reserve.devices.isEmpty()) {
                json.number("code", 1);
                json.string("msg", "reserve device");
                return;
            }

            try (Connection connection = Tools.getConnection()) {
                connection.setAutoCommit(false);
                UserDao userDao = new UserDao(connection);
                DeviceDao deviceDao = new DeviceDao(connection);

                // 执行用户登录
                User user = userDao.getUserForUpdate(account);
                if (user == null) {
                    json.number("code", 2);
                    return;
                }
                if (!password.equals(user.password)) {
                    json.number("code", 3);
                    return;
                }

                // 执行特点用户类型的相关限制检查
                switch (user.type) {
                    case 0: {
                        // 学生一次只能预约一台设备
                        if (reserve.devices.size() > 1) {
                            json.number("code", 4);
                            return;
                        }
                        // 学生同类型只能预约一种设备
                        if (!user.getReserve().checkOne(reserve.type, reserve.start, reserve.end)) {
                            json.number("code", 4);
                            return;
                        }
                        break;
                    }
                    default:
                        throw new RuntimeException("未知用户类型");
                }

                List<Integer> success = new LinkedList<>();
                List<Integer> fails = new LinkedList<>();
                // 检查全部预约设备可用性
                // 避免死锁
                for (Iterator<Integer> it = reserve.devices.iterator(); it.hasNext(); ) {
                    int deviceUuid = it.next();
                    Device device = deviceDao.getDeviceForUpdate(deviceUuid);
                    // 检查设备类型和可用性
                    if (device.type != reserve.type || !device.check(reserve.start, reserve.end)) {
                        // 不满足就移除
                        it.remove();
                        // 添加失败项
                        fails.add(deviceUuid);
                    } else {
                        DeviceReserve deviceReserve = new DeviceReserve();
                        deviceReserve.user = user.uuid;
                        deviceReserve.start = reserve.start;
                        deviceReserve.end = reserve.end;
                        device.reserves.get().add(deviceReserve);
                        deviceDao.updateDevice(device);
                        connection.commit();
                        // 添加成功项
                        success.add(deviceUuid);
                    }
                }
                user.removeBefore(now);
                user.getReserve().add(reserve);
                userDao.updateUser(user);
                connection.commit();
                json.number("code", 0);
                try (ArrayGen array = json.array("fails")) {
                    for (int f : fails) {
                        array.number(f);
                    }
                }
                try (ArrayGen array = json.array("success")) {
                    for (int f : success) {
                        array.number(f);
                    }
                }
                json.number("start", reserve.start.getTime());
                json.number("end", reserve.end.getTime());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            resp.getWriter().println(json.toJson());
        }
    }
}
