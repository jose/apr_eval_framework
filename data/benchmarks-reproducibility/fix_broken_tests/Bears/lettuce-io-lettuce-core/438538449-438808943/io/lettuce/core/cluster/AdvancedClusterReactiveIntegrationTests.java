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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import io.lettuce.core.*;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.api.reactive.RedisAdvancedClusterReactiveCommands;
import io.lettuce.core.cluster.api.reactive.RedisClusterReactiveCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.test.*;
import io.lettuce.test.condition.EnabledOnCommand;
import io.netty.util.internal.ConcurrentSet;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class AdvancedClusterReactiveIntegrationTests extends TestSupport {

    private static final String KEY_ON_NODE_1 = "a";
    private static final String KEY_ON_NODE_2 = "b";

    private final RedisClusterClient clusterClient;
    private final RedisAdvancedClusterReactiveCommands<String, String> commands;
    private final RedisAdvancedClusterCommands<String, String> syncCommands;

    @Inject
    AdvancedClusterReactiveIntegrationTests(RedisClusterClient clusterClient,
            StatefulRedisClusterConnection<String, String> connection) {
        this.clusterClient = clusterClient;
        this.commands = connection.reactive();
        this.syncCommands = connection.sync();
    }

    @BeforeEach
    void setUp() {
        syncCommands.flushall();
    }

    @Test
    void unknownNodeId() {
    }

    @Test
    void invalidHost() {
    }

    @Test
    void msetCrossSlot() {
    }

    @Test
    void msetnxCrossSlot() {
    }

    @Test
    void mgetCrossSlot() {
    }

    @Test
    void mgetCrossSlotStreaming() {
    }

    @Test
    void delCrossSlot() {
    }

    @Test
    @EnabledOnCommand("UNLINK")
    void unlinkCrossSlot() {
    }

    @Test
    void clientSetname() {
    }

    @Test
    void clientSetnameRunOnError() {
    }

    @Test
    void dbSize() {
    }

    @Test
    void flushall() {
    }

    @Test
    void flushdb() {
    }

    @Test
    void keys() {
    }

    @Test
    void keysDoesNotRunIntoRaceConditions() {
    }

    @Test
    void keysStreaming() {
    }

    @Test
    void randomKey() {
    }

    @Test
    void scriptFlush() {
    }

    @Test
    void scriptKill() {
    }

    @Test
    void scriptLoad() {
    }

    @Test
    @Disabled("Run me manually, I will shutdown all your cluster nodes so you need to restart the Redis Cluster after this test")
    void shutdown() {
    }

    @Test
    void readFromSlaves() {
    }

    @Test
    void clusterScan() {
    }

    @Test
    void clusterScanWithArgs() {
    }

    @Test
    void clusterScanStreaming() {
    }

    @Test
    void clusterScanStreamingWithArgs() {
    }

    private void writeKeysToTwoNodes() {
        syncCommands.set(KEY_ON_NODE_1, value);
        syncCommands.set(KEY_ON_NODE_2, value);
    }

    Map<String, String> prepareMset() {
        Map<String, String> mset = new HashMap<>();
        for (char c = 'a'; c < 'z'; c++) {
            String key = new String(new char[] { c, c, c });
            mset.put(key, "value-" + key);
        }
        return mset;
    }
}
