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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import java.time.Duration;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.Delay;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;
import io.lettuce.test.WithPassword;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class ConnectionCommandIntegrationTests extends TestSupport {

    private final RedisClient client;
    private final RedisCommands<String, String> redis;

    @Inject
    ConnectionCommandIntegrationTests(RedisClient client, StatefulRedisConnection<String, String> connection) {
        this.client = client;
        this.redis = connection.sync();
    }

    @Test
    void auth() {
    }

    @Test
    void echo() {
    }

    @Test
    void ping() {
    }

    @Test
    void select() {
    }

    @Test
    void authNull() {
    }

    @Test
    void authEmpty() {
    }

    @Test
    void authReconnect() {
    }

    @Test
    void selectReconnect() {
    }

    @Test
    void getSetReconnect() {
    }

    @Test
    @SuppressWarnings("unchecked")
    void authInvalidPassword() {
    }

    @Test
    @SuppressWarnings("unchecked")
    void selectInvalid() {
    }

    @Test
    void testDoubleToString() {
    }
}
