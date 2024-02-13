package com.lql.userservice.repository;

import com.lql.userservice.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    private final String DEMO_VIDEO_ID = "demo-video-id";
    private final String DEMO_OAUTH_ID = "1234";

    @BeforeEach
    public void setUp() {

        userRepository.deleteAll()
                .then(userService.createUser(DEMO_OAUTH_ID))
                .then(userRepository.findByOauthId(DEMO_OAUTH_ID))
                .map(user -> {
                    user.getVideoIds().addAll(List.of(DEMO_VIDEO_ID, "213123"));
                    return user;
                }).flatMap(userRepository::save)
                .block();
    }

    @Test
    void findByVideoIdsContains() {


        userRepository.findByVideoIdsContains(DEMO_VIDEO_ID)
//                .filter(user -> user.getOauthId().equals(DEMO_OAUTH_ID))
                .switchIfEmpty(Mono.error(new RuntimeException("User not found or not correct")))
                .subscribe(System.out::println)
        ;
    }
}