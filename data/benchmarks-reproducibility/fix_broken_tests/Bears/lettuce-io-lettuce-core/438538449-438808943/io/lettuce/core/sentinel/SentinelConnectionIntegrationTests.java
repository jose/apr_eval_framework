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
package io.lettuce.core.sentinel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;
import io.lettuce.core.sentinel.api.async.RedisSentinelAsyncCommands;
import io.lettuce.core.sentinel.api.sync.RedisSentinelCommands;
import io.lettuce.test.Futures;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;
import io.lettuce.test.settings.TestSettings;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
public class SentinelConnectionIntegrationTests extends TestSupport {

    private final RedisClient redisClient;
    private StatefulRedisSentinelConnection<String, String> connection;
    private RedisSentinelCommands<String, String> sentinel;
    private RedisSentinelAsyncCommands<String, String> sentinelAsync;

    @Inject
    public SentinelConnectionIntegrationTests(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @BeforeEach
    void before() {

        this.connection = this.redisClient.connectSentinel(SentinelTestSettings.SENTINEL_URI);
        this.sentinel = getSyncConnection(this.connection);
        this.sentinelAsync = this.connection.async();
    }

    protected RedisSentinelCommands<String, String> getSyncConnection(StatefulRedisSentinelConnection<String, String> connection) {
        return connection.sync();
    }

    @AfterEach
    void after() {
        this.connection.close();
    }

    @Test
    void testAsync() {
    }

    @Test
    void testFuture() throws Exception {
    }

    @Test
    void testStatefulConnection() {
    }

    @Test
    void testSyncConnection() {
    }

    @Test
    void testSyncAsyncConversion() {
    }

    @Test
    void testSyncClose() {
    }

    @Test
    void testAsyncClose() {
    }

    @Test
    void connectToOneNode() {
    }

    @Test
    void connectWithByteCodec() {
    }

    @Test
    void sentinelConnectionPingBeforeConnectShouldDiscardPassword() {
    }

    @Test
    void sentinelConnectionShouldSetClientName() {
    }

    @Test
    void sentinelManagedConnectionShouldSetClientName() {
    }
}
