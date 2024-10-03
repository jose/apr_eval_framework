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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.junit.jupiter.api.Test;

/**
 * @author Mark Paluch
 */
class RedisURIBuilderUnitTests {

    @Test
    void sentinel() {
    }

    @Test
    void sentinelWithHostShouldFail() {
    }

    @Test
    void sentinelWithPort() {
    }

    @Test
    void shouldFailIfBuilderIsEmpty() {
    }

    @Test
    void redisWithHostAndPort() {
    }

    @Test
    void redisWithPort() {
    }

    @Test
    void redisWithClientName() {
    }

    @Test
    void redisHostAndPortWithInvalidPort() {
    }

    @Test
    void redisWithInvalidPort() {
    }

    @Test
    void redisFromUrl() {
    }

    @Test
    void redisFromUrlNoPassword() {
    }

    @Test
    void redisFromUrlPassword() {
    }

    @Test
    void redisWithSSL() {
    }

    @Test
    void redisSslFromUrl() {
    }

    @Test
    void redisSentinelFromUrl() {
    }

    @Test
    void redisSentinelWithInvalidPort() {
    }

    @Test
    void redisSentinelWithMasterIdAndInvalidPort() {
    }

    @Test
    void redisSentinelWithNullMasterId() {
    }

    @Test
    void redisSentinelWithSSLNotPossible() {
    }

    @Test
    void redisSentinelWithTLSNotPossible() {
    }

    @Test
    void invalidScheme() {
    }

    @Test
    void redisSocket() throws IOException {
    }

    @Test
    void redisSocketWithPassword() throws IOException {
    }

}
