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

import static io.lettuce.core.Range.Boundary.including;
import static io.lettuce.core.ZStoreArgs.Builder.max;
import static io.lettuce.core.ZStoreArgs.Builder.min;
import static io.lettuce.core.ZStoreArgs.Builder.sum;
import static io.lettuce.core.ZStoreArgs.Builder.weights;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.*;
import io.lettuce.core.Range.Boundary;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.ListStreamingAdapter;
import io.lettuce.test.condition.EnabledOnCommand;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SortedSetCommandIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    protected SortedSetCommandIntegrationTests(RedisCommands<String, String> redis) {
        this.redis = redis;
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void zadd() {
    }

    @Test
    void zaddScoredValue() {
    }

    @Test
    void zaddnx() {
    }

    @Test
    void zaddWrongArguments() {
    }

    @Test
    void zaddnxWrongArguments() {
    }

    @Test
    void zaddxx() {
    }

    @Test
    void zaddch() {
    }

    @Test
    void zaddincr() {
    }

    @Test
    void zaddincrnx() {
    }

    @Test
    void zaddincrxx() {
    }

    @Test
    void zcard() {
    }

    @Test
    void zcount() {
    }

    @Test
    void zincrby() {
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    void zinterstore() {
    }

    @Test
    @EnabledOnCommand("BZPOPMIN")
    void bzpopmin() {
    }

    @Test
    @EnabledOnCommand("BZPOPMAX")
    void bzpopmax() {
    }

    @Test
    @EnabledOnCommand("ZPOPMIN")
    void zpopmin() {
    }

    @Test
    @EnabledOnCommand("ZPOPMAX")
    void zpopmax() {
    }

    @Test
    void zrange() {
    }

    @Test
    void zrangeStreaming() {
    }

    private void setup() {
        redis.zadd(key, 1.0, "a", 2.0, "b", 3.0, "c");
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    void zrangeWithScores() {
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    void zrangeWithScoresStreaming() {
    }

    @Test
    void zrangebyscore() {
    }

    @Test
    void zrangebyscoreStreaming() {
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    void zrangebyscoreWithScores() {
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    void zrangebyscoreWithScoresInfinity() {
    }

    @Test
    void zrangebyscoreWithScoresStreaming() {
    }

    @Test
    void zrank() {
    }

    @Test
    void zrem() {
    }

    @Test
    void zremrangebyscore() {
    }

    @Test
    void zremrangebyrank() {
    }

    @Test
    void zrevrange() {
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    void zrevrangeWithScores() {
    }

    @Test
    void zrevrangeStreaming() {
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    void zrevrangeWithScoresStreaming() {
    }

    @Test
    void zrevrangebylex() {
    }

    @Test
    void zrevrangebyscore() {
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    void zrevrangebyscoreWithScores() {
    }

    @Test
    void zrevrangebyscoreStreaming() {
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    void zrevrangebyscoreWithScoresStreaming() {
    }

    @Test
    void zrevrank() {
    }

    @Test
    void zscore() {
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    void zunionstore() {
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    void zStoreArgs() {
    }

    @Test
    void zsscan() {
    }

    @Test
    void zsscanWithCursor() {
    }

    @Test
    void zsscanWithCursorAndArgs() {
    }

    @Test
    void zscanStreaming() {
    }

    @Test
    void zscanStreamingWithCursor() {
    }

    @Test
    void zscanStreamingWithCursorAndArgs() {
    }

    @Test
    void zscanStreamingWithArgs() {
    }

    @Test
    void zscanMultiple() {
    }

    @Test
    void zscanMatch() {
    }

    @Test
    void zlexcount() {
    }

    @Test
    void zrangebylex() {
    }

    @Test
    void zremrangebylex() {
    }

    void setup100KeyValues(Set<String> expect) {
        for (int i = 0; i < 100; i++) {
            redis.zadd(key + 1, i, value + i);
            redis.zadd(key, i, value + i);
            expect.add(value + i);
        }
    }
}
