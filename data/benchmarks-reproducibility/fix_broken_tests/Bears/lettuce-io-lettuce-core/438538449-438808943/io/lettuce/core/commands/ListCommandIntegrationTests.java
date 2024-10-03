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
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.TestSupport;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.ListStreamingAdapter;
import io.lettuce.test.condition.RedisConditions;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ListCommandIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    protected ListCommandIntegrationTests(RedisCommands<String, String> redis) {
        this.redis = redis;
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void blpop() {
    }

    @Test
    void blpopTimeout() {
    }

    @Test
    void brpop() {
    }

    @Test
    void brpoplpush() {
    }

    @Test
    void lindex() {
    }

    @Test
    void linsert() {
    }

    @Test
    void llen() {
    }

    @Test
    void lpop() {
    }

    @Test
    void lpush() {
    }

    @Test
    void lpushx() {
    }

    @Test
    void lpushxVariadic() {
    }

    @Test
    void lrange() {
    }

    @Test
    void lrangeStreaming() {
    }

    @Test
    void lrem() {
    }

    @Test
    void lset() {
    }

    @Test
    void ltrim() {
    }

    @Test
    void rpop() {
    }

    @Test
    void rpoplpush() {
    }

    @Test
    void rpush() {
    }

    @Test
    void rpushx() {
    }

    @Test
    void rpushxVariadic() {
    }
}
