package com.lql.userservice.repository;

import com.lql.userservice.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByOauthId(String oauthId);

    Mono<User> findByVideoIdsContains(String videoId);
}
