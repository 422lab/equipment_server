package com.kystu.equipment.model;

import com.dxzc.tools.DelayLoader;

import java.sql.Timestamp;

public class User {

    public int uuid;
    public int type;
    public String account;
    public String password;
    public String name;
    public DelayLoader<String, UseReserveSet> reserves;
    public String image;

    public UseReserveSet getReserve() {
        return reserves.get();
    }

    public boolean removeBefore(Timestamp time) {
        return getReserve().removeBefore(time);
    }

}
