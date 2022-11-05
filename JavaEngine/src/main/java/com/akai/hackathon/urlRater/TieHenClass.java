package com.akai.hackathon.urlRater;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.akai.hackathon.database.UrlRepository;
import com.akai.hackathon.database.Urls;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

enum DomainRates {
    Whitelist,
    Pass,
    Deny
}

public class TieHenClass {

    @Autowired
    static UrlRepository repo;

    static double decayFactor = 0.95;

    public static int rateUrl(String url) {
        int rating = 0;

        // Rate domain
        DomainRates domainClassification = rateDomain(url);
        switch (domainClassification) {
            case Whitelist:
                System.out.printf("Website %s is whitelisted%n", url);
                repo.saveAndFlush(new Urls(url, 100));
                break;
            case Pass:
                System.out.printf("Website %s is passed%n", url);
                break;
            case Deny:
                System.out.printf("Website %s is denied%n", url);
                break;
        }

//        try {
//            traversePage(url);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }

//        try {
//            temp(url);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }

//        try {
//            Runtime.getRuntime().exec("python ./src/main/resources/run_this.py https://www.gov.pl/");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return rating;
    }

    private static void temp(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");

        for (Element linkText : links) {
//            System.out.println(linkText.toString());
            if (linkText.toString().contains("http")) {
                System.out.println(linkText);
            }
        }

    }

    private static void basicTrustRank(String url) {
        List<String> urls = grabUrls(url);

        Map<String, Integer> domainMap = new HashMap<>();

        // Count domain appearances
        Pattern patternDomainExtract = Pattern.compile("^(?:(?:https|http):\\/\\/)(?:www\\.)?(\\w+\\.\\w+(?:\\.\\w+)*)");
        Matcher domainMatcher;
        for (String link : urls) {
            domainMatcher = patternDomainExtract.matcher(link);
            String temp = domainMatcher.group(1);
            Optional.ofNullable(domainMap.get(temp))
                    .map(count -> domainMap.replace(temp, count + 1))
                    .orElseGet(() -> domainMap.put(temp, 1));
        }

        // Calculate weights
        Map<String, Integer> databaseTrustRanks = repo.findAllById(domainMap.keySet()).stream().collect(Collectors.toMap(com.akai.hackathon.database.Urls::getUrl, com.akai.hackathon.database.Urls::getRating));
        for (String key : domainMap.keySet()) {
            domainMap.replace(key, domainMap.get(key) * databaseTrustRanks.getOrDefault(key, 0));
        }

        domainMatcher = patternDomainExtract.matcher(url);
        repo.saveAndFlush(new Urls(domainMatcher.group(1), domainMap.values().stream().reduce(0, Integer::sum) / urls.size()));
    }

    private static List<String> grabUrls(String url) {
        return List.of();
    }

    private static DomainRates rateDomain(String url) {
        // Compile pattern
        Pattern patternDomainExtract = Pattern.compile("\\.(\\w+)");
        Matcher domainMatcher = patternDomainExtract.matcher(url);

        while (domainMatcher.find()) {
            if (trustedDomains.containsKey(domainMatcher.group(1))) {
                return DomainRates.Whitelist;
            }
        }


        // TODO blacklist some websites. For now all are passed
        return DomainRates.Pass;
    }

    private static int traversePage(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc.title());

        Elements textSources = doc.select("p,h1,h2,h3,h4,h5,h6");
        for (Element text : textSources) {
            System.out.println(text.toString());
        }

        return 0;
    }

    private static HashMap<String, Integer> trustedDomains = new HashMap<>(Map.of(
            "edu", 1, "gov", 1
    ));

    private static HashMap<String, HashMap<String, Integer>> blacklistedCombinations;
}
