package com.lql.apigateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class DemoController {

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("ok");
    }

    @GetMapping("/info")
    public Mono<Authentication> getInfo(Authentication authentication) {
        return Mono.just(authentication);
    }
}
