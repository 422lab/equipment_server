package com.dxzc.json;

import java.math.BigDecimal;

public class ObjectGen extends JsonGen {
    public ObjectGen() {
        super();
        builder.append("{");
        empty = true;
    }

    public ObjectGen(JsonGen jsonGen) {
        super(jsonGen);
        builder.append("{");
        empty = true;
    }

    private boolean empty;

    private void split() {
        checkActivity();
        if (empty) {
            empty = false;
        } else {
            builder.append(",");
        }
    }

    public ArrayGen array(String key) {
        split();
        stringToJson(builder, key).append(":");
        return new ArrayGen(this);
    }

    public ObjectGen object(String key) {
        split();
        stringToJson(builder, key).append(":");
        return new ObjectGen(this);
    }

    public ObjectGen string(String key, String string) {
        split();
        stringToJson(builder, key).append(":");
        stringToJson(builder, string);
        return this;
    }

    public ObjectGen nullptr(String key) {
        split();
        stringToJson(builder, key).append(":");
        builder.append("null");
        return this;
    }

    public ObjectGen number(String key, int number) {
        split();
        stringToJson(builder, key).append(":");
        builder.append(number);
        return this;
    }

    public ObjectGen number(String key, long number) {
        split();
        stringToJson(builder, key).append(":");
        builder.append(number);
        return this;
    }

    public ObjectGen number(String key, BigDecimal number) {
        split();
        stringToJson(builder, key).append(":");
        builder.append(number);
        return this;
    }

    public ObjectGen number(String key, float number) {
        split();
        stringToJson(builder, key).append(":");
        builder.append(number);
        return this;
    }

    public ObjectGen number(String key, double number) {
        split();
        stringToJson(builder, key).append(":");
        builder.append(number);
        return this;
    }

    @Override
    public void close() {
        if (isActivity()) {
            builder.append("}");
        }
        super.close();
    }
}
