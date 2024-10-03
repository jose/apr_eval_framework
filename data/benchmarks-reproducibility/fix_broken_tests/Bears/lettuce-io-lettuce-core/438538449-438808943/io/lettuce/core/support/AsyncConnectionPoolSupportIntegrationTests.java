/*
 * Copyright 2017-2018 the original author or authors.
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
import java.util.concurrent.CompletableFuture;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.StatefulRedisClusterConnectionImpl;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.test.Futures;
import io.lettuce.test.resource.FastShutdown;
import io.lettuce.test.resource.TestClientResources;
import io.netty.channel.group.ChannelGroup;

/**
 * @author Mark Paluch
 */
class AsyncConnectionPoolSupportIntegrationTests extends TestSupport {

    private static RedisClient client;
    private static Set<?> channels;
    private static RedisURI uri = RedisURI.Builder.redis(host, port).build();

    @BeforeAll
    static void setupClient() {

        client = RedisClient.create(TestClientResources.create(), uri);
        client.setOptions(ClientOptions.create());
        channels = (ChannelGroup) ReflectionTestUtils.getField(client, "channels");
    }

    @AfterAll
    static void afterClass() {
        FastShutdown.shutdown(client);
        FastShutdown.shutdown(client.getResources());
    }

    @Test
    void asyncPoolShouldWorkWithWrappedConnections() {
    }

    @Test
    void asyncPoolShouldCloseConnectionsAboveMaxIdleSize() {
    }

    @Test
    void asyncPoolShouldWorkWithPlainConnections() {
    }

    @Test
    void asyncPoolUsingWrappingShouldPropagateExceptionsCorrectly() {
    }

    @Test
    void wrappedConnectionShouldUseWrappers() {
    }

    @Test
    void wrappedObjectClosedAfterReturn() {
    }

    @Test
    void shouldPropagateAsyncFlow() {
    }

    private void borrowAndReturn(AsyncPool<StatefulRedisConnection<String, String>> pool) {

        for (int i = 0; i < 10; i++) {
            StatefulRedisConnection<String, String> connection = Futures.get(pool.acquire());
            RedisCommands<String, String> sync = connection.sync();
            sync.ping();
            Futures.await(pool.release(connection));
        }
    }

    private void borrowAndClose(AsyncPool<StatefulRedisConnection<String, String>> pool) {

        for (int i = 0; i < 10; i++) {
            StatefulRedisConnection<String, String> connection = Futures.get(pool.acquire());
            RedisCommands<String, String> sync = connection.sync();
            sync.ping();
            connection.close();
        }
    }

    private void borrowAndCloseAsync(AsyncPool<StatefulRedisConnection<String, String>> pool) {

        for (int i = 0; i < 10; i++) {
            StatefulRedisConnection<String, String> connection = Futures.get(pool.acquire());
            RedisCommands<String, String> sync = connection.sync();
            sync.ping();
            Futures.get(connection.closeAsync());
        }
    }
}
