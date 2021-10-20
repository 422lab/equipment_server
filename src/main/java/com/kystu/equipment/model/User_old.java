package com.kystu.equipment.model;

import com.kystu.equipment.Json_old;
import com.kystu.equipment.jdbc_old.Jdbc;

import java.sql.Timestamp;

/**
 * 用户模型.
 */
public class User_old {

    /**
     * 用户的标识符.
     */
    public int uuid;

    /**
     * 用户账号.
     */
    public String account;

    /**
     * 用户图片.
     */
    public String image;

    /**
     * 用户使用的第一种设备.
     */
    public int device1;

    /**
     * 用户使用第一种设备开始时间.
     */
    public Timestamp device1_start;

    /**
     * 用户使用第一种设备结束时间.
     */
    public Timestamp device1_finish;

    /**
     * 根据系统时间判断用户使用正在使用或者预约了一个第一种设备.
     *
     * @return 第一种设备使用情况
     */
    public boolean useDevice1() {
        if (device1 == 0) {
            return false;
        }
        if (device1_finish == null || device1_finish.before(Jdbc.currentTimestamp())) {
            return false;
        }
        return true;
    }

    /**
     * 返回该用户状态的JSON表示.
     *
     * @return 代表JSON的字符串
     */
    @Override
    public String toString() {
        return "{\"uuid\":" + uuid +
                ",\"account\":" + Json_old.json(account) +
                ",\"image\":" + Json_old.json(image) +
                ",\"device1\":" + device1 +
                ",\"device1_start\":" + Json_old.json(device1_start) +
                ",\"device1_finish\":" + Json_old.json(device1_finish) +
                "}";
    }
}
