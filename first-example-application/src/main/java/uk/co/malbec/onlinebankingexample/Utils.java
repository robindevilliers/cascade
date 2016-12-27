package uk.co.malbec.onlinebankingexample;


import uk.co.malbec.onlinebankingexample.model.AccountType;
import uk.co.malbec.onlinebankingexample.model.User;

public class Utils {
    public static boolean isAccountPresent(User user, AccountType accountType) {
        if (user == null || user.getAccounts() == null) {
            return false;
        }

        return user.getAccounts().stream().anyMatch(account -> account.getType().equals(accountType));
    }
}
