package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao {

    // Retrieve all users
    List<User> getUsers();

    // Retrieve a user by their ID
    User getUserById(int id);

    // Retrieve a user by their username
    User getUserByUsername(String username);

    // Create a new user
    User createUser(RegisterUserDto user);

    // Retrieve the balance of a user by their account ID
    BigDecimal getBalanceByUserId(int userId);

    List<User> getAllUsers();

    User getUserByAccountId(int accountId);

}
