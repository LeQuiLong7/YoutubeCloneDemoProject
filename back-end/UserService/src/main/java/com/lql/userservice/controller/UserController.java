package com.lql.userservice.controller;


import com.lql.userservice.model.User;
import com.lql.userservice.model.dto.SubscriptionRequest;
import com.lql.userservice.model.dto.UserDTO;
import com.lql.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public Mono<User> getInfo(ServerWebExchange exchange){
        String userOauthId = exchange.getRequest().getHeaders().getFirst("userOauthId");
        return userService.findByOauthId(userOauthId);
    }
    @GetMapping("/info/{id}")
    public Mono<UserDTO> getInfoByOauthId(@PathVariable String id){
        return userService.findByOauthId(id).map(UserDTO::toDTO);
    }
    @GetMapping("/full-info-by-id/{id}")
    public Mono<User> getFullInfoById(@PathVariable String id){
        return userService.findUserById(id);
    }
    @GetMapping("/{videoId}")
    public Mono<UserDTO> getUserByVideoId(@PathVariable String videoId) {
        return userService.findByVideoId(videoId);
    }


    @GetMapping("/detail-by-videoId/{videoId}")
    public Mono<User> getUserInfoDetailByVideoId(@PathVariable String videoId) {
        return userService.findDetailByVideoId(videoId);
    }

    @GetMapping("/view/{videoId}")
    public Mono<Void> viewVideoById(@PathVariable String videoId, ServerWebExchange exchange) {
        String userOauthId = exchange.getRequest().getHeaders().getFirst("userOauthId");

        return userService.viewVideoById(userOauthId, videoId);
    }

    @GetMapping("/videos/all")
    public Mono<List<String>> getVideosByOauthId(ServerWebExchange exchange) {
        String userOauthId = exchange.getRequest().getHeaders().getFirst("userOauthId");
        return userService.getAllVideosByOauthId(userOauthId).collectList();
    }
    @GetMapping("/history")
    public Mono<List<String>> getHistoryByOauthId(ServerWebExchange exchange) {
        String userOauthId = exchange.getRequest().getHeaders().getFirst("userOauthId");
        return userService.getHistoryByOauthId(userOauthId);
    }
    @GetMapping("/likedVideos")
    public Mono<List<String>> getLikedVideosByOauthId(ServerWebExchange exchange) {
        String userOauthId = exchange.getRequest().getHeaders().getFirst("userOauthId");
        return userService.getLikedVideoByOauthId(userOauthId);
    }

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> subscribeTo(@RequestBody @Valid SubscriptionRequest subscriptionRequest){
        return userService.subscribe(subscriptionRequest);
    }

    @PutMapping("/changeChannelName")
    public Mono<Void> changeChanelName(@RequestParam("newName") String newName, ServerWebExchange exchange) {
        String userOauthId = exchange.getRequest().getHeaders().getFirst("userOauthId");

        return userService.changeChanelName(userOauthId, newName);
    }

    @PutMapping("/likeVideo")
    public Mono<Void> likeVideo(@RequestParam("videoId") String videoId, ServerWebExchange exchange) {
        String userOauthId = exchange.getRequest().getHeaders().getFirst("userOauthId");

        return userService.likeVideo(userOauthId, videoId);
    }



    @DeleteMapping("/{videoId}")
    public Mono<String> deleteVideoById(@PathVariable String videoId, ServerWebExchange exchange) {
        String userOauthId = exchange.getRequest().getHeaders().getFirst("userOauthId");
        return userService.deleteVideoById(userOauthId ,videoId).thenReturn("success");
    }
}
