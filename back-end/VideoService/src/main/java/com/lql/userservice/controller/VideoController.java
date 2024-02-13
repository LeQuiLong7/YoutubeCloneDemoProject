package com.lql.userservice.controller;

import com.lql.userservice.model.Comment;
import com.lql.userservice.model.Video;
import com.lql.userservice.model.dto.VideoDTO;
import com.lql.userservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/videos")
@RequiredArgsConstructor
@Slf4j
public class VideoController {
    private final VideoService videoService;
    @PostMapping
    public Mono<String> uploadVideo(@RequestPart FilePart videoFile,
                                    @RequestPart FilePart thumbnailFile,
                                    @RequestPart String title,
                                    @RequestPart String description,
                                    ServerWebExchange exchange) {
        String userOauthId = exchange.getRequest().getHeaders().getFirst("userOauthId");
        return videoService.uploadFile(videoFile, thumbnailFile, title, description, userOauthId);
    }

    @PostMapping("/comments/{videoId}")
    public Mono<Comment> comment(@PathVariable String videoId,
                                 @RequestPart String comment,
                                 ServerWebExchange exchange) {
        log.info("comment {} for video {}", comment, videoId);
        String userOauthId = exchange.getRequest().getHeaders().getFirst("userOauthId");
        return videoService.comment(videoId, userOauthId, comment);
    }


    @GetMapping("/{n}")
    public Flux<VideoDTO> getRecommendVideos(@PathVariable Integer n) {
        return videoService.getRecommendation(n);
    }

//    @GetMapping("/view/{videoId}")
//    public Mono<String> viewVideos(@PathVariable String videoId) {
//        return videoService.viewVideo(videoId);
//    }

    @GetMapping("/details/{videoId}")
    public Mono<Video> findById(@PathVariable String videoId) {
        return videoService.findById(videoId);
    }
}
