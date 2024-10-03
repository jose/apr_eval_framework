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
package io.lettuce.core.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Proxy;
import java.util.Set;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisAdvancedClusterAsyncCommandsImpl;
import io.lettuce.core.cluster.RedisAdvancedClusterReactiveCommandsImpl;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.StatefulRedisClusterConnectionImpl;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.reactive.RedisAdvancedClusterReactiveCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.masterslave.MasterSlave;
import io.lettuce.core.masterslave.StatefulRedisMasterSlaveConnection;
import io.lettuce.test.Wait;
import io.lettuce.test.resource.FastShutdown;
import io.lettuce.test.resource.TestClientResources;
import io.lettuce.test.settings.TestSettings;
import io.netty.channel.group.ChannelGroup;

/**
 * @author Mark Paluch
 */
class ConnectionPoolSupportIntegrationTests extends TestSupport {

    private static RedisClient client;
    private static Set<?> channels;

    @BeforeAll
    static void setupClient() {
        client = RedisClient.create(TestClientResources.create(), RedisURI.Builder.redis(host, port).build());
        client.setOptions(ClientOptions.create());
        channels = (ChannelGroup) ReflectionTestUtils.getField(client, "channels");
    }

    @AfterAll
    static void afterClass() {
        FastShutdown.shutdown(client);
        FastShutdown.shutdown(client.getResources());
    }

    @Test
    void genericPoolShouldWorkWithWrappedConnections() throws Exception {
    }

    @Test
    void genericPoolShouldCloseConnectionsAboveMaxIdleSize() throws Exception {
    }

    @Test
    void genericPoolShouldWorkWithPlainConnections() throws Exception {
    }

    @Test
    void softReferencePoolShouldWorkWithPlainConnections() throws Exception {
    }

    @Test
    void genericPoolUsingWrappingShouldPropagateExceptionsCorrectly() throws Exception {
    }

    @Test
    void wrappedConnectionShouldUseWrappers() throws Exception {
    }

    @Test
    void wrappedMasterSlaveConnectionShouldUseWrappers() throws Exception {
    }

    @Test
    void wrappedClusterConnectionShouldUseWrappers() throws Exception {
    }

    @Test
    void plainConnectionShouldNotUseWrappers() throws Exception {
    }

    @Test
    void softRefPoolShouldWorkWithWrappedConnections() throws Exception {
    }

    @Test
    void wrappedObjectClosedAfterReturn() throws Exception {
    }

    @Test
    void tryWithResourcesReturnsConnectionToPool() throws Exception {
    }

    @Test
    void tryWithResourcesReturnsSoftRefConnectionToPool() throws Exception {
    }

    private void borrowAndReturn(ObjectPool<StatefulRedisConnection<String, String>> pool) throws Exception {

        for (int i = 0; i < 10; i++) {
            StatefulRedisConnection<String, String> connection = pool.borrowObject();
            RedisCommands<String, String> sync = connection.sync();
            sync.ping();
            pool.returnObject(connection);
        }
    }

    private void borrowAndCloseTryWithResources(ObjectPool<StatefulRedisConnection<String, String>> pool) throws Exception {

        for (int i = 0; i < 10; i++) {
            try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
                RedisCommands<String, String> sync = connection.sync();
                sync.ping();
            }
        }
    }

    private void borrowAndClose(ObjectPool<StatefulRedisConnection<String, String>> pool) throws Exception {

        for (int i = 0; i < 10; i++) {
            StatefulRedisConnection<String, String> connection = pool.borrowObject();
            RedisCommands<String, String> sync = connection.sync();
            sync.ping();
            connection.close();
        }
    }
}
