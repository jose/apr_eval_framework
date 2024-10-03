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

import static io.lettuce.test.settings.TestSettings.host;
import static io.lettuce.test.settings.TestSettings.port;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Arrays;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.MigrateArgs;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.CanConnect;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;
import io.lettuce.test.settings.TestSettings;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class RunOnlyOnceServerCommandIntegrationTests extends TestSupport {

    private final RedisClient client;
    private final StatefulRedisConnection<String, String> connection;
    private final RedisCommands<String, String> redis;

    @Inject
    RunOnlyOnceServerCommandIntegrationTests(RedisClient client, StatefulRedisConnection<String, String> connection) {

        this.client = client;
        this.connection = connection;
        this.redis = connection.sync();
    }

    /**
     * Executed in order: 1 this test causes a stop of the redis. This means, you cannot repeat the test without restarting your
     * redis.
     */
    @Test
    void debugSegfault() {
    }

    /**
     * Executed in order: 2
     */
    @Test
    void migrate() {
    }

    /**
     * Executed in order: 3
     */
    @Test
    void migrateCopyReplace() {
    }

    /**
     * Executed in order: 4 this test causes a stop of the redis. This means, you cannot repeat the test without restarting your
     * redis.
     */
    @Test
    void shutdown() {
    }
}
