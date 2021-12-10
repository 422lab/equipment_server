package com.kystu.equipment.dao;

import com.dxzc.json.ArrayGen;
import com.dxzc.json.ObjectGen;
import com.dxzc.tools.DelayLoader;
import com.kystu.equipment.Tools;
import com.kystu.equipment.model.*;
import com.mysql.cj.xdevapi.JsonParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UserDao extends Dao {

    public UserDao(Connection connection) {
        super(connection);
    }

    public boolean insertUser(User user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert users (account, password) values (?, ?);")) {
            statement.setString(1, user.account);
            statement.setString(2, user.password);
            return statement.executeUpdate() != 0;
        }
    }

    public int updateUser(User user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("update users set type = ?, account = ?, password = ?, name = ?, reserves = ?, image = ? where uuid = ?;")) {
            statement.setInt(1, user.type);
            statement.setString(2, user.account);
            statement.setString(3, user.password);
            statement.setString(4, user.name);
            String reserves;
            if (user.reserves.isLoaded()) {
                ObjectGen json = new ObjectGen();
                user.reserves.get().toJson(json);
                reserves = json.toJson();
            } else {
                reserves = user.reserves.getUnload();
            }
            statement.setString(5, reserves);
            statement.setString(6, user.image);
            statement.setInt(7, user.uuid);
            return statement.executeUpdate();
        }
    }

    public static Set<User> resultSetToUserSet(ResultSet resultSet) throws SQLException {
        Set<User> set = new HashSet<>();
        while (resultSet.next()) {
            User user = new User();
            user.uuid = resultSet.getInt("uuid");
            user.type = resultSet.getInt("type");
            user.account = resultSet.getString("account");
            user.password = resultSet.getString("password");
            user.name = resultSet.getString("name");
            String reserves = resultSet.getString("reserves");
            user.reserves = new DelayLoader<>(reserves, str -> new UseReserveSet(JsonParser.parseDoc(str)));
            user.image = resultSet.getString("image");
            set.add(user);
        }
        return set;
    }

    public User getUser(int uuid) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("select * from users where uuid = ?;")) {
            statement.setInt(1, uuid);
            try (ResultSet set = statement.executeQuery()) {
                return Tools.onlyOneOr(resultSetToUserSet(set), null);
            }
        }
    }

    public User getUserForUpdate(int uuid) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("select * from users where uuid = ? for update;")) {
            statement.setInt(1, uuid);
            try (ResultSet set = statement.executeQuery()) {
                return Tools.onlyOneOr(resultSetToUserSet(set), null);
            }
        }
    }

    public User getUser(String account) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("select * from users where account = ?;")) {
            statement.setString(1, account);
            try (ResultSet set = statement.executeQuery()) {
                return Tools.onlyOneOr(resultSetToUserSet(set), null);
            }
        }
    }

    public User getUserForUpdate(String account) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("select * from users where account = ? for update;")) {
            statement.setString(1, account);
            try (ResultSet set = statement.executeQuery()) {
                return Tools.onlyOneOr(resultSetToUserSet(set), null);
            }
        }
    }

}
