package com.kystu.equipment.model;

import com.kystu.equipment.Json_old;
import com.kystu.equipment.jdbc_old.Jdbc;

import java.sql.Timestamp;

/**
 * 第一种设备模型.
 */
public class Device1 {

    /**
     * 设备标识.
     */
    public int uuid;

    /**
     * 设备地址.
     */
    public String local;

    /**
     * 设备状态.
     */
    public int state;

    /**
     * 上次设备连接时间.
     */
    public Timestamp last;

    /**
     * 预约用户.
     */
    public int user;

    /**
     * 上次预约开始时间.
     */
    public Timestamp start;

    /**
     * 上次预约结束时间.
     */
    public Timestamp finish;

    /**
     * 判断设备是否被占用.
     *
     * @return 设备占用情况
     */
    public boolean used() {
        if (user == 0) {
            return false;
        }
        if (finish == null || finish.before(Jdbc.currentTimestamp())) {
            return false;
        }
        return true;
    }

    /**
     * 设备状态的JSON表示.
     *
     * @return JSON字符串
     */
    @Override
    public String toString() {
        Timestamp now = Jdbc.currentTimestamp();
        return "{\"uuid\":" + uuid +
                ",\"local\":" + Json_old.json(local) +
                ",\"state\"" + ":" + ((user != 0 && now.after(start) && now.before(finish)) ? 2 : 0) +
                ",\"state0\":" + state +
                ",\"last\":" + Json_old.json(last) +
                ",\"user\":" + user +
                ",\"start\":" + Json_old.json(start) +
                ",\"finish\":" + Json_old.json(finish) +
                "}";
    }
}
