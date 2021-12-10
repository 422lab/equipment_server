package com.kystu.equipment.servlet;

import com.dxzc.json.ArrayGen;
import com.dxzc.json.ObjectGen;
import com.kystu.equipment.GetPostTools;
import com.kystu.equipment.Tools;
import com.kystu.equipment.dao.DeviceDao;
import com.kystu.equipment.model.Device;
import com.kystu.equipment.model.DeviceReserve;
import com.kystu.equipment.model.DeviceReserveSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "SelectDevice", value = "/selectDevice")
public class SelectDeviceServlet extends BaseServlet {
    @Override
    protected void doGetOrPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        GetPostTools tools = new GetPostTools(req);
        ObjectGen json = new ObjectGen();
        try {
            String localLike = tools.getParameter("localLike");
            if (localLike == null) {
                json.number("code", 1);
                return;
            }
            int type;
            try {
                type = Integer.parseInt(tools.getParameter("type"));
            } catch (NumberFormatException e) {
                json.number("code", 1);
                return;
            }

            try (Connection connection = Tools.getConnection()) {
                DeviceDao deviceDao = new DeviceDao(connection);
                List<Device> devices = deviceDao.selectDevice(type, localLike);
                json.number("code", 0);
                try (ArrayGen array = json.array("devices")) {
                    for (Device i : devices) {
                        try (ObjectGen object = array.object()) {
                            object.number("uuid", i.uuid);
                            object.string("description", i.description);
                            object.string("local", i.local);
                            object.number("last", i.last.getTime());
                            try (ArrayGen reserves = object.array("reserves")) {
                                for (DeviceReserve r : i.getReserves()) {
                                    try (ObjectGen reserve = reserves.object()) {
                                        reserve.number("start", r.start.getTime());
                                        reserve.number("end", r.end.getTime());
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            resp.getWriter().print(json.toJson());
        }
    }
}
