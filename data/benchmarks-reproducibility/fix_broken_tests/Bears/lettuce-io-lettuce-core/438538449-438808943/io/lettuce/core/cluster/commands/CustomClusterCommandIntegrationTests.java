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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.RedisCommandExecutionException;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.TestSupport;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.commands.CustomCommandIntegrationTests;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.*;
import io.lettuce.test.Futures;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class CustomClusterCommandIntegrationTests extends TestSupport {

    private final StatefulRedisClusterConnection<String, String> connection;
    private RedisAdvancedClusterCommands<String, String> redis;

    @Inject
    CustomClusterCommandIntegrationTests(StatefulRedisClusterConnection<String, String> connection) {
        this.connection = connection;
        this.redis = connection.sync();
        this.redis.flushall();
    }

    @Test
    void dispatchSet() {
    }

    @Test
    void dispatchWithoutArgs() {
    }

    @Test
    void dispatchShouldFailForWrongDataType() {
    }

    @Test
    void clusterAsyncPing() {
    }

    @Test
    void clusterAsyncBatchPing() {
    }

    @Test
    void clusterAsyncBatchSet() {
    }

    @Test
    void clusterFireAndForget() {
    }
}
