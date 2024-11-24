package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.AccountService;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
public class AccountController {


    private final AccountService accountService;
    private final UserDao userDao;

    public AccountController(AccountService accountService, UserDao userDao) {
        this.accountService = accountService;
        this.userDao = userDao;
    }

    @GetMapping("/balance")
    public BigDecimal getBalance(Authentication authentication) {
        try {
            String username = authentication.getName(); // Get the username from the authentication object
            User user = userDao.getUserByUsername(username); // Retrieve the User object using the username
            if (user == null) {
                throw new RuntimeException("User not found.");
            }
            return accountService.getBalance(user.getId()); // Delegate to AccountService to get the balance
        } catch (Exception e) {
            // Handle exceptions as needed
            throw new RuntimeException("Failed to retrieve balance: " + e.getMessage());
        }
    }

    @GetMapping("/{accountId}/user")
    public ResponseEntity<User> getUserByAccountId(@PathVariable int accountId) {
        User user = accountService.getUserByAccountId(accountId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

