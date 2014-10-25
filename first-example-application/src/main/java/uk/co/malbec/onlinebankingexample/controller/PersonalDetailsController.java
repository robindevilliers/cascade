package uk.co.malbec.onlinebankingexample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.co.malbec.onlinebankingexample.model.User;

import javax.servlet.http.HttpSession;

@Controller
public class PersonalDetailsController {

    @Autowired
    private HttpSession httpSession;

    @RequestMapping("/personal")
    public ModelAndView personal() {

        User user = (User) httpSession.getAttribute("user");

        ModelAndView modelAndView = new ModelAndView("personal");
        modelAndView.addObject("displayLogin", false);
        modelAndView.addObject("personalDetails", user.getPersonalDetails());
        return modelAndView;
    }

    @RequestMapping(value = "/edit_address", method = RequestMethod.GET)
    public ModelAndView editAddress() {

        User user = (User) httpSession.getAttribute("user");

        ModelAndView modelAndView = new ModelAndView("edit_address");
        modelAndView.addObject("displayLogin", false);
        modelAndView.addObject("address", user.getPersonalDetails().getAddress());
        return modelAndView;
    }

    @RequestMapping(value = "/save_address", method = RequestMethod.POST)
    public ModelAndView saveAddress(@RequestParam("address") String address) {

        User user = (User) httpSession.getAttribute("user");

        user.getPersonalDetails().setAddress(address);

        ModelAndView modelAndView = new ModelAndView("personal");
        modelAndView.addObject("displayLogin", false);
        modelAndView.addObject("personalDetails", user.getPersonalDetails());
        return modelAndView;
    }

    @RequestMapping(value = "/edit_mobile", method = RequestMethod.GET)
    public ModelAndView editMobile() {

        User user = (User) httpSession.getAttribute("user");

        ModelAndView modelAndView = new ModelAndView("edit_mobile");
        modelAndView.addObject("displayLogin", false);
        modelAndView.addObject("mobile", user.getPersonalDetails().getMobile());
        return modelAndView;
    }

    @RequestMapping(value = "/save_mobile", method = RequestMethod.POST)
    public ModelAndView saveMobile(@RequestParam("mobile") String mobile) {

        User user = (User) httpSession.getAttribute("user");

        user.getPersonalDetails().setMobile(mobile);

        ModelAndView modelAndView = new ModelAndView("personal");
        modelAndView.addObject("displayLogin", false);
        modelAndView.addObject("personalDetails", user.getPersonalDetails());
        return modelAndView;
    }

    @RequestMapping(value = "/edit_email", method = RequestMethod.GET)
    public ModelAndView editEmail() {

        User user = (User) httpSession.getAttribute("user");

        ModelAndView modelAndView = new ModelAndView("edit_email");
        modelAndView.addObject("displayLogin", false);
        modelAndView.addObject("email", user.getPersonalDetails().getEmail());
        return modelAndView;
    }

    @RequestMapping(value = "/save_email", method = RequestMethod.POST)
    public ModelAndView saveEmail(@RequestParam("email") String email) {

        User user = (User) httpSession.getAttribute("user");

        user.getPersonalDetails().setEmail(email);

        ModelAndView modelAndView = new ModelAndView("personal");
        modelAndView.addObject("displayLogin", false);
        modelAndView.addObject("personalDetails", user.getPersonalDetails());
        return modelAndView;
    }
}
