package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferService.class);

    private final TransferDao transferDao;
    private final AccountDao accountDao;
    private final UserService userService;

    public TransferService(TransferDao transferDao, AccountDao accountDao, UserService userService) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userService = userService;
    }

    public List<Transfer> getTransferHistory(int userId) {
        return transferDao.getTransfersByUserId(userId);
    }

    public boolean createTransfer(Transfer transfer) {
        log.info("Initiating transfer from {} to {} for amount {}", transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

        // Validate amount
        if (transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        // Check if sender has enough balance
        BigDecimal senderBalance = accountDao.getBalanceByUserId(transfer.getAccountFrom());
        if (senderBalance.compareTo(transfer.getAmount()) < 0) {
            log.error("Transfer failed: Insufficient balance for user {}", transfer.getAccountFrom());
            throw new RuntimeException("Insufficient balance");
        }

        // Prevent transferring to self
        if (transfer.getAccountFrom() == transfer.getAccountTo()) {
            log.error("Transfer failed: Attempt to transfer to the same account for user {}", transfer.getAccountFrom());
            throw new RuntimeException("Cannot transfer to self");
        }

        // Deduct amount from sender's account
        accountDao.updateAccountBalance(transfer.getAccountFrom(), senderBalance.subtract(transfer.getAmount()));

        // Add amount to receiver's account
        BigDecimal receiverBalance = accountDao.getBalanceByUserId(transfer.getAccountTo());
        accountDao.updateAccountBalance(transfer.getAccountTo(), receiverBalance.add(transfer.getAmount()));
        Account accountFrom=accountDao.findAccountByUserId(transfer.getFromUserId());
        Account accountTo=accountDao.findAccountByUserId(transfer.getToUserId());
        transfer.setAccountFromId(accountFrom.getAccountId());
        transfer.setAccountToId(accountTo.getAccountId());

        boolean success = transferDao.createTransfer(transfer);
        if (success) {
            log.info("Transfer successful: Transfer ID {}", transfer.getTransferId());
        } else {
            log.error("Transfer failed during database operation");
        }

        return success;
    }

    public Transfer getTransferById(int transferId) {
        return transferDao.getTransferById(transferId);
    }

    public void approveOrRejectTransfer(int transferId, int statusId) {
        transferDao.updateTransferStatus(transferId, statusId);
    }

    public int getUserIdByUsername(String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return user.getId();
        } else {
            log.error("User not found with username: {}", username);
            throw new RuntimeException("User not found");
        }
    }

    public List<Transfer> getPendingTransfersByUserId(int userId) {
        return transferDao.findPendingRequestsByUserId(userId);
    }

    public boolean createTransferRequest(Transfer transfer) {
        log.info("Creating transfer request from {} to {} for amount {}", transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

        // Validate amount
        if (transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Request amount must be greater than zero");
        }

        // Prevent requesting from self
        if (transfer.getAccountFrom() == transfer.getAccountTo()) {
            log.error("Transfer request failed: Attempt to request from the same account for user {}", transfer.getAccountFrom());
            throw new RuntimeException("Cannot request from self");
        }

        // Set transfer status to Pending and type to Request
        transfer.setTransferStatusId(1); // Assuming 1 is Pending
        transfer.setTransferTypeId(1); // Assuming 1 is Request

        boolean success = transferDao.createTransfer(transfer);
        if (success) {
            log.info("Transfer request created successfully: Transfer ID {}", transfer.getTransferId());
        } else {
            log.error("Transfer request creation failed during database operation");
        }

        return success;
    }
}
