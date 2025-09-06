package org.example.db;

import org.example.db.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepository.class.getName());

    protected String generateProcedureCall(String spName, int parametersCount) {
        return "{call " + spName + " (" + "?,".repeat(parametersCount).replaceAll(",$", "") + ")}";
    }

    protected Connection getConnection() throws SQLException {
        try {
            Connection connection = DBConnection.getDataSource().getConnection();
            if (connection == null || connection.isClosed()) {
                LOGGER.error("Error in getting connection, closed or null");
                return null;
            }
            return connection;
        } catch (SQLException e) {
            LOGGER.error("Error in getting connection , err => {}", e.getMessage());
            throw e;
        }
    }
}
