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
package io.lettuce.core.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.*;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.KeyValueStreamingAdapter;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.ListStreamingAdapter;
import io.lettuce.test.condition.EnabledOnCommand;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HashCommandIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    protected HashCommandIntegrationTests(RedisCommands<String, String> redis) {
        this.redis = redis;
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void hdel() {
    }

    @Test
    void hexists() {
    }

    @Test
    void hget() {
    }

    @Test
    void hgetall() {
    }

    @Test
    void hgetallStreaming() {
    }

    @Test
    void hincrby() {
    }

    @Test
    void hincrbyfloat() {
    }

    @Test
    void hkeys() {
    }

    @Test
    void hkeysStreaming() {
    }

    private void setup() {
        assertThat(redis.hkeys(key)).isEqualTo(list());
        redis.hset(key, "one", "1");
        redis.hset(key, "two", "2");
    }

    @Test
    void hlen() {
    }

    @Test
    @EnabledOnCommand("HSTRLEN")
    void hstrlen() {
    }

    @Test
    void hmget() {
    }

    private void setupHmget() {
        assertThat(redis.hmget(key, "one", "two")).isEqualTo(list(KeyValue.empty("one"), KeyValue.empty("two")));
        redis.hset(key, "one", "1");
        redis.hset(key, "two", "2");
    }

    @Test
    void hmgetStreaming() {
    }

    @Test
    void hmset() {
    }

    @Test
    void hmsetWithNulls() {
    }

    @Test
    void hset() {
    }

    @Test
    void hsetnx() {
    }

    @Test
    void hvals() {
    }

    @Test
    void hvalsStreaming() {
    }

    @Test
    void hscan() {
    }

    @Test
    void hscanWithCursor() {
    }

    @Test
    void hscanWithCursorAndArgs() {
    }

    @Test
    void hscanStreaming() {
    }

    @Test
    void hscanStreamingWithCursor() {
    }

    @Test
    void hscanStreamingWithCursorAndArgs() {
    }

    @Test
    void hscanStreamingWithArgs() {
    }

    @Test
    void hscanMultiple() {
    }

    @Test
    void hscanMatch() {
    }

    void setup100KeyValues(Map<String, String> expect) {
        for (int i = 0; i < 100; i++) {
            expect.put(key + i, value + 1);
        }

        redis.hmset(key, expect);
    }
}
