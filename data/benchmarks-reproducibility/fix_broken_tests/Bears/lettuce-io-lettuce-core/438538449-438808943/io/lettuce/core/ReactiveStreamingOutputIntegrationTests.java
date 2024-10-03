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

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import reactor.test.StepVerifier;
import io.lettuce.core.GeoArgs.Unit;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.KeysAndValues;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.condition.EnabledOnCommand;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReactiveStreamingOutputIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;
    private final RedisReactiveCommands<String, String> reactive;

    @Inject
    ReactiveStreamingOutputIntegrationTests(StatefulRedisConnection<String, String> connection) {
        this.redis = connection.sync();
        this.reactive = connection.reactive();
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void keyListCommandShouldReturnAllElements() {
    }

    @Test
    void valueListCommandShouldReturnAllElements() {
    }

    @Test
    void booleanListCommandShouldReturnAllElements() {
    }

    @Test
    void scoredValueListCommandShouldReturnAllElements() {
    }

    @Test
    @EnabledOnCommand("GEORADIUS")
    void geoWithinListCommandShouldReturnAllElements() {
    }

    @Test
    @EnabledOnCommand("GEOPOS")
    void geoCoordinatesListCommandShouldReturnAllElements() {
    }
}
