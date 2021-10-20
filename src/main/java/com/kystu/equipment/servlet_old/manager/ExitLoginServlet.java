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
 * 退出管理系统登录.
 * <p>
 * 参数:
 * 无
 * <p>
 * 返回:
 * code
 * 可能为
 * {@link #SUCCESS}
 * {@link #ERROR}
 */
@WebServlet(name = "ExitLoginServlet", value = "/manager/exitLogin")
public class ExitLoginServlet extends HttpServlet {

    /**
     * 成功返回码.
     */
    public static final int SUCCESS = 1;

    /**
     * 失败返回码.
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        HttpSession se = req.getSession();
        Object manager = se.getAttribute("manager");
        se.removeAttribute("manager");
        if (manager != null && manager.equals(true)) {
            out.println("{\"code\":" + SUCCESS + "}");
        } else {
            out.println("{\"code\":" + ERROR + "}");
        }

    }
}
