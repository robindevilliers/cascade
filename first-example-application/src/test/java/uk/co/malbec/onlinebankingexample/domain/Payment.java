package uk.co.malbec.onlinebankingexample.domain;

public class Payment {

    private String date;
    private String description;
    private String reference;
    private String accountNumber;
    private String sortCode;
    private String amount;
    private String cleared;

    public String getDate() {
        return date;
    }

    public Payment setDate(String date) {
        this.date = date;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Payment setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public Payment setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Payment setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public String getSortCode() {
        return sortCode;
    }

    public Payment setSortCode(String sortCode) {
        this.sortCode = sortCode;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public Payment setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getCleared() {
        return cleared;
    }

    public Payment setCleared(String cleared) {
        this.cleared = cleared;
        return this;
    }
}