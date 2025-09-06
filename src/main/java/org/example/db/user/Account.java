package org.example.db.user;

import org.example.db.Constants.AccountType;

import java.math.BigDecimal;

public record Account(int userId, String accountNumber, BigDecimal balance, String currency, AccountType accountType) {
}
