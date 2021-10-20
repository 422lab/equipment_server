package com.kystu.equipment.servlet_old.manager;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录后台管理系统.
 * <p>
 * 参数:
 * account 账号
 * password 密码
 * <p>
 * 返回:
 * code
 * 可能为:
 * {@link #SUCCESS}
 * {@link #ERROR}
 */
@WebServlet(name = "LoginServlet", value = "/manager/getUser")
public class LoginServlet extends HttpServlet {

    /**
     * 登录成功反回码.
     */
    public static final int SUCCESS = 1;

    /**
     * 登录失败返回码.
     */
    public static final int ERROR = 0;

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
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        HttpSession se = req.getSession();

        String account = req.getParameter("account");
        String password = req.getParameter("password");

        if ("root".equals(account) && "qsh@2001218".equals(password)) {
            se.setAttribute("manager", true);
            out.println("{\"code\":" + SUCCESS + "}");
        } else {
            se.removeAttribute("manager");
            out.println("{\"code\":" + ERROR + "}");
        }
    }
}
