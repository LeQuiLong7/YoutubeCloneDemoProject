package com.lql.userservice.controller;

import com.lql.userservice.model.Video;
import com.lql.userservice.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class VideoControllerTest {


    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private WebClient.Builder webclient;

    private List<Video> fakeData() {
        return List.of(new Video(null, "a", "a", "a", "a", 0, null),
                new Video(null, "b", "b", "b", "b", 0, null),
                new Video(null, "c", "c", "c", "c", 0, null),
                new Video(null, "d", "d", "d", "d", 0, null));
    }

    @BeforeEach
    void setUp() {
        videoRepository.deleteAll()
                .thenMany(videoRepository.saveAll(fakeData()))
                .then().block();
    }

    @Test
    void getRecommendVideos() throws InterruptedException {
        webclient.build()
                .get()
                .uri("http://localhost:8001/api/v1/videos/1")
                .retrieve().bodyToMono(String.class)
                .doOnNext(System.out::println)
                .subscribe();

        Thread.sleep(5000);
    }
}