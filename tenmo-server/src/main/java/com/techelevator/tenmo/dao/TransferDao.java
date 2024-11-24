package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    // Method to get all transfers associated with a user (both sent and received)
    List<Transfer> getTransfersByUserId(int userId);

    // Method to find pending transfer requests for a user
    List<Transfer> findPendingRequestsByUserId(int userId);

    // Method to create a new transfer
    boolean createTransfer(Transfer transfer);

    // Method to get a specific transfer by its ID
    Transfer getTransferById(int transferId);

    // Method to update the status of a transfer
    void updateTransferStatus(int transferId, int statusId);
}
