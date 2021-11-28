package com.kystu.equipment.servlet;

import com.dxzc.json.ObjectGen;
import com.kystu.equipment.GetPostTools;
import com.kystu.equipment.Tools;
import com.kystu.equipment.dao.DeviceDao;
import com.kystu.equipment.dao.UserDao;
import com.kystu.equipment.model.Device;
import com.kystu.equipment.model.DeviceReserve;
import com.kystu.equipment.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet(name = "DeviceLink", value = "/deviceLink")
public class DeviceLinkServlet extends BaseServlet {

    @Override
    protected void doGetOrPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        resp.setBufferSize(400);
        GetPostTools tools = new GetPostTools(req);
        ObjectGen json = new ObjectGen();
        try {
            Timestamp now = Tools.currentTimestamp();
            json.number("time", now.getTime());
            int uuid;
            try {
                uuid = Integer.parseInt(tools.getParameter("uuid"));
            } catch (NumberFormatException e) {
                json.number("code", 1);
                return;
            }
            try (Connection connection = Tools.getConnection()) {
                connection.setAutoCommit(false);
                DeviceDao deviceDao = new DeviceDao(connection);
                UserDao userDao = new UserDao(connection);
                Device device = deviceDao.getDeviceForUpdate(uuid);
                if (device == null) {
                    json.number("code", 2);
                    connection.commit();
                    return;
                }
                if (!device.password.equals(tools.getParameter("password"))) {
                    json.number("code", 3);
                    connection.commit();
                    return;
                }
                json.number("code", 0);

                // 当试图移除过时的预约成功时说明存在用户切换
                if (device.reserveRemoveBefore(now)) {
                    // 这时需要重置控制字符串为默认值
                    device.control = "{}";
                }
                DeviceReserve reserve = device.getReserve(now);
                String newState = tools.getParameter("state");
                if (newState != null) {
                    device.state = newState;
                }
                device.last = now;
                deviceDao.updateDevice(device);
                connection.commit();
                connection.setAutoCommit(true);

                if (reserve == null) {
                    json.number("state", 0);
                    DeviceReserve near = device.getReserves().getNearReserve(now);
                    if (near == null) {
                        json.number("reserved", 0);
                    }else{
                        json.number("reserved", 1);
                        User user = userDao.getUser(near.user);
                        json.string("userAccount", user.account);
                        json.string("userName", user.name);
                        json.number("start", near.start.getTime());
                        json.number("end", near.end.getTime());
                    }
                } else {
                    json.number("state", 1);
                    json.string("control", device.control);
                    User user = userDao.getUser(reserve.user);
                    json.string("userAccount", user.account);
                    json.string("userName", user.name);
                    json.number("start", reserve.start.getTime());
                    json.number("end", reserve.end.getTime());
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            resp.getWriter().print(json.toJson());
        }
    }
}
