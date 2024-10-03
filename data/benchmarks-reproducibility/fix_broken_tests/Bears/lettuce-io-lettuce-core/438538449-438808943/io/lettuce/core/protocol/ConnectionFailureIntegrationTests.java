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
package io.lettuce.core.protocol;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.test.*;
import io.lettuce.test.server.RandomResponseServer;
import io.lettuce.test.settings.TestSettings;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectionFailureIntegrationTests extends TestSupport {

    private final RedisClient client;
    private final RedisURI defaultRedisUri = RedisURI.Builder.redis(TestSettings.host(), TestSettings.port()).build();

    @Inject
    ConnectionFailureIntegrationTests(RedisClient client) {
        this.client = client;
    }

    /**
     * Expect to run into Invalid first byte exception instead of timeout.
     *
     * @throws Exception
     */
    @Test
    void pingBeforeConnectFails() throws Exception {
    }

    /**
     * Simulates a failure on reconnect by changing the port to a invalid server and triggering a reconnect. Meanwhile a command
     * is fired to the connection and the watchdog is triggered afterwards to reconnect.
     *
     * Expectation: Command after failed reconnect contains the reconnect exception.
     *
     * @throws Exception
     */
    @Test
    void pingBeforeConnectFailOnReconnect() throws Exception {
    }

    /**
     * Simulates a failure on reconnect by changing the port to a invalid server and triggering a reconnect.
     *
     * Expectation: {@link io.lettuce.core.ConnectionEvents.Reconnect} events are sent.
     *
     * @throws Exception
     */
    @Test
    void pingBeforeConnectFailOnReconnectShouldSendEvents() throws Exception {
    }

    /**
     * Simulates a failure on reconnect by changing the port to a invalid server and triggering a reconnect. Meanwhile a command
     * is fired to the connection and the watchdog is triggered afterwards to reconnect.
     *
     * Expectation: Queued commands are canceled (reset), subsequent commands contain the connection exception.
     *
     * @throws Exception
     */
    @Test
    void cancelCommandsOnReconnectFailure() throws Exception {
    }

    /**
     * Expect to disable {@link ConnectionWatchdog} when closing a broken connection.
     *
     */
    @Test
    void closingDisconnectedConnectionShouldDisableConnectionWatchdog() {
    }

    RandomResponseServer getRandomResponseServer() throws InterruptedException {
        RandomResponseServer ts = new RandomResponseServer();
        ts.initialize(TestSettings.nonexistentPort());
        return ts;
    }
}
