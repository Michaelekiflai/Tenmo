package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TransferService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public TransferService(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    // Method to get transfer history for the authenticated user
    public List<Transfer> getTransferHistory(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Transfer[]> response = restTemplate.exchange(
                baseUrl + "/transfers", HttpMethod.GET, entity, Transfer[].class);

        if (response.getBody() == null) {
            return List.of(); // Return an empty list if no transfers are found
        }

        return Arrays.asList(response.getBody());
    }

    // Method to get pending transfer requests for the authenticated user
    public List<Transfer> getPendingRequests(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Transfer[]> response = restTemplate.exchange(
                baseUrl + "/transfers/pending", HttpMethod.GET, entity, Transfer[].class);

        if (response.getBody() == null) {
            return List.of(); // Return an empty list if no pending requests are found
        }

        return Arrays.asList(response.getBody());
    }

    // Method to send TE bucks
    public boolean sendBucks(AuthenticatedUser currentUser, int toUserId, BigDecimal amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());

        Transfer transfer = new Transfer();
        transfer.setAccountFrom(currentUser.getUser().getId());
        transfer.setAccountTo(toUserId);
        transfer.setAmount(amount);
        transfer.setTransferTypeId(2); // Assuming 2 is for "Send" type
        transfer.setTransferStatusId(2); // Assuming 2 is for "Approved" status

        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        ResponseEntity<Transfer> response = restTemplate.exchange(
                baseUrl + "/transfers", HttpMethod.POST, entity, Transfer.class);

        return response.getStatusCode().is2xxSuccessful();
    }

    // Method to request TE bucks
    public boolean requestBucks(AuthenticatedUser currentUser, int fromUserId, BigDecimal amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());

        Transfer transfer = new Transfer();
        transfer.setAccountFrom(fromUserId);
        transfer.setAccountTo(currentUser.getUser().getId());
        transfer.setAmount(amount);
        transfer.setTransferTypeId(1); // Assuming 1 is for "Request" type
        transfer.setTransferStatusId(1); // Assuming 1 is for "Pending" status

        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        ResponseEntity<Transfer> response = restTemplate.exchange(
                baseUrl + "/transfers", HttpMethod.POST, entity, Transfer.class);

        return response.getStatusCode().is2xxSuccessful();
    }

    // Method to update transfer status (e.g., approve or reject a request)
    public boolean updateTransferStatus(AuthenticatedUser currentUser, int transferId, int statusId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());

        String url = baseUrl + "/transfers/" + transferId + "/status/" + statusId;
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                url, HttpMethod.PUT, entity, Void.class);

        return response.getStatusCode().is2xxSuccessful();
    }

    // Method to create a transfer (implementation added)
    public boolean createTransfer(AuthenticatedUser currentUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        // If using authentication, set the bearer token here
        // headers.setBearerAuth(currentUser.getToken()); // If AuthenticatedUser is needed

        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        ResponseEntity<Transfer> response = restTemplate.exchange(
                baseUrl + "/transfers", HttpMethod.POST, entity, Transfer.class);

        return response.getStatusCode().is2xxSuccessful();  // Return true if the request was successful
    }

    public boolean createTransferRequest(AuthenticatedUser currentUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken()); // Set the bearer token for authentication

        // Set the transfer type and status for a request
        transfer.setTransferTypeId(1); // Assuming 1 is for "Request" type
        transfer.setTransferStatusId(1); // Assuming 1 is for "Pending" status

        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        ResponseEntity<Transfer> response = restTemplate.exchange(
                baseUrl + "/transfers/request", HttpMethod.POST, entity, Transfer.class);

        return response.getStatusCode().is2xxSuccessful();  // Return true if the request was successful
    }

    public Transfer getTransferById(int transferId) {
        HttpHeaders headers = new HttpHeaders();
        AuthenticatedUser currentUser = null;
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Transfer> response = restTemplate.exchange(
                baseUrl + "/transfers/" + transferId, HttpMethod.GET, entity, Transfer.class);

        return response.getBody();
    }

}
