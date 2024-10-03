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
package io.lettuce.core.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;
import io.lettuce.core.event.Event;
import io.lettuce.core.event.EventBus;
import io.lettuce.core.metrics.CommandLatencyCollector;
import io.lettuce.core.metrics.DefaultCommandLatencyCollectorOptions;
import io.lettuce.test.Futures;
import io.lettuce.test.resource.FastShutdown;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.Timer;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;

/**
 * @author Mark Paluch
 */
class DefaultClientResourcesUnitTests {

    @Test
    void testDefaults() throws Exception {
    }

    @Test
    void testBuilder() throws Exception {
    }

    @Test
    void testDnsResolver() {
    }

    @Test
    void testProvidedResources() {
    }

    @Test
    void mutateResources() {
    }

    @Test
    void testSmallPoolSize() {
    }

    @Test
    void testEventBus() {
    }

    @Test
    void delayInstanceShouldRejectStatefulDelay() {
    }

    @Test
    void reconnectDelayCreatesNewForStatefulDelays() {
    }

    @Test
    void reconnectDelayReturnsSameInstanceForStatelessDelays() {
    }
}
