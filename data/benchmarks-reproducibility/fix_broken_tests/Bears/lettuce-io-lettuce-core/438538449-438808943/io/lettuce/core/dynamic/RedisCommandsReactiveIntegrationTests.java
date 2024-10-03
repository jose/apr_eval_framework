/*
 * Copyright 2016-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core.dynamic;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.dynamic.annotation.Command;
import io.lettuce.test.LettuceExtension;
import io.reactivex.Maybe;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class RedisCommandsReactiveIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    RedisCommandsReactiveIntegrationTests(StatefulRedisConnection<String, String> connection) {
        this.redis = connection.sync();
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void reactive() {
    }

    @Test
    void shouldHandlePresentValue() {
    }

    @Test
    void shouldHandleAbsentValue() {
    }

    @Test
    void shouldHandlePresentValueRxJava() throws InterruptedException {
    }

    @Test
    void shouldHandleAbsentValueRxJava() throws InterruptedException {
    }

    interface MultipleExecutionModels extends Commands {

        @Command("SET")
        Mono<String> setReactive(String key, String value);

        Mono<String> get(String key);

        @Command("GET")
        Maybe<String> getRxJava(String key);
    }
}
