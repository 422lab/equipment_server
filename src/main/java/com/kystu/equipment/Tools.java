package com.kystu.equipment;

import com.mysql.cj.xdevapi.JsonNumber;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;

public class Tools {

    public static boolean equals(Timestamp a, Timestamp b) {
        return a.getTime() == b.getTime();
    }

    public static String read(BufferedReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (String string; (string = reader.readLine()) != null; ) {
            builder.append(string);
        }
        return builder.toString();
    }

    public static Timestamp timestamp(JsonNumber number) {
        return new Timestamp(number.getBigDecimal().longValue());
    }

    /**
     * 获取当前时间戳.
     *
     * @return 当前时间戳
     */
    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取数据源.
     *
     * @return 数据源
     */
    private static DataSource getDataSource() {
        try {
            return InitialContext.doLookup("java:comp/env/equipment");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 得到一个数据库连接.
     *
     * @return 数据库连接
     * @throws SQLException 数据库访问异常
     */
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public static <T> T onlyOne(Iterable<T> set) {
        Iterator<T> iterator = set.iterator();
        if (iterator.hasNext()) {
            T obj = iterator.next();
            if (iterator.hasNext()) {
                throw new RuntimeException();
            }
            return obj;
        }
        throw new RuntimeException();
    }

    public static <T> T onlyOneOr(Iterable<T> set, T v) {
        Iterator<T> iterator = set.iterator();
        if (iterator.hasNext()) {
            T obj = iterator.next();
            if (iterator.hasNext()) {
                return v;
            }
            return obj;
        }
        return v;
    }

}
