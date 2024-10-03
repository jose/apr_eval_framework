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

import static io.lettuce.core.cluster.ClusterTestUtil.getNodeId;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.cluster.models.slots.ClusterSlotRange;
import io.lettuce.core.cluster.models.slots.ClusterSlotsParser;
import io.lettuce.test.Delay;
import io.lettuce.test.Futures;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class ClusterCommandIntegrationTests extends TestSupport {

    private final RedisClient client;
    private final RedisClusterClient clusterClient;
    private final StatefulRedisConnection<String, String> connection;
    private final RedisClusterAsyncCommands<String, String> async;
    private final RedisClusterCommands<String, String> sync;

    @Inject
    ClusterCommandIntegrationTests(RedisClient client, RedisClusterClient clusterClient) {

        this.client = client;
        this.clusterClient = clusterClient;

        this.connection = client.connect(RedisURI.Builder.redis(host, ClusterTestSettings.port1).build());
        this.sync = connection.sync();
        this.async = connection.async();
    }

    @AfterEach
    void after() {
        connection.close();
    }

    @Test
    void testClusterBumpEpoch() {
    }

    @Test
    void testClusterInfo() {
    }

    @Test
    void testClusterNodes() {
    }

    @Test
    void testClusterNodesSync() {
    }

    @Test
    void testClusterSlaves() {
    }

    @Test
    void testAsking() {
    }

    @Test
    void testReset() {
    }

    @Test
    void testClusterSlots() {
    }

    @Test
    void readOnly() throws Exception {
    }

    @Test
    void readOnlyWithReconnect() throws Exception {
    }

    @Test
    void readOnlyReadWrite() throws Exception {
    }

    @Test
    void clusterSlaves() {
    }

    private void prepareReadonlyTest(String key) {

        async.set(key, value);

        String resultB = Futures.get(async.get(key));
        assertThat(resultB).isEqualTo(value);
        Delay.delay(Duration.ofMillis(500)); // give some time to replicate
    }

    private static void waitUntilValueIsVisible(String key, RedisCommands<String, String> commands) {
        Wait.untilTrue(() -> commands.get(key) != null).waitOrTimeout();
    }
}
