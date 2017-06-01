package com.github.robindevilliers.onlinebankingexample;


import com.github.robindevilliers.onlinebankingexample.model.AccountType;
import com.github.robindevilliers.onlinebankingexample.model.User;

public class Utils {
    public static boolean isAccountPresent(User user, AccountType accountType) {
        if (user == null || user.getAccounts() == null) {
            return false;
        }

        return user.getAccounts().stream().anyMatch(account -> account.getType().equals(accountType));
    }
}
