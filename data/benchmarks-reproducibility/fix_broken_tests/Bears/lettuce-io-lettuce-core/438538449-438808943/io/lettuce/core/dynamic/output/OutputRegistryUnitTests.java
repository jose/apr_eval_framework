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
package io.lettuce.core.dynamic.output;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.lettuce.core.ScoredValue;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.dynamic.support.ClassTypeInformation;
import io.lettuce.core.dynamic.support.ResolvableType;
import io.lettuce.core.output.*;

/**
 * @author Mark Paluch
 */
class OutputRegistryUnitTests {

    @Test
    void getKeyOutputType() {
    }

    @Test
    void getStringListOutputType() {
    }

    @Test
    void componentTypeOfKeyOuputWithCodecIsAssignableFromString() {
    }

    @Test
    void componentTypeOfKeyListOuputWithCodecIsAssignableFromListOfString() {
    }

    @Test
    void streamingTypeOfKeyOuputWithCodecIsAssignableFromString() {
    }

    @Test
    void streamingTypeOfKeyListOuputWithCodecIsAssignableFromListOfString() {
    }

    @Test
    void customizedValueOutput() {
    }

    private static abstract class IntermediateOutput<K1, V1> extends CommandOutput<K1, V1, V1> {

        IntermediateOutput(RedisCodec<K1, V1> codec, V1 output) {
            super(codec, null);
        }
    }

    private static class KeyTypedOutput extends IntermediateOutput<ByteBuffer, byte[]> {

        KeyTypedOutput(RedisCodec<ByteBuffer, byte[]> codec) {
            super(codec, null);
        }
    }
}
