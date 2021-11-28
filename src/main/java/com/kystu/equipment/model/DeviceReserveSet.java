package com.kystu.equipment.model;

import com.dxzc.json.ArrayGen;
import com.dxzc.json.ObjectGen;
import com.kystu.equipment.Tools;
import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonValue;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class DeviceReserveSet implements Iterable<DeviceReserve> {
    public DeviceReserveSet() {

    }

    public DeviceReserveSet(JsonValue json) {
        DbDoc object = (DbDoc) json;
        for (Map.Entry<String, JsonValue> entry : object.entrySet()) {
            switch (entry.getKey()) {
                case "set": {
                    for (JsonValue item : (JsonArray) entry.getValue()) {
                        DeviceReserve reserve = new DeviceReserve();
                        reserve.fromJson(item);
                        set.add(reserve);
                    }
                    break;
                }
            }
        }
    }

    private final HashSet<DeviceReserve> set = new HashSet<>();

    public boolean add(DeviceReserve reserve) {
        return set.add(reserve);
    }

    public void toJson(ObjectGen json) {
        try (ArrayGen array = json.array("set")) {
            for (DeviceReserve reserve : set) {
                try (ObjectGen item = array.object()) {
                    reserve.toJson(item);
                }
            }
        }
    }

    public int size() {
        return set.size();
    }

    public boolean removeBefore(Timestamp timestamp) {
        return set.removeIf(reserve -> reserve.end.before(timestamp));
    }

    public DeviceReserve get(Timestamp timestamp) {
        return Tools.onlyOneOr(set.stream()
                .filter(reserve ->
                        reserve.start.before(timestamp) && reserve.end.after(timestamp))
                .collect(Collectors.toSet()), null);
    }

    public boolean check(Timestamp start, Timestamp end) {
        for (DeviceReserve i : set) {
            if (i.end.after(start) && i.start.before(end)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<DeviceReserve> iterator() {
        return set.iterator();
    }

    public boolean removeReserve(Timestamp start, Timestamp end) {
        return set.removeIf(i -> Tools.equals(i.start, start) && Tools.equals(i.end, end));
    }

    public DeviceReserve getNearReserve(Timestamp now) {
        removeBefore(now);
        return set.stream().min(Comparator.comparing(a -> a.start)).orElse(null);
    }
}
