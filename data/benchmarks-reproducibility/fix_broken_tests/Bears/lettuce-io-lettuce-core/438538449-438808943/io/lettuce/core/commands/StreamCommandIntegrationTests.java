/*
 * Copyright 2018 the original author or authors.
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

import static io.lettuce.core.protocol.CommandType.XINFO;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.*;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.*;
import io.lettuce.core.XReadArgs.StreamOffset;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.models.stream.PendingMessage;
import io.lettuce.core.models.stream.PendingParser;
import io.lettuce.core.output.NestedMultiOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.condition.EnabledOnCommand;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledOnCommand("XADD")
public class StreamCommandIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    protected StreamCommandIntegrationTests(RedisCommands<String, String> redis) {
        this.redis = redis;
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void xadd() {
    }

    @Test
    void xaddMaxLen() {
    }

    @Test
    void xaddMaxLenEfficientTrimming() {
    }

    @Test
    void xdel() {
    }

    @Test
    void xtrim() {
    }

    @Test
    void xrange() {
    }

    @Test
    void xrevrange() {
    }

    @Test
    void xread() {
    }

    @Test
    void xreadTransactional() {
    }

    @Test
    void xgroupCreate() {
    }

    @Test
    void xgroupread() {
    }

    @Test
    void xpendingWithGroup() {
    }

    @Test
    void xpending() {
    }

    @Test
    void xack() {
    }

    @Test
    void xclaim() {
    }

    @Test
    void xclaimWithArgs() {
    }

    @Test
    void xgroupDestroy() {
    }

    @Test
    void xgroupDelconsumer() {
    }

    @Test
    void xgroupSetid() {
    }
}
