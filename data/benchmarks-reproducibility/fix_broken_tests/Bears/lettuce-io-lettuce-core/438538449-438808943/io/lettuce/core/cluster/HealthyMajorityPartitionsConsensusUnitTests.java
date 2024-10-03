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

import static io.lettuce.core.cluster.PartitionsConsensusTestSupport.createMap;
import static io.lettuce.core.cluster.PartitionsConsensusTestSupport.createNode;
import static io.lettuce.core.cluster.PartitionsConsensusTestSupport.createPartitions;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;

/**
 * @author Mark Paluch
 */
class HealthyMajorityPartitionsConsensusUnitTests {

    private RedisClusterNode node1 = createNode(1);
    private RedisClusterNode node2 = createNode(2);
    private RedisClusterNode node3 = createNode(3);
    private RedisClusterNode node4 = createNode(4);
    private RedisClusterNode node5 = createNode(5);

    @Test
    void sameSharedViewShouldDecideForHealthyNodes() {
    }

    @Test
    void unhealthyNodeViewShouldDecideForHealthyNodes() {
    }

    @Test
    void splitNodeViewShouldDecideForHealthyNodes() {
    }

    @Test
    void splitUnhealthyNodeViewShouldDecideForHealthyNodes() {
    }
}
