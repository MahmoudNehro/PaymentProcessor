package org.example.db.user;

import org.example.db.BaseRepository;
import org.example.db.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class UserRepo extends BaseRepository {
    private final Logger LOGGER = LoggerFactory.getLogger(UserRepo.class.getName());

    public void createUser(User user) {
        String spCall = generateProcedureCall("CREATE_USER", 2);

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall(spCall)) {

            long startService = System.nanoTime();

            // CPU: setting params
            stmt.setString(1, user.name());
            stmt.setString(2, user.email());

            long beforeDb = System.nanoTime();

            // IO: DB call (wait time)
            int res = stmt.executeUpdate();

            long afterDb = System.nanoTime();

            long serviceTime = (beforeDb - startService);
            long waitTime = (afterDb - beforeDb);

            double serviceMs = serviceTime / 1_000_000.0;
            double waitMs = waitTime / 1_000_000.0;

            LOGGER.info("Service time = {} ms, Wait time = {} ms", serviceMs, waitMs);

            if (res > 0) {
                LOGGER.info("Successfully created user {}", user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createAccount(Account account) {
        String spCall = generateProcedureCall("CREATE_ACCOUNT", 5);

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall(spCall)) {

            long startService = System.nanoTime();

            // CPU: setting params
            stmt.setInt(1, account.userId());
            stmt.setString(2, account.accountNumber());
            stmt.setObject(3, account.balance());
            stmt.setString(4, account.currency());
            stmt.setObject(5, account.accountType().name());

            long beforeDb = System.nanoTime();

            // IO: DB call (wait time)
            int res = stmt.executeUpdate();

            long afterDb = System.nanoTime();

            long serviceTime = (beforeDb - startService);
            long waitTime = (afterDb - beforeDb);

            double serviceMs = serviceTime / 1_000_000.0;
            double waitMs = waitTime / 1_000_000.0;

            LOGGER.info("Service time = {} ms, Wait time = {} ms", serviceMs, waitMs);

            if (res > 0) {
                LOGGER.info("Successfully created account {}", account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
