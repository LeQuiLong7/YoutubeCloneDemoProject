package com.lql.userservice.reactive;


import com.lql.userservice.model.User;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.List;

public class ReactiveTest {


    @Test
    public void test() {
        User user = new User(null, null, null, List.of("1", "2"), null, null, null, null);

        Mono.just(user)
                .flatMapIterable(user1 -> user.getVideoIds())
                .subscribe(System.out::println);

    }
}
