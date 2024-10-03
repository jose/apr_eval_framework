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
import static org.assertj.core.api.Fail.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TestSupport;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.internal.LettuceLists;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.test.resource.TestClientResources;
import io.lettuce.test.settings.TestSettings;

/**
 * @author Mark Paluch
 */
class ClusterPartiallyDownIntegrationTests extends TestSupport {

    private static ClientResources clientResources;

    private static int port1 = 7579;
    private static int port2 = 7580;
    private static int port3 = 7581;
    private static int port4 = 7582;

    private static final RedisURI URI_1 = RedisURI.create(TestSettings.host(), port1);
    private static final RedisURI URI_2 = RedisURI.create(TestSettings.host(), port2);
    private static final RedisURI URI_3 = RedisURI.create(TestSettings.host(), port3);
    private static final RedisURI URI_4 = RedisURI.create(TestSettings.host(), port4);

    private RedisClusterClient redisClusterClient;

    @BeforeAll
    static void beforeClass() {
        clientResources = TestClientResources.get();
    }

    @AfterEach
    void after() {
        redisClusterClient.shutdown();
    }

    @Test
    void connectToPartiallyDownCluster() {
    }

    @Test
    void operateOnPartiallyDownCluster() {
    }

    @Test
    void seedNodesAreOffline() {
    }

    @Test
    void partitionNodesAreOffline() {
    }
}
