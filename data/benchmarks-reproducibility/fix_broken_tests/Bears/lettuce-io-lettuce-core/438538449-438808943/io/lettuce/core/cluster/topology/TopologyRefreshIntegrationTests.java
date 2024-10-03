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

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import io.lettuce.category.SlowTests;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.async.BaseRedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTestSettings;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.test.Delay;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;
import io.lettuce.test.resource.FastShutdown;
import io.lettuce.test.settings.TestSettings;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * Test for topology refreshing.
 *
 * @author Mark Paluch
 */
@SuppressWarnings({ "unchecked" })
@SlowTests
@ExtendWith(LettuceExtension.class)
class TopologyRefreshIntegrationTests extends TestSupport {

    private static final String host = TestSettings.hostAddr();
    private final RedisClient client;

    private RedisClusterClient clusterClient;
    private RedisCommands<String, String> redis1;
    private RedisCommands<String, String> redis2;

    @Inject
    TopologyRefreshIntegrationTests(RedisClient client) {
        this.client = client;
    }

    @BeforeEach
    void openConnection() {
        clusterClient = RedisClusterClient.create(client.getResources(), RedisURI.Builder
                .redis(host, ClusterTestSettings.port1).build());
        redis1 = client.connect(RedisURI.Builder.redis(ClusterTestSettings.host, ClusterTestSettings.port1).build()).sync();
        redis2 = client.connect(RedisURI.Builder.redis(ClusterTestSettings.host, ClusterTestSettings.port2).build()).sync();
    }

    @AfterEach
    void closeConnection() {
        redis1.getStatefulConnection().close();
        redis2.getStatefulConnection().close();
        FastShutdown.shutdown(clusterClient);
    }

    @Test
    void shouldUnsubscribeTopologyRefresh() {
    }

    @Test
    void changeTopologyWhileOperations() {
    }

    @Test
    void dynamicSourcesProvidesClientCountForAllNodes() {
    }

    @Test
    void staticSourcesProvidesClientCountForSeedNodes() {
    }

    @Test
    void adaptiveTopologyUpdateOnDisconnectNodeIdConnection() {
    }

    @Test
    void adaptiveTopologyUpdateOnDisconnectHostAndPortConnection() {
    }

    @Test
    void adaptiveTopologyUpdateOnDisconnectDefaultConnection() {
    }

    @Test
    void adaptiveTopologyUpdateIsRateLimited() {
    }

    @Test
    void adaptiveTopologyUpdatetUsesTimeout() {
    }

    @Test
    void adaptiveTriggerDoesNotFireOnSingleReconnect() {
    }

    @Test
    void adaptiveTriggerOnMoveRedirection() {
    }

    private void runReconnectTest(
            BiFunction<RedisAdvancedClusterAsyncCommands<String, String>, RedisClusterNode, BaseRedisAsyncCommands> function) {

        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()//
                .refreshTriggersReconnectAttempts(0)//
                .enableAllAdaptiveRefreshTriggers()//
                .build();
        clusterClient.setOptions(ClusterClientOptions.builder().topologyRefreshOptions(topologyRefreshOptions).build());
        RedisAdvancedClusterAsyncCommands<String, String> clusterConnection = clusterClient.connect().async();

        RedisClusterNode node = clusterClient.getPartitions().getPartition(0);
        BaseRedisAsyncCommands closeable = function.apply(clusterConnection, node);
        clusterClient.getPartitions().clear();

        closeable.quit();

        Wait.untilTrue(() -> {
            return !clusterClient.getPartitions().isEmpty();
        }).waitOrTimeout();

        if (closeable instanceof RedisAdvancedClusterCommands) {
            ((RedisAdvancedClusterCommands) closeable).getStatefulConnection().close();
        }
        clusterConnection.getStatefulConnection().close();
    }
}
