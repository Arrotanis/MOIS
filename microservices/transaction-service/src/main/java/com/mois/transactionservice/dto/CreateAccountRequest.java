package com.mois.transactionservice.dto;

public class CreateAccountRequest {
    private Long ownerAccountId;

    // Standard getters and setters
    public Long getOwnerAccountId() {
        return ownerAccountId;
    }

    public void setOwnerAccountId(Long ownerAccountId) {
        this.ownerAccountId = ownerAccountId;
    }
}
