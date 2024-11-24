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

    public Transfer(int transferId, int fromUserId, int toUserId, BigDecimal amount, String otherUsername) {
        this.transferId = transferId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.otherUsername = otherUsername;
    }

    public Transfer() {}

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getAccountFrom() {  // Getter for fromUserId
        return fromUserId;
    }

    public void setAccountFrom(int fromUserId) {  // Setter for fromUserId
        this.fromUserId = fromUserId;
    }

    public int getAccountTo() {  // Getter for toUserId
        return toUserId;
    }

    public void setAccountTo(int toUserId) {  // Setter for toUserId
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
}
