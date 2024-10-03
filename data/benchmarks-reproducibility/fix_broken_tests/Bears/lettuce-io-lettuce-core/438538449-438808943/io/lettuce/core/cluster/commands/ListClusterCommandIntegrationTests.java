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

import org.junit.jupiter.api.Test;

import io.lettuce.core.cluster.ClusterTestUtil;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.commands.ListCommandIntegrationTests;

/**
 * @author Mark Paluch
 */
class ListClusterCommandIntegrationTests extends ListCommandIntegrationTests {

    private final RedisClusterCommands<String, String> redis;

    @Inject
    ListClusterCommandIntegrationTests(StatefulRedisClusterConnection<String, String> connection) {
        super(ClusterTestUtil.redisCommandsOverCluster(connection));
        this.redis = connection.sync();
    }


    // re-implementation because keys have to be on the same slot
    @Test
    void brpoplpush() {
    }

    @Test
    void brpoplpushTimeout() {
    }

    @Test
    void blpop() {
    }

    @Test
    void brpop() {
    }

    @Test
    void rpoplpush() {
    }
}
