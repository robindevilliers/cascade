package com.github.robindevilliers.onlinebankingexample.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import com.github.robindevilliers.onlinebankingexample.model.User;

import javax.servlet.http.HttpSession;

@Controller
public class NoticeController {

    @Autowired
    private HttpSession httpSession;

    @RequestMapping("/notice")
    public ModelAndView notice() {

        User user = (User) httpSession.getAttribute("user");

        if (user.getNotices() != null && user.getNotices().size() > 0) {
            String notice = user.getNotices().remove(0);
            ModelAndView modelAndView = new ModelAndView("notice");
            modelAndView.addObject("notice", notice);
            modelAndView.addObject("displayLogin", false);
            return modelAndView;
        } else {
            return new ModelAndView(new RedirectView("/portfolio"));
        }
    }

}

