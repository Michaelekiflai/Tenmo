package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private int fromUserId;  // or accountFrom
    private int toUserId;    // or accountTo
    private BigDecimal amount;
    private String otherUsername;
    private int transferTypeId;
    private int transferStatusId;
    private int accountFromId;
    private int accountToId;

    public int getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(int accountFromId) {
        this.accountFromId = accountFromId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(int accountToId) {
        this.accountToId = accountToId;
    }

    // Constructor
    public Transfer() {}

    public Transfer(int transferId, int fromUserId, int toUserId, BigDecimal amount, String otherUsername, int transferTypeId, int transferStatusId) {
        this.transferId = transferId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.otherUsername = otherUsername;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
    }

    // Getters and Setters
    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getAccountFrom() {  // or getFromUserId()
        return fromUserId;
    }

    public void setAccountFrom(int fromUserId) {  // or setFromUserId()
        this.fromUserId = fromUserId;
    }

    public int getAccountTo() {  // or getToUserId()
        return toUserId;
    }

    public void setAccountTo(int toUserId) {  // or setToUserId()
        this.toUserId = toUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    public void setOtherUsername(String otherUsername) {
        this.otherUsername = otherUsername;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

}
