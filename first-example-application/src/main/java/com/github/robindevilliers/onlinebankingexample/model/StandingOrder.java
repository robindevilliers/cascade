package com.github.robindevilliers.onlinebankingexample.model;


import java.util.Date;

public class StandingOrder {

    private Integer id;
    private Date dueDate;
    private String description;
    private String reference;
    private StandingOrderPeriod period;
    private Integer amount;

    private String accountNumber;
    private String sortCode;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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

    public StandingOrderPeriod getPeriod() {
        return period;
    }

    public void setPeriod(StandingOrderPeriod period) {
        this.period = period;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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
}
