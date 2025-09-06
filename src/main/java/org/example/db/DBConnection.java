package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnection {
    private static HikariDataSource dataSource;


    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/payment_processor?useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(10); // Example: max 10 connections

        dataSource = new HikariDataSource(config);

    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}
