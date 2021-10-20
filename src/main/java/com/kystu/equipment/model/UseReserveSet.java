package com.kystu.equipment.model;

import com.dxzc.json.ArrayGen;
import com.dxzc.json.ObjectGen;
import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonValue;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UseReserveSet implements Iterable<UseReserve> {
    public UseReserveSet() {

    }

    public UseReserveSet(JsonValue json) {
        DbDoc object = (DbDoc) json;
        for (Map.Entry<String, JsonValue> entry : object.entrySet()) {
            switch (entry.getKey()) {
                case "set": {
                    for (JsonValue item : (JsonArray) entry.getValue()) {
                        UseReserve reserve = new UseReserve(item);
                        set.add(reserve);
                    }
                    break;
                }
            }
        }
    }

    private final Set<UseReserve> set = new HashSet<>();

    public boolean add(UseReserve reserve) {
        return set.add(reserve);
    }

    public void toJson(ObjectGen json) {
        try (ArrayGen array = json.array("set")) {
            for (UseReserve reserve : set) {
                try (ObjectGen item = array.object()) {
                    reserve.toJson(item);
                }
            }
        }
    }

    public boolean checkOne(int type, Timestamp start, Timestamp end) {
        for (UseReserve i : set) {
            if (i.type == type && i.end.after(start) && i.start.before(end)) {
                return false;
            }
        }
        return true;
    }

    public boolean removeBefore(Timestamp time) {
        return set.removeIf(reserve -> reserve.end.before(time));
    }

    @Override
    public Iterator<UseReserve> iterator() {
        return set.iterator();
    }
}
