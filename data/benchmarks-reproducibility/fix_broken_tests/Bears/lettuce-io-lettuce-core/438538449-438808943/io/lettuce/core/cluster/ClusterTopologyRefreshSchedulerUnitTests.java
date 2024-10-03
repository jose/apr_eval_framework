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
package io.lettuce.core.cluster;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import io.lettuce.core.resource.ClientResources;
import io.lettuce.test.Delay;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author Mark Paluch
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ClusterTopologyRefreshSchedulerUnitTests {

    private ClusterTopologyRefreshScheduler sut;

    private ClusterTopologyRefreshOptions immediateRefresh = ClusterTopologyRefreshOptions.builder().enablePeriodicRefresh(1, TimeUnit.MILLISECONDS)
            .enableAllAdaptiveRefreshTriggers().build();

    private ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
            .topologyRefreshOptions(immediateRefresh).build();

    @Mock
    private ClientResources clientResources;

    @Mock
    private RedisClusterClient clusterClient;

    @Mock
    private EventExecutorGroup eventExecutors;

    @BeforeEach
    void before() {

        when(clientResources.eventExecutorGroup()).thenReturn(eventExecutors);

        sut = new ClusterTopologyRefreshScheduler(clusterClient, clientResources);
    }

    @Test
    void runShouldSubmitRefreshShouldTrigger() {
    }

    @Test
    void runnableShouldCallPartitionRefresh() {
    }

    @Test
    void shouldNotSubmitIfOptionsNotSet() {
    }

    @Test
    void shouldNotSubmitIfExecutorIsShuttingDown() {
    }

    @Test
    void shouldNotSubmitIfExecutorIsShutdown() {
    }

    @Test
    void shouldNotSubmitIfExecutorIsTerminated() {
    }

    @Test
    void shouldTriggerRefreshOnAskRedirection() {
    }

    @Test
    void shouldNotTriggerAdaptiveRefreshUsingDefaults() {
    }

    @Test
    void shouldTriggerRefreshOnMovedRedirection() {
    }

    @Test
    void shouldTriggerRefreshOnReconnect() {
    }

    @Test
    void shouldTriggerRefreshOnUnknownNode() {
    }

    @Test
    void shouldNotTriggerRefreshOnFirstReconnect() {
    }

    @Test
    void shouldRateLimitAdaptiveRequests() {
    }
}
