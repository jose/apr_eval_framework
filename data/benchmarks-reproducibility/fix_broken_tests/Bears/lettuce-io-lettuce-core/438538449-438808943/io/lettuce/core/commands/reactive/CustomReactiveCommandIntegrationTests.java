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
package io.lettuce.core.commands.reactive;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.ValueListOutput;
import io.lettuce.core.output.ValueOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class CustomReactiveCommandIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    CustomReactiveCommandIntegrationTests(StatefulRedisConnection<String, String> connection) {
        this.redis = connection.sync();
    }

    @Test
    void dispatchGetAndSet() {
    }

    @Test
    void dispatchList() {
    }
}
