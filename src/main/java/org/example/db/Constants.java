package org.example.db;

public class Constants {
    public enum UserChoice {
        CREATE_USER(1);

        public final int i;

        UserChoice(int i) {
            this.i = i;
        }

        public int getI() {
            return i;
        }
    }

    public enum AccountType {
        WALLET, CARD, BANK;
    }

    public enum PaymentStatus {
        PENDING, SUCCESS, FAILED, CANCELLED;
    }

    public enum TransactionType {
        DEBIT, CREDIT;
    }

    public static final String KEY_JDBC_URL = "JDBC_URL";
    public static final String KEY_JDBC_USER_NAME = "db_user_name";
    public static final String KEY_JDBC_PASSWORD = "db_password";
}
