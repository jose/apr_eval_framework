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
package io.lettuce.core.commands;

import static io.lettuce.core.BitFieldArgs.offset;
import static io.lettuce.core.BitFieldArgs.signed;
import static io.lettuce.core.BitFieldArgs.typeWidthBasedOffset;
import static io.lettuce.core.BitFieldArgs.unsigned;
import static io.lettuce.core.BitFieldArgs.OverflowType.WRAP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.BitFieldArgs;
import io.lettuce.core.RedisClient;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.condition.EnabledOnCommand;

/**
 * @author Will Glozer
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BitCommandIntegrationTests extends TestSupport {

    private final RedisClient client;
    private final RedisCommands<String, String> redis;

    protected RedisCommands<String, String> bitstring;

    @Inject
    protected BitCommandIntegrationTests(RedisClient client, RedisCommands<String, String> redis) {
        this.client = client;
        this.redis = redis;
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
        this.bitstring = client.connect(new BitStringCodec()).sync();
    }

    @AfterEach
    void tearDown() {
        this.bitstring.getStatefulConnection().close();
    }

    @Test
    void bitcount() {
    }

    @Test
    void bitfieldType() {
    }

    @Test
    void bitfieldTypeSigned65() {
    }

    @Test
    void bitfieldTypeUnsigned64() {
    }

    @Test
    void bitfieldBuilderEmptyPreviousType() {
    }

    @Test
    void bitfieldArgsTest() {
    }

    @Test
    @EnabledOnCommand("BITFIELD")
    void bitfield() {
    }

    @Test
    @EnabledOnCommand("BITFIELD")
    void bitfieldGetWithOffset() {
    }

    @Test
    @EnabledOnCommand("BITFIELD")
    void bitfieldSet() {
    }

    @Test
    @EnabledOnCommand("BITFIELD")
    void bitfieldWithOffsetSet() {
    }

    @Test
    @EnabledOnCommand("BITFIELD")
    void bitfieldIncrBy() {
    }

    @Test
    @EnabledOnCommand("BITFIELD")
    void bitfieldWithOffsetIncrBy() {
    }

    @Test
    @EnabledOnCommand("BITFIELD")
    void bitfieldOverflow() {
    }

    @Test
    void bitpos() {
    }

    @Test
    void bitposOffset() {
    }

    @Test
    void bitopAnd() {
    }

    @Test
    void bitopNot() {
    }

    @Test
    void bitopOr() {
    }

    @Test
    void bitopXor() {
    }

    @Test
    void getbit() {
    }

    @Test
    void setbit() {
    }

}
