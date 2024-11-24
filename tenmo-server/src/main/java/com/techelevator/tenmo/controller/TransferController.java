package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {
    private static final Logger log = LoggerFactory.getLogger(TransferController.class);


    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping
    public List<Transfer> getTransferHistory(Authentication authentication) {
        String username = authentication.getName();
        int userId = transferService.getUserIdByUsername(username);
        return transferService.getTransferHistory(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransfer(@RequestBody Transfer transfer) {
        log.info("........inside create transfer............");
        transferService.createTransfer(transfer);
    }



    @GetMapping("/{id}")
    public Transfer getTransferById(@PathVariable int id) {
        return transferService.getTransferById(id);
    }
    @GetMapping("/pending")
    public List<Transfer> getPendingRequests(Authentication authentication) {
        String username = authentication.getName();
        int userId = transferService.getUserIdByUsername(username);
        return transferService.getPendingTransfersByUserId(userId);
    }
    @PostMapping("/request")
    public ResponseEntity<Void> requestBucks(@RequestBody Transfer transfer) {
        transferService.createTransferRequest(transfer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
