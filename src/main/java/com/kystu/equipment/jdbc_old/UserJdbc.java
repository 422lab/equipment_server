package com.kystu.equipment.jdbc_old;

import com.kystu.equipment.model.User_old;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserJdbc extends Jdbc {

    public UserJdbc() throws SQLException {
    }

    private static User_old readAUser(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }
        User_old user = readUser(resultSet);
        if (resultSet.next()) {
            return null;
        }
        return user;
    }

    private static User_old readUser(ResultSet resultSet) throws SQLException {
        User_old user = new User_old();
        user.uuid = resultSet.getInt("uuid");
        user.account = resultSet.getString("account");
        user.image = resultSet.getString("image");
        user.device1 = resultSet.getInt("device1");
        user.device1_start = resultSet.getTimestamp("device1_start");
        user.device1_finish = resultSet.getTimestamp("device1_finish");
        return user;
    }

    /**
     * 列出所有用户.
     *
     * @return 用户列表
     * @throws SQLException 数据库访问异常
     */
    public List<User_old> listUser() throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("select * from user;")) {
            try (ResultSet re = st.executeQuery()) {
                List<User_old> list = new LinkedList<>();
                while (re.next()) {
                    list.add(readUser(re));
                }
                return list;
            }
        }
    }

    /**
     * 用户登录.
     *
     * @param account  用户账号
     * @param password 用户密码
     * @return 用户
     * @throws SQLException 数据库访问异常
     */
    public User_old login(String account, String password) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("select * from user where account = ? and password = ?;")) {
            st.setString(1, account);
            st.setString(2, password);
            try (ResultSet re = st.executeQuery()) {
                return readAUser(re);
            }
        }
    }

    /**
     * 用户注册.
     *
     * @param account  注册账号
     * @param password 注册密码
     * @return 注册结果
     * @throws SQLException 数据库访问异常
     */
    public boolean logon(String account, String password) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("insert into user (account, password) values (?, ?);")) {
            st.setString(1, account);
            st.setString(2, password);
            return st.executeUpdate() != 0;
        }
    }

    public String getUserImage(int uuid) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("select image from user where uuid = ?;")) {
            st.setInt(1, uuid);
            try (ResultSet re = st.executeQuery()) {
                if (!re.next()) {
                    return null;
                }
                String image = re.getString("image");
                if (re.next()) {
                    return null;
                }
                return image;
            }
        }
    }

}
