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
package io.lettuce.core.cluster.commands;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.TestSupport;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.ClusterTestUtil;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.condition.EnabledOnCommand;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeyClusterCommandIntegrationTests extends TestSupport {

    private final StatefulRedisClusterConnection<String, String> clusterConnection;
    private final RedisCommands<String, String> redis;

    @Inject
    KeyClusterCommandIntegrationTests(StatefulRedisClusterConnection<String, String> clusterConnection) {
        this.clusterConnection = clusterConnection;
        this.redis = ClusterTestUtil.redisCommandsOverCluster(clusterConnection);
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void del() {
    }

    @Test
    void exists() {
    }

    @Test
    @EnabledOnCommand("TOUCH")
    void touch() {
    }

    @Test
    @EnabledOnCommand("UNLINK")
    void unlink() {
    }
}
