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

import static io.lettuce.core.RedisURI.Builder.redis;
import static io.lettuce.core.codec.StringCodec.UTF8;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;
import io.lettuce.test.Futures;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 * @author Jongyeol Choi
 */
@ExtendWith(LettuceExtension.class)
class RedisClientConnectIntegrationTests extends TestSupport {

    private static final Duration EXPECTED_TIMEOUT = Duration.ofMillis(500);

    private final RedisClient client;

    @Inject
    RedisClientConnectIntegrationTests(RedisClient client) {
        this.client = client;
    }

    @BeforeEach
    void before() {
        client.setDefaultTimeout(EXPECTED_TIMEOUT);
    }

    /*
     * Standalone/Stateful
     */
    @Test
    void connectClientUri() {
    }

    @Test
    void connectCodecClientUri() {
    }

    @Test
    void connectOwnUri() {
    }

    @Test
    void connectMissingHostAndSocketUri() {
    }

    @Test
    void connectSentinelMissingHostAndSocketUri() {
    }

    @Test
    void connectCodecOwnUri() {
    }

    @Test
    void connectAsyncCodecOwnUri() {
    }

    @Test
    void connectCodecMissingHostAndSocketUri() {
    }

    @Test
    void connectcodecSentinelMissingHostAndSocketUri() {
    }

    @Test
    @Disabled("Non-deterministic behavior. Can cause a deadlock")
    void shutdownSyncInRedisFutureTest() {
    }

    @Test
    void shutdownAsyncInRedisFutureTest() {
    }

    /*
     * Standalone/PubSub Stateful
     */
    @Test
    void connectPubSubClientUri() {
    }

    @Test
    void connectPubSubCodecClientUri() {
    }

    @Test
    void connectPubSubOwnUri() {
    }

    @Test
    void connectPubSubMissingHostAndSocketUri() {
    }

    @Test
    void connectPubSubSentinelMissingHostAndSocketUri() {
    }

    @Test
    void connectPubSubCodecOwnUri() {
    }

    @Test
    void connectPubSubAsync() {
    }

    @Test
    void connectPubSubCodecMissingHostAndSocketUri() {
    }

    @Test
    void connectPubSubCodecSentinelMissingHostAndSocketUri() {
    }

    /*
     * Sentinel Stateful
     */
    @Test
    void connectSentinelClientUri() {
    }

    @Test
    void connectSentinelCodecClientUri() {
    }

    @Test
    void connectSentinelAndMissingHostAndSocketUri() {
    }

    @Test
    void connectSentinelSentinelMissingHostAndSocketUri() {
    }

    @Test
    void connectSentinelOwnUri() {
    }

    @Test
    void connectSentinelCodecOwnUri() {
    }

    @Test
    void connectSentinelCodecMissingHostAndSocketUri() {
    }

    @Test
    void connectSentinelCodecSentinelMissingHostAndSocketUri() {
    }

    private static RedisURI invalidSentinel() {

        RedisURI redisURI = new RedisURI();
        redisURI.getSentinels().add(new RedisURI());

        return redisURI;
    }
}
