package com.kystu.equipment.servlet_old.user;

import com.kystu.equipment.Checker;
import com.kystu.equipment.jdbc_old.UserJdbc;
import com.kystu.equipment.model.User_old;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * 用户注册接口.
 * <p>
 * 参数:
 * account 用户注册账号
 * password 用户注册密码
 * <p>
 * 返回:
 * code 注册操作状态
 * 可能为:
 * {@link #SUCCESS}
 * {@link #ERROR}
 * {@link #PARAMETER}
 * <p>
 * 注意:如果注册成功,自动进入登录状态
 */
@WebServlet(name = "LogonServlet", value = "/user/logon")
public class LogonServlet extends HttpServlet {

    /**
     * 注册成功返回码.
     */
    public static final int SUCCESS = 0;

    /**
     * 注册失败返回码.
     */
    public static final int ERROR = 1;

    /**
     * 注册参数错误返回码.
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");

        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        HttpSession se = req.getSession();

        String account = req.getParameter("account");
        String password = req.getParameter("password");

        if (!Checker.isValidAccount(account) || !Checker.isValidPassword(password)) {
            out.println("{\"code\":" + PARAMETER + "}");
            return;
        }

        try (UserJdbc jdbc = new UserJdbc()) {
            if (jdbc.logon(account, password)) {
                User_old user = jdbc.login(account, password);
                se.setAttribute("uuid", user.uuid);
                se.setAttribute("account", account);
                se.setAttribute("password", password);
                out.println("{\"code\":" + SUCCESS + "}");
            } else {
                out.println("{\"code\":" + ERROR + "}");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
