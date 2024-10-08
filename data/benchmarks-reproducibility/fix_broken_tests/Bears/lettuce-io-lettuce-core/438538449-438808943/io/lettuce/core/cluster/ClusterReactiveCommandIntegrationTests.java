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

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import reactor.test.StepVerifier;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.reactive.RedisClusterReactiveCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.cluster.models.slots.ClusterSlotRange;
import io.lettuce.core.cluster.models.slots.ClusterSlotsParser;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class ClusterReactiveCommandIntegrationTests {

    private final RedisClusterClient clusterClient;
    private final RedisClusterReactiveCommands<String, String> reactive;
    private final RedisClusterCommands<String, String> sync;

    @Inject
    ClusterReactiveCommandIntegrationTests(RedisClusterClient clusterClient,
            StatefulRedisClusterConnection<String, String> connection) {
        this.clusterClient = clusterClient;

        this.reactive = connection.reactive();
        this.sync = connection.sync();
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
    void testAsking() {
    }

    @Test
    void testClusterSlots() {
    }

    @Test
    void clusterSlaves() {
    }
}
