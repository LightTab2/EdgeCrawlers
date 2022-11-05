package com.akai.hackathon.controller;

import com.akai.hackathon.database.UrlRepository;
import com.akai.hackathon.database.Urls;
import com.akai.hackathon.urlRater.TieHenClass;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UrlRepository repo;
    private final JSONObject json = new JSONObject();
    Random rand = new Random();

    @PostMapping(value = "/checkSite", consumes = "application/json", produces = "application/json")
    String getResponse(@RequestBody Map<String, String> sentence) {
        String url = sentence.get("url");
        int response;

        Optional<Urls> opt = repo.findById(url);
        if (opt.isPresent()) {
            response = opt.get().getRating();
        } else {
            response = rand.nextInt(101);
            repo.saveAndFlush(new Urls(url, response));
        }

        json.put("percent", response);
        return json.toString();
    }

    @GetMapping(value = "/test")
    void test() {
        TieHenClass.rateUrl("https://www.gov.pl/");
    }
}
