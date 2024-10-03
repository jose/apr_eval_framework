/*
 * Copyright 2016-2018 the original author or authors.
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
package io.lettuce.core.cluster;

import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.*;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.test.KeysAndValues;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScanIteratorIntegrationTests extends TestSupport {

    private final StatefulRedisClusterConnection<String, String> connection;
    private final RedisClusterCommands<String, String> redis;

    @Inject
    ScanIteratorIntegrationTests(StatefulRedisClusterConnection<String, String> connection) {
        this.connection = connection;
        this.redis = connection.sync();
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void scanShouldThrowNoSuchElementExceptionOnEmpty() {
    }

    @Test
    void keysSinglePass() {
    }

    @Test
    void keysMultiPass() {
    }

    @Test
    void hscanShouldThrowNoSuchElementExceptionOnEmpty() {
    }

    @Test
    void hashSinglePass() {
    }

    @Test
    void hashMultiPass() {
    }

    @Test
    void sscanShouldThrowNoSuchElementExceptionOnEmpty() {
    }

    @Test
    void setSinglePass() {
    }

    @Test
    void setMultiPass() {
    }

    @Test
    void zscanShouldThrowNoSuchElementExceptionOnEmpty() {
    }

    @Test
    void zsetSinglePass() {
    }

    @Test
    void zsetMultiPass() {
    }
}
