package com.lql.userservice.controller;

import com.lql.userservice.model.User;
import com.lql.userservice.model.dto.SubscriptionRequest;
import com.lql.userservice.repository.UserRepository;
import com.lql.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    private final String BASE_URL = "http://localhost:8000/api/v1/users";
    private final String MOCK_USER_OAUTH_ID_1 = "123";
    private final String MOCK_USER_OAUTH_ID_2 = "456";
    private String MOCK_USER_ID_1;
    private String MOCK_USER_ID_2;


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll()
                .then(Mono.zip(userService.createUser(MOCK_USER_OAUTH_ID_1), userService.createUser(MOCK_USER_OAUTH_ID_2)))
                .then(
                        Mono.zip(
                                userRepository.findByOauthId(MOCK_USER_OAUTH_ID_1).doOnNext(user -> MOCK_USER_ID_1 = user.getId()),
                                userRepository.findByOauthId(MOCK_USER_OAUTH_ID_2).doOnNext(user -> MOCK_USER_ID_2 = user.getId())
                        ))
                .block();


    }

    @Test
    void changeChanelName() {

        String newChannelName = "MaGaming";
        String url = String.format("%s/%s?%s=%s", BASE_URL, "changeChannelName", "newName", newChannelName);
        webTestClient.put()
                .uri(url)
                .header("userOauthId", MOCK_USER_OAUTH_ID_1)
                .exchange()
                .expectStatus().isOk();


        User user = userRepository.findByOauthId(MOCK_USER_OAUTH_ID_1).block();


        assertThat(user.getChanelName()).isNotNull().isEqualTo(newChannelName);
    }

    @Test
    void subscribeTo() {
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest(MOCK_USER_ID_1, MOCK_USER_ID_2);

        webTestClient.post()
                .uri(BASE_URL + "/subscribe")
                .bodyValue(subscriptionRequest)
                .exchange()
                .expectStatus().isCreated();


        User user1 = userRepository.findById(MOCK_USER_ID_1).block();
        User user2 = userRepository.findById(MOCK_USER_ID_2).block();

        assertThat(user1.getSubscribedToIds()).hasSize(1).contains(MOCK_USER_ID_2);
        assertThat(user2.getSubscribersIds()).hasSize(1).contains(MOCK_USER_ID_1);
    }

    @Test
    void likeVideo() {
        String mockVideoId = "abc";
        String uri = BASE_URL + "/likeVideo?videoId=" + mockVideoId;
        webTestClient.put()
                .uri(uri)
                .header("userOauthId", MOCK_USER_OAUTH_ID_1)
                .exchange()
                .expectStatus().isOk();

        User user = userRepository.findByOauthId(MOCK_USER_OAUTH_ID_1).block();


        assertThat(user.getLikedHistory()).isNotNull().hasSize(1).contains(mockVideoId);
    }
}