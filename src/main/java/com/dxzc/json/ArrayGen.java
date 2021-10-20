package com.dxzc.json;

import java.math.BigDecimal;

public class ArrayGen extends JsonGen {
    public ArrayGen() {
        super();
        builder.append("[");
        empty = true;
    }

    public ArrayGen(JsonGen jsonGen) {
        super(jsonGen);
        builder.append("[");
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

    public ArrayGen array() {
        split();
        return new ArrayGen(this);
    }

    public ObjectGen object() {
        split();
        return new ObjectGen(this);
    }

    public ArrayGen string(String string) {
        split();
        stringToJson(builder, string);
        return this;
    }

    public ArrayGen nullptr() {
        split();
        builder.append("null");
        return this;
    }

    public ArrayGen number(int number) {
        split();
        builder.append(number);
        return this;
    }

    public ArrayGen number(long number) {
        split();
        builder.append(number);
        return this;
    }

    public ArrayGen number(BigDecimal number) {
        split();
        builder.append(number);
        return this;
    }

    public ArrayGen number(float number) {
        split();
        builder.append(number);
        return this;
    }

    public ArrayGen number(double number) {
        split();
        builder.append(number);
        return this;
    }

    @Override
    public void close() {
        if(isActivity()) {
            builder.append("]");
        }
        super.close();
    }
}
