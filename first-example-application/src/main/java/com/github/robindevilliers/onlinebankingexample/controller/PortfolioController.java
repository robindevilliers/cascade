package com.github.robindevilliers.onlinebankingexample.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.github.robindevilliers.onlinebankingexample.Utils;
import com.github.robindevilliers.onlinebankingexample.model.AccountType;
import com.github.robindevilliers.onlinebankingexample.model.User;

import javax.servlet.http.HttpSession;

@Controller
public class PortfolioController {

    @Autowired
    private HttpSession httpSession;

    @RequestMapping("/portfolio")
    public ModelAndView portfolio() {

        User user = (User) httpSession.getAttribute("user");

        ModelAndView modelAndView = new ModelAndView("portfolio");
        modelAndView.addObject("displayLogin", false);
        modelAndView.addObject("accounts", user.getAccounts());
        modelAndView.addObject("currentAccountPresent", Utils.isAccountPresent(user, AccountType.Current));
        return modelAndView;
    }

}
