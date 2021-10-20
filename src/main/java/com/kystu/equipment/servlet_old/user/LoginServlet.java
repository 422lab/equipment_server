package com.kystu.equipment.servlet_old.user;

import com.kystu.equipment.Checker;
import com.kystu.equipment.jdbc_old.UserJdbc;
import com.kystu.equipment.model.User_old;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * 用户登录接口.
 * <p>
 * 参数:
 * account 账号
 * password 密码
 * <p>
 * 返回:
 * code 登录操作状态
 * 可能为:
 * {@link #SUCCESS}
 * {@link #ERROR}
 * {@link #PARAMETER}
 */
@WebServlet(name = "UserLoginServlet", value = "/user/getUser")
public class LoginServlet extends HttpServlet {


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


    /**
     * 由于调试需要对post进行的转发.
     * <p>
     * 可能会删除get接口.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Origin", resp.getHeader("Origin"));
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();

        String account = req.getParameter("account");
        String password = req.getParameter("password");

        if (!Checker.isValidAccount(account) || !Checker.isValidPassword(password)) {
            out.println("{\"code\":" + PARAMETER + "}");
            return;
        }

        try (UserJdbc jdbc = new UserJdbc()) {
            User_old user = jdbc.login(account, password);
            if (user != null) {
                out.println("{\"code\":" + SUCCESS + "}");
            } else {
                out.println("{\"code\":" + ERROR + "}");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}