package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class AccountService {
    private final AccountDao accountDao;
    private final UserDao userDao; // Inject UserDao

    public AccountService(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao; // Initialize UserDao
    }

    public BigDecimal getBalance(int userId) {
        return accountDao.getBalanceByUserId(userId);
    }

    public Account findAccountByUserId(int userId) {
        return accountDao.findAccountByUserId(userId);
    }

    public boolean updateAccountBalance(int userId, BigDecimal newBalance) {
        return accountDao.updateAccountBalance(userId, newBalance);
    }

    public User getUserByAccountId(int accountId) {
        return userDao.getUserByAccountId(accountId); // Delegate to UserDao
    }
}

