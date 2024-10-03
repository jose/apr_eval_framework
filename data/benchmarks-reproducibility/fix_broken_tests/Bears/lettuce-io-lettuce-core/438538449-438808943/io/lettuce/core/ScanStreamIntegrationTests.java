/*
 * Copyright 2017-2018 the original author or authors.
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
package io.lettuce.core;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScanStreamIntegrationTests extends TestSupport {

    private final StatefulRedisConnection<String, String> connection;
    private final RedisCommands<String, String> redis;

    @Inject
    ScanStreamIntegrationTests(StatefulRedisConnection<String, String> connection) {
        this.connection = connection;
        this.redis = connection.sync();
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void shouldScanIteratively() {
    }

    @Test
    void shouldHscanIteratively() {
    }

    @Test
    void shouldSscanIteratively() {
    }

    @Test
    void shouldZscanIteratively() {
    }
}
