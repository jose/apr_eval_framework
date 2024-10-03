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

import static io.lettuce.test.LettuceExtension.Connection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.api.reactive.RedisAdvancedClusterReactiveCommands;
import io.lettuce.core.cluster.api.reactive.RedisClusterReactiveCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.test.Futures;
import io.lettuce.test.KeysAndValues;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.ListStreamingAdapter;
import io.lettuce.test.condition.EnabledOnCommand;
import io.lettuce.test.settings.TestSettings;

/**
 * @author Mark Paluch
 */
@SuppressWarnings("rawtypes")
@ExtendWith(LettuceExtension.class)
class AdvancedClusterClientIntegrationTests extends TestSupport {

    private static final String KEY_ON_NODE_1 = "a";
    private static final String KEY_ON_NODE_2 = "b";

    private final RedisClusterClient clusterClient;
    private final StatefulRedisClusterConnection<String, String> clusterConnection;
    private final RedisAdvancedClusterAsyncCommands<String, String> async;
    private final RedisAdvancedClusterCommands<String, String> sync;

    @Inject
    AdvancedClusterClientIntegrationTests(RedisClusterClient clusterClient,
            StatefulRedisClusterConnection<String, String> clusterConnection) {
        this.clusterClient = clusterClient;

        this.clusterConnection = clusterConnection;
        this.async = clusterConnection.async();
        this.sync = clusterConnection.sync();
    }

    @BeforeEach
    void setUp() {
        this.sync.flushall();
    }

    @Test
    void nodeConnections() {
    }

    @Test
    void unknownNodeId() {
    }

    @Test
    void invalidHost() {
    }

    @Test
    void partitions() {
    }

    @Test
    void differentConnections() {
    }

    @Test
    void msetRegular() {
    }

    @Test
    void msetCrossSlot() {
    }

    @Test
    void msetnxCrossSlot() {
    }

    @Test
    void mgetRegular() {
    }

    @Test
    void mgetCrossSlot() {
    }

    @Test
    @EnabledOnCommand("UNLINK")
    void delRegular() {
    }

    @Test
    void delCrossSlot() {
    }

    @Test
    @EnabledOnCommand("UNLINK")
    void unlinkRegular() {
    }

    @Test
    @EnabledOnCommand("UNLINK")
    void unlinkCrossSlot() {
    }

    private List<String> prepareKeys() {

        msetCrossSlot();
        List<String> keys = new ArrayList<>();
        for (char c = 'a'; c < 'z'; c++) {
            String key = new String(new char[] { c, c, c });
            keys.add(key);
        }
        return keys;
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
    void testSync() {
    }

    @Test
    @Inject
    void routeCommandToNoAddrPartition(@New StatefulRedisClusterConnection<String, String> connectionUnderTest) {
    }

    @Test
    @Inject
    void routeCommandToForbiddenHostOnRedirect(
            @Connection(requiresNew = true) StatefulRedisClusterConnection<String, String> connectionUnderTest) {
    }

    @Test
    void getConnectionToNotAClusterMemberForbidden() {
    }

    @Test
    void getConnectionToNotAClusterMemberAllowed() {
    }

    @Test
    @Inject
    void pipelining(@New StatefulRedisClusterConnection<String, String> connectionUnderTest) {
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

    @Test
    void clusterScanCursorFinished() {
    }

    @Test
    void clusterScanCursorNotReused() {
    }

    String value(int i) {
        return value + "-" + i;
    }

    String key(int i) {
        return key + "-" + i;
    }

    private void writeKeysToTwoNodes() {
        sync.set(KEY_ON_NODE_1, value);
        sync.set(KEY_ON_NODE_2, value);
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
