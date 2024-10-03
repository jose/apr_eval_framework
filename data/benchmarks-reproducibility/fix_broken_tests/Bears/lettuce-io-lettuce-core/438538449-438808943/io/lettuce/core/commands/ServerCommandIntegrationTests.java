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
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.models.command.CommandDetail;
import io.lettuce.core.models.command.CommandDetailParser;
import io.lettuce.core.models.role.RedisInstance;
import io.lettuce.core.models.role.RoleParser;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;
import io.lettuce.test.condition.EnabledOnCommand;
import io.lettuce.test.condition.RedisConditions;
import io.lettuce.test.settings.TestSettings;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServerCommandIntegrationTests extends TestSupport {

    private final RedisClient client;
    private final RedisCommands<String, String> redis;

    @Inject
    protected ServerCommandIntegrationTests(RedisClient client, RedisCommands<String, String> redis) {
        this.client = client;
        this.redis = redis;
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void bgrewriteaof() {
    }

    @Test
    void bgsave() {
    }

    @Test
    void clientGetSetname() {
    }

    @Test
    void clientPause() {
    }

    @Test
    void clientKill() {
    }

    @Test
    void clientKillExtended() {
    }

    @Test
    void clientList() {
    }

    @Test
    void clientUnblock() throws InterruptedException {
    }

    @Test
    void commandCount() {
    }

    @Test
    void command() {
    }

    @Test
    void commandInfo() {
    }

    @Test
    void configGet() {
    }

    @Test
    void configResetstat() {
    }

    @Test
    void configSet() {
    }

    @Test
    void configRewrite() {
    }

    @Test
    void dbsize() {
    }

    @Test
    @Disabled("Causes instabilities")
    void debugCrashAndRecover() {
    }

    @Test
    void debugHtstats() {
    }

    @Test
    void debugObject() {
    }

    @Test
    void debugReload() {
    }

    @Test
    @Disabled("Causes instabilities")
    void debugRestart() {
    }

    @Test
    void debugSdslen() {
    }

    @Test
    void flushall() {
    }

    @Test
    void flushallAsync() {
    }

    @Test
    void flushdb() {
    }

    @Test
    void flushdbAsync() {
    }

    @Test
    void info() {
    }

    @Test
    void lastsave() {
    }

    @Test
    void save() {
    }

    @Test
    void slaveof() {
    }

    @Test
    void slaveofEmptyHost() {
    }

    @Test
    void role() {
    }

    @Test
    void slaveofNoOne() {
    }

    @Test
    @SuppressWarnings("unchecked")
    void slowlog() {
    }

    @Test
    @EnabledOnCommand("SWAPDB")
    void swapdb() {
    }

    private boolean noSaveInProgress() {

        String info = redis.info();

        return !info.contains("aof_rewrite_in_progress:1") && !info.contains("rdb_bgsave_in_progress:1");
    }
}
