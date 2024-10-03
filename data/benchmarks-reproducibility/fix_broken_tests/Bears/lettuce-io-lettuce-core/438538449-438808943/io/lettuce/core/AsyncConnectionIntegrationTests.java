/*
 * Copyright 2011-2018 the original author or authors.
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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.test.Delay;
import io.lettuce.test.Futures;
import io.lettuce.test.LettuceExtension;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class AsyncConnectionIntegrationTests extends TestSupport {

    private final RedisClient client;
    private final StatefulRedisConnection<String, String> connection;
    private final RedisAsyncCommands<String, String> async;

    @Inject
    AsyncConnectionIntegrationTests(RedisClient client, StatefulRedisConnection<String, String> connection) {
        this.client = client;
        this.connection = connection;
        this.async = connection.async();
        this.connection.sync().flushall();
    }

    @Test
    void multi() {
    }

    @Test
    void watch() {
    }

    @Test
    void futureListener() {
    }

    @Test
    void futureListenerCompleted() {
    }

    @Test
    void discardCompletesFutures() {
    }

    @Test
    void awaitAll() {
    }

    @Test
    void awaitAllTimeout() {
    }
}
