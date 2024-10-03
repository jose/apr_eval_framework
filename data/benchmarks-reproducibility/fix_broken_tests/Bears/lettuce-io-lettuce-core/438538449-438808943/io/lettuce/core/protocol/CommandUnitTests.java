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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.lettuce.core.RedisException;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.output.CommandOutput;
import io.lettuce.core.output.StatusOutput;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
public class CommandUnitTests {

    private RedisCodec<String, String> codec = new Utf8StringCodec();
    private Command<String, String, String> sut;

    @BeforeEach
    void createCommand() {

        CommandOutput<String, String, String> output = new StatusOutput<>(codec);
        sut = new Command<>(CommandType.INFO, output, null);
    }

    @Test
    void isCancelled() {
    }

    @Test
    void isDone() {
    }

    @Test
    void get() {
    }

    @Test
    void getError() {
    }

    @Test
    void setOutputAfterCompleted() {
    }

    @Test
    void testToString() {
    }

    @Test
    void customKeyword() {
    }

    @Test
    void customKeywordWithArgs() {
    }

    @Test
    void getWithTimeout() {
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
