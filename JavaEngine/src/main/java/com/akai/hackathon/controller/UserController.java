package com.akai.hackathon.controller;

import com.akai.hackathon.database.UrlRepository;
import com.akai.hackathon.database.Urls;
import com.akai.hackathon.database.User;
import com.akai.hackathon.database.UserRepository;
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
    UrlRepository urlRepo;

    @Autowired
    UserRepository userRepo;
    private final JSONObject json = new JSONObject();
    Random rand = new Random();

    @PostMapping(value = "/checkSite", consumes = "application/json", produces = "application/json")
    String getResponse(@RequestBody Map<String, String> sentence) {
        String url = sentence.get("url");
        int response;

        Optional<Urls> opt = urlRepo.findById(url);
        if (opt.isPresent()) {
            response = opt.get().getRating();
        } else {
            response = rand.nextInt(101);
            urlRepo.saveAndFlush(new Urls(url, response));
        }

        json.put("percent", response);
        return json.toString();
    }

    @GetMapping(value = "/test")
    void test() {
        TieHenClass.rateUrl("https://www.gov.pl/");
    }

    @PostMapping(value = "/addUser", consumes = "application/json", produces = "application/json")
    String newUser(@RequestBody Map<String, String> sentence) {
        String response;
        if (userRepo.existsById(sentence.get("name"))) {
            response = "User already exists.";
        } else {
            User user = new User(sentence.get("name"), sentence.get("password"), sentence.get("category"));
            userRepo.saveAndFlush(user);
            response = "OK";
        }

        json.put("status", response);
        return json.toString();
    }

    @PostMapping(value = "/checkUser", consumes = "application/json", produces = "application/json")
    User checkUser(@RequestBody Map<String, String> sentence) {
        if (userRepo.findById(sentence.get("name")).isPresent()) {
            return userRepo.findById(sentence.get("name")).get();
        }
        return null;
    }
}
