package com.akai.hackathon.urlRater;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.akai.hackathon.database.UrlRepository;
import com.akai.hackathon.database.Urls;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

enum DomainRates {
    Whitelist,
    Pass,
    Deny
}


@Component
public class TieHenClass {

    @Autowired
    UrlRepository repo;

    @Autowired
    ScrapperClient scrapper;

    static double decayFactor = 0.95;

    public int rateUrl(String url) {
        int rating = 0;

        // Rate domain
        DomainRates domainClassification = rateDomain(url);
        switch (domainClassification) {
            case Whitelist:
                //System.out.printf("Website %s is whitelisted%n", url);
                repo.saveAndFlush(new Urls(url, 100));
                break;
            case Pass:
                rating = basicTrustRank(url);
                //System.out.printf("Website %s is passed%n", url);
                break;
            case Deny:
                //System.out.printf("Website %s is denied%n", url);
                break;
        }


        return rating;
    }

    private void temp(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");

        for (Element linkText : links) {
//            System.out.println(linkText.toString());
            if (linkText.toString().contains("http")) {
                System.out.println(linkText);
            }
        }

    }

    private int basicTrustRank(String url) {
        List<String> urls = grabUrls(url);

        Map<String, Integer> domainMap = new HashMap<>();

        // Count domain appearances
        Pattern patternDomainExtract = Pattern.compile("""
        ^(?:(?:(?:https|http):\\/\\/)(?:www\\.)?)?(?<named>\\w+\\.\\w+(?:\\.\\w+)*)""");
        for (String link : urls) {
            Matcher domainMatcher = patternDomainExtract.matcher(link);
            domainMatcher.find();
            String temp = domainMatcher.group("named");
            Optional.ofNullable(domainMap.get(temp))
                    .map(count -> domainMap.replace(temp, count + 1))
                    .orElseGet(() -> domainMap.put(temp, 1));
        }

        // Calculate weights
        Map<String, Integer> databaseTrustRanks = repo.findAllById(domainMap.keySet()).stream().collect(Collectors.toMap(com.akai.hackathon.database.Urls::getUrl, com.akai.hackathon.database.Urls::getRating));
        for (String key : domainMap.keySet()) {
            domainMap.replace(key, domainMap.get(key) * databaseTrustRanks.getOrDefault(key, 0));
        }

        // Add to database
        Matcher domainMatcher = patternDomainExtract.matcher(url);
        domainMatcher.find();
        var rating = domainMap.values().stream().reduce(0, Integer::sum) / urls.size();
        repo.saveAndFlush(new Urls(domainMatcher.group("named"), rating));

        return rating;
    }

    public List<String> grabUrls(String url) {
        return scrapper.getUrls(url);
    }

    private DomainRates rateDomain(String url) {
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

    private int traversePage(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc.title());

        Elements textSources = doc.select("p,h1,h2,h3,h4,h5,h6");
        for (Element text : textSources) {
            System.out.println(text.toString());
        }

        return 0;
    }

    private HashMap<String, Integer> trustedDomains = new HashMap<>(Map.of(
            "edu", 1, "gov", 1
    ));

    private HashMap<String, HashMap<String, Integer>> blacklistedCombinations;
}
