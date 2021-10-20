package com.kystu.equipment.dao;

import com.dxzc.json.ObjectGen;
import com.dxzc.tools.DelayLoader;
import com.kystu.equipment.Tools;
import com.kystu.equipment.model.Device;
import com.kystu.equipment.model.DeviceReserveSet;
import com.mysql.cj.xdevapi.JsonParser;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DeviceDao extends Dao {
    public DeviceDao(Connection connection) {
        super(connection);
    }

    public int updateDevice(Device device) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("update devices set type = ?, password = ?, description = ?, local = ?, reserves = ?, control = ?, state = ?, last = ? where uuid = ?;")) {
            statement.setInt(1, device.type);
            statement.setString(2, device.password);
            statement.setString(3, device.description);
            statement.setString(4, device.local);
            String reserves;
            if (device.reserves.isLoaded()) {
                ObjectGen json = new ObjectGen();
                device.reserves.get().toJson(json);
                reserves = json.toJson();
            } else {
                reserves = device.reserves.getUnload();
            }
            statement.setString(5, reserves);
            statement.setString(6, device.control);
            statement.setString(7, device.state);
            statement.setTimestamp(8, device.last);
            statement.setInt(9, device.uuid);
            return statement.executeUpdate();
        }
    }

    public static List<Device> resultSetToDeviceList(ResultSet resultSet) throws SQLException {
        List<Device> set = new LinkedList<>();
        while (resultSet.next()) {
            Device device = new Device();
            device.uuid = resultSet.getInt("uuid");
            device.password = resultSet.getString("password");
            device.type = resultSet.getInt("type");
            device.description = resultSet.getString("description");
            device.local = resultSet.getString("local");
            String reserves = resultSet.getString("reserves");
            device.reserves = new DelayLoader<>(reserves, str -> new DeviceReserveSet(JsonParser.parseDoc(str)));
            device.control = resultSet.getString("control");
            device.state = resultSet.getString("state");
            device.last = resultSet.getTimestamp("last");
            set.add(device);
        }
        return set;
    }

    public Device getDevice(int uuid) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("select * from devices where uuid = ?;")) {
            statement.setInt(1, uuid);
            try (ResultSet set = statement.executeQuery()) {
                return Tools.onlyOneOr(resultSetToDeviceList(set), null);
            }
        }
    }

    public Device getDeviceForUpdate(int uuid) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("select * from devices where uuid = ? for update;")) {
            statement.setInt(1, uuid);
            try (ResultSet set = statement.executeQuery()) {
                return Tools.onlyOneOr(resultSetToDeviceList(set), null);
            }
        }
    }

    public List<Device> selectDevice(int type, String localLike) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("select * from devices where type = ? and local like ?;")) {
            st.setInt(1, type);
            st.setString(2, localLike);
            try (ResultSet set = st.executeQuery()) {
                return resultSetToDeviceList(set);
            }
        }
    }


}
