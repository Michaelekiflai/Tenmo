package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class UserService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public UserService(RestTemplate restTemplate, String apiBaseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = apiBaseUrl;
    }

    // Method to get a user by username
    public User getUserByUsername(AuthenticatedUser currentUser, String username) {
        String url = baseUrl + "/users/" + username;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, entity, User.class);
        return response.getBody();
    }

    // Method to get all users
    public List<User> getAllUsers(AuthenticatedUser currentUser) {
        String url = baseUrl + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
        return Arrays.asList(response.getBody());
    }

    // Method to get a user by ID
    public User getUserById(AuthenticatedUser currentUser, int recipientId) {
        String url = baseUrl + "/users/id/" + recipientId; // Assuming the endpoint for fetching user by ID is /users/id/{id}
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, entity, User.class);
        return response.getBody();
    }
    public User getUserByAccountId(AuthenticatedUser currentUser, int accountId) {
        String url = baseUrl + "/accounts/" + accountId + "/user";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, entity, User.class);
        return response.getBody();
    }

}