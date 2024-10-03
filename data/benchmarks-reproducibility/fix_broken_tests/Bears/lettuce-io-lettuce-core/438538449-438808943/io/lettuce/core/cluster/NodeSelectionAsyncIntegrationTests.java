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

import static io.lettuce.core.ScriptOutputType.STATUS;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.TestSupport;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.AsyncExecutions;
import io.lettuce.core.cluster.api.async.AsyncNodeSelection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.internal.LettuceSets;
import io.lettuce.test.Delay;
import io.lettuce.test.Futures;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class NodeSelectionAsyncIntegrationTests extends TestSupport {

    private final RedisClusterClient clusterClient;
    private final RedisAdvancedClusterAsyncCommands<String, String> commands;

    @Inject
    NodeSelectionAsyncIntegrationTests(RedisClusterClient clusterClient,
            StatefulRedisClusterConnection<String, String> connection) {

        this.clusterClient = clusterClient;
        this.commands = connection.async();
        connection.sync().flushall();
    }

    @Test
    void testMultiNodeOperations() {
    }

    @Test
    void testNodeSelectionCount() {
    }

    @Test
    void testNodeSelection() {
    }

    @Test
    void testDynamicNodeSelection() {
    }

    @Test
    void testNodeSelectionAsyncPing() {
    }

    @Test
    void testStaticNodeSelection() {
    }

    @Test
    void testAsynchronicityOfMultiNodeExecution() {
    }

    @Test
    void testSlavesReadWrite() {
    }

    @Test
    void testSlavesWithReadOnly() {
    }

    void waitForReplication(String key, int port) {
        waitForReplication(commands, key, port);
    }

    static void waitForReplication(RedisAdvancedClusterAsyncCommands<String, String> commands, String key, int port)
 {

        AsyncNodeSelection<String, String> selection = commands
                .slaves(redisClusterNode -> redisClusterNode.getUri().getPort() == port);
        Wait.untilNotEquals(null, () -> {
            for (CompletableFuture<String> future : selection.commands().get(key).futures()) {

                Futures.await(future);

                String result = Futures.get((Future<String>) future);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }).waitOrTimeout();
    }
}
