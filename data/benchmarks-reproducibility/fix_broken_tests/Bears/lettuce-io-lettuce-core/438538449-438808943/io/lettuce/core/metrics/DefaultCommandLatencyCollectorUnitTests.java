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
package io.lettuce.core.metrics;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import io.lettuce.core.metrics.DefaultCommandLatencyCollector.PauseDetectorWrapper;
import io.lettuce.core.protocol.CommandType;
import io.netty.channel.local.LocalAddress;

/**
 * @author Mark Paluch
 */
@ExtendWith(MockitoExtension.class)
class DefaultCommandLatencyCollectorUnitTests {

    private DefaultCommandLatencyCollector sut;

    @Test
    void shutdown() {
    }

    @Test
    void simpleCreateShouldNotInitializePauseDetector() {
    }

    @Test
    void latencyRecordShouldInitializePauseDetectorWrapper() {
    }

    @Test
    void shutdownShouldReleasePauseDetector() {
    }

    @Test
    void verifyMetrics() {
    }

    @Test
    void verifyCummulativeMetrics() {
    }

    private void setupData() {
        sut.recordCommandLatency(LocalAddress.ANY, LocalAddress.ANY, CommandType.BGSAVE, MILLISECONDS.toNanos(100),
                MILLISECONDS.toNanos(1000));
        sut.recordCommandLatency(LocalAddress.ANY, LocalAddress.ANY, CommandType.BGSAVE, MILLISECONDS.toNanos(200),
                MILLISECONDS.toNanos(1000));
        sut.recordCommandLatency(LocalAddress.ANY, LocalAddress.ANY, CommandType.BGSAVE, MILLISECONDS.toNanos(300),
                MILLISECONDS.toNanos(1000));
    }
}
