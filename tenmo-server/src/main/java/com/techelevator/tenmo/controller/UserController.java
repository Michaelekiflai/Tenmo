package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getUsers();
    }
    @GetMapping("/accounts/{accountId}/user")
    public User getUserByAccountId(@PathVariable int accountId) {
        return userService.getUserByAccountId(accountId);
    }

}
