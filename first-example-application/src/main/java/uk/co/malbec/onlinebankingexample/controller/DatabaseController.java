package uk.co.malbec.onlinebankingexample.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.co.malbec.onlinebankingexample.BackendService;
import uk.co.malbec.onlinebankingexample.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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