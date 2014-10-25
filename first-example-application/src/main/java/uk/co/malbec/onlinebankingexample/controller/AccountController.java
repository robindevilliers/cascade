package uk.co.malbec.onlinebankingexample.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.co.malbec.onlinebankingexample.model.Account;
import uk.co.malbec.onlinebankingexample.model.AccountType;
import uk.co.malbec.onlinebankingexample.model.RecentPayment;
import uk.co.malbec.onlinebankingexample.model.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

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

        if (AccountType.Current.equals(account.getType())){
            Integer available = account.getBalance();
            for (RecentPayment recentPayment : user.getRecentPayments()){
                if (recentPayment.getCleared() == null){
                    available -= recentPayment.getAmount();
                }
            }
            modelAndView.addObject("available", available);
        }


        return modelAndView;
    }
}
