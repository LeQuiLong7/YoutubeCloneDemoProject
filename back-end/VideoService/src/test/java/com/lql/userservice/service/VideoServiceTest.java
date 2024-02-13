package com.lql.userservice.service;

import com.lql.userservice.model.Video;
import com.lql.userservice.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

@SpringBootTest
@ActiveProfiles("test")
public class VideoServiceTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoService videoService;

    @Test
    public void uploadFileTest() {

        Mono.just(new Video(null, "a", "a", "a", "a", 0, null))
                .flatMap(videoRepository::save)
                .doOnNext(video -> {
                    System.out.println(video.getId());
                }).subscribe();

    }

    @Test
    void getUserInfoByVideoId() {


        videoService.getUserInfoMapByVideoId("213123").doOnNext(System.out::println).block();

    }
}
