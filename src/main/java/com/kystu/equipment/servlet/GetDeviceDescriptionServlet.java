package com.kystu.equipment.servlet;

import com.dxzc.json.ArrayGen;
import com.dxzc.json.ObjectGen;
import com.kystu.equipment.GetPostTools;
import com.kystu.equipment.Tools;
import com.kystu.equipment.dao.DeviceDao;
import com.kystu.equipment.model.Device;
import com.kystu.equipment.model.DeviceReserve;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "GetDeviceDescription", value = "/getDeviceDescription")
public class GetDeviceDescriptionServlet extends BaseServlet {
    @Override
    protected void doGetOrPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        GetPostTools tools = new GetPostTools(req);
        ObjectGen json = new ObjectGen();
        try {
            int deviceId;
            try {
                deviceId = Integer.parseInt(tools.getParameter("device"));
            } catch (NumberFormatException e) {
                json.number("code", 1);
                json.string("msg", "device? " + e);
                return;
            }

            try (Connection connection = Tools.getConnection()) {
                DeviceDao deviceDao = new DeviceDao(connection);
                Device device = deviceDao.getDevice(deviceId);
                if (device == null) {
                    json.number("code", 2);
                    json.string("msg", "找不到设备");
                    return;
                }
                json.number("code", 0);
                json.number("type", device.type);
                json.string("description", device.description);
                json.string("local", device.local);
                json.number("last", device.last.getTime());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            resp.getWriter().print(json.toJson());
        }
    }
}
