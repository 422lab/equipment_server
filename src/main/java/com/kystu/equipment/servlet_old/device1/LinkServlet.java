package com.kystu.equipment.servlet_old.device1;

import com.kystu.equipment.Json_old;
import com.kystu.equipment.jdbc_old.Device1Jdbc;
import com.kystu.equipment.jdbc_old.Jdbc;
import com.kystu.equipment.model.Device1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * 第一种设备与服务器建立的连接.
 * <p>
 * 参数:
 * uuid 设备的唯一标识
 * password 设备密码
 * <p>
 * 返回:
 * code 状态
 * 可能为:
 * {@link #SUCCESS}
 * {@link #ERROR}
 * {@link #PARAMETER}
 * 当code == {@link #SUCCESS}时:
 * state {@link Device1#toString()}
 * <p>
 * time 服务器时间
 */
@WebServlet(name = "LinkServlet", value = "/device1/link")
public class LinkServlet extends HttpServlet {

    public static final int SUCCESS = 0;

    public static final int ERROR = 1;

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
        resp.setBufferSize(200);

        PrintWriter out = resp.getWriter();
        String uuid = req.getParameter("uuid");
        String password = req.getParameter("password");
        int code;
        Device1 device1;
        if (uuid != null && password != null) {
            try (Device1Jdbc jdbc = new Device1Jdbc()) {
                device1 = jdbc.link(Integer.parseInt(uuid), Integer.parseInt(password));
                if (device1 != null) {
                    code = SUCCESS;
                } else {
                    code = ERROR;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                device1 = null;
                code = PARAMETER;
            }
        } else {
            device1 = null;
            code = PARAMETER;
        }

        if (code == SUCCESS) {
            out.print("{\"code\":");
            out.print(code);
            out.print(",\"state\":");
            out.print(device1.toString());
            out.print(",\"time\":");
            out.print(Json_old.json(Jdbc.currentTimestamp()));
            out.println("}");
        } else {
            out.print("{\"code\":");
            out.print(code);
            out.print(",\"time\":");
            out.print(Json_old.json(Jdbc.currentTimestamp()));
            out.println("}");

        }
        out.close();
    }
}
