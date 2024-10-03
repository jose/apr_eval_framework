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
package io.lettuce.core.protocol;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.*;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ConnectionEvents;
import io.lettuce.core.RedisException;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.metrics.CommandLatencyCollector;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.tracing.Tracing;
import io.lettuce.test.Delay;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * @author Mark Paluch
 * @author Jongyeol Choi
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommandHandlerUnitTests {

    private Queue<RedisCommand<String, String, ?>> stack;

    private CommandHandler sut;

    private final Command<String, String, String> command = new Command<>(CommandType.APPEND, new StatusOutput<>(
            StringCodec.UTF8), null);

    @Mock
    private ChannelHandlerContext context;

    @Mock
    private Channel channel;

    @Mock
    private ChannelConfig config;

    @Mock
    private ChannelPipeline pipeline;

    @Mock
    private EventLoop eventLoop;

    @Mock
    private ClientResources clientResources;

    @Mock
    private Endpoint endpoint;

    @Mock
    private ChannelPromise promise;

    @Mock
    private CommandLatencyCollector latencyCollector;

    @BeforeAll
    static void beforeClass() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext();
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(CommandHandler.class.getName());
        loggerConfig.setLevel(Level.ALL);
    }

    @AfterAll
    static void afterClass() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext();
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(CommandHandler.class.getName());
        loggerConfig.setLevel(null);
    }

    @BeforeEach
    void before() throws Exception {

        when(context.channel()).thenReturn(channel);
        when(context.alloc()).thenReturn(ByteBufAllocator.DEFAULT);
        when(channel.pipeline()).thenReturn(pipeline);
        when(channel.eventLoop()).thenReturn(eventLoop);
        when(channel.remoteAddress()).thenReturn(new InetSocketAddress(Inet4Address.getLocalHost(), 1234));
        when(channel.localAddress()).thenReturn(new InetSocketAddress(Inet4Address.getLocalHost(), 1234));
        when(channel.config()).thenReturn(config);
        when(eventLoop.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Runnable r = (Runnable) invocation.getArguments()[0];
            r.run();
            return null;
        });

        when(latencyCollector.isEnabled()).thenReturn(true);
        when(clientResources.commandLatencyCollector()).thenReturn(latencyCollector);
        when(clientResources.tracing()).thenReturn(Tracing.disabled());

        sut = new CommandHandler(ClientOptions.create(), clientResources, endpoint);
        stack = (Queue) ReflectionTestUtils.getField(sut, "stack");
    }

    @Test
    void testChannelActive() throws Exception {
    }

    @Test
    void testExceptionChannelActive() throws Exception {
    }

    @Test
    void testIOExceptionChannelActive() throws Exception {
    }

    @Test
    void testExceptionChannelInactive() throws Exception {
    }

    @Test
    void testExceptionWithQueue() throws Exception {
    }

    @Test
    void testExceptionWhenClosed() throws Exception {
    }

    @Test
    void isConnectedShouldReportFalseForNOT_CONNECTED() {
    }

    @Test
    void isConnectedShouldReportFalseForREGISTERED() {
    }

    @Test
    void isConnectedShouldReportTrueForCONNECTED() {
    }

    @Test
    void isConnectedShouldReportTrueForACTIVATING() {
    }

    @Test
    void isConnectedShouldReportTrueForACTIVE() {
    }

    @Test
    void isConnectedShouldReportFalseForDISCONNECTED() {
    }

    @Test
    void isConnectedShouldReportFalseForDEACTIVATING() {
    }

    @Test
    void isConnectedShouldReportFalseForDEACTIVATED() {
    }

    @Test
    void isConnectedShouldReportFalseForCLOSED() {
    }

    @Test
    void shouldNotWriteCancelledCommand() throws Exception {
    }

    @Test
    void shouldNotWriteCancelledCommands() throws Exception {
    }

    @Test
    void shouldCancelCommandOnQueueSingleFailure() throws Exception {
    }

    @Test
    void shouldCancelCommandOnQueueBatchFailure() throws Exception {
    }

    @Test
    void shouldFailOnDuplicateCommands() throws Exception {
    }

    @Test
    void shouldWriteActiveCommands() throws Exception {
    }

    @Test
    void shouldNotWriteCancelledCommandBatch() throws Exception {
    }

    @Test
    void shouldWriteSingleActiveCommandsInBatch() throws Exception {
    }

    @Test
    void shouldWriteActiveCommandsInBatch() throws Exception {
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldWriteActiveCommandsInMixedBatch() throws Exception {
    }

    @Test
    void shouldRecordCorrectFirstResponseLatency() throws Exception {
    }

    @Test
    void shouldIgnoreNonReadableBuffers() throws Exception {
    }
}
