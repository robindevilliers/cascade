package com.github.robindevilliers.onlinebankingexample.domain;

public class Transaction {
    private String date;
    private String description;
    private String type;
    private String amount;

    public Transaction(String date, String description, String type, String amount) {
        this.date = date;
        this.description = description;
        this.type = type;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
