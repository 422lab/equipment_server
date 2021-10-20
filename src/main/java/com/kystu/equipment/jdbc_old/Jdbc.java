package com.kystu.equipment.jdbc_old;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

/**
 * 对数据库操作的基本封装.
 */
public class Jdbc implements AutoCloseable {

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
    private static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    /**
     * 建立一个数据库连接.
     *
     * @throws SQLException 数据库访问异常
     */
    public Jdbc() throws SQLException {
        connection = getConnection();
    }

    /**
     * 保有的数据库连接
     */
    public final Connection connection;

    /**
     * 关闭数据库连接.
     */
    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置自动提交.
     *
     * @param autoCommit 自动提交情况
     * @throws SQLException 数据库访问异常
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    /**
     * 回滚操作.
     *
     * @throws SQLException 数据库访问异常
     */
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * 提交操作.
     *
     * @throws SQLException 数据库访问异常
     */
    public void commit() throws SQLException {
        connection.commit();
    }

}
