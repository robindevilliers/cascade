package com.github.robindevilliers.onlinebankingexample.controller;


import com.github.robindevilliers.onlinebankingexample.BackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.robindevilliers.onlinebankingexample.model.User;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/database/set-user")
public class DatabaseController {


    @Autowired
    private BackendService backendService;


    @RequestMapping(method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map setData(@RequestBody User user) {
        backendService.setUser(user);

        return new HashMap<String, String>(){{put("success","ok");}};
    }

}