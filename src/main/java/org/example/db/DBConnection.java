package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

import static org.example.db.Constants.*;

public class DBConnection {
    private static HikariDataSource dataSource;

    public static void initDBConnection() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getenv(KEY_JDBC_URL));
        config.setUsername(System.getenv(KEY_JDBC_USER_NAME));
        config.setPassword(System.getenv(KEY_JDBC_PASSWORD));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }

    public static void closeConnection() {
        if (!dataSource.isClosed()) {
            if (dataSource != null) {
                try {
                    dataSource.getConnection().close();
                    dataSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    dataSource = null;
                }
            }
        }
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}