package com.dxzc.json;

import com.mysql.cj.xdevapi.*;

public abstract class JsonGen implements AutoCloseable {

    public static StringBuilder stringToJson(StringBuilder builder, String str) {
        if (str == null) {
            return builder.append("null");
        }
        builder.append("\"");
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\b':
                    builder.append("\\b");
                    break;
                case '\f':
                    builder.append("\\f");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                case '\t':
                    builder.append("\\t");
                    break;
                case '\"':
                    builder.append("\\\"");
                    break;
                case '\\':
                    builder.append("\\\\");
                    break;
                default:
                    builder.append(c);
            }
        }
        return builder.append("\"");
    }

    private enum State {
        ACTIVITY,
        BORROW,
        CLOSED,
    }

    public JsonGen() {
        parent = null;
        state = State.ACTIVITY;
        builder = new StringBuilder();
    }

    protected JsonGen(JsonGen borrow) {
        if (borrow.state != State.ACTIVITY) {
            throw new RuntimeException();
        }
        parent = borrow;
        state = State.ACTIVITY;
        builder = borrow.builder;
        borrow.state = State.BORROW;
    }

    private final JsonGen parent;

    private State state;

    protected final StringBuilder builder;

    protected void checkActivity() {
        if (state != State.ACTIVITY) {
            throw new RuntimeException();
        }
    }

    protected boolean isActivity() {
        return state == State.ACTIVITY;
    }

    @Override
    public void close() {
        if (state == State.BORROW) {
            throw new RuntimeException();
        }
        if (state == State.ACTIVITY) {
            state = State.CLOSED;
            if (parent != null) {
                parent.state = State.ACTIVITY;
            }
        }
    }

    public String toJson() {
        try {
            close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return builder.toString();
    }


}
