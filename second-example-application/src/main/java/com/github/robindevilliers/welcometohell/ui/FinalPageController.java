package com.github.robindevilliers.welcometohell.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FinalPageController {

    @Autowired
    private HttpSession httpSession;

    @RequestMapping(value = "/final", method = RequestMethod.GET)
    public ModelAndView handleGet()
            throws IOException {

        String wizardSessionId = (String) httpSession.getAttribute("wizardSessionId");

        Map<String, Object> model = new HashMap<>();

        model.put("fontSize", "1.5em");
        model.put("title", "Conclusion");

        return new ModelAndView("final", model);
    }
}
