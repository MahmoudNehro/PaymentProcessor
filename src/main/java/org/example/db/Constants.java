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
}
