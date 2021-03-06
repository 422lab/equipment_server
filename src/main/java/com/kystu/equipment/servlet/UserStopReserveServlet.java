package com.kystu.equipment.servlet;

import com.dxzc.json.ObjectGen;
import com.kystu.equipment.GetPostTools;
import com.kystu.equipment.Tools;
import com.kystu.equipment.dao.DeviceDao;
import com.kystu.equipment.dao.UserDao;
import com.kystu.equipment.model.Device;
import com.kystu.equipment.model.UseReserve;
import com.kystu.equipment.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;

@WebServlet(name = "UserStopReserve", value = "/userStopReserve")
public class UserStopReserveServlet extends BaseServlet {
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
                return;
            }
            Timestamp start;
            Timestamp end;
            int device;
            String startStr = tools.getParameter("start");
            String endStr = tools.getParameter("end");
            String deviceStr = tools.getParameter("device");
            try {
                start = new Timestamp(Long.parseLong(startStr));
                end = new Timestamp(Long.parseLong(endStr));
                device = Integer.parseInt(deviceStr);
            } catch (NumberFormatException e) {
                json.string("msg", "Parameter? start = " + startStr + ", end = " + endStr + ", device = " + deviceStr + e.toString());
                json.number("code", 1);
                return;
            }
            try (Connection connection = Tools.getConnection()) {
                connection.setAutoCommit(false);
                UserDao userDao = new UserDao(connection);
                DeviceDao deviceDao = new DeviceDao(connection);

                // ??????????????????
                User user = userDao.getUserForUpdate(account);
                if (user == null) {
                    json.number("code", 2);
                    return;
                }
                if (!password.equals(user.password)) {
                    json.number("code", 3);
                    return;
                }
                boolean success = false;
                for (Iterator<UseReserve> it = user.getReserve().iterator(); it.hasNext(); ) {
                    UseReserve r = it.next();
                    if (Tools.equals(r.start, start) && Tools.equals(r.end, end)) {
                        if (r.devices.removeIf(i -> i == device)) {
                            success = true;
                        }
                        if (r.devices.isEmpty()) {
                            it.remove();
                        }
                    }
                }

                user.removeBefore(now);
                userDao.updateUser(user);
                if (success) {
                    Device removeDevice = deviceDao.getDeviceForUpdate(device);
                    if (removeDevice.removeReserve(start, end)) {
                        if (end.after(now)) {
                            removeDevice.control = "{}";
                        }
                    }
                    deviceDao.updateDevice(removeDevice);
                    connection.commit();
                    json.number("code", 0);
                    return;
                }
                connection.rollback();
                json.number("code", 4);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            resp.getWriter().println(json.toJson());
        }
    }
}
