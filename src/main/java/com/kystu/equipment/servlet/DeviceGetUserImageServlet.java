package com.kystu.equipment.servlet;

import com.dxzc.json.ObjectGen;
import com.kystu.equipment.GetPostTools;
import com.kystu.equipment.Tools;
import com.kystu.equipment.dao.DeviceDao;
import com.kystu.equipment.dao.UserDao;
import com.kystu.equipment.model.Device;
import com.kystu.equipment.model.DeviceReserve;
import com.kystu.equipment.model.User;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet(name = "DeviceGetUserImage", value = "/deviceGetUserImage")
public class DeviceGetUserImageServlet extends BaseServlet {
    @Override
    protected void doGetOrPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/octet-stream");
        resp.setBufferSize(20000);
        DataOutput out = new DataOutputStream(resp.getOutputStream());
        GetPostTools tools = new GetPostTools(req);
        Timestamp now = Tools.currentTimestamp();
        int uuid;
        try {
            uuid = Integer.parseInt(tools.getParameter("uuid"));
        } catch (NumberFormatException e) {
            out.writeByte(1);
            return;
        }
        String password = req.getParameter("password");
        if (password == null) {
            out.writeByte(1);
            return;
        }
        try (Connection connection = Tools.getConnection()) {
            DeviceDao deviceDao = new DeviceDao(connection);
            UserDao userDao = new UserDao(connection);
            Device device = deviceDao.getDevice(uuid);
            if (device == null) {
                out.writeByte(2);
                return;
            }
            if (!device.password.equals(tools.getParameter("password"))) {
                out.writeByte(3);
                return;
            }
            DeviceReserve reserve = device.getReserve(now);
            if (reserve == null) {
                out.writeByte(4);
                return;
            }
            User user = userDao.getUser(reserve.user);
            if (user.image == null) {
                out.writeByte(5);
            }
            try {
                BufferedImage i = ImageIO.read(new URL(user.image));
                int width = i.getWidth();
                int height = i.getHeight();
                int[] data = i.getRGB(0, 0, width, height, null, 0, width);
                out.writeByte(0);
                out.writeInt(width);
                out.writeInt(height);
                for (int x = 0; x < width * height; x++) {
                    int color = data[x];
                    int r = (color >> 16) & 0xFF;
                    int g = (color >> 8) & 0xFF;
                    int b = color & 0xFF;
                    r >>= 3;
                    g >>= 2;
                    b >>= 3;
                    int outColor = (b << 11) | (g << 5) | r;
                    out.writeShort(outColor);
                }
            } catch (IOException e) {
                out.writeByte(6);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
