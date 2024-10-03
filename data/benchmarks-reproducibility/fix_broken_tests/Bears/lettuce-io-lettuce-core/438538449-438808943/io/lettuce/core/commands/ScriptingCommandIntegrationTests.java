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

import static io.lettuce.core.ScriptOutputType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisException;
import io.lettuce.core.RedisNoScriptException;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScriptingCommandIntegrationTests extends TestSupport {

    private final RedisClient client;
    private final RedisCommands<String, String> redis;

    @Inject
    protected ScriptingCommandIntegrationTests(RedisClient client, RedisCommands<String, String> redis) {
        this.client = client;
        this.redis = redis;
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @AfterEach
    void tearDown() {

        Wait.untilNoException(() -> {
            try {
                redis.scriptKill();
            } catch (RedisException e) {
                // ignore
            }
            redis.ping();
        }).waitOrTimeout();

    }

    @Test
    void eval() {
    }

    @Test
    void evalWithSingleKey() {
    }

    @Test
    void evalWithNonAsciiChar() {
    }

    @Test
    void evalReturningNullInMulti() {
    }

    @Test
    void evalWithKeys() {
    }

    @Test
    void evalWithArgs() {
    }

    @Test
    void evalsha() {
    }

    @Test
    void evalshaWithMulti() {
    }

    @Test
    void evalshaWithKeys() {
    }

    @Test
    void evalshaWithArgs() {
    }

    @Test
    void script() throws InterruptedException {
    }
}
