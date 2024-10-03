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
package io.lettuce.core.masterslave;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import io.lettuce.core.ConnectionFuture;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.internal.Futures;
import io.lettuce.core.protocol.AsyncCommand;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.core.resource.ClientResources;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author Mark Paluch
 */
@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SentinelTopologyRefreshUnitTests {

    private static final RedisURI host1 = RedisURI.create("localhost", 1234);
    private static final RedisURI host2 = RedisURI.create("localhost", 3456);

    @Mock
    private RedisClient redisClient;

    @Mock
    private StatefulRedisPubSubConnection<String, String> connection;

    @Mock
    private RedisPubSubAsyncCommands<String, String> pubSubAsyncCommands;

    @Mock
    private ClientResources clientResources;

    @Mock
    private EventExecutorGroup eventExecutors;

    @Mock
    private Runnable refreshRunnable;

    @Captor
    private ArgumentCaptor<Runnable> captor;

    private SentinelTopologyRefresh sut;

    @BeforeEach
    void before() {

        when(redisClient.connectPubSubAsync(any(StringCodec.class), eq(host1))).thenReturn(
                ConnectionFuture.completed(null, connection));
        when(clientResources.eventExecutorGroup()).thenReturn(eventExecutors);
        when(redisClient.getResources()).thenReturn(clientResources);
        when(connection.async()).thenReturn(pubSubAsyncCommands);

        AsyncCommand<String, String, Void> command = new AsyncCommand<>(new Command<>(CommandType.PSUBSCRIBE, null));
        command.complete();

        when(connection.async().psubscribe(anyString())).thenReturn(command);

        sut = new SentinelTopologyRefresh(redisClient, "mymaster", Collections.singletonList(host1));
    }

    @AfterEach
    void tearDown() {

        verify(redisClient, never()).connect(any(), any());
        verify(redisClient, never()).connectPubSub(any(), any());
    }

    @Test
    void bind() {
    }

    @Test
    void bindWithSecondSentinelFails() {
    }

    @Test
    void bindWithSentinelRecovery() {
    }

    @Test
    void bindDuringClose() {
    }

    @Test
    void close() {
    }

    @Test
    void bindAfterClose() {
    }

    @Test
    void shouldNotProcessOtherEvents() {
    }

    @Test
    void shouldProcessSlaveDown() {
    }

    @Test
    void shouldProcessSlaveAdded() {
    }

    @Test
    void shouldProcessSlaveBackUp() {
    }

    @Test
    void shouldProcessElectedLeader() {
    }

    @Test
    void shouldProcessSwitchMaster() {
    }

    @Test
    void shouldProcessFixSlaveConfig() {
    }

    @Test
    void shouldProcessConvertToSlave() {
    }

    @Test
    void shouldProcessRoleChange() {
    }

    @Test
    void shouldProcessFailoverEnd() {
    }

    @Test
    void shouldProcessFailoverTimeout() {
    }

    @Test
    void shouldExecuteOnceWithinATimeout() {
    }

    @Test
    void shouldNotProcessIfExecutorIsShuttingDown() {
    }

    private RedisPubSubAdapter<String, String> getAdapter() {
        return (RedisPubSubAdapter<String, String>) ReflectionTestUtils.getField(sut, "adapter");
    }
}
