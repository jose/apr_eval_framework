/*
 * Copyright 2017-2018 the original author or authors.
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
package io.lettuce.core.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import io.lettuce.test.Futures;

/**
 * @author Mark Paluch
 */
class BoundedAsyncPoolUnitTests {

    private AtomicInteger counter = new AtomicInteger();
    private List<String> destroyed = new ArrayList<>();

    private AsyncObjectFactory<String> STRING_OBJECT_FACTORY = new AsyncObjectFactory<String>() {
        @Override
        public CompletableFuture<String> create() {
            return CompletableFuture.completedFuture(counter.incrementAndGet() + "");
        }

        @Override
        public CompletableFuture<Void> destroy(String object) {
            destroyed.add(object);
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletableFuture<Boolean> validate(String object) {
            return CompletableFuture.completedFuture(true);
        }
    };

    @Test
    void shouldCreateObject() {
    }

    @Test
    void shouldCreateMinIdleObject() {
    }

    @Test
    void shouldCreateMaintainMinIdleObject() {
    }

    @Test
    void shouldCreateMaintainMinMaxIdleObject() {
    }

    @Test
    void shouldReturnObject() {
    }

    @Test
    void shouldReuseObjects() {
    }

    @Test
    void shouldDestroyIdle() {
    }

    @Test
    void shouldExhaustPool() {
    }

    @Test
    void shouldClearPool() {
    }

    @Test
    void shouldExhaustPoolConcurrent() {
    }

    @Test
    void shouldConcurrentlyFail() {
    }
}
