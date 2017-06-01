package com.github.robindevilliers.onlinebankingexample.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import com.github.robindevilliers.onlinebankingexample.BackendService;
import com.github.robindevilliers.onlinebankingexample.RandomNumberService;
import com.github.robindevilliers.onlinebankingexample.model.User;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private BackendService backendService;

    @Autowired
    private RandomNumberService randomNumberService;

    @Autowired
    private HttpSession httpSession;

    @RequestMapping("/authenticate")
    public ModelAndView authenticate(@RequestParam("username") String username, @RequestParam("password") String password) {


        User user = backendService.getUser(username);

        if (user != null && user.getPassword().equals(password)) {

            httpSession.setAttribute("user", user);


            int length = user.getChallengePhrase().length();

            List<Integer> challenge = new ArrayList();

            for (int i = 0; i < 3; i++) {
                int candidate = randomNumberService.getRandomNumber() % length;

                while (challenge.contains(candidate)) {
                    candidate = randomNumberService.getRandomNumber() % length;
                }
                challenge.add(candidate);
            }

            Collections.sort(challenge);

            httpSession.setAttribute("challenge", challenge);


            ModelAndView modelAndView = new ModelAndView("challenge");
            modelAndView.addObject("displayLogin", false);
            modelAndView.addObject("numberOne", challenge.get(0) + 1);
            modelAndView.addObject("numberTwo", challenge.get(1) + 1);
            modelAndView.addObject("numberThree", challenge.get(2) + 1);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("index");
            modelAndView.addObject("displayLogin", true);
            modelAndView.addObject("displayFailedModal", true);

            return modelAndView;
        }


    }

    @RequestMapping("/answer")
    public ModelAndView answer(@RequestParam("input1") String input1, @RequestParam("input2") String input2, @RequestParam("input3") String input3) {

        User user = (User) httpSession.getAttribute("user");
        List<Integer> challenge = (List<Integer>) httpSession.getAttribute("challenge");

        String challengePhrase = user.getChallengePhrase();
        if (challengePhrase.charAt(challenge.get(0)) == input1.charAt(0) &&
                challengePhrase.charAt(challenge.get(1)) == input2.charAt(0) &&
                challengePhrase.charAt(challenge.get(2)) == input3.charAt(0)) {

            httpSession.setAttribute("authenticated", true);
            if (user.getNotices() != null && user.getNotices().size() > 0){
                String notice = user.getNotices().remove(0);
                ModelAndView modelAndView = new ModelAndView("notice");
                modelAndView.addObject("notice", notice);
                modelAndView.addObject("displayLogin", false);
                return modelAndView;
            } else {

                return new ModelAndView(new RedirectView("/portfolio"));
            }

        } else {
            httpSession.setAttribute("user", null);
            httpSession.setAttribute("authenticated", null);
            ModelAndView modelAndView = new ModelAndView("index");
            modelAndView.addObject("displayLogin", true);
            modelAndView.addObject("displayFailedModal", true);
            return modelAndView;
        }


    }
}