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

@WebServlet(name = "UserGetReserve", value = "/userGetReserve")
public class UserGetReserveServlet extends BaseServlet {
    @Override
    protected void doGetOrPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        GetPostTools tools = new GetPostTools(req);
        ObjectGen json = new ObjectGen();
        try {
            String account = tools.getParameter("account");
            String password = tools.getParameter("password");
            if (account == null || password == null) {
                json.number("code", 1);
                return;
            }
            try (Connection connection = Tools.getConnection()) {
                connection.setAutoCommit(false);
                UserDao userDao = new UserDao(connection);
                DeviceDao deviceDao = new DeviceDao(connection);

                // 执行用户登录
                User user = userDao.getUser(account);
                if (user == null) {
                    json.number("code", 2);
                    return;
                }
                if (!password.equals(user.password)) {
                    json.number("code", 3);
                    return;
                }
                json.number("code", 0);
                user.getReserve().toJson(json.object("reserved"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            resp.getWriter().println(json.toJson());
        }
    }
}
