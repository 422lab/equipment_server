package com.kystu.equipment.servlet_old.device1;

import com.kystu.equipment.jdbc_old.Device1Jdbc;
import com.kystu.equipment.model.Device1;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

@WebServlet(name = "GetUserImageServlet", value = "/device1/getUserImage")
public class GetUserImageServlet extends HttpServlet {

    public static final int SUCCESS = 0;

    public static final int ERROR = 1;

    public static final int PARAMETER = 2;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/octet-stream");
        DataOutput out = new DataOutputStream(resp.getOutputStream());

        String uuid = req.getParameter("uuid");
        String password = req.getParameter("password");
        int code;
        Device1 device1;
        if (uuid != null && password != null) {
            try (Device1Jdbc jdbc = new Device1Jdbc()) {
                device1 = jdbc.link(Integer.parseInt(uuid), Integer.parseInt(password));
                if (device1 != null) {
                    String image = jdbc.getUserImage(device1.uuid);
                    try {
                        BufferedImage i = ImageIO.read(new URL(image));
                        int width = i.getWidth();
                        int height = i.getHeight();
                        int[] data = i.getRGB(0, 0, width, height, null, 0, width);
                        out.writeInt(0);
                        out.writeInt(width);
                        out.writeInt(height);
                        for (int x = 0; x < width * height; x++) {
                            out.writeInt(data[x]);
                        }
                    } catch (IOException e) {
                        out.writeInt(-1);
                    }
                } else {
                    out.writeInt(-1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                out.writeInt(-1);
            }
        } else {
            out.writeInt(-1);
        }
    }
}
