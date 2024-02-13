package com.lql.userservice.service;

import com.lql.userservice.exception.model.UserNotFoundException;
import com.lql.userservice.model.User;
import com.lql.userservice.model.dto.SubscriptionRequest;
import com.lql.userservice.model.dto.UserDTO;
import com.lql.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
//    private final WebClient.Builder webClientBuilder;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "video-create-topic")
    public void videoCreatedListener(String userOauthId__videoId) {

        log.info("listen on {}", userOauthId__videoId);
        String[] split = userOauthId__videoId.split("__");
        userRepository.findByOauthId(split[0])
                .map(user -> {
                    user.getVideoIds().add(split[1]);
                    return user;
                })
                .flatMap(userRepository::save)
                .subscribe();
    }

//    public Mono<User> uploadFile(String oauthId, FilePart filePart, String title, String description) {
//        Mono<Video> videoMono = uploadFile(filePart, title, description);
//        Mono<User> userMono = userRepository.findByOauthId(oauthId);
//        return videoMono.flatMap(video ->
//                userMono.map(user -> {
//                    user.getVideoIds().add(video.getId());
//                    return user;
//                })
//        ).flatMap(userRepository::save);
//    }

    public Mono<Void> deleteVideoById(String authId, String videoId) {

        return userRepository.findByOauthId(authId)
                .doOnNext(user -> kafkaTemplate.send("video-delete-topic", videoId))
                .map(user -> {
                    user.getVideoIds().remove(videoId);
                    return user;
                })
                .flatMap(userRepository::save)
                .then();
    }

public Mono<User> findUserById(String id) {
        return userRepository.findById(id);
}

    public Mono<Void> likeVideo(String userOauthId, String videoId) {
        return userRepository.findByOauthId(userOauthId)
                .switchIfEmpty(Mono.error(() -> new UserNotFoundException("OauthId " + userOauthId)))
                .map(user -> {
                    List<String> likedHistory = user.getLikedHistory();
                    int index = likedHistory.indexOf(videoId);
                    if(index != -1) {
                        likedHistory.remove(index);
                    }
                    likedHistory.add(videoId);
                    return user;
                }).flatMap(userRepository::save)
                .then();
    }


    public Mono<Void> subscribe(SubscriptionRequest subscriptionRequest) {
            return Mono.zip(userRepository.findById(subscriptionRequest.getSubscriberId())
                    .map(user -> {
                        user.getSubscribedToIds().add(subscriptionRequest.getSubscribeToId());
                        return user;
                    }).flatMap(userRepository::save),
                    userRepository.findById(subscriptionRequest.getSubscribeToId())
                            .map(user -> {
                                user.getSubscribersIds().add(subscriptionRequest.getSubscriberId());
                                return user;
                            }).flatMap(userRepository::save)
            ).then();
    }


    public Mono<List<String>> getHistoryByOauthId(String oauthId) {
        return userRepository.findByOauthId(oauthId)
                .map(User::getWatchHistory);
    }
    public Mono<List<String>> getLikedVideoByOauthId(String oauthId) {
        return userRepository.findByOauthId(oauthId)
                .map(User::getLikedHistory);
    }
    public Mono<Void> viewVideoById(String oauthId, String videoId) {
        return userRepository.findByOauthId(oauthId)
                .map(user -> {
                    List<String> newList = user.getWatchHistory().stream().filter(id -> !id.equals(videoId)).collect(Collectors.toList());
                    newList.add(videoId);
                    user.setWatchHistory(newList);
                    return user;
                })
                .doOnNext(user ->  kafkaTemplate.send("video-view-topic", videoId))
                .flatMap(userRepository::save)
                .doOnError(e -> log.error("Error viewing video {}", videoId))
                .then();
    }
    public Flux<String> getAllVideosByOauthId(String oauthId) {
        return userRepository.findByOauthId(oauthId)
                .flatMapIterable(User::getVideoIds)
                .defaultIfEmpty("empty");

//                .flatMapMany(user -> Mono.just(user.getVideoIds()))
    }
    public Mono<Void> changeChanelName(String authId, String newChanelName) {

        return userRepository.findByOauthId(authId)
                .map(user -> {
                    user.setChanelName(newChanelName);
                    return user;
                })
                .flatMap(userRepository::save)
                .doOnError((e) -> log.error("Error changing chanel name for {}", authId))
                .then();
    }
//
//    public Mono<Video> uploadFile(FilePart filePart, String title, String description) {
//        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
//        multipartBodyBuilder.part("file", filePart);
//        multipartBodyBuilder.part("title", title);
//        multipartBodyBuilder.part("description", description);
//
//
//        return webClientBuilder.build()
//                .post()
//                .uri("http://localhost:8001/api/v1/videos")
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
//                .retrieve()
//                .bodyToMono(Video.class);
//    }

    public Mono<User> createUser(String oauthId) {

        ArrayList<String> emptyList = new ArrayList<>();
        Set<String> emptySet = new HashSet<>();
        User user = new User(null, oauthId, oauthId, emptyList, emptySet, emptySet, emptyList, emptyList);
        return userRepository.save(user);
    }

    public Mono<UserDTO> findByVideoId(String videoId) {
        return userRepository.findByVideoIdsContains(videoId)
                .doOnError(ex -> log.error("error finding user by video id {}", videoId))
                .map(UserDTO::toDTO)
                .defaultIfEmpty(new UserDTO("undefined", "undefined"))
                .onErrorReturn(new UserDTO("undefined", "undefined"))
                ;
    }
    public Mono<User> findDetailByVideoId(String videoId) {
        return userRepository.findByVideoIdsContains(videoId)
                .doOnError(ex -> log.error("error finding user by video id {}", videoId))
                .defaultIfEmpty(new User("undefined", "undefined", "undefined", null, null, null, null, null))
                .onErrorReturn(new User("undefined", "undefined", "undefined", null, null, null, null, null))
                ;
    }
    public Mono<UserDTO> findById(String id) {
        return userRepository.findById(id)
                .doOnError(ex -> log.error("error finding user id {}", id))
                .map(UserDTO::toDTO)
                .defaultIfEmpty(new UserDTO("undefined", "undefined"))
                .onErrorReturn(new UserDTO("undefined", "undefined"));
    }

    public Mono<User> findByOauthId(String oauthId) {

        return userRepository.findByOauthId(oauthId)
                .switchIfEmpty(createUser(oauthId));
//                .defaultIfEmpty(new User("undefined", null, null, null, null, null, null, null));
    }

    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }
}
