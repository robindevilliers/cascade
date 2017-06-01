package com.github.robindevilliers.onlinebankingexample.domain;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String name;
    private String type;
    private String number;
    private int balance;

    private List<Transaction> transactions = new ArrayList<>();

    public Account(String name, String type, String number, int balance) {
        this.name = name;
        this.type = type;
        this.number = number;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
