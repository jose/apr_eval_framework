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

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import reactor.test.StepVerifier;
import io.lettuce.core.TestSupport;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.reactive.RedisAdvancedClusterReactiveCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 */
@SuppressWarnings("unchecked")
@ExtendWith(LettuceExtension.class)
class RedisReactiveClusterClientIntegrationTests extends TestSupport {

    private final RedisAdvancedClusterCommands<String, String> sync;
    private final RedisAdvancedClusterReactiveCommands<String, String> reactive;

    @Inject
    RedisReactiveClusterClientIntegrationTests(StatefulRedisClusterConnection<String, String> connection) {
        this.sync = connection.sync();
        this.reactive = connection.reactive();
    }

    @Test
    void testClusterCommandRedirection() {
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
}
