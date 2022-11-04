package com.akai.hackathon.controller;

import com.akai.hackathon.database.UrlRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UrlRepository repo;
    private final JSONObject json = new JSONObject();

    @GetMapping(value = "/checkSite", consumes = "application/json", produces = "application/json")
    String getResponse(@RequestBody Map<String, String> sentence) {
        //String query = sentence.get("url");
        String response = "65";
//        repo.saveAndFlush(new Urls(1,"google.com",70));
//        repo.saveAndFlush(new Urls(2,"facebook.com",30));
//        repo.saveAndFlush(new Urls(3,"wp.pl",20));

        json.put("response", response);
        return json.toString();
    }
}
