package com.kystu.equipment;

import java.sql.Timestamp;

/**
 * 生成JSON的辅助代码.
 */
public class Json_old {

    /**
     * 对字符串进行转义.
     *
     * @param str 需要转义的字符串
     * @return 转义结果
     */
    public static String json(String str) {
        if (str == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\r':
                    sb.append("\\r");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\'':
                    sb.append("\\'");
                    break;
                default:
                    sb.append(c);
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    /**
     * 生成时间戳json表示.
     *
     * @param time 时间戳
     * @return JSON表示
     */
    public static String json(Timestamp time) {
        if (time == null) {
            return "null";
        }
        return String.valueOf(time.getTime());
    }

}
