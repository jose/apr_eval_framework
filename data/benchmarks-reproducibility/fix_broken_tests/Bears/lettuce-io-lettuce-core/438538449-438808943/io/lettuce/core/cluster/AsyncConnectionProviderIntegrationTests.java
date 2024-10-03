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
package io.lettuce.core.cluster;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.util.SocketUtils;
import org.springframework.util.StopWatch;

import reactor.core.publisher.Mono;
import io.lettuce.core.ConnectionFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.ClusterNodeConnectionFactory.ConnectionKey;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.internal.AsyncConnectionProvider;
import io.lettuce.test.Futures;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.settings.TestSettings;
import io.netty.channel.ConnectTimeoutException;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class AsyncConnectionProviderIntegrationTests {

    private final RedisClusterClient redisClient;
    private ServerSocket serverSocket;
    private CountDownLatch connectInitiated = new CountDownLatch(1);

    private AsyncConnectionProvider<ConnectionKey, StatefulRedisConnection<String, String>, ConnectionFuture<StatefulRedisConnection<String, String>>> sut;

    @Inject
    AsyncConnectionProviderIntegrationTests(RedisClusterClient redisClient) {
        this.redisClient = redisClient;
    }

    @BeforeEach
    void before() throws Exception {

        serverSocket = new ServerSocket(SocketUtils.findAvailableTcpPort(), 1);

        sut = new AsyncConnectionProvider<>(
                new AbstractClusterNodeConnectionFactory<String, String>(redisClient.getResources()) {
            @Override
            public ConnectionFuture<StatefulRedisConnection<String, String>> apply(ConnectionKey connectionKey) {

                RedisURI redisURI = RedisURI.create(TestSettings.host(), serverSocket.getLocalPort());
                redisURI.setTimeout(Duration.ofSeconds(5));

                ConnectionFuture<StatefulRedisConnection<String, String>> future = redisClient.connectToNodeAsync(
                        StringCodec.UTF8, "", null,
                        Mono.just(new InetSocketAddress(connectionKey.host, serverSocket.getLocalPort())));

                connectInitiated.countDown();

                return future;
            }
        });
    }

    @AfterEach
    void after() throws Exception {
        serverSocket.close();
    }

    @Test
    void shouldCreateConnection() throws IOException {
    }

    @Test
    void shouldMaintainConnectionCount() throws IOException {
    }

    @Test
    void shouldCloseConnectionByKey() throws IOException {
    }

    @Test
    void shouldCloseConnections() throws IOException {
    }

    @Test
    void connectShouldFail() throws Exception {
    }

    @Test
    void connectShouldFailConcurrently() throws Exception {
    }

    @Test
    void shouldCloseAsync() throws Exception {
    }
}
