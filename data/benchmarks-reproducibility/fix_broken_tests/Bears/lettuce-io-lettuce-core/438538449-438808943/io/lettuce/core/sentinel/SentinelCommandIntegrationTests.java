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

import static io.lettuce.test.settings.TestSettings.hostAddr;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;
import io.lettuce.core.sentinel.api.sync.RedisSentinelCommands;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;
import io.lettuce.test.settings.TestSettings;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
public class SentinelCommandIntegrationTests extends TestSupport {

    private final RedisClient redisClient;
    private StatefulRedisSentinelConnection<String, String> connection;
    private RedisSentinelCommands<String, String> sentinel;

    @Inject
    public SentinelCommandIntegrationTests(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @BeforeEach
    void before() {

        this.connection = this.redisClient.connectSentinel(SentinelTestSettings.SENTINEL_URI);
        this.sentinel = getSyncConnection(this.connection);
    }

    protected RedisSentinelCommands<String, String> getSyncConnection(StatefulRedisSentinelConnection<String, String> connection) {
        return connection.sync();
    }

    @AfterEach
    void after() {
        this.connection.close();
    }

    @Test
    void getMasterAddr() {
    }

    @Test
    void getMasterAddrButNoMasterPresent() {
    }

    @Test
    void getMasterAddrByName() {
    }

    @Test
    void masters() {
    }

    @Test
    void sentinelConnectWith() {
    }

    @Test
    void sentinelConnectWrongMaster() {
    }

    @Test
    void getMaster() {
    }

    @Test
    void role() {
    }

    @Test
    void getSlaves() {
    }

    @Test
    void reset() {
    }

    @Test
    void failover() {
    }

    @Test
    void monitor() {
    }

    @Test
    void ping() {
    }

    @Test
    void set() {
    }
}
