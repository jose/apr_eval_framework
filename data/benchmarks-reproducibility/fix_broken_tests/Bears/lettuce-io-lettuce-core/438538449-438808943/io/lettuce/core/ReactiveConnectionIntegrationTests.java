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

import static io.lettuce.core.ClientOptions.DisconnectedBehavior.REJECT_COMMANDS;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.Delay;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.Wait;

@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReactiveConnectionIntegrationTests extends TestSupport {

    private final StatefulRedisConnection<String, String> connection;
    private final RedisCommands<String, String> redis;
    private final RedisReactiveCommands<String, String> reactive;

    @Inject
    ReactiveConnectionIntegrationTests(StatefulRedisConnection<String, String> connection) {
        this.connection = connection;
        this.redis = connection.sync();
        this.reactive = connection.reactive();
    }

    @BeforeEach
    void setUp() {
        this.connection.async().flushall();
    }

    @Test
    void doNotFireCommandUntilObservation() {
    }

    @Test
    void fireCommandAfterObserve() {
    }

    @Test
    void isOpen() {
    }

    @Test
    void getStatefulConnection() {
    }

    @Test
    @Inject
    void testCancelCommand(@New StatefulRedisConnection<String, String> connection) {
    }

    @Test
    void testEcho() {
    }

    @Test
    @Inject
    void testMonoMultiCancel(@New StatefulRedisConnection<String, String> connection) {
    }

    @Test
    @Inject
    void testFluxCancel(@New StatefulRedisConnection<String, String> connection) {
    }

    @Test
    void multiSubscribe() throws Exception {
    }

    @Test
    @Inject
    void transactional(RedisClient client) throws Exception {
    }

    @Test
    void auth() {
    }

    @Test
    void subscriberCompletingWithExceptionShouldBeHandledSafely() {
    }

    @Test
    @Inject
    void subscribeWithDisconnectedClient(RedisClient client) {
    }

    private static Subscriber<String> createSubscriberWithExceptionOnComplete() {
        return new Subscriber<String>() {

            @Override
            public void onSubscribe(Subscription s) {
                s.request(1000);
            }

            @Override
            public void onComplete() {
                throw new RuntimeException("throwing something");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String s) {
            }
        };
    }

    private static class CompletionSubscriber implements Subscriber<Object> {

        private final List<Object> result;

        CompletionSubscriber(List<Object> result) {
            this.result = result;
        }

        @Override
        public void onSubscribe(Subscription s) {
            s.request(1000);
        }

        @Override
        public void onComplete() {
            result.add("completed");
        }

        @Override
        public void onError(Throwable e) {
            result.add(e);
        }

        @Override
        public void onNext(Object o) {
            result.add(o);
        }
    }
}
