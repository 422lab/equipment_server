package com.kystu.equipment.model;

import com.dxzc.tools.DelayLoader;
import com.kystu.equipment.Tools;

import java.sql.Timestamp;
import java.util.Set;

public class Device {

    public int uuid;
    public String password;
    public int type;
    public String description;
    public String local;
    public DelayLoader<String, DeviceReserveSet> reserves;
    public String control;
    public String state;
    public Timestamp last;

    public DeviceReserveSet getReserves() {
        return reserves.get();
    }

    public boolean reserveRemoveBefore(Timestamp timestamp) {
        return getReserves().removeBefore(timestamp);
    }

    public int reservesSize() {
        return getReserves().size();
    }

    public DeviceReserve getReserve(Timestamp timestamp) {
        return getReserves().get(timestamp);
    }

    public boolean check(Timestamp start, Timestamp end) {
        return getReserves().check(start, end);
    }

    public boolean removeReserve(Timestamp start, Timestamp end) {
        return getReserves().removeReserve(start, end);
    }
}
