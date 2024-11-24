package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        List<User> users = new ArrayList<>();
        while (results.next()) {
            users.add(mapRowToUser(results));
        }
        return users;
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToUser(results);
        }
        return null; // or throw an exception if user not found
    }

    @Override
    public User getUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);

        if (results.next()) {
            return mapRowToUser(results);
        }
        return null;
    }


    @Override
    public User createUser(RegisterUserDto user) {
        if (user.getUsername() == null) {
            throw new DaoException("Username cannot be null");
        }

        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?) RETURNING user_id";
        String passwordHash = new BCryptPasswordEncoder().encode(user.getPassword());

        try {
            int userId = jdbcTemplate.queryForObject(sql, int.class, user.getUsername(), passwordHash);
            return getUserById(userId);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Username already exists", e);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }


    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    @Override
    public User getUserByAccountId(int accountId) {
        String sql = "SELECT u.user_id, u.username, u.password_hash FROM tenmo_user u " +
                "JOIN account a ON u.user_id = a.user_id WHERE a.account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            return mapRowToUser(results);
        }
        return null;
    }


    @Override
    public List<User> getAllUsers() {
        return getUsers(); // If this method should behave the same as getUsers()
    }


    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }
}
