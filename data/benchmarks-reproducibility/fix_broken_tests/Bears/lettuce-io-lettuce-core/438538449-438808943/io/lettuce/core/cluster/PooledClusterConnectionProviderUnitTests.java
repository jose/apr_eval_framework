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
package io.lettuce.core.cluster;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.ClusterConnectionProvider.Intent;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.protocol.AsyncCommand;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.resource.ClientResources;

/**
 * @author Mark Paluch
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PooledClusterConnectionProviderUnitTests {

    private static final Utf8StringCodec CODEC = new Utf8StringCodec();

    private PooledClusterConnectionProvider<String, String> sut;

    @Mock
    SocketAddress socketAddressMock;

    @Mock
    RedisClusterClient clientMock;

    @Mock
    RedisChannelWriter writerMock;

    @Mock(extraInterfaces = StatefulRedisConnection.class)
    RedisChannelHandler<String, String> channelHandlerMock;

    private StatefulRedisConnection<String, String> nodeConnectionMock;

    @Mock
    RedisCommands<String, String> commandsMock;

    @Mock
    RedisAsyncCommands<String, String> asyncCommandsMock;

    @Mock
    ClientResources clientResourcesMock;

    @Mock
    ClusterEventListener clusterEventListener;

    private Partitions partitions = new Partitions();

    @BeforeEach
    void before() {

        nodeConnectionMock = (StatefulRedisConnection) channelHandlerMock;

        sut = new PooledClusterConnectionProvider<>(clientMock, writerMock, CODEC, clusterEventListener);

        List<Integer> slots1 = IntStream.range(0, 8192).boxed().collect(Collectors.toList());
        List<Integer> slots2 = IntStream.range(8192, SlotHash.SLOT_COUNT).boxed().collect(Collectors.toList());

        partitions.add(new RedisClusterNode(RedisURI.create("localhost", 1), "1", true, null, 0, 0, 0, slots1, Collections
                .singleton(RedisClusterNode.NodeFlag.MASTER)));
        partitions.add(new RedisClusterNode(RedisURI.create("localhost", 2), "2", true, "1", 0, 0, 0, slots2, Collections
                .singleton(RedisClusterNode.NodeFlag.SLAVE)));

        sut.setPartitions(partitions);

        when(nodeConnectionMock.async()).thenReturn(asyncCommandsMock);
    }

    @Test
    void shouldObtainConnection() {
    }

    @Test
    void shouldObtainConnectionReadFromSlave() {
    }

    @Test
    void shouldCloseConnectionOnConnectFailure() {
    }

    @Test
    void shouldRetryConnectionAttemptAfterConnectionAttemptWasBroken() {
    }

    @Test
    void shouldSelectSuccessfulConnectionIfOtherNodesFailed() {
    }

    @Test
    void shouldFailIfAllReadCandidateNodesFail() {
    }

    @Test
    void shouldCloseConnections() {
    }

    @Test
    void shouldRejectConnectionsToUnknownNodeId() {
    }

    @Test
    void shouldRejectConnectionsToUnknownNodeHostAndPort() {
    }
}
