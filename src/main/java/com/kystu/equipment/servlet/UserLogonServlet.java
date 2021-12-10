package com.kystu.equipment.servlet;

import com.dxzc.json.ObjectGen;
import com.kystu.equipment.GetPostTools;
import com.kystu.equipment.Tools;
import com.kystu.equipment.dao.UserDao;
import com.kystu.equipment.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "UserLogon", value = "/userLogon")
public class UserLogonServlet extends BaseServlet {

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
                UserDao userDao = new UserDao(connection);
                User user = new User();
                user.account = account;
                user.password = password;
                if (userDao.insertUser(user)) {
                    json.number("code", 0);
                } else {
                    json.number("code", 2);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            resp.getWriter().print(json.toJson());
        }
    }

}
