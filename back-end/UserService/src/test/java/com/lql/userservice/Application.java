package com.lql.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@SpringBootTest
public class Application {
    @Test
    public void test() {
//        Mono<Integer> just = Mono.just(1);
        Flux<Integer> range = Flux.range(2, 5);
        range.parallel().log()
                .map(integer -> {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return 5;
                }).flatMap(integer -> {
                    System.err.println(integer);
                    return Mono.just("Hello");
                })
                .subscribe(System.out::println);


    }

    @Test
    public void testDefaultIfEmpty() {
//        Mono.just(2)
                get()
                .map(i -> i + 5)
                .defaultIfEmpty(-1)
                .subscribe(System.out::println);
    }

    public Mono<Integer> get() {
        return Mono.empty();
    }
}
