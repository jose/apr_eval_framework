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
package io.lettuce.core.cluster.commands.reactive;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import io.lettuce.core.KeyValue;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.reactive.RedisAdvancedClusterReactiveCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.commands.StringCommandIntegrationTests;
import io.lettuce.test.ReactiveSyncInvocationHandler;

/**
 * @author Mark Paluch
 */
class StringClusterReactiveCommandIntegrationTests extends StringCommandIntegrationTests {

    private final StatefulRedisClusterConnection<String, String> connection;
    private final RedisClusterCommands<String, String> redis;

    @Inject
    StringClusterReactiveCommandIntegrationTests(StatefulRedisClusterConnection<String, String> connection) {
        super(ReactiveSyncInvocationHandler.sync(connection));
        this.connection = connection;
        this.redis = connection.sync();
    }

    @Test
    void msetnx() {
    }

    @Test
    void mget() {
    }
}
