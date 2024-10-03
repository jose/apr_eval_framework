/*
 * Copyright 2017-2018 the original author or authors.
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
package io.lettuce.core.masterslave;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.protocol.RedisCommand;
import io.lettuce.core.resource.ClientResources;

/**
 * @author Mark Paluch
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MasterSlaveChannelWriterUnitTests {

    @Mock
    private MasterSlaveConnectionProvider<String, String> connectionProvider;

    @Mock
    private ClientResources clientResources;

    @Mock
    private StatefulRedisConnection<String, String> connection;

    @Test
    void shouldReturnIntentForWriteCommand() {
    }

    @Test
    void shouldReturnDefaultIntentForNoCommands() {
    }

    @Test
    void shouldReturnIntentForReadCommand() {
    }

    @Test
    void shouldReturnIntentForMixedCommands() {
    }

    @Test
    void shouldBindTransactionsToMaster() {
    }

    @Test
    void shouldBindTransactionsToMasterInBatch() {
    }

    @Test
    void shouldDeriveIntentFromCommandTypeAfterTransaction() {
    }

    @Test
    void shouldDeriveIntentFromCommandTypeAfterDiscardedTransaction() {
    }

    @Test
    void shouldDeriveIntentFromCommandBatchTypeAfterDiscardedTransaction() {
    }

    private static Command<String, String, String> mockCommand(CommandType multi) {
        return new Command<>(multi, new StatusOutput<>(StringCodec.UTF8));
    }
}
