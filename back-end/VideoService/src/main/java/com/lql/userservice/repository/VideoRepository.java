package com.lql.userservice.repository;

import com.lql.userservice.model.Video;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends ReactiveMongoRepository<Video, String> {
}
