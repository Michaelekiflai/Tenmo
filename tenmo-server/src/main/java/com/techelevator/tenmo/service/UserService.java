package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public List<User> getAllUsersExceptCurrent(int currentUserId) {
        List<User> users = userDao.getUsers();
        // Remove the current user from the list
        users.removeIf(user -> user.getId() == currentUserId);
        return users;
    }

    public User getUserByAccountId(int accountId) {
        return userDao.getUserByAccountId(accountId);
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }
}
