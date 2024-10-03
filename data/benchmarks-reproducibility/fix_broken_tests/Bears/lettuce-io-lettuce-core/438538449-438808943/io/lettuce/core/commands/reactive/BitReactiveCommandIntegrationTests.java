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

import static io.lettuce.core.BitFieldArgs.offset;
import static io.lettuce.core.BitFieldArgs.signed;
import static io.lettuce.core.BitFieldArgs.typeWidthBasedOffset;
import static io.lettuce.core.BitFieldArgs.OverflowType.FAIL;
import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;
import io.lettuce.core.BitFieldArgs;
import io.lettuce.core.RedisClient;
import io.lettuce.core.Value;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisStringReactiveCommands;
import io.lettuce.core.commands.BitCommandIntegrationTests;
import io.lettuce.test.ReactiveSyncInvocationHandler;

/**
 * @author Mark Paluch
 */
class BitReactiveCommandIntegrationTests extends BitCommandIntegrationTests {

    private RedisStringReactiveCommands<String, String> reactive;

    @Inject
    BitReactiveCommandIntegrationTests(RedisClient client, StatefulRedisConnection<String, String> connection) {
        super(client, ReactiveSyncInvocationHandler.sync(connection));
        this.reactive = connection.reactive();
    }

    @Test
    void bitfield() {
    }

    @Test
    void bitfieldGetWithOffset() {
    }

    @Test
    void bitfieldSet() {
    }

    @Test
    void bitfieldWithOffsetSet() {
    }

    @Test
    void bitfieldIncrBy() {
    }

    @Test
    void bitfieldOverflow() {
    }
}
