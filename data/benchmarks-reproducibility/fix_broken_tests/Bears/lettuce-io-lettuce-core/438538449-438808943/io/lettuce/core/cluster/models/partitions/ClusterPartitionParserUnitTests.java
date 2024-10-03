/*
 * Copyright 2018 the original author or authors.
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
package io.lettuce.core.cluster.models.partitions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import io.lettuce.core.RedisURI;
import io.lettuce.core.internal.LettuceLists;

class ClusterPartitionParserUnitTests {

    private static String nodes = "c37ab8396be428403d4e55c0d317348be27ed973 127.0.0.1:7381 master - 111 1401258245007 222 connected 7000 12000 12002-16383\n"
            + "3d005a179da7d8dc1adae6409d47b39c369e992b 127.0.0.1:7380 master - 0 1401258245007 2 disconnected 8000-11999 [8000->-4213a8dabb94f92eb6a860f4d0729e6a25d43e0c] [5461-<-c37ab8396be428403d4e55c0d317348be27ed973]\n"
            + "4213a8dabb94f92eb6a860f4d0729e6a25d43e0c 127.0.0.1:7379 myself,slave 4213a8dabb94f92eb6a860f4d0729e6a25d43e0c 0 0 1 connected 0-6999 7001-7999 12001\n"
            + "5f4a2236d00008fba7ac0dd24b95762b446767bd :0 myself,master - 0 0 1 connected [5460->-5f4a2236d00008fba7ac0dd24b95762b446767bd] [5461-<-5f4a2236d00008fba7ac0dd24b95762b446767bd]";

    private static String nodesWithIPv6Addresses = "c37ab8396be428403d4e55c0d317348be27ed973 affe:affe:123:34::1:7381 master - 111 1401258245007 222 connected 7000 12000 12002-16383\n"
            + "3d005a179da7d8dc1adae6409d47b39c369e992b [dead:beef:dead:beef::1]:7380 master - 0 1401258245007 2 disconnected 8000-11999 [8000->-4213a8dabb94f92eb6a860f4d0729e6a25d43e0c] [5461-<-c37ab8396be428403d4e55c0d317348be27ed973]\n"
            + "4213a8dabb94f92eb6a860f4d0729e6a25d43e0c 127.0.0.1:7379 myself,slave 4213a8dabb94f92eb6a860f4d0729e6a25d43e0c 0 0 1 connected 0-6999 7001-7999 12001\n"
            + "5f4a2236d00008fba7ac0dd24b95762b446767bd :0 myself,master - 0 0 1 connected [5460->-5f4a2236d00008fba7ac0dd24b95762b446767bd] [5461-<-5f4a2236d00008fba7ac0dd24b95762b446767bd]";

    private static String nodesWithBusPort = "c37ab8396be428403d4e55c0d317348be27ed973 127.0.0.1:7381@17381 slave 4213a8dabb94f92eb6a860f4d0729e6a25d43e0c 0 1454482721690 3 connected\n"
            + "3d005a179da7d8dc1adae6409d47b39c369e992b 127.0.0.1:7380@17380 master - 0 1454482721690 0 connected 12000-16383\n"
            + "4213a8dabb94f92eb6a860f4d0729e6a25d43e0c 127.0.0.1:7379@17379 myself,master - 0 0 1 connected 0-11999\n"
            + "5f4a2236d00008fba7ac0dd24b95762b446767bd 127.0.0.1:7382@17382 slave 3d005a179da7d8dc1adae6409d47b39c369e992b 0 1454482721690 2 connected";

    @Test
    void shouldParseNodesCorrectly() {
    }

    @Test
    void shouldParseNodesWithBusPort() {
    }

    @Test
    void shouldParseNodesIPv6Address() {
    }

    @Test
    void getNodeByHashShouldReturnCorrectNode() {
    }

    @Test
    void testModel() {
    }

    RedisClusterNode mockRedisClusterNode() {
        RedisClusterNode node = new RedisClusterNode();
        node.setConfigEpoch(1);
        node.setConnected(true);
        node.setFlags(new HashSet<>());
        node.setNodeId("abcd");
        node.setPingSentTimestamp(2);
        node.setPongReceivedTimestamp(3);
        node.setSlaveOf("me");
        node.setSlots(LettuceLists.unmodifiableList(1, 2, 3));
        node.setUri(new RedisURI("localhost", 1, Duration.ofDays(1)));
        return node;
    }

    @Test
    void createNode() {
    }
}
