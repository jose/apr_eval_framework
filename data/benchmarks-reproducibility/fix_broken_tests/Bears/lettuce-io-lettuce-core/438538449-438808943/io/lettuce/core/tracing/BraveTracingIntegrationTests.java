/*
 * Copyright 2018 the original author or authors.
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
package io.lettuce.core.tracing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;
import zipkin2.Span;
import brave.ScopedSpan;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.test.Wait;
import io.netty.channel.unix.DomainSocketAddress;

/**
 * @author Mark Paluch
 */
class BraveTracingIntegrationTests extends TestSupport {

    private static ClientResources clientResources;
    private static RedisClient client;
    private static Tracing clientTracing;
    private static Queue<Span> spans = new LinkedBlockingQueue<>();

    @BeforeAll
    static void beforeClass() {

        clientTracing = Tracing.newBuilder().localServiceName("client")
                .currentTraceContext(CurrentTraceContext.Default.create()).spanReporter(spans::add).build();

        clientResources = DefaultClientResources.builder().tracing(BraveTracing.create(clientTracing)).build();
        client = RedisClient.create(clientResources, RedisURI.Builder.redis(host, port).build());
    }

    @BeforeEach
    void before() {

        Tracer tracer = clientTracing.tracer();
        if (tracer.currentSpan() != null) {
            clientTracing.tracer().currentSpan().abandon();
        }

        spans.clear();
    }

    @AfterAll
    static void afterClass() {

        clientTracing.close();
        clientResources.shutdown(0, 0, TimeUnit.MILLISECONDS);
    }

    @Test
    void pingWithTrace() {
    }

    @Test
    void pingWithTraceShouldCatchErrors() {
    }

    @Test
    void reactivePing() {
    }

    @Test
    void reactivePingWithTrace() {
    }

    @Test
    void reactiveGetAndSetWithTrace() {
    }

    @Test
    void reactiveGetAndSetWithTraceProvider() {
    }

    @Test
    void shouldReportSimpleServiceName() {
    }
}
