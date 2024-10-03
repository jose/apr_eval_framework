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
package io.lettuce.core.masterslave;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.models.role.RedisInstance;
import io.lettuce.core.models.role.RedisNodeDescription;
import io.lettuce.test.settings.TestSettings;

/**
 * @author Mark Paluch
 */
class MasterSlaveTopologyProviderUnitTests {

    private StatefulRedisConnection<String, String> connectionMock = mock(StatefulRedisConnection.class);

    private MasterSlaveTopologyProvider sut = new MasterSlaveTopologyProvider(connectionMock,
            RedisURI.Builder.redis(TestSettings.host(), TestSettings.port()).build());

    @Test
    void shouldParseMaster() {
    }

    @Test
    void shouldParseMasterAndSlave() {
    }

    @Test
    void shouldParseMasterHostname() {
    }

    @Test
    void shouldParseIPv6MasterAddress() {
    }

    @Test
    void shouldFailWithoutRole() {
    }

    @Test
    void shouldFailWithInvalidRole() {
    }

    @Test
    void shouldParseSlaves() {
    }

    @Test
    void shouldParseIPv6SlaveAddress() {
    }
}
