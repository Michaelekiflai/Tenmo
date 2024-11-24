package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
    BigDecimal getBalance(int userId);

    BigDecimal getBalanceByUserId(int userId);

    Account findAccountByUserId(int userId);

    boolean updateAccountBalance(int userId, BigDecimal newBalance);
}

