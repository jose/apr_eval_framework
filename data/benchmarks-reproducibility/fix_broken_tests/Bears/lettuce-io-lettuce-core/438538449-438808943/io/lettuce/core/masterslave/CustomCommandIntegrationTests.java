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
package io.lettuce.core.masterslave;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Arrays;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.*;
import io.lettuce.test.Futures;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class CustomCommandIntegrationTests extends TestSupport {

    private final RedisClient redisClient;

    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> redis;

    @Inject
    CustomCommandIntegrationTests(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @BeforeEach
    void before() {

        RedisURI uri = RedisURI.create("redis-sentinel://127.0.0.1:26379?sentinelMasterId=mymaster&timeout=5s");
        connection = MasterSlave.connect(redisClient, StringCodec.UTF8, uri);
        redis = connection.sync();
        redis.flushall();
    }

    @AfterEach
    void after() {
        connection.close();
    }

    @Test
    void dispatchSet() {
    }

    @Test
    void dispatchWithoutArgs() {
    }

    @Test
    void dispatchShouldFailForWrongDataType() {
    }

    @Test
    void dispatchTransactions() {
    }

    @Test
    void masterSlaveAsyncPing() {
    }

    @Test
    void masterSlaveAsyncBatchPing() {
    }

    @Test
    void masterSlaveFireAndForget() {
    }

    private StatefulRedisConnection<String, String> getStandaloneConnection() {

        assumeTrue(redis.getStatefulConnection() instanceof StatefulRedisConnection);
        return redis.getStatefulConnection();
    }

    public enum MyCommands implements ProtocolKeyword {
        PING, SET, INFO;

        private final byte name[];

        MyCommands() {
            // cache the bytes for the command name. Reduces memory and cpu pressure when using commands.
            name = name().getBytes();
        }

        @Override
        public byte[] getBytes() {
            return name;
        }
    }
}
