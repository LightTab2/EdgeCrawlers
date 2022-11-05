package com.akai.hackathon.controller;

import com.akai.hackathon.database.UrlRepository;
import com.akai.hackathon.database.Urls;
import com.akai.hackathon.database.User;
import com.akai.hackathon.database.UserRepository;
import com.akai.hackathon.urlRater.TieHenClass;
import org.json.JSONArray;
import lombok.AllArgsConstructor;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
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

    @Autowired
    TieHenClass tieHenClass;
    private final JSONObject json = new JSONObject();
    Random rand = new Random();

    @AllArgsConstructor
    private class TokenTime {
        public String token;
        public Instant expireTime;
    }
    private Map<String, TokenTime> userSessions = new HashMap<>();

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
        tieHenClass.rateUrl("https://www.gov.pl/");
    }

    @PostMapping(value = "/addUser", consumes = "application/json", produces = "application/json")
    String newUser(@RequestBody Map<String, String> sentence) {
        String response;
        if (userRepo.existsById(sentence.get("name"))) {
            response = "User already exists.";
        } else {
            User user = new User(sentence.get("name"), SHA3String(sentence.get("password")), sentence.get("category"));
            userRepo.saveAndFlush(user);
            response = "OK";
        }

        json.put("status", response);
        return json.toString();
    }

    private String SHA3String(String text) {
        SHA3.DigestSHA3 sha3 = new SHA3.Digest256();
        sha3.update(text.getBytes());
        StringBuilder passwordBuilder = new StringBuilder();

        for (byte b : sha3.digest()) {
            passwordBuilder.append(String.format("%02x", b & 0xFF));
        }

        return passwordBuilder.toString();
    }

    @PostMapping(value = "/checkUser", consumes = "application/json", produces = "application/json")
    String checkUser(@RequestBody Map<String, String> sentence) {
        if (userRepo.findById(sentence.get("name")).isPresent()) {
            String passwordHash = userRepo.findById(sentence.get("name")).get().getPassword();
            String passwordHashFromUser = SHA3String(sentence.get("password"));

            if (passwordHash.equals(passwordHashFromUser)) {
                updateUserSession(sentence.get("name"));
                return userSessions.get(sentence.get("name")).token;
            }
            else {
                return "Wrong password";
            }
        }

        return "User does not exist";
    }

    private void updateUserSession(String username) {
        if(userSessions.containsKey(username)) {
            userSessions.replace(username, new TokenTime(userSessions.get(username).token, Instant.now().plus(2, ChronoUnit.HOURS)));
        } else {
            String token = SHA3String(String.valueOf(Instant.now().toEpochMilli()));
            userSessions.put(username, new TokenTime(token, Instant.now().plus(2, ChronoUnit.HOURS)));
        }
    }
}
