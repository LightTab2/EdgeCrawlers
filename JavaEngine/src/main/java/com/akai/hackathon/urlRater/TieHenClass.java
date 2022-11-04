package com.akai.hackathon.urlRater;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

enum DomainRates {
    Whitelist,
    Pass,
    Deny
}

public class TieHenClass {
    public static float rateUrl(String url) {
        float rating = 1;

        // Rate domain
        DomainRates domainClassification = rateDomain(url);
        switch (domainClassification) {
            case Whitelist:
                System.out.printf("Website %s is whitelisted%n", url);
                break;
            case Pass:
                System.out.printf("Website %s is passed%n", url);
                break;
            case Deny:
                System.out.printf("Website %s is denied%n", url);
                break;
        }

        try {
            traversePage(url);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return rating;
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
        return 0;
    }

    private static HashMap<String, Integer> trustedDomains = new HashMap<>(Map.of(
            "edu", 1, "gov", 1
    ));
}
