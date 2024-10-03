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
package io.lettuce.core.pubsub;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.metrics.DefaultCommandLatencyCollector;
import io.lettuce.core.metrics.DefaultCommandLatencyCollectorOptions;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.protocol.RedisCommand;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.tracing.Tracing;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

/**
 * @author Mark Paluch
 */
@ExtendWith(MockitoExtension.class)
class PubSubCommandHandlerUnitTests {

    private Queue<RedisCommand<String, String, ?>> stack;

    private PubSubCommandHandler<String, String> sut;

    private final Command<String, String, String> command = new Command<>(CommandType.APPEND, new StatusOutput<>(
            new Utf8StringCodec()), null);

    @Mock
    private ChannelHandlerContext context;

    @Mock
    private Channel channel;

    @Mock
    private ChannelConfig channelConfig;

    @Mock
    private ChannelPipeline pipeline;

    @Mock
    private EventLoop eventLoop;

    @Mock
    private ClientResources clientResources;

    @Mock
    private PubSubEndpoint<String, String> endpoint;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void before() {

        when(channel.config()).thenReturn(channelConfig);
        when(context.alloc()).thenReturn(ByteBufAllocator.DEFAULT);
        when(context.channel()).thenReturn(channel);
        when(channel.pipeline()).thenReturn(pipeline);
        when(channel.eventLoop()).thenReturn(eventLoop);
        when(eventLoop.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Runnable r = (Runnable) invocation.getArguments()[0];
            r.run();
            return null;
        });

        when(clientResources.commandLatencyCollector()).thenReturn(
                new DefaultCommandLatencyCollector(DefaultCommandLatencyCollectorOptions.create()));
        when(clientResources.tracing()).thenReturn(Tracing.disabled());

        sut = new PubSubCommandHandler<>(ClientOptions.create(), clientResources, StringCodec.UTF8, endpoint);
        stack = (Queue) ReflectionTestUtils.getField(sut, "stack");
    }

    @Test
    void shouldCompleteCommandExceptionallyOnOutputFailure() throws Exception {
    }

    @Test
    void shouldDecodeRegularCommand() throws Exception {
    }

    @Test
    void shouldDecodeTwoCommands() throws Exception {
    }

    @Test
    void shouldPropagatePubSubResponseToOutput() throws Exception {
    }

    @Test
    void shouldPropagateInterleavedPubSubResponseToOutput() throws Exception {
    }
}
