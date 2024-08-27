package com.ntd.calculator.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    @Value("${JAWSDB_URL}")
    private String jawsdbUrl;

    @PostConstruct
    public void configureDatabaseProperties() throws URISyntaxException {
        URI jdbUri = new URI(jawsdbUrl);

        String username = jdbUri.getUserInfo().split(":")[0];
        String password = jdbUri.getUserInfo().split(":")[1];
        String port = String.valueOf(jdbUri.getPort());
        String jdbUrl = "jdbc:mysql://" + jdbUri.getHost() + ":" + port + jdbUri.getPath();

        System.setProperty("spring.datasource.url", jdbUrl);
        System.setProperty("spring.datasource.username", username);
        System.setProperty("spring.datasource.password", password);
    }
}