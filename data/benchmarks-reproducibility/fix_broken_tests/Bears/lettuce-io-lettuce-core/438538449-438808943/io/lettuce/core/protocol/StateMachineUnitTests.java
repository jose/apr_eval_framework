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

import static io.lettuce.core.protocol.RedisStateMachine.State;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.lettuce.core.RedisException;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.output.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
class StateMachineUnitTests {
    private RedisCodec<String, String> codec = new Utf8StringCodec();
    private Charset charset = Charset.forName("UTF-8");
    private CommandOutput<String, String, String> output;
    private RedisStateMachine rsm;

    @BeforeAll
    static void beforeClass() {

        LoggerContext ctx = (LoggerContext) LogManager.getContext();
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(RedisStateMachine.class.getName());
        loggerConfig.setLevel(Level.ALL);
    }

    @AfterAll
    static void afterClass() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext();
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(RedisStateMachine.class.getName());
        loggerConfig.setLevel(null);
    }

    @BeforeEach
    final void createStateMachine() {
        output = new StatusOutput<>(codec);
        rsm = new RedisStateMachine();
    }

    @Test
    void single() {
    }

    @Test
    void error() {
    }

    @Test
    void errorWithoutLineBreak() {
    }

    @Test
    void integer() {
    }

    @Test
    void bulk() {
    }

    @Test
    void multi() {
    }

    @Test
    void multiEmptyArray1() {
    }

    @Test
    void multiEmptyArray2() {
    }

    @Test
    void multiEmptyArray3() {
    }

    @Test
    void partialFirstLine() {
    }

    @Test
    void invalidReplyType() {
    }

    @Test
    void sillyTestsForEmmaCoverage() {
    }

    ByteBuf buffer(String content) {
        return Unpooled.copiedBuffer(content, charset);
    }
}
