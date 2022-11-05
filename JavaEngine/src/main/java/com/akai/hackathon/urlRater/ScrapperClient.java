package com.akai.hackathon.urlRater;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Component
public class ScrapperClient {

    @Value("${scrapper.url}")
    private String scrapper_url;

    public List<String> getUrls(String uri) {
        ArrayList<String> links = new ArrayList<>();
        try {
            URL url = new URL(scrapper_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(
                        "{\"url\":\"%s\"}".formatted(uri).getBytes("utf-8")
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
            con.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(String.valueOf(response));
            JsonNode linksJson = json.get("links");

            for (int i = 0; i < linksJson.size(); i++) {
                System.out.println(linksJson.get(i).asText());
                links.add(linksJson.get(i).asText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return links;
    }
}
