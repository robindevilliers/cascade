package uk.co.malbec.onlinebankingexample.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GeneralController {
    @RequestMapping("/view/{viewName}")
    public ModelAndView main(@PathVariable("viewName") String viewName) {

        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addObject("displayLogin", false);
        return modelAndView;
    }

}
