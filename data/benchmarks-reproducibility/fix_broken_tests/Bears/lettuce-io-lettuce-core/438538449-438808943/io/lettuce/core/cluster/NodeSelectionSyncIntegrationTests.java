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
import static org.assertj.core.api.Fail.fail;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.RedisCommandExecutionException;
import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.Executions;
import io.lettuce.core.cluster.api.sync.NodeSelection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.internal.LettuceSets;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class NodeSelectionSyncIntegrationTests extends TestSupport {

    private final RedisClusterClient clusterClient;
    private final RedisAdvancedClusterCommands<String, String> commands;

    @Inject
    NodeSelectionSyncIntegrationTests(RedisClusterClient clusterClient,
            StatefulRedisClusterConnection<String, String> connection) {

        this.clusterClient = clusterClient;
        this.commands = connection.sync();
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
    void testNodeSelectionPing() {
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

    static void waitForReplication(RedisAdvancedClusterCommands<String, String> commands, String key, int port)
 {

        NodeSelection<String, String> selection = commands
                .slaves(redisClusterNode -> redisClusterNode.getUri().getPort() == port);
        Wait.untilNotEquals(null, () -> {

            Executions<String> strings = selection.commands().get(key);
            if (strings.stream().filter(s -> s != null).findFirst().isPresent()) {
                return "OK";
            }

            return null;
        }).waitOrTimeout();
    }
}
