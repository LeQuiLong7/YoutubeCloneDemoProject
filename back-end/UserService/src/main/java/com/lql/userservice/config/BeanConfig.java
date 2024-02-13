package com.lql.userservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }


    @Bean
    public NewTopic topic1() {
        return new NewTopic("video-delete-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic("video-view-topic", 1, (short) 1);
    }
}
