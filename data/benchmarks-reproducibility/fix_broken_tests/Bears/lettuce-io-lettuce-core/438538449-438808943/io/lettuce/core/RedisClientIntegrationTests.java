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
package io.lettuce.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.resource.DefaultEventLoopGroupProvider;
import io.lettuce.test.Futures;
import io.lettuce.test.Wait;
import io.lettuce.test.resource.FastShutdown;
import io.lettuce.test.resource.TestClientResources;
import io.lettuce.test.settings.TestSettings;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author Mark Paluch
 */
class RedisClientIntegrationTests extends TestSupport {

    private final ClientResources clientResources = TestClientResources.get();

    @Test
    void shouldNotifyListener() {
    }

    @Test
    void shouldNotNotifyListenerAfterRemoval() {
    }

    @Test
    void reuseClientConnections() throws Exception {
    }

    @Test
    void reuseClientConnectionsShutdownTwoClients() throws Exception {
    }

    @Test
    void managedClientResources() throws Exception {
    }

    private void connectAndClose(RedisClient client) {
        client.connect().close();
    }

    private RedisClient newClient(DefaultClientResources clientResources) {
        return RedisClient.create(clientResources, RedisURI.create(TestSettings.host(), TestSettings.port()));
    }

    private Map<Class<? extends EventExecutorGroup>, EventExecutorGroup> getExecutors(ClientResources clientResources)
            throws Exception {
        Field eventLoopGroupsField = DefaultEventLoopGroupProvider.class.getDeclaredField("eventLoopGroups");
        eventLoopGroupsField.setAccessible(true);
        return (Map) eventLoopGroupsField.get(clientResources.eventLoopGroupProvider());
    }

    private class TestConnectionListener implements RedisConnectionStateListener {

        volatile SocketAddress onConnectedSocketAddress;
        volatile RedisChannelHandler<?, ?> onConnected;
        volatile RedisChannelHandler<?, ?> onDisconnected;
        volatile RedisChannelHandler<?, ?> onException;

        @Override
        public void onRedisConnected(RedisChannelHandler<?, ?> connection, SocketAddress socketAddress) {
            onConnected = connection;
            onConnectedSocketAddress = socketAddress;
        }

        @Override
        public void onRedisDisconnected(RedisChannelHandler<?, ?> connection) {
            onDisconnected = connection;
        }

        @Override
        public void onRedisExceptionCaught(RedisChannelHandler<?, ?> connection, Throwable cause) {
            onException = connection;
        }
    }
}
