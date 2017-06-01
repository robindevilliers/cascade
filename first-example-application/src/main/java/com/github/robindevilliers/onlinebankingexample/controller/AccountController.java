package com.github.robindevilliers.onlinebankingexample.controller;


import com.github.robindevilliers.onlinebankingexample.Utils;
import com.github.robindevilliers.onlinebankingexample.model.Account;
import com.github.robindevilliers.onlinebankingexample.model.RecentPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.github.robindevilliers.onlinebankingexample.model.AccountType;
import com.github.robindevilliers.onlinebankingexample.model.User;

import javax.servlet.http.HttpSession;

@Controller
public class AccountController {

    @Autowired
    private HttpSession httpSession;

    @RequestMapping("/account/{accountNumber}")
    public ModelAndView account(@PathVariable String accountNumber) {

        User user = (User) httpSession.getAttribute("user");

        Account account = null;
        for (Account a : user.getAccounts()){
            if (a.getNumber().equals(accountNumber)){
                account = a;
                break;
            }
        }




        ModelAndView modelAndView = new ModelAndView("account");
        modelAndView.addObject("displayLogin", false);
        modelAndView.addObject("account", account);
        modelAndView.addObject("currentAccountPresent", Utils.isAccountPresent(user, AccountType.Current));

        if (AccountType.Current.equals(account.getType())){
            Integer available = account.getBalance();
            if (user.getRecentPayments() != null){
                for (RecentPayment recentPayment : user.getRecentPayments()){
                    if (recentPayment.getCleared() == null){
                        available -= recentPayment.getAmount();
                    }
                }
            }

            modelAndView.addObject("available", available);
        }


        return modelAndView;
    }
}
