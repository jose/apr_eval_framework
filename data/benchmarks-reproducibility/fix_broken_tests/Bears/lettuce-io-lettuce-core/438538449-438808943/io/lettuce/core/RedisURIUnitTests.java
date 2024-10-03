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

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.lettuce.core.internal.LettuceSets;

/**
 * @author Mark Paluch
 */
class RedisURIUnitTests {

    @Test
    void equalsTest() {
    }

    @Test
    void setUsage() {
    }

    @Test
    void mapUsage() {
    }

    @Test
    void simpleUriTest() {
    }

    @Test
    void shouldThrowIllegalArgumentExceptionOnMalformedUri() {
    }

    @Test
    void sslUriTest() {
    }

    @Test
    void tlsUriTest() {
    }

    @Test
    void multipleClusterNodesTest() {
    }

    @Test
    void sentinelEqualsTest() {
    }

    @Test
    void sentinelUriTest() {
    }

    @Test
    void socketEqualsTest() {
    }

    @Test
    void socketUriTest() {
    }

    @Test
    void socketAltUriTest() {
    }

    @Test
    void timeoutParsingTest() {
    }

    @Test
    void queryStringDecodingTest() {
    }

    @Test
    void timeoutParsingWithJunkParamTest() {
    }

    private RedisURI checkUriTimeout(String uri, long expectedTimeout, TimeUnit expectedUnit) {
        RedisURI redisURI = RedisURI.create(uri);
        assertThat(expectedUnit.convert(redisURI.getTimeout().toNanos(), TimeUnit.NANOSECONDS)).isEqualTo(expectedTimeout);
        return redisURI;
    }

    @Test
    void databaseParsingTest() {
    }

    @Test
    void clientNameParsingTest() {
    }

    @Test
    void parsingWithInvalidValuesTest() {
    }
}
