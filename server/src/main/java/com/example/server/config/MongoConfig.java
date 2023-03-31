package com.example.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {
            // Inject the mongo connection String
    @Value("${mongo.url}")
    private String mongoUrl;

    @Primary
    @Bean
    public MongoTemplate createTemplate() {
        MongoClient client = MongoClients.create(mongoUrl);
        return new MongoTemplate(client, "marvel");
    }

}
    