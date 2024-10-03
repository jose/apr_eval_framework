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

import static io.lettuce.test.settings.TestSettings.host;
import static io.lettuce.test.settings.TestSettings.hostAddr;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.lettuce.core.RedisCommandExecutionException;
import io.lettuce.core.RedisException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.Executions;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.test.CanConnect;
import io.lettuce.test.resource.FastShutdown;
import io.lettuce.test.resource.TestClientResources;

/**
 * @author Mark Paluch
 */
class RedisClusterPasswordSecuredSslIntegrationTests extends TestSupport {

    private static final int CLUSTER_PORT_SSL_1 = 7443;
    private static final int CLUSTER_PORT_SSL_2 = 7444;
    private static final int CLUSTER_PORT_SSL_3 = 7445;

    private static final String SLOT_1_KEY = "8HMdi";
    private static final String SLOT_16352_KEY = "UyAa4KqoWgPGKa";

    private static RedisURI redisURI = RedisURI.Builder.redis(host(), CLUSTER_PORT_SSL_1).withPassword("foobared")
            .withSsl(true).withVerifyPeer(false).build();
    private static RedisClusterClient redisClient = RedisClusterClient.create(TestClientResources.get(), redisURI);

    @BeforeEach
    void before() {
        assumeTrue(CanConnect.to(host(), CLUSTER_PORT_SSL_1), "Assume that stunnel runs on port 7443");
        assumeTrue(CanConnect.to(host(), CLUSTER_PORT_SSL_2), "Assume that stunnel runs on port 7444");
        assumeTrue(CanConnect.to(host(), CLUSTER_PORT_SSL_3), "Assume that stunnel runs on port 7445");
        assumeTrue(CanConnect.to(host(), 7479), "Assume that Redis runs on port 7479");
        assumeTrue(CanConnect.to(host(), 7480), "Assume that Redis runs on port 7480");
        assumeTrue(CanConnect.to(host(), 7481), "Assume that Redis runs on port 7481");
    }

    @AfterAll
    static void afterClass() {
        FastShutdown.shutdown(redisClient);
    }

    @Test
    void defaultClusterConnectionShouldWork() {
    }

    @Test
    void partitionViewShouldContainClusterPorts() {
    }

    @Test
    void routedOperationsAreWorking() {
    }

    @Test
    void nodeConnectionsShouldWork() {
    }

    @Test
    void nodeSelectionApiShouldWork() {
    }

    @Test
    void connectionWithoutPasswordShouldFail() {
    }

    @Test
    void connectionWithoutPasswordShouldFail2() {
    }

    @Test
    void clusterNodeRefreshWorksForMultipleIterations() {
    }
}
