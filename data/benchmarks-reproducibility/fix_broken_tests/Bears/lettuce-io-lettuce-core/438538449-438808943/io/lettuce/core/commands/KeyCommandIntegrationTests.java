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
import static org.junit.Assert.assertNotEquals;

import java.time.Duration;
import java.util.*;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.*;
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
public class KeyCommandIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    protected KeyCommandIntegrationTests(RedisCommands<String, String> redis) {
        this.redis = redis;
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void del() {
    }

    @Test
    @EnabledOnCommand("UNLINK")
    void unlink() {
    }

    @Test
    void dump() {
    }

    @Test
    void exists() {
    }

    @Test
    void existsVariadic() {
    }

    @Test
    void expire() {
    }

    @Test
    void expireat() {
    }

    @Test
    void keys() {
    }

    @Test
    void keysStreaming() {
    }

    @Test
    public void move() {
    }

    @Test
    void objectEncoding() {
    }

    @Test
    void objectIdletime() {
    }

    @Test
    void objectRefcount() {
    }

    @Test
    void persist() {
    }

    @Test
    void pexpire() {
    }

    @Test
    void pexpireat() {
    }

    @Test
    void pttl() {
    }

    @Test
    void randomkey() {
    }

    @Test
    void rename() {
    }

    @Test
    void renameNonexistentKey() {
    }

    @Test
    void renamenx() {
    }

    @Test
    void renamenxNonexistentKey() {
    }

    @Test
    void restore() {
    }

    @Test
    void restoreReplace() {
    }

    @Test
    @EnabledOnCommand("TOUCH")
    void touch() {
    }

    @Test
    void ttl() {
    }

    @Test
    void type() {
    }

    @Test
    void scan() {
    }

    @Test
    void scanWithArgs() {
    }

    @Test
    void scanInitialCursor() {
    }

    @Test
    void scanFinishedCursor() {
    }

    @Test
    void scanNullCursor() {
    }

    @Test
    void scanStreaming() {
    }

    @Test
    void scanStreamingWithCursor() {
    }

    @Test
    void scanStreamingWithCursorAndArgs() {
    }

    @Test
    void scanStreamingArgs() {
    }

    @Test
    void scanMultiple() {
    }

    @Test
    void scanMatch() {
    }

    void setup100KeyValues(Set<String> expect) {
        for (int i = 0; i < 100; i++) {
            redis.set(key + i, value + i);
            expect.add(key + i);
        }
    }
}
