package com.kystu.equipment.servlet_old.manager;

import com.kystu.equipment.jdbc_old.Device1Jdbc;
import com.kystu.equipment.model.Device1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * 列出所有的第一种设备.
 * <p>
 * 参数:
 * 无
 * <p>
 * 返回:
 * code
 * 可能为:
 * {@link #SUCCESS}
 * {@link #ERROR}
 */
@WebServlet(name = "ListDevice1Servlet", value = "/manager/listDevice1")
public class ListDevice1Servlet extends HttpServlet {

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
        if (manager != null && manager.equals(true)) {
            try (Device1Jdbc jdbc = new Device1Jdbc()) {
                List<Device1> list = jdbc.listDevice1();
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
                    out.print(device1);
                }
                out.print("]}");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            out.println("{\"code\":" + ERROR + "}");
        }

    }
}
