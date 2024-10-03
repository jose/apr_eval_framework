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
package io.lettuce.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import reactor.test.StepVerifier;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.CompressionCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.test.LettuceExtension;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class CustomCodecIntegrationTests extends TestSupport {

    private final RedisClient client;

    @Inject
    CustomCodecIntegrationTests(RedisClient client) {
        this.client = client;
    }

    @Test
    void testJavaSerializer() {
    }

    @Test
    void testJavaSerializerReactive() {
    }

    @Test
    void testDeflateCompressedJavaSerializer() {
    }

    @Test
    void testGzipompressedJavaSerializer() {
    }

    @Test
    void testByteCodec() {
    }

    @Test
    void testByteBufferCodec() {
    }

    class SerializedObjectCodec implements RedisCodec<String, Object> {

        private Charset charset = Charset.forName("UTF-8");

        @Override
        public String decodeKey(ByteBuffer bytes) {
            return charset.decode(bytes).toString();
        }

        @Override
        public Object decodeValue(ByteBuffer bytes) {
            try {
                byte[] array = new byte[bytes.remaining()];
                bytes.get(array);
                ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(array));
                return is.readObject();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public ByteBuffer encodeKey(String key) {
            return charset.encode(key);
        }

        @Override
        public ByteBuffer encodeValue(Object value) {
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(bytes);
                os.writeObject(value);
                return ByteBuffer.wrap(bytes.toByteArray());
            } catch (IOException e) {
                return null;
            }
        }
    }

    class ByteBufferCodec implements RedisCodec<ByteBuffer, ByteBuffer> {

        @Override
        public ByteBuffer decodeKey(ByteBuffer bytes) {

            ByteBuffer decoupled = ByteBuffer.allocate(bytes.remaining());
            decoupled.put(bytes);
            return (ByteBuffer) decoupled.flip();
        }

        @Override
        public ByteBuffer decodeValue(ByteBuffer bytes) {

            ByteBuffer decoupled = ByteBuffer.allocate(bytes.remaining());
            decoupled.put(bytes);
            return (ByteBuffer) decoupled.flip();
        }

        @Override
        public ByteBuffer encodeKey(ByteBuffer key) {
            return key.asReadOnlyBuffer();
        }

        @Override
        public ByteBuffer encodeValue(ByteBuffer value) {
            return value.asReadOnlyBuffer();
        }
    }
}
