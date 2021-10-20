package com.kystu.equipment.servlet_old.user;

import com.kystu.equipment.jdbc_old.Device1Jdbc;
import com.kystu.equipment.jdbc_old.Jdbc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 用户使用一个第一种设备.
 * <p>
 * 参数:
 * device1 设备id
 * start 开始时间
 * finish 结束时间
 * <p>
 * 返回:
 * code
 * 可能为:
 * {@link #SUCCESS}
 * {@link #ERROR}
 * {@link #PARAMETER}
 * {@link #USED}
 * <p>
 * 注意:
 * 需要处于用户登录状态.
 */
@WebServlet(name = "UseDevice1Servlet", value = "/user/useDevice1")
public class UseDevice1Servlet extends HttpServlet {

    /**
     * 成功返回码.
     */
    public static final int SUCCESS = 0;

    /**
     * 失败返回码.
     */
    public static final int ERROR = 1;

    /**
     * 参数错误返回码.
     */
    public static final int PARAMETER = 2;

    /**
     * 占用返回码.
     */
    public static final int USED = 3;

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

        String device1 = req.getParameter("device1");
        String start = req.getParameter("start");
        String finish = req.getParameter("finish");
        int id;
        Timestamp st;
        Timestamp ft;
        try {
            id = Integer.parseInt(device1);
            st = new Timestamp(Long.parseLong(start));
            ft = new Timestamp(Long.parseLong(finish));
        } catch (NullPointerException | NumberFormatException e) {
            out.println("{\"code\":" + PARAMETER + "}");
            return;
        }

        if (Jdbc.currentTimestamp().after(st) || st.after(ft)) {
            out.println("{\"code\":" + PARAMETER + "}");
            return;
        }

        String account = req.getParameter("account");
        String password = req.getParameter("password");
        if (account == null || password == null) {
            out.println("{\"code\":" + ERROR + "}");
            return;
        }
        try (Device1Jdbc jdbc = new Device1Jdbc()) {
            boolean success = jdbc.useDevice1(account, password, id, st, ft);
            if (success) {
                out.println("{\"code\":" + SUCCESS + "}");
            } else {
                out.println("{\"code\":" + USED + "}");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
