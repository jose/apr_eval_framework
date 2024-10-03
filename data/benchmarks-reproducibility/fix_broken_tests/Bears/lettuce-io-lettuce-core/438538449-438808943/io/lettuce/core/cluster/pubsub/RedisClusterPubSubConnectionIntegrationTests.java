/*
 * Copyright 2016-2018 the original author or authors.
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
package io.lettuce.core.cluster.pubsub;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.RedisURI;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.cluster.pubsub.api.async.NodeSelectionPubSubAsyncCommands;
import io.lettuce.core.cluster.pubsub.api.async.PubSubAsyncNodeSelection;
import io.lettuce.core.cluster.pubsub.api.reactive.NodeSelectionPubSubReactiveCommands;
import io.lettuce.core.cluster.pubsub.api.reactive.PubSubReactiveNodeSelection;
import io.lettuce.core.cluster.pubsub.api.sync.NodeSelectionPubSubCommands;
import io.lettuce.core.cluster.pubsub.api.sync.PubSubNodeSelection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.support.PubSubTestListener;
import io.lettuce.test.Futures;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class RedisClusterPubSubConnectionIntegrationTests extends TestSupport {

    private final RedisClusterClient clusterClient;

    private final PubSubTestListener connectionListener = new PubSubTestListener();
    private final PubSubTestListener nodeListener = new PubSubTestListener();

    private StatefulRedisClusterConnection<String, String> connection;
    private StatefulRedisClusterPubSubConnection<String, String> pubSubConnection;
    private StatefulRedisClusterPubSubConnection<String, String> pubSubConnection2;

    @Inject
    RedisClusterPubSubConnectionIntegrationTests(RedisClusterClient clusterClient) {
        this.clusterClient = clusterClient;
    }

    @BeforeEach
    void openPubSubConnection() {
        connection = clusterClient.connect();
        pubSubConnection = clusterClient.connectPubSub();
        pubSubConnection2 = clusterClient.connectPubSub();

    }

    @AfterEach
    void closePubSubConnection() {
        connection.close();
        pubSubConnection.close();
        pubSubConnection2.close();
    }

    @Test
    void testRegularClientPubSubChannels() {
    }

    @Test
    void testRegularClientPublish() throws Exception {
    }

    @Test
    void testPubSubClientPublish() throws Exception {
    }

    @Test
    void testConnectToLeastClientsNode() {
    }

    @Test
    void testRegularClientPubSubPublish() throws Exception {
    }

    @Test
    void testGetConnectionAsyncByNodeId() {
    }

    @Test
    void testGetConnectionAsyncByHostAndPort() {
    }

    @Test
    void testNodeIdSubscription() throws Exception {
    }

    @Test
    void testNodeMessagePropagationSubscription() throws Exception {
    }

    @Test
    void testNodeHostAndPortMessagePropagationSubscription() throws Exception {
    }

    @Test
    void testAsyncSubscription() throws Exception {
    }

    @Test
    void testSyncSubscription() throws Exception {
    }

    @Test
    void testReactiveSubscription() throws Exception {
    }

    @Test
    void testClusterListener() throws Exception {
    }

    private RedisClusterNode getOtherThan(String nodeId) {
        for (RedisClusterNode redisClusterNode : clusterClient.getPartitions()) {
            if (redisClusterNode.getNodeId().equals(nodeId)) {
                continue;
            }
            return redisClusterNode;
        }

        throw new IllegalStateException("No other nodes than " + nodeId + " available");
    }
}
