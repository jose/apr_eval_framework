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

import static io.lettuce.test.ConnectionTestUtil.getChannel;
import static io.lettuce.test.ConnectionTestUtil.getConnectionWatchdog;
import static io.lettuce.test.ConnectionTestUtil.getStack;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import java.net.ServerSocket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.*;
import io.lettuce.test.Futures;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;
import io.lettuce.test.WithPassword;
import io.lettuce.test.settings.TestSettings;
import io.netty.channel.Channel;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class ClientOptionsIntegrationTests extends TestSupport {

    private final RedisClient client;

    @Inject
    ClientOptionsIntegrationTests(RedisClient client) {
        this.client = client;
    }

    @Test
    void variousClientOptions() {
    }

    @Test
    void requestQueueSize() {
    }

    @Test
    void requestQueueSizeAppliedForReconnect() {
    }

    @Test
    void testHitRequestQueueLimitReconnectWithAuthCommand() {
    }

    @Test
    void testHitRequestQueueLimitReconnectWithUriAuth() {
    }

    @Test
    void testHitRequestQueueLimitReconnectWithUriAuthPingCommand() {
    }

    private void testHitRequestQueueLimit(RedisAsyncCommands<String, String> connection) {

        ConnectionWatchdog watchdog = getConnectionWatchdog(connection.getStatefulConnection());

        watchdog.setListenOnChannelInactive(false);

        connection.quit();

        Wait.untilTrue(() -> !connection.getStatefulConnection().isOpen()).waitOrTimeout();

        List<RedisFuture<String>> pings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            pings.add(connection.ping());
        }

        watchdog.setListenOnChannelInactive(true);
        watchdog.scheduleReconnect();

        for (RedisFuture<String> ping : pings) {
            assertThat(Futures.get(ping)).isEqualTo("PONG");
        }

        connection.getStatefulConnection().close();
    }

    @Test
    void requestQueueSizeOvercommittedReconnect() {
    }

    @Test
    void disconnectedWithoutReconnect() {
    }

    @Test
    void disconnectedRejectCommands() {
    }

    @Test
    void disconnectedAcceptCommands() {
    }

    @Test
    @Inject
    void pingBeforeConnect(StatefulRedisConnection<String, String> sharedConnection) {
    }

    @Test
    void pingBeforeConnectTimeout() throws Exception {
    }

    @Test
    void pingBeforeConnectWithAuthentication() {
    }

    @Test
    void pingBeforeConnectWithAuthenticationTimeout() {
    }

    @Test
    void pingBeforeConnectWithSslAndAuthentication() {
    }

    @Test
    void pingBeforeConnectWithAuthenticationFails() {
    }

    @Test
    void pingBeforeConnectWithSslAndAuthenticationFails() {
    }

    @Test
    void appliesCommandTimeoutToAsyncCommands() {
    }

    @Test
    void appliesCommandTimeoutToReactiveCommands() {
    }

    @Test
    void timeoutExpiresBatchedCommands() {
    }

    @Test
    void pingBeforeConnectWithQueuedCommandsAndReconnect() throws Exception {
    }

    @Test
    void authenticatedPingBeforeConnectWithQueuedCommandsAndReconnect() {
    }
}
