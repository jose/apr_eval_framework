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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.*;
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
public class SetCommandIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    protected SetCommandIntegrationTests(RedisCommands<String, String> redis) {
        this.redis = redis;
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void sadd() {
    }

    @Test
    void scard() {
    }

    @Test
    void sdiff() {
    }

    @Test
    void sdiffStreaming() {
    }

    @Test
    void sdiffstore() {
    }

    @Test
    void sinter() {
    }

    @Test
    void sinterStreaming() {
    }

    @Test
    void sinterstore() {
    }

    @Test
    void sismember() {
    }

    @Test
    void smove() {
    }

    @Test
    void smembers() {
    }

    @Test
    void smembersStreaming() {
    }

    @Test
    void spop() {
    }

    @Test
    void spopMultiple() {
    }

    @Test
    void srandmember() {
    }

    @Test
    void srandmemberStreaming() {
    }

    @Test
    void srem() {
    }

    @Test
    void sremEmpty() {
    }

    @Test
    void sremNulls() {
    }

    @Test
    void sunion() {
    }

    @Test
    void sunionEmpty() {
    }

    @Test
    void sunionStreaming() {
    }

    @Test
    void sunionstore() {
    }

    @Test
    void sscan() {
    }

    @Test
    void sscanWithCursor() {
    }

    @Test
    void sscanWithCursorAndArgs() {
    }

    @Test
    void sscanStreaming() {
    }

    @Test
    void sscanStreamingWithCursor() {
    }

    @Test
    void sscanStreamingWithCursorAndArgs() {
    }

    @Test
    void sscanStreamingArgs() {
    }

    @Test
    void sscanMultiple() {
    }

    @Test
    void scanMatch() {
    }

    void setup100KeyValues(Set<String> expect) {
        for (int i = 0; i < 100; i++) {
            redis.sadd(key, value + i);
            expect.add(value + i);
        }
    }

    private void setupSet() {
        redis.sadd(key, "a", "b", "c");
        redis.sadd("key1", "a", "b", "c", "d");
        redis.sadd("key2", "c");
        redis.sadd("key3", "a", "c", "e");
    }

}
