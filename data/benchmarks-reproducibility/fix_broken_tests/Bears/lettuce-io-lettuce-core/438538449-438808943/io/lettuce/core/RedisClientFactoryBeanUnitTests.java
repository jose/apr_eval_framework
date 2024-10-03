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

import java.net.URI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.lettuce.core.support.RedisClientFactoryBean;
import io.lettuce.test.resource.FastShutdown;

/**
 * @author Mark Paluch
 */
class RedisClientFactoryBeanUnitTests {

    private RedisClientFactoryBean sut = new RedisClientFactoryBean();

    @AfterEach
    void tearDown() throws Exception {
        FastShutdown.shutdown(sut.getObject());
        sut.destroy();
    }

    @Test
    void testSimpleUri() throws Exception {
    }

    @Test
    void testSimpleUriWithoutDB() throws Exception {
    }

    @Test
    void testSimpleUriWithoutDB2() throws Exception {
    }

    @Test
    void testSimpleUriWithPort() throws Exception {
    }

    @Test
    void testSentinelUri() throws Exception {
    }

    @Test
    void testSentinelUriWithPort() throws Exception {
    }

    @Test
    void testMultipleSentinelUri() throws Exception {
    }

    @Test
    void testMultipleSentinelUriWithPorts() throws Exception {
    }
}
