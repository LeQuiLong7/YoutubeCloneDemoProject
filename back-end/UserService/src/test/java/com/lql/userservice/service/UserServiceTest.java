package com.lql.userservice.service;

import com.lql.userservice.exception.model.UserNotFoundException;
import com.lql.userservice.model.User;
import com.lql.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    private final String MOCK_USER_OAUTH_ID_1 = "123";
    private final String MOCK_USER_OAUTH_ID_2 = "456";
    private String MOCK_USER_ID_1;
    private String MOCK_USER_ID_2;



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
    void likeVideo() {
        String mockVideoId = "video_id_1";
        userService.likeVideo(MOCK_USER_OAUTH_ID_1, mockVideoId).block();

        User user = userRepository.findByOauthId(MOCK_USER_OAUTH_ID_1).block();

        assertThat(user.getLikedHistory()).isNotNull().hasSize(1).contains(mockVideoId);

    }

    @Test
    void likeVideoThatAlreadyLiked() {
        String mockVideoId1 = "video_id_1";
        String mockVideoId2 = "video_id_2";
        userService.likeVideo(MOCK_USER_OAUTH_ID_1, mockVideoId1)
                .then(userService.likeVideo(MOCK_USER_OAUTH_ID_1, mockVideoId2))
                .then(userService.likeVideo(MOCK_USER_OAUTH_ID_1, mockVideoId1))
                .block();

        User user = userRepository.findByOauthId(MOCK_USER_OAUTH_ID_1).block();

        assertThat(user.getLikedHistory())
                .isNotNull()
                .hasSize(2)
                .contains(mockVideoId1, mockVideoId2)
        ;
        assertThat(user.getLikedHistory().get(0)).isEqualTo(mockVideoId2);
        assertThat(user.getLikedHistory().get(1)).isEqualTo(mockVideoId1);
    }
    @Test
    void likeVideoWithWrongUserId() {
        String mockVideoId = "video_id_1";
        String notExistsUserId = "a";
        assertThrows(UserNotFoundException.class, () -> userService.likeVideo(notExistsUserId, mockVideoId)
                .block());

    }
}