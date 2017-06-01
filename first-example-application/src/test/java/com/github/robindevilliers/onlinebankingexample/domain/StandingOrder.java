package com.github.robindevilliers.onlinebankingexample.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class StandingOrder {

    private String id;
    private String dueDate;
    private String description;
    private String reference;
    private String period;
    private String amount;
    private String accountNumber;
    private String sortCode;

    public StandingOrder(){

    }

    public StandingOrder(StandingOrder standingOrder) {
        this.id = standingOrder.id;
        this.dueDate = standingOrder.dueDate;
        this.description = standingOrder.description;
        this.reference = standingOrder.reference;
        this.period = standingOrder.period;
        this.amount = standingOrder.amount;
        this.accountNumber = standingOrder.accountNumber;
        this.sortCode = standingOrder.sortCode;
    }

    public String getId() {
        return id;
    }

    public StandingOrder setId(String id) {
        this.id = id;
        return this;
    }

    public String getDueDate() {
        return dueDate;
    }

    public StandingOrder setDueDate(String dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public StandingOrder setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public StandingOrder setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public String getPeriod() {
        return period;
    }

    public StandingOrder setPeriod(String period) {
        this.period = period;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public StandingOrder setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public StandingOrder setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public String getSortCode() {
        return sortCode;
    }

    public StandingOrder setSortCode(String sortCode) {
        this.sortCode = sortCode;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StandingOrder that = (StandingOrder) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(dueDate, that.dueDate)
                .append(description, that.description)
                .append(reference, that.reference)
                .append(period, that.period)
                .append(amount, that.amount)
                .append(accountNumber, that.accountNumber)
                .append(sortCode, that.sortCode)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(dueDate)
                .append(description)
                .append(reference)
                .append(period)
                .append(amount)
                .append(accountNumber)
                .append(sortCode)
                .toHashCode();
    }
}
