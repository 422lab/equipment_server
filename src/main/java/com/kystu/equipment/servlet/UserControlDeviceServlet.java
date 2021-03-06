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

@WebServlet(name = "UserControlDevice", value = "/userControlDevice")
public class UserControlDeviceServlet extends BaseServlet {

    @Override
    protected void doGetOrPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        GetPostTools tools = new GetPostTools(req);
        ObjectGen json = new ObjectGen();
        try {
            String account = tools.getParameter("account");
            String password = tools.getParameter("password");
            String control = tools.getParameter("control");
            if (account == null || password == null) {
                json.number("code", 1);
                return;
            }
            int deviceUuid;
            try {
                deviceUuid = Integer.parseInt(tools.getParameter("device"));
            } catch (NumberFormatException e) {
                json.number("code", 1);
                return;
            }
            try (Connection connection = Tools.getConnection()) {
                UserDao userDao = new UserDao(connection);
                User user = userDao.getUser(account);
                if (user == null) {
                    json.number("code", 2);
                    return;
                }
                if (!password.equals(user.password)) {
                    json.number("code", 3);
                    return;
                }
                DeviceDao deviceDao = new DeviceDao(connection);
                Device device;
                if (control != null) {
                    connection.setAutoCommit(false);
                    device = deviceDao.getDeviceForUpdate(deviceUuid);
                } else {
                    device = deviceDao.getDevice(deviceUuid);
                }
                if (device == null) {
                    json.number("code", 4);
                    return;
                }
                Timestamp now = Tools.currentTimestamp();
                DeviceReserve reserve = device.getReserve(now);
                if (reserve != null && reserve.user != user.uuid) {
                    // fixme: ????????????????????????????????????????????????
                    reserve = device.getReserves().will(now);
                    if (reserve != null && reserve.user != user.uuid) {
                        json.number("code", 4);
                        return;
                    }
                    json.number("code", 0);
                    json.string("state", device.state);
                    json.number("last", device.last.getTime());
                    return;
                }
                if (control != null) {
                    device.control = control;
                    deviceDao.updateDevice(device);
                    connection.commit();
                    connection.setAutoCommit(true);
                }
                json.number("code", 0);
                json.string("state", device.state);
                json.number("last", device.last.getTime());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            resp.getWriter().print(json.toJson());
        }
    }
}
