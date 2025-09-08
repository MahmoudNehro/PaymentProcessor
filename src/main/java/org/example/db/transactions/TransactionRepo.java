package org.example.db.transactions;

import org.example.db.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class TransactionRepo extends BaseRepository {
    private final Logger LOGGER = LoggerFactory.getLogger(TransactionRepo.class.getName());

    public void makeTransaction(Transaction transaction) {
        String spCall = generateProcedureCall("MAKE_TRANSACTION", 7);

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall(spCall)) {

            long startService = System.nanoTime();

            stmt.setString(1, transaction.accountNumber());
            stmt.setObject(2, transaction.amount());
            stmt.setString(3, transaction.currency());
            stmt.setObject(4, transaction.type().name());
            stmt.setObject(5, transaction.referenceId().toString());
            stmt.registerOutParameter(6, Types.INTEGER);
            stmt.registerOutParameter(7, Types.VARCHAR);

            long beforeDb = System.nanoTime();

            stmt.executeUpdate();

            long afterDb = System.nanoTime();

            long serviceTime = (beforeDb - startService);
            long waitTime = (afterDb - beforeDb);

            double serviceMs = serviceTime / 1_000_000.0;
            double waitMs = waitTime / 1_000_000.0;

            LOGGER.info("Service time = {} ms, Wait time = {} ms", serviceMs, waitMs);

            if (stmt.getInt("status_code") == 200) {
                LOGGER.info("Successfully made transaction! {}", transaction);
            } else {
                LOGGER.error("Failed to make transaction with amount {} and error {}", transaction.amount(), stmt.getString("status_msg"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
