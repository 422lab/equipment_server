package com.kystu.equipment.model;


import com.dxzc.json.ArrayGen;
import com.dxzc.json.ObjectGen;
import com.kystu.equipment.Tools;
import com.mysql.cj.xdevapi.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UseReserve {

    public int type;
    public Timestamp start;
    public Timestamp end;
    public Set<Integer> devices;

    public UseReserve(JsonValue json) {
        devices = new HashSet<>();
        DbDoc map = (DbDoc) json;
        for (Map.Entry<String, JsonValue> entry : map.entrySet()) {
            switch (entry.getKey()) {
                case "type":
                    type = ((JsonNumber)entry.getValue()).getInteger();
                    break;
                case "start":
                    start = Tools.timestamp((JsonNumber) entry.getValue());
                    break;
                case "end":
                    end = Tools.timestamp((JsonNumber) entry.getValue());
                    break;
                case "devices":
                    for (JsonValue device : ((JsonArray) entry.getValue())) {
                        devices.add(((JsonNumber) device).getInteger());
                    }
                    break;
            }
        }
    }

    public void toJson(ObjectGen json) {
        json.number("type", type);
        json.number("start", start.getTime());
        json.number("end", end.getTime());
        try (ArrayGen array = json.array("devices")) {
            for (int device : devices) {
                array.number(device);
            }
        }
    }

}
