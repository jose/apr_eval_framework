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

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import io.lettuce.core.cluster.ClusterConnectionProvider.Intent;
import io.lettuce.core.internal.HostAndPort;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.protocol.RedisCommand;

/**
 * @author Mark Paluch
 */
class ClusterDistributionChannelWriterUnitTests {

    @Test
    void shouldParseAskTargetCorrectly() {
    }

    @Test
    void shouldParseIPv6AskTargetCorrectly() {
    }

    @Test
    void shouldParseMovedTargetCorrectly() {
    }

    @Test
    void shouldParseIPv6MovedTargetCorrectly() {
    }

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
}
