package com.akai.hackathon.controller;

import com.akai.hackathon.urlRater.TieHenClass;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
public class userController {
    private final JSONObject json = new JSONObject();

    @GetMapping(value = "/checkSite", consumes = "application/json", produces = "application/json")
    String getResponse(@RequestBody Map<String, String> sentence) {
        //String query = sentence.get("url");
        String response = "65";

        json.put("response", response);
        return json.toString();
    }

    @GetMapping(value = "/test")
    void test() {
        TieHenClass.rateUrl("https://www.gov.pl/");
    }
}