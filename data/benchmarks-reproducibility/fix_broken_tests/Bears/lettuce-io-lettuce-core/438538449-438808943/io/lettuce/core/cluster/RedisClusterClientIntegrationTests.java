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

import static io.lettuce.core.cluster.ClusterTestUtil.getOwnPartition;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.cluster.pubsub.StatefulRedisClusterPubSubConnection;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.protocol.AsyncCommand;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.test.Delay;
import io.lettuce.test.Futures;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;
import io.lettuce.test.resource.FastShutdown;
import io.lettuce.test.resource.TestClientResources;
import io.lettuce.test.settings.TestSettings;

/**
 * @author Mark Paluch
 */
@SuppressWarnings("unchecked")
@ExtendWith(LettuceExtension.class)
class RedisClusterClientIntegrationTests extends TestSupport {

    private final RedisClient client;
    private final RedisClusterClient clusterClient;

    private StatefulRedisConnection<String, String> redis1;
    private StatefulRedisConnection<String, String> redis2;
    private StatefulRedisConnection<String, String> redis3;
    private StatefulRedisConnection<String, String> redis4;

    private RedisCommands<String, String> redissync1;
    private RedisCommands<String, String> redissync2;
    private RedisCommands<String, String> redissync3;
    private RedisCommands<String, String> redissync4;

    private RedisAdvancedClusterCommands<String, String> sync;
    private StatefulRedisClusterConnection<String, String> connection;

    @Inject
    RedisClusterClientIntegrationTests(RedisClient client, RedisClusterClient clusterClient) {
        this.client = client;
        this.clusterClient = clusterClient;
    }

    @BeforeEach
    void before() {

        clusterClient.setOptions(ClusterClientOptions.create());

        redis1 = client.connect(RedisURI.Builder.redis(host, ClusterTestSettings.port1).build());
        redis2 = client.connect(RedisURI.Builder.redis(host, ClusterTestSettings.port2).build());
        redis3 = client.connect(RedisURI.Builder.redis(host, ClusterTestSettings.port3).build());
        redis4 = client.connect(RedisURI.Builder.redis(host, ClusterTestSettings.port4).build());

        redissync1 = redis1.sync();
        redissync2 = redis2.sync();
        redissync3 = redis3.sync();
        redissync4 = redis4.sync();

        clusterClient.reloadPartitions();
        connection = clusterClient.connect();
        sync = connection.sync();
    }

    @AfterEach
    void after() {
        connection.close();
        redis1.close();

        redissync1.getStatefulConnection().close();
        redissync2.getStatefulConnection().close();
        redissync3.getStatefulConnection().close();
        redissync4.getStatefulConnection().close();
    }

    @Test
    void statefulConnectionFromSync() {
    }

    @Test
    void statefulConnectionFromAsync() {
    }

    @Test
    void shouldApplyTimeoutOnRegularConnection() {
    }

    @Test
    void shouldApplyTimeoutOnRegularConnectionUsingCodec() {
    }

    @Test
    void shouldApplyTimeoutOnPubSubConnection() {
    }

    @Test
    void shouldApplyTimeoutOnPubSubConnectionUsingCodec() {
    }

    @Test
    void clusterConnectionShouldSetClientName() {
    }

    @Test
    void pubSubclusterConnectionShouldSetClientName() {
    }

    @Test
    void reloadPartitions() {
    }

    @Test
    void reloadPartitionsWithDynamicSourcesFallsBackToInitialSeedNodes() {
    }

    @Test
    void testClusteredOperations() {
    }

    @Test
    void testReset() {
    }

    @Test
    @SuppressWarnings({ "rawtypes" })
    void testClusterCommandRedirection() {
    }

    @Test
    @SuppressWarnings({ "rawtypes" })
    void testClusterRedirection() {
    }

    @Test
    @SuppressWarnings({ "rawtypes" })
    void testClusterRedirectionLimit() throws Exception {
    }

    @Test
    void closeConnection() {
    }

    @Test
    void clusterAuth() {
    }

    @Test
    void clusterAuthPingBeforeConnect() {
    }

    @Test
    void clusterNeedsAuthButNotSupplied() {
    }

    @Test
    void noClusterNodeAvailable() {
    }

    @Test
    void getClusterNodeConnection() {
    }

    @Test
    void operateOnNodeConnection() {
    }

    @Test
    void testGetConnectionAsyncByNodeId() {
    }

    @Test
    void testGetConnectionAsyncByHostAndPort() {
    }

    @Test
    void testStatefulConnection() {
    }

    @Test
    void getButNoPartitionForSlothash() {
    }

    @Test
    void readOnlyOnCluster() {
    }

    @Test
    void getKeysInSlot() {
    }

    @Test
    void countKeysInSlot() {
    }

    @Test
    void testClusterCountFailureReports() {
    }

    @Test
    void testClusterKeyslot() {
    }

    @Test
    void testClusterSaveconfig() {
    }

    @Test
    void testClusterSetConfigEpoch() {
    }

    @Test
    void testReadFrom() {
    }

    @Test
    void testReadFromNull() {
    }

    @Test
    void testPfmerge() {
    }
}
