package com.kystu.equipment.dao;

import java.sql.Connection;

public abstract class Dao {

    public Dao(Connection connection) {
        this.connection = connection;
    }

    /**
     * 保有的数据库连接
     */
    protected final Connection connection;


}
