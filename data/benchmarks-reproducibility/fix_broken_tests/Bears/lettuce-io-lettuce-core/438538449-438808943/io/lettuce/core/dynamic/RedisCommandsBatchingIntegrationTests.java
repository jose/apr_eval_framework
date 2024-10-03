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
package io.lettuce.core.dynamic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisCommandExecutionException;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.dynamic.annotation.Command;
import io.lettuce.core.dynamic.batch.BatchException;
import io.lettuce.core.dynamic.batch.BatchExecutor;
import io.lettuce.core.dynamic.batch.BatchSize;
import io.lettuce.core.dynamic.batch.CommandBatching;
import io.lettuce.test.Futures;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class RedisCommandsBatchingIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    RedisCommandsBatchingIntegrationTests(StatefulRedisConnection<String, String> connection) {
        this.redis = connection.sync();
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void selectiveBatching() {
    }

    @Test
    void selectiveBatchingShouldHandleErrors() {
    }

    @Test
    void shouldExecuteBatchingSynchronously() {
    }

    @Test
    void shouldHandleSynchronousBatchErrors() {
    }

    @Test
    void shouldExecuteBatchingAynchronously() {
    }

    @Test
    void shouldHandleAsynchronousBatchErrors() throws Exception {
    }

    @BatchSize(5)
    static interface Batching extends Commands {

        void set(String key, String value);

        void llen(String key);

        @Command("SET")
        RedisFuture<String> setAsync(String key, String value);

        @Command("LLEN")
        RedisFuture<Long> llenAsync(String key);
    }

    static interface SelectiveBatching extends Commands, BatchExecutor {

        void set(String key, String value);

        void set(String key, String value, CommandBatching commandBatching);

        void llen(String key, CommandBatching commandBatching);

    }

}
