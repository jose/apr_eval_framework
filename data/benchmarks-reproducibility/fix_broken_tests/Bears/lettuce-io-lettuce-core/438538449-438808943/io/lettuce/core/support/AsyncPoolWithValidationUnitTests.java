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

import static io.lettuce.core.internal.Futures.failed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import io.lettuce.core.RedisException;
import io.lettuce.test.Futures;

/**
 * @author Mark Paluch
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AsyncPoolWithValidationUnitTests {

    @Mock
    AsyncObjectFactory<String> factory;

    @BeforeEach
    void before() {
        when(factory.destroy(any())).thenReturn(CompletableFuture.completedFuture(null));
    }

    private void mockCreation() {

        AtomicInteger counter = new AtomicInteger();
        when(factory.create()).then(invocation -> CompletableFuture.completedFuture("" + counter.incrementAndGet()));
    }

    @Test
    void objectCreationShouldFail() {
    }

    @Test
    void objectCreationFinishesAfterShutdown() {
    }

    @Test
    void objectCreationCanceled() {
    }

    @Test
    void shouldCreateObjectWithTestOnBorrowFailExceptionally() {
    }

    @Test
    void shouldCreateObjectWithTestOnBorrowSuccess() {
    }

    @Test
    void shouldCreateObjectWithTestOnBorrowFailState() {
    }

    @Test
    void shouldCreateFailedObjectWithTestOnBorrowFail() {
    }

    @Test
    void shouldTestObjectOnBorrowSuccessfully() {
    }

    @Test
    void shouldTestObjectOnBorrowFailState() {
    }

    @Test
    void shouldTestObjectOnBorrowFailExceptionally() {
    }

    @Test
    void shouldTestObjectOnReturnSuccessfully() {
    }

    @Test
    void shouldTestObjectOnReturnFailState() {
    }

    @Test
    void shouldTestObjectOnReturnFailExceptionally() {
    }

    @Test
    void shouldRefillIdleObjects() {
    }

    @Test
    void shouldDisposeIdleObjects() {
    }
}
