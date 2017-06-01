package com.github.robindevilliers.onlinebankingexample.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;

    private String password;

    private String challengePhrase;

    private List<String> notices = new ArrayList<String>();

    private List<StandingOrder> standingOrders = new ArrayList<StandingOrder>();

    private List<RecentPayment> recentPayments = new ArrayList<RecentPayment>();

    private List<Account> accounts = new ArrayList<Account>();

    private PersonalDetails personalDetails;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChallengePhrase() {
        return challengePhrase;
    }

    public void setChallengePhrase(String challengePhrase) {
        this.challengePhrase = challengePhrase;
    }


    public List<String> getNotices() {
        return notices;
    }

    public void setNotices(List<String> notices) {
        this.notices = notices;
    }

    public List<StandingOrder> getStandingOrders() {
        return standingOrders;
    }

    public void setStandingOrders(List<StandingOrder> standingOrders) {
        this.standingOrders = standingOrders;
    }

    public List<RecentPayment> getRecentPayments() {
        return recentPayments;
    }

    public void setRecentPayments(List<RecentPayment> recentPayments) {
        this.recentPayments = recentPayments;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }
}
