package com.akai.hackathon.controller;

import com.akai.hackathon.database.SessionManager;
import com.akai.hackathon.database.UrlRepository;
import com.akai.hackathon.database.Urls;
import com.akai.hackathon.database.UserRepository;
import com.akai.hackathon.database.*;
import com.akai.hackathon.urlRater.TieHenClass;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.akai.hackathon.database.SessionManager.SHA3String;

@CrossOrigin(origins = {"http://localhost:4000/", "http://150.254.40.15", "http://150.254.40.14:4000", "http://150.254.40.15:4000", "chrome-extension://cohkddidpgdnmeladpmlabmhnlgpmdgd"})
@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UrlRepository urlRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    SessionManager sessionManager;
    @Autowired
    SpecialistsRateRepository srRepo;

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
    @CrossOrigin(origins = {"http://localhost:4000/", "http://150.254.40.15", "http://150.254.40.14:4000", "http://150.254.40.15:4000", "chrome-extension://cohkddidpgdnmeladpmlabmhnlgpmdgd"})
    String getResponse(@RequestBody Map<String, String> sentence) {
        String url = sentence.get("url");
        int response;
        int occurrences;

        Optional<Urls> opt = urlRepo.findById(url);
        if (opt.isPresent()) {
            response = opt.get().getRating();
            occurrences = opt.get().getOccurrences();
        } else {
            response = rand.nextInt(101);
            //response = (new TieHenClass()).rateUrl(url);
            occurrences = 1;
            urlRepo.saveAndFlush(new Urls(url, response, 1));
        }

        json.put("percent", response);
        json.put("occurrences", occurrences);
        return json.toString();
    }

    @GetMapping(value = "/test")
    @CrossOrigin(origins = {"http://localhost:4000/", "http://150.254.40.15", "http://150.254.40.14:4000", "http://150.254.40.15:4000", "chrome-extension://cohkddidpgdnmeladpmlabmhnlgpmdgd"})
    void test() {
        tieHenClass.rateUrl("https://www.gov.pl/");
    }

    //@PostMapping(value = "/addUser", consumes = "application/json", produces = "application/json")
    //String newUser(@RequestBody Map<String, String> sentence) {
    //    String response;
    //    if (userRepo.existsById(sentence.get("name"))) {
    //        response = "User already exists.";
    //    } else {
    //        User user = new User(sentence.get("name"), SHA3String(sentence.get("password")), sentence.get("category"));
    //        userRepo.saveAndFlush(user);
    //        response = "OK";
    //    }
    //
    //    json.put("status", response);
    //    return json.toString();
    //}

    @PostMapping(value = "/checkUser", consumes = "application/json")
    @CrossOrigin(origins = {"http://localhost:4000/", "http://150.254.40.15", "http://150.254.40.14:4000", "http://150.254.40.15:4000", "chrome-extension://cohkddidpgdnmeladpmlabmhnlgpmdgd"})
    String checkUser(@RequestBody Map<String, String> sentence) {
        if (userRepo.findById(sentence.get("name")).isPresent()) {
            String passwordHash = userRepo.findById(sentence.get("name")).get().getPassword();
            String passwordHashFromUser = SHA3String(sentence.get("password"));

            if (passwordHash.equals(passwordHashFromUser)) {
                updateUserSession(sentence.get("name"));
                return sessionManager.getSession(sentence.get("name")).getToken();
            }
            else {
                return "Wrong password";
            }
        }

        return "User does not exist";
    }

//    @ResponseStatus(value = HttpStatus.FORBIDDEN)
//    static public class MissingTokenException extends RuntimeException {
//        public MissingTokenException() {
//            super();
//        }
//        public MissingTokenException(String message, Throwable cause) {
//            super(message, cause);
//        }
//        public MissingTokenException(String message) {
//            super(message);
//        }
//        public MissingTokenException(Throwable cause) {
//            super(cause);
//        }
//    }

    @GetMapping(value = "/urlData", produces = "application/json")
    @CrossOrigin(origins = {"http://localhost:4000/", "http://150.254.40.15", "http://150.254.40.14:4000", "http://150.254.40.15:4000", "chrome-extension://cohkddidpgdnmeladpmlabmhnlgpmdgd"})
    String getUrlData(HttpServletRequest request)
    {
        json.put("status", "ok");

        if (request.getCookies() == null)
            return "{\"status\":\"wynocha1\"}";
        Map<String, String> cookies = Arrays.stream(request.getCookies())
                .collect(Collectors.toMap(
                        Cookie::getName,
                        Cookie::getValue));
        Optional<String> token = Optional.ofNullable(cookies.get("sessionToken"));
        Optional<String> user = Optional.ofNullable(cookies.get("userName"));

        try{
            if (token.isEmpty() || user.isEmpty() || !sessionManager.checkAuthorization(token.get()).equals(user.get()))
            {
                return "{\"status\":\"wynocha2\"}";
            }
        } catch (SessionManager.SessionManagerException e) {
            return "{\"status\":\"wynocha3\"}";
        }

        AtomicInteger i = new AtomicInteger();
        urlRepo.findAll().stream()
                .sorted((url1, url2) ->
                        Integer.compare(url2.getOccurrences(), url1.getOccurrences()))
                .forEach( url -> {
                    JSONObject jsonChild = new JSONObject();
                    jsonChild.put("url", url.getUrl());
                    jsonChild.put("rating", url.getRating());
                    jsonChild.put("occurrences", url.getOccurrences());
                    json.put(Integer.toString(i.getAndIncrement()), jsonChild);
                });
        return json.toString();
    }

    @PostMapping(value = "/addRatetoDB", consumes = "application/json", produces = "application/json")
    @CrossOrigin(origins = {"http://localhost:4000/", "http://150.254.40.15", "http://150.254.40.14:4000", "http://150.254.40.15:4000", "chrome-extension://cohkddidpgdnmeladpmlabmhnlgpmdgd"})
    String testEndpoint(@RequestBody Map<String, String> sentence) {
        System.out.println(sentence);
        String username = sentence.get("username");
        String url = sentence.get("url");
        String ratePositive = sentence.get("ratePositive");

        SpecialistsRate tmp = new SpecialistsRate(username, url, Boolean.getBoolean(ratePositive));
        srRepo.saveAndFlush(tmp);

            return json.toString();
    }


    private void updateUserSession(String username) {
        sessionManager.tokenRenewal(username);
    }
}
