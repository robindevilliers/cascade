package com.github.robindevilliers.onlinebankingexample.model;


import java.util.Date;

public class RecentPayment {
    private Date date;
    private String description;
    private String reference;
    private String accountNumber;
    private String sortCode;
    private Integer amount;
    private Date cleared;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getCleared() {
        return cleared;
    }

    public void setCleared(Date cleared) {
        this.cleared = cleared;
    }
}
