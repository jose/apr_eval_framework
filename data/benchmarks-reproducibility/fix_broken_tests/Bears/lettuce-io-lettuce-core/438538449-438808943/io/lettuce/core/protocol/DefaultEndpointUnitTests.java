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
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.channels.ClosedChannelException;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

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

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisException;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.internal.LettuceFactories;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.test.ConnectionTestUtil;
import io.netty.channel.*;
import io.netty.handler.codec.EncoderException;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * @author Mark Paluch
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultEndpointUnitTests {

    private Queue<RedisCommand<String, String, ?>> queue = LettuceFactories.newConcurrentQueue(1000);

    private DefaultEndpoint sut;

    private final Command<String, String, String> command = new Command<>(CommandType.APPEND, new StatusOutput<>(
            new Utf8StringCodec()), null);

    @Mock
    private Channel channel;

    @Mock
    private ConnectionFacade connectionFacade;

    @Mock
    private ConnectionWatchdog connectionWatchdog;

    @Mock
    private ClientResources clientResources;

    private ChannelPromise promise;

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
    void before() {

        promise = new DefaultChannelPromise(channel);
        when(channel.writeAndFlush(any())).thenAnswer(invocation -> {
            if (invocation.getArguments()[0] instanceof RedisCommand) {
                queue.add((RedisCommand) invocation.getArguments()[0]);
            }

            if (invocation.getArguments()[0] instanceof Collection) {
                queue.addAll((Collection) invocation.getArguments()[0]);
            }
            return promise;
        });

        when(channel.write(any())).thenAnswer(invocation -> {
            if (invocation.getArguments()[0] instanceof RedisCommand) {
                queue.add((RedisCommand) invocation.getArguments()[0]);
            }

            if (invocation.getArguments()[0] instanceof Collection) {
                queue.addAll((Collection) invocation.getArguments()[0]);
            }
            return promise;
        });

        sut = new DefaultEndpoint(ClientOptions.create(), clientResources);
        sut.setConnectionFacade(connectionFacade);
    }

    @Test
    void writeConnectedShouldWriteCommandToChannel() {
    }

    @Test
    void writeDisconnectedShouldBufferCommands() {
    }

    @Test
    void notifyChannelActiveActivatesFacade() {
    }

    @Test
    void notifyChannelActiveArmsConnectionWatchdog() {
    }

    @Test
    void notifyChannelInactiveDeactivatesFacade() {
    }

    @Test
    void notifyExceptionShouldStoreException() {
    }

    @Test
    void notifyChannelActiveClearsStoredException() {
    }

    @Test
    void notifyDrainQueuedCommandsShouldBufferCommands() {
    }

    @Test
    void notifyDrainQueuedCommandsShouldWriteCommands() {
    }

    @Test
    void shouldCancelCommandsOnEncoderException() {
    }

    @Test
    void writeShouldRejectCommandsInDisconnectedState() {
    }

    @Test
    void writeShouldRejectCommandsInClosedState() {
    }

    @Test
    void writeWithoutAutoReconnectShouldRejectCommandsInDisconnectedState() {
    }

    @Test
    void closeCleansUpResources() {
    }

    @Test
    void closeAllowsOnlyOneCall() {
    }

    @Test
    void retryListenerCompletesSuccessfullyAfterDeferredRequeue() {
    }

    @Test
    void retryListenerDoesNotRetryCompletedCommands() {
    }

    @Test
    void testMTCConcurrentConcurrentWrite() throws Throwable {
    }

    /**
     * Test of concurrent access to locks. Two concurrent writes.
     */
    static class MTCConcurrentConcurrentWrite extends MultithreadedTestCase {

        private final Command<String, String, String> command;
        private TestableEndpoint handler;

        MTCConcurrentConcurrentWrite(Command<String, String, String> command, ClientResources clientResources) {

            this.command = command;

            handler = new TestableEndpoint(ClientOptions.create(), clientResources) {

                @Override
                protected <C extends RedisCommand<?, ?, T>, T> void writeToBuffer(C command) {

                    waitForTick(2);

                    Object sharedLock = ReflectionTestUtils.getField(this, "sharedLock");
                    AtomicLong writers = (AtomicLong) ReflectionTestUtils.getField(sharedLock, "writers");
                    assertThat(writers.get()).isEqualTo(2);
                    waitForTick(3);
                    super.writeToBuffer(command);
                }
            };
        }

        public void thread1() {

            waitForTick(1);
            handler.write(command);
        }

        public void thread2() {

            waitForTick(1);
            handler.write(command);
        }
    }

    static class TestableEndpoint extends DefaultEndpoint {

        /**
         * Create a new {@link DefaultEndpoint}.
         *
         * @param clientOptions client options for this connection, must not be {@literal null}.
         * @param clientResources client resources for this connection, must not be {@literal null}.
         */
        TestableEndpoint(ClientOptions clientOptions, ClientResources clientResources) {
            super(clientOptions, clientResources);
        }
    }
}
