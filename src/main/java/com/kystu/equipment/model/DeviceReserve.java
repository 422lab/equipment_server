package com.kystu.equipment.model;

import com.dxzc.json.ObjectGen;
import com.kystu.equipment.Tools;
import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.JsonNumber;
import com.mysql.cj.xdevapi.JsonValue;

import java.sql.Timestamp;
import java.util.Map;

public class DeviceReserve {

    public Timestamp start;
    public Timestamp end;
    public int user;


    public void fromJson(JsonValue json) {
        DbDoc map = (DbDoc) json;
        for (Map.Entry<String, JsonValue> entry : map.entrySet()) {
            switch (entry.getKey()) {
                case "start":
                    start = Tools.timestamp((JsonNumber) entry.getValue());
                    break;
                case "end":
                    end = Tools.timestamp((JsonNumber) entry.getValue());
                    break;
                case "user":
                    user = ((JsonNumber) entry.getValue()).getInteger();
                    break;
            }
        }
    }

    public void toJson(ObjectGen json) {
        json.number("start", start.getTime());
        json.number("end", end.getTime());
        json.number("user", user);
    }
}
