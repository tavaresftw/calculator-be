package com.ntd.calculator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() throws URISyntaxException {
        URI redisUri = new URI(System.getenv("REDISCLOUD_URL"));

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisUri.getHost());
        config.setPort(redisUri.getPort());
        if (redisUri.getUserInfo() != null) {
            String password = redisUri.getUserInfo().split(":", 2)[1];
            config.setPassword(password);
        }

        return new LettuceConnectionFactory(config);
    }



    @Bean
    public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}