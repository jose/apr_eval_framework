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

import static io.lettuce.core.cluster.ClusterTestSettings.createSlots;
import static io.lettuce.core.cluster.ClusterTestUtil.getNodeId;
import static io.lettuce.core.cluster.ClusterTestUtil.getOwnPartition;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.*;

import io.lettuce.category.SlowTests;
import io.lettuce.core.*;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.cluster.models.partitions.ClusterPartitionParser;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.test.ConnectionTestUtil;
import io.lettuce.test.Futures;
import io.lettuce.test.Wait;
import io.lettuce.test.resource.DefaultRedisClient;
import io.lettuce.test.resource.FastShutdown;
import io.lettuce.test.resource.TestClientResources;
import io.lettuce.test.settings.TestSettings;

/**
 * Test for mutable cluster setup scenarios.
 *
 * @author Mark Paluch
 * @since 3.0
 */
@SuppressWarnings({ "unchecked" })
@SlowTests
public class RedisClusterSetupTest extends TestSupport {

    private static final String host = TestSettings.hostAddr();

    private static final ClusterTopologyRefreshOptions PERIODIC_REFRESH_ENABLED = ClusterTopologyRefreshOptions.builder()
            .enablePeriodicRefresh(1, TimeUnit.SECONDS).dynamicRefreshSources(false).build();

    private static RedisClusterClient clusterClient;
    private static RedisClient client = DefaultRedisClient.get();

    private RedisCommands<String, String> redis1;
    private RedisCommands<String, String> redis2;

    @Rule
    public ClusterRule clusterRule = new ClusterRule(clusterClient, ClusterTestSettings.port5, ClusterTestSettings.port6);

    @BeforeClass
    public static void setupClient() {
        clusterClient = RedisClusterClient.create(TestClientResources.get(),
                RedisURI.Builder.redis(host, ClusterTestSettings.port5).build());
    }

    @AfterClass
    public static void shutdownClient() {
        FastShutdown.shutdown(clusterClient);
    }

    @Before
    public void openConnection() {
        redis1 = client.connect(RedisURI.Builder.redis(ClusterTestSettings.host, ClusterTestSettings.port5).build()).sync();
        redis2 = client.connect(RedisURI.Builder.redis(ClusterTestSettings.host, ClusterTestSettings.port6).build()).sync();
        clusterRule.clusterReset();
    }

    @After
    public void closeConnection() {
        redis1.getStatefulConnection().close();
        redis2.getStatefulConnection().close();
    }

    @Test
    public void clusterMeet() {
    }

    @Test
    public void clusterForget() {
    }

    @Test
    public void clusterDelSlots() {
    }

    @Test
    public void clusterSetSlots() {
    }

    @Test
    public void clusterSlotMigrationImport() {
    }

    @Test
    public void clusterTopologyRefresh() {
    }

    @Test
    public void changeTopologyWhileOperations() throws Exception {
    }

    @Test
    public void slotMigrationShouldUseAsking() {
    }

    @Test
    public void disconnectedConnectionRejectTest() throws Exception {
    }

    @Test
    public void atLeastOnceForgetNodeFailover() throws Exception {
    }

    @Test
    public void expireStaleNodeIdConnections() throws Exception {
    }

    private void assertRoutedExecution(RedisClusterAsyncCommands<String, String> clusterConnection) {
        assertExecuted(clusterConnection.set("A", "value")); // 6373
        assertExecuted(clusterConnection.set("t", "value")); // 15891
        assertExecuted(clusterConnection.set("p", "value")); // 16023
    }

    @Test
    public void doNotExpireStaleNodeIdConnections() throws Exception {
    }

    @Test
    public void expireStaleHostAndPortConnections() throws Exception {
    }

    @Test
    public void readFromSlaveTest() {
    }

    @Test
    public void readFromNearestTest() {
    }

    private PooledClusterConnectionProvider<String, String> getPooledClusterConnectionProvider(
            RedisAdvancedClusterAsyncCommands<String, String> clusterAsyncConnection) {

        RedisChannelHandler<String, String> channelHandler = getChannelHandler(clusterAsyncConnection);
        ClusterDistributionChannelWriter writer = (ClusterDistributionChannelWriter) channelHandler.getChannelWriter();
        return (PooledClusterConnectionProvider<String, String>) writer.getClusterConnectionProvider();
    }

    private RedisChannelHandler<String, String> getChannelHandler(
            RedisAdvancedClusterAsyncCommands<String, String> clusterAsyncConnection) {
        return (RedisChannelHandler<String, String>) clusterAsyncConnection.getStatefulConnection();
    }

    private void assertExecuted(RedisFuture<String> set) {
        Futures.await(set);
        assertThat(set.getError()).isNull();
        assertThat(Futures.get(set)).isEqualTo("OK");
    }

    private void suspendConnection(RedisClusterAsyncCommands<String, String> asyncCommands) {

        ConnectionTestUtil.getConnectionWatchdog(((RedisAsyncCommands<?, ?>) asyncCommands).getStatefulConnection())
                .setReconnectSuspended(true);
        asyncCommands.quit();

        Wait.untilTrue(() -> !asyncCommands.isOpen()).waitOrTimeout();
    }

    private void shiftAllSlotsToNode1() {

        redis1.clusterDelSlots(createSlots(12000, 16384));
        redis2.clusterDelSlots(createSlots(12000, 16384));

        waitForSlots(redis2, 0);

        RedisClusterNode redis2Partition = getOwnPartition(redis2);

        Wait.untilTrue(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                Partitions partitions = ClusterPartitionParser.parse(redis1.clusterNodes());
                RedisClusterNode partition = partitions.getPartitionByNodeId(redis2Partition.getNodeId());

                if (!partition.getSlots().isEmpty()) {
                    removeRemaining(partition);
                }

                return partition.getSlots().size() == 0;
            }

            private void removeRemaining(RedisClusterNode partition) {
                try {
                    redis1.clusterDelSlots(toIntArray(partition.getSlots()));
                } catch (Exception o_O) {
                    // ignore
                }
            }
        }).waitOrTimeout();

        redis1.clusterAddSlots(createSlots(12000, 16384));
        waitForSlots(redis1, 16384);

        Wait.untilTrue(clusterRule::isStable).waitOrTimeout();
    }

    private int[] toIntArray(List<Integer> list) {
        return list.parallelStream().mapToInt(Integer::intValue).toArray();
    }

    private void waitForSlots(RedisClusterCommands<String, String> connection, int slotCount) {
        Wait.untilEquals(slotCount, () -> getOwnPartition(connection).getSlots().size()).waitOrTimeout();
    }
}
