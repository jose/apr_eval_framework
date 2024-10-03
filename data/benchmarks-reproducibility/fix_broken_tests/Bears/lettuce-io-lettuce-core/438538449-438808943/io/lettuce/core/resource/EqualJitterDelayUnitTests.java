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

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 * @author Jongyeol Choi
 * @author Mark Paluch
 */
class EqualJitterDelayUnitTests {

    @Test
    void shouldNotCreateIfLowerBoundIsNegative() {
    }

    @Test
    void shouldNotCreateIfLowerBoundIsSameAsUpperBound() {
    }

    @Test
    void negativeAttemptShouldReturnZero() {
    }

    @Test
    void zeroShouldReturnZero() {
    }

    @Test
    void testDefaultDelays() {
    }
}
