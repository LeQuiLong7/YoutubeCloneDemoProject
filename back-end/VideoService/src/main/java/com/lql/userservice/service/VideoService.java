package com.lql.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lql.userservice.model.Comment;
import com.lql.userservice.model.Video;
import com.lql.userservice.model.dto.VideoDTO;
import com.lql.userservice.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {
    private final VideoRepository videoRepository;
    private final S3Service s3Service;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    private final String BUCKET_NAME = "your-bucket-name";

    @KafkaListener(topics = "video-delete-topic")
    public void deleteById(String videoId) {
        Mono<Video> video = videoRepository.findById(videoId);
        video
                .doOnNext(v -> s3Service.deleteObject(BUCKET_NAME, v.getS3Id()))
                .doOnNext(v -> s3Service.deleteObject(BUCKET_NAME, v.getS3ThumbnailId()))
                .flatMap(videoRepository::delete)
                .subscribe();
    }



    public Flux<VideoDTO> getRecommendation(int n) {
        return videoRepository.findAll()
                .take(n * 10)
                .takeLast(10)
                .flatMap(video ->
                        getUserInfoMapByVideoId(video.getId())
                                .map(map -> new VideoDTO(video.getId(),
                                        video.getThumbnailURL(),
                                        video.getTitle(),
                                        video.getTotalViews(),
                                        map.get("userId"),
                                        map.get("chanelName")))
                );
    }

    public Mono<Comment> comment(String videoId, String userId, String comment) {

        Comment c = new Comment(userId, comment, Instant.now());

        return videoRepository.findById(videoId)
                .switchIfEmpty(Mono.error(new RuntimeException("video %s does not exists".formatted(videoId))))
                .map(video -> {
                    video.getComments().add(c);
                    return video;
                })
                .flatMap(videoRepository::save)
                .doOnError(throwable -> {
                    System.err.println(throwable.getMessage());
                })
                .onErrorResume(throwable -> Mono.empty())
                .thenReturn(c);
        }


    public Mono<Map<String, String>> getUserInfoMapByVideoId(String videoId) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8000/api/v1/users/" + videoId)
                .retrieve()
                .bodyToMono(String.class)
                .map(s -> {
                    try {
                        return ((Map<String, String>) objectMapper.readValue(s, Map.class));
                    } catch (JsonProcessingException e) {
                        throw Exceptions.propagate(e);
                    }
                })
                .onErrorReturn(Map.of("channelName", "undefined", "userId", "undefined"));
    }


    @KafkaListener(topics = "video-view-topic")
    public void viewVideo(String videoId) {
         videoRepository.findById(videoId)
                .map(video -> {
                    video.setTotalViews(video.getTotalViews() + 1);
                    return video;
                }).flatMap(videoRepository::save)
                .doOnError(err -> log.error("Error viewing video {}", videoId))
                 .subscribe();
    }

    public Mono<Video> findById(String videoId) {
        return videoRepository.findById(videoId);
    }


    public Mono<String> uploadFile(FilePart videoFile, FilePart thumbnailFile, String title, String description, String userOauthId) {

        String videoId = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(videoFile.filename());
        String thumbnailId = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(thumbnailFile.filename());


        return Mono.zip(toFile(videoFile)
                        .flatMap(file -> s3Service.uploadObject(BUCKET_NAME, videoId, file))
                        .map((putObjectResultMono) ->
                                s3Service.generateUrl(BUCKET_NAME, videoId, get7DaysExpiresDate())
                        ),

                        toFile(thumbnailFile)
                        .flatMap(file -> s3Service.uploadObject(BUCKET_NAME, thumbnailId, file))
                        .map((putObjectResultMono) ->
                                s3Service.generateUrl(BUCKET_NAME, thumbnailId, get7DaysExpiresDate())
                        ),
                        (videoURL, thumbnailURL) -> new Video(null, videoId, videoURL, thumbnailId, thumbnailURL, title, description, 0, new ArrayList<>()))
                .flatMap(videoRepository::save)
                .doOnNext(video -> {
                    kafkaTemplate.send("video-create-topic", String.format("%s__%s", userOauthId, video.getId()));
                })
                .thenReturn("success")
                .onErrorReturn("Error");
    }

    private Date get7DaysExpiresDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 24 * 7);
        return calendar.getTime();
    }

    private Mono<File> toFile(FilePart filePart) {
        try {
            File tempFile = File.createTempFile("temp", filePart.filename());

            return filePart.transferTo(tempFile)
                    .then(Mono.just(tempFile))
                    .doFinally(signalType -> {
                        // Close the tempFile in the doFinally block.
                        if (tempFile.exists()) {
                            tempFile.delete();
                        }
                    });
        } catch (IOException e) {
            log.error("Error while transferring file", e);
            return Mono.error(e);
        }

    }


        @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 3) // Run every 3 days
    public void getNewUrl() {
         videoRepository.findAll()
                .doOnNext((video) -> log.info("getting new url for video {}", video.getS3Id()))
                .map(video -> {
                    video.setVideoURL(s3Service.generateUrl(BUCKET_NAME, video.getS3Id(), get7DaysExpiresDate()));
                    video.setThumbnailURL(s3Service.generateUrl(BUCKET_NAME, video.getS3ThumbnailId(), get7DaysExpiresDate()));
                    return video;
                })
                .flatMap(videoRepository::save)
                .subscribe();
    }
}
