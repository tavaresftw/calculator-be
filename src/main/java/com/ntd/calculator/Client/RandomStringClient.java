package com.ntd.calculator.Client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RandomStringClient {

    private final RestTemplate restTemplate;

    public RandomStringClient() {
        this.restTemplate = new RestTemplate();
    }

    public String getRandomString(Integer len) {
        String url = "https://www.random.org/strings/?num=1&len=" + len + "&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}
