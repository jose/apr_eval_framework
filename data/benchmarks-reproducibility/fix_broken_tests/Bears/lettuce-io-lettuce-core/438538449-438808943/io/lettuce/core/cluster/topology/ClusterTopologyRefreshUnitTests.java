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
package io.lettuce.core.cluster.topology;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import io.lettuce.core.ConnectionFuture;
import io.lettuce.core.RedisException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.protocol.RedisCommand;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DnsResolvers;
import io.lettuce.core.resource.SocketAddressResolver;
import io.lettuce.test.settings.TestSettings;

/**
 * @author Mark Paluch
 * @author Christian Weitendorf
 */
@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ClusterTopologyRefreshUnitTests {

    private static final long COMMAND_TIMEOUT_NS = TimeUnit.MILLISECONDS.toNanos(10);

    private static final String NODE_1_VIEW = "1 127.0.0.1:7380 master,myself - 0 1401258245007 2 disconnected 8000-11999\n"
            + "2 127.0.0.1:7381 master - 111 1401258245007 222 connected 7000 12000 12002-16383\n";
    private static final String NODE_2_VIEW = "1 127.0.0.1:7380 master - 0 1401258245007 2 disconnected 8000-11999\n"
            + "2 127.0.0.1:7381 master,myself - 111 1401258245007 222 connected 7000 12000 12002-16383\n";

    private ClusterTopologyRefresh sut;

    @Mock
    private RedisClusterClient client;

    @Mock
    private StatefulRedisConnection<String, String> connection;

    @Mock
    private ClientResources clientResources;

    @Mock
    private NodeConnectionFactory nodeConnectionFactory;

    @Mock
    private StatefulRedisConnection<String, String> connection1;

    @Mock
    private RedisAsyncCommands<String, String> asyncCommands1;

    @Mock
    private StatefulRedisConnection<String, String> connection2;

    @Mock
    private RedisAsyncCommands<String, String> asyncCommands2;

    @BeforeEach
    void before() {

        when(clientResources.socketAddressResolver()).thenReturn(SocketAddressResolver.create(DnsResolvers.JVM_DEFAULT));
        when(connection1.async()).thenReturn(asyncCommands1);
        when(connection2.async()).thenReturn(asyncCommands2);
        when(connection1.closeAsync()).thenReturn(CompletableFuture.completedFuture(null));
        when(connection2.closeAsync()).thenReturn(CompletableFuture.completedFuture(null));

        when(connection1.dispatch(any(RedisCommand.class))).thenAnswer(invocation -> {

            TimedAsyncCommand command = (TimedAsyncCommand) invocation.getArguments()[0];
            if (command.getType() == CommandType.CLUSTER) {
                command.getOutput().set(ByteBuffer.wrap(NODE_1_VIEW.getBytes()));
                command.complete();
            }

            if (command.getType() == CommandType.CLIENT) {
                command.getOutput().set(ByteBuffer.wrap("c1\nc2\n".getBytes()));
                command.complete();
            }

            command.encodedAtNs = 10;
            command.completedAtNs = 50;

            return command;
        });

        when(connection2.dispatch(any(RedisCommand.class))).thenAnswer(invocation -> {

            TimedAsyncCommand command = (TimedAsyncCommand) invocation.getArguments()[0];
            if (command.getType() == CommandType.CLUSTER) {
                command.getOutput().set(ByteBuffer.wrap(NODE_2_VIEW.getBytes()));
                command.complete();
            }

            if (command.getType() == CommandType.CLIENT) {
                command.getOutput().set(ByteBuffer.wrap("".getBytes()));
                command.complete();
            }

            command.encodedAtNs = 10;
            command.completedAtNs = 20;

            return command;
        });

        sut = new ClusterTopologyRefresh(nodeConnectionFactory, clientResources);
    }

    @Test
    void getNodeSpecificViewsNode1IsFasterThanNode2() throws Exception {
    }

    @Test
    void partitionsReturnedAsReported() throws Exception {
    }

    @Test
    void getNodeSpecificViewTestingNoAddrFilter() throws Exception {
    }

    @Test
    void getNodeSpecificViewsNode2IsFasterThanNode1() throws Exception {
    }

    @Test
    void shouldAttemptToConnectOnlyOnce() {
    }

    @Test
    void shouldFailIfNoNodeConnects() {
    }

    @Test
    void shouldShouldDiscoverNodes() {
    }

    @Test
    void shouldShouldNotDiscoverNodes() {
    }

    @Test
    void shouldNotFailOnDuplicateSeedNodes() {
    }

    @Test
    void shouldCloseConnections() {
    }

    @Test
    void undiscoveredAdditionalNodesShouldBeLastUsingClientCount() {
    }

    @Test
    void discoveredAdditionalNodesShouldBeOrderedUsingClientCount() {
    }

    @Test
    void undiscoveredAdditionalNodesShouldBeLastUsingLatency() {
    }

    @Test
    void discoveredAdditionalNodesShouldBeOrderedUsingLatency() {
    }

    Requests createClusterNodesRequests(int duration, String nodes) {

        RedisURI redisURI = RedisURI.create("redis://localhost:" + duration);
        Connections connections = new Connections();
        connections.addConnection(redisURI, connection);

        Requests requests = connections.requestTopology();
        TimedAsyncCommand<String, String, String> command = requests.getRequest(redisURI);

        command.getOutput().set(ByteBuffer.wrap(nodes.getBytes()));
        command.complete();
        command.encodedAtNs = 0;
        command.completedAtNs = duration;

        return requests;
    }

    Requests createClientListRequests(int duration, String response) {

        RedisURI redisURI = RedisURI.create("redis://localhost:" + duration);
        Connections connections = new Connections();
        connections.addConnection(redisURI, connection);

        Requests requests = connections.requestTopology();
        TimedAsyncCommand<String, String, String> command = requests.getRequest(redisURI);

        command.getOutput().set(ByteBuffer.wrap(response.getBytes()));
        command.complete();

        return requests;
    }

    private static <T> ConnectionFuture<T> completedFuture(T value) {

        return ConnectionFuture.from(InetSocketAddress.createUnresolved(TestSettings.host(), TestSettings.port()),
                CompletableFuture.completedFuture(value));
    }

    private static <T> ConnectionFuture<T> completedWithException(Exception e) {

        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(e);

        return ConnectionFuture.from(InetSocketAddress.createUnresolved(TestSettings.host(), TestSettings.port()),
                future);
    }
}
