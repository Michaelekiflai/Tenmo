package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Arrays;

public class AccountService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    // Constructor to accept RestTemplate and baseUrl
    public AccountService(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    // Define the getBalance method
    public BigDecimal getBalance(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<BigDecimal> response = restTemplate.exchange(
                baseUrl + "/account/balance", HttpMethod.GET, entity, BigDecimal.class);

        return response.getBody();
    }

}