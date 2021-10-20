package com.kystu.equipment.jdbc_old;

import com.kystu.equipment.model.Device1;
import com.kystu.equipment.model.User_old;

import java.sql.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * 对{@code device1}数据表相关操作的封装.
 */
public class Device1Jdbc extends UserJdbc {

    public Device1Jdbc() throws SQLException {
    }

    /**
     * 从结果集中读取一个{@link Device1}并且检查是否为唯一一个.
     *
     * @param resultSet 结果集
     * @return 设备
     * @throws SQLException 数据库访问异常
     */
    private static Device1 readADevice1(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }
        Device1 device1 = readDevice1(resultSet);
        if (resultSet.next()) {
            return null;
        }
        return device1;
    }

    /**
     * 从结果集中读取一个{@link Device1}.
     *
     * @param resultSet 结果集
     * @return 设备
     * @throws SQLException 数据库访问异常
     */
    private static Device1 readDevice1(ResultSet resultSet) throws SQLException {
        Device1 device1 = new Device1();
        device1.uuid = resultSet.getInt("uuid");
        device1.local = resultSet.getString("local");
        device1.state = resultSet.getInt("state");
        device1.user = resultSet.getInt("user");
        device1.last = resultSet.getTimestamp("last");
        device1.start = resultSet.getTimestamp("start");
        device1.finish = resultSet.getTimestamp("finish");
        return device1;
    }

    public Device1 link(int uuid, int password) throws SQLException {
        setAutoCommit(false);
        Device1 device1;
        try (PreparedStatement st = connection.prepareStatement("select * from device1 where uuid = ? and password = ? for update;")) {
            st.setInt(1, uuid);
            st.setInt(2, password);
            try (ResultSet re = st.executeQuery()) {
                device1 = readADevice1(re);
            }
        }
        if (device1 == null) {
            rollback();
            return null;
        }
        try (PreparedStatement st = connection.prepareStatement("update device1 set last = ? where uuid = ?;")) {
            st.setTimestamp(1, currentTimestamp());
            st.setInt(2, uuid);
            st.executeUpdate();
        }
        commit();
        return device1;
    }

    /**
     * 用户预约第一种设备.
     *
     * @param account  用户账号
     * @param password 用户密码
     * @param device1  设备标识
     * @param start    开始时间
     * @param finish   结束时间
     * @return 预约状态
     * @throws SQLException 数据库访问异常
     */
    public boolean useDevice1(String account, String password, int device1, Timestamp start, Timestamp finish) throws SQLException {
        setAutoCommit(false);
        User_old user = login(account, password);
        if (user == null || user.useDevice1()) {
            rollback();
            return false;
        }
        int uuid = user.uuid;
        Device1 d1;
        try (PreparedStatement st = connection.prepareStatement("select * from device1 where uuid = ? for update;")) {
            st.setInt(1, device1);
            try (ResultSet re = st.executeQuery()) {
                d1 = readADevice1(re);
            }
        }
        if (d1 == null || d1.used()) {
            rollback();
            return false;
        }
        try (PreparedStatement st = connection.prepareStatement("update user set device1 = ?, device1_start = ?, device1_finish = ? where uuid = ?;")) {
            st.setInt(1, device1);
            st.setTimestamp(2, start);
            st.setTimestamp(3, finish);
            st.setInt(4, uuid);
            st.executeUpdate();
        }
        try (PreparedStatement st = connection.prepareStatement("update device1 set user = ?, start = ?, finish = ? where uuid = ?;")) {
            st.setInt(1, uuid);
            st.setTimestamp(2, start);
            st.setTimestamp(3, finish);
            st.setInt(4, device1);
            st.executeUpdate();
        }
        commit();
        return true;
    }

    public List<Device1> listDevice1() throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("select * from device1;")) {
            try (ResultSet rs = st.executeQuery()) {
                List<Device1> list = new LinkedList<>();
                while (rs.next()) {
                    list.add(readDevice1(rs));
                }
                return list;
            }
        }
    }

    public List<Device1> selectByLocationLike(String like) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("select * from device1 where local like ? and state = 0 and device1.last > ? and (user = 0 or finish < ?);")) {
            st.setString(1, like);
            Calendar time = Calendar.getInstance();
            time.add(Calendar.MINUTE, -10);
            st.setTimestamp(2, new Timestamp(time.getTime().getTime()));
            st.setTimestamp(3, currentTimestamp());
            List<Device1> devices = new LinkedList<>();
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    devices.add(readDevice1(rs));
                }
            }
            return devices;
        }
    }

}
