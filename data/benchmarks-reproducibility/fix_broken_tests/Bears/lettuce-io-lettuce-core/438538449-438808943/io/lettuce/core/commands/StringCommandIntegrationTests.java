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

import static io.lettuce.core.SetArgs.Builder.ex;
import static io.lettuce.core.SetArgs.Builder.nx;
import static io.lettuce.core.SetArgs.Builder.px;
import static io.lettuce.core.SetArgs.Builder.xx;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisException;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.KeyValueStreamingAdapter;
import io.lettuce.test.LettuceExtension;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringCommandIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    protected StringCommandIntegrationTests(RedisCommands<String, String> redis) {
        this.redis = redis;
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void append() {
    }

    @Test
    void get() {
    }

    @Test
    void getbit() {
    }

    @Test
    void getrange() {
    }

    @Test
    void getset() {
    }

    @Test
    void mget() {
    }

    protected void setupMget() {
        assertThat(redis.mget(key)).isEqualTo(list(KeyValue.empty("key")));
        redis.set("one", "1");
        redis.set("two", "2");
    }

    @Test
    void mgetStreaming() {
    }

    @Test
    void mset() {
    }

    @Test
    void msetnx() {
    }

    @Test
    void set() {
    }

    @Test
    void setNegativeEX() {
    }

    @Test
    void setNegativePX() {
    }

    @Test
    void setbit() {
    }

    @Test
    void setex() {
    }

    @Test
    void psetex() {
    }

    @Test
    void setnx() {
    }

    @Test
    void setrange() {
    }

    @Test
    void strlen() {
    }

    @Test
    void time() {
    }
}
