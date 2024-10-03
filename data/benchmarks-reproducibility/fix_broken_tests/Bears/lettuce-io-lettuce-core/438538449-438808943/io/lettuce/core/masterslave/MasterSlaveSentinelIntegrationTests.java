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

import static io.lettuce.core.masterslave.MasterSlaveTest.slaveCall;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import io.lettuce.core.*;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.sentinel.SentinelTestSettings;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.settings.TestSettings;
import io.netty.channel.group.ChannelGroup;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class MasterSlaveSentinelIntegrationTests extends TestSupport {

    private final Pattern pattern = Pattern.compile("role:(\\w+)");
    private final RedisClient redisClient;

    @Inject
    MasterSlaveSentinelIntegrationTests(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Test
    void testMasterSlaveSentinelBasic() {
    }

    @Test
    void masterSlaveConnectionShouldSetClientName() {
    }

    @Test
    void testMasterSlaveSentinelWithTwoUnavailableSentinels() {
    }

    @Test
    void testMasterSlaveSentinelWithUnavailableSentinels() {
    }

    @Test
    void testMasterSlaveSentinelConnectionCount() {
    }

    @Test
    void testMasterSlaveSentinelClosesSentinelConnections() {
    }

    private void assertThatServerIs(String server, String expectation) {
        Matcher matcher = pattern.matcher(server);

        assertThat(matcher.find()).isTrue();
        assertThat(matcher.group(1)).isEqualTo(expectation);
    }
}
