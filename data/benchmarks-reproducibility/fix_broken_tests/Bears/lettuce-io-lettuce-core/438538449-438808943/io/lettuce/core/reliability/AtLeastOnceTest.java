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
package io.lettuce.core.reliability;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.output.IntegerOutput;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.*;
import io.lettuce.test.ConnectionTestUtil;
import io.lettuce.test.Delay;
import io.lettuce.test.Wait;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.EncoderException;
import io.netty.util.Version;

/**
 * @author Mark Paluch
 */
class AtLeastOnceTest extends AbstractRedisClientTest {

    private final Utf8StringCodec CODEC = new Utf8StringCodec();
    private String key = "key";

    @BeforeEach
    void before() {
        client.setOptions(ClientOptions.builder().autoReconnect(true).build());

        // needs to be increased on slow systems...perhaps...
        client.setDefaultTimeout(3, TimeUnit.SECONDS);

        RedisCommands<String, String> connection = client.connect().sync();
        connection.flushall();
        connection.flushdb();
        connection.getStatefulConnection().close();
    }

    @Test
    void connectionIsConnectedAfterConnect() {
    }

    @Test
    void reconnectIsActiveHandler() {
    }

    @Test
    void basicOperations() {
    }

    @Test
    void noBufferedCommandsAfterExecute() {
    }

    @Test
    void commandIsExecutedOnce() {
    }

    @Test
    void commandFailsWhenFailOnEncode() {
    }

    @Test
    void commandNotFailedChannelClosesWhileFlush() {
    }

    @Test
    void commandRetriedChannelClosesWhileFlush() {
    }

    AsyncCommand<String, String, Object> getBlockOnEncodeCommand(final CountDownLatch block) {
        return new AsyncCommand<String, String, Object>(new Command<>(CommandType.INCR, new IntegerOutput(CODEC),
                new CommandArgs<>(CODEC).addKey(key))) {

            @Override
            public void encode(ByteBuf buf) {
                try {
                    block.await();
                } catch (InterruptedException e) {
                }
                super.encode(buf);
            }
        };
    }

    @Test
    void commandFailsDuringDecode() {
    }

    @Test
    void commandCancelledOverSyncAPIAfterConnectionIsDisconnected() {
    }

    @Test
    void retryAfterConnectionIsDisconnected() throws Exception {
    }

    private Throwable getException(RedisFuture<?> command) {
        try {
            command.get();
        } catch (InterruptedException e) {
            return e;
        } catch (ExecutionException e) {
            return e.getCause();
        }
        return null;
    }

}
