package com.kystu.equipment.servlet_old.guest;

import com.kystu.equipment.jdbc_old.Device1Jdbc;
import com.kystu.equipment.model.Device1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * 检索可用的第一种设备.
 * <p>
 * 参数:
 * like local检索条件
 * <p>
 * 返回:
 * code
 * 可能为:
 * {@link #SUCCESS}
 * {@link #PARAMETER}
 * list device1数组
 */
@WebServlet(name = "SelectDevice1Servlet", value = "/guest/selectDevice1")
public class SelectDevice1Servlet extends HttpServlet {

    /**
     * 检索成功返回码.
     */
    public static final int SUCCESS = 0;

    /**
     * 参数错误返回码.
     */
    public static final int PARAMETER = 1;

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
        String like = req.getParameter("like");
        if (like == null) {
            out.println("{\"code\":" + PARAMETER + "}");
            return;
        }
        try (Device1Jdbc jdbc = new Device1Jdbc()) {
            List<Device1> list = jdbc.selectByLocationLike(like);
            out.print("{\"code\":");
            out.print(SUCCESS);
            out.print(",\"list\":[");
            boolean first = true;
            for (Device1 device1 : list) {
                if (first) {
                    first = false;
                } else {
                    out.print(",");
                }
                out.print(device1.toString());
            }
            out.println("]}");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
