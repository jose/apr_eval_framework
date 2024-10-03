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

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;
import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisException;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.commands.TransactionCommandIntegrationTests;
import io.lettuce.test.ReactiveSyncInvocationHandler;

/**
 * @author Mark Paluch
 */
public class TransactionReactiveCommandIntegrationTests extends TransactionCommandIntegrationTests {

    private final RedisClient client;
    private final RedisReactiveCommands<String, String> commands;
    private final StatefulRedisConnection<String, String> connection;

    @Inject
    public TransactionReactiveCommandIntegrationTests(RedisClient client, StatefulRedisConnection<String, String> connection) {
        super(client, ReactiveSyncInvocationHandler.sync(connection));
        this.client = client;
        this.commands = connection.reactive();
        this.connection = connection;
    }

    @Test
    void discard() {
    }

    @Test
    void watchRollback() {
    }

    @Test
    void execSingular() {
    }

    @Test
    void errorInMulti() {
    }

    @Test
    void resultOfMultiIsContainedInCommandFlux() {
    }

    @Test
    void resultOfMultiIsContainedInExecObservable() {
    }
}
