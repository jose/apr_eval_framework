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
package io.lettuce.core.cluster.topology;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandType;

/**
 * @author Mark Paluch
 */
class RequestsUnitTests {

    @Test
    void shouldCreateTopologyView() throws Exception {
    }

    @Test
    void shouldCreateTopologyViewWithoutClientCount() throws Exception {
    }

    @Test
    void awaitShouldReturnAwaitedTime() throws Exception {
    }

    @Test
    void awaitShouldReturnAwaitedTimeIfNegative() throws Exception {
    }

    private TimedAsyncCommand getCommand(String response) {
        Command<String, String, String> command = new Command<>(CommandType.TYPE,
                new StatusOutput<>(new Utf8StringCodec()));
        TimedAsyncCommand timedAsyncCommand = new TimedAsyncCommand(command);

        command.getOutput().set(ByteBuffer.wrap(response.getBytes()));
        timedAsyncCommand.complete();
        return timedAsyncCommand;
    }
}
