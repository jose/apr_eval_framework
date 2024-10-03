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

import static io.lettuce.core.protocol.LettuceCharsets.buffer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.lettuce.core.*;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.output.CommandOutput;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.test.Futures;

/**
 * @author Mark Paluch
 */
public class AsyncCommandUnitTests {

    private RedisCodec<String, String> codec = new Utf8StringCodec();
    private Command<String, String, String> internal;
    private AsyncCommand<String, String, String> sut;

    @BeforeEach
    final void createCommand() {
        CommandOutput<String, String, String> output = new StatusOutput<>(codec);
        internal = new Command<>(CommandType.INFO, output, null);
        sut = new AsyncCommand<>(internal);
    }

    @Test
    void isCancelled() {
    }

    @Test
    void isDone() {
    }

    @Test
    void awaitAllCompleted() {
    }

    @Test
    void awaitAll() {
    }

    @Test
    void awaitNotCompleted() {
    }

    @Test
    void awaitWithExecutionException() {
    }

    @Test
    void awaitWithCancelledCommand() {
    }

    @Test
    void awaitAllWithExecutionException() {
    }

    @Test
    void getError() {
    }

    @Test
    void getErrorAsync() {
    }

    @Test
    void completeExceptionally() {
    }

    @Test
    void asyncGet() {
    }

    @Test
    void customKeyword() {
    }

    @Test
    void customKeywordWithArgs() {
    }

    @Test
    void getWithTimeout() throws Exception {
    }

    @Test
    void getTimeout() {
    }

    @Test
    void awaitTimeout() {
    }

    @Test
    void getInterrupted() {
    }

    @Test
    void getInterrupted2() {
    }

    @Test
    void awaitInterrupted2() {
    }

    @Test
    void outputSubclassOverride1() {
    }

    @Test
    void outputSubclassOverride2() {
    }

    @Test
    void sillyTestsForEmmaCoverage() {
    }

    private enum MyKeywords implements ProtocolKeyword {
        DUMMY;

        @Override
        public byte[] getBytes() {
            return name().getBytes();
        }
    }
}
