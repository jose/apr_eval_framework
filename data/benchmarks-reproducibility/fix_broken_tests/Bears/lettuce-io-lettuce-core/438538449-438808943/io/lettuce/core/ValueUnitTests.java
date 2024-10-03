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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

/**
 * @author Mark Paluch
 */
class ValueUnitTests {

    @Test
    void shouldCreateEmptyValueFromOptional() {
    }

    @Test
    void shouldCreateEmptyValue() {
    }

    @Test
    void shouldCreateNonEmptyValueFromOptional() {
    }

    @Test
    void shouldCreateEmptyValueFromValue() {
    }

    @Test
    void shouldCreateNonEmptyValueFromValue() {
    }

    @Test
    void justShouldCreateValueFromValue() {
    }

    @Test
    void justShouldRejectEmptyValueFromValue() {
    }

    @Test
    void shouldCreateNonEmptyValue() {
    }

    @Test
    void optionalShouldReturnOptional() {
    }

    @Test
    void emptyValueOptionalShouldReturnOptional() {
    }

    @Test
    void getValueOrElseShouldReturnValue() {
    }

    @Test
    void getValueOrElseShouldReturnOtherValue() {
    }

    @Test
    void orElseThrowShouldReturnValue() {
    }

    @Test
    void emptyValueGetValueOrElseShouldThrowException() {
    }

    @Test
    void getValueOrElseGetShouldReturnValue() {
    }

    @Test
    void emptyValueGetValueOrElseGetShouldReturnOtherValue() {
    }

    @Test
    void mapShouldMapValue() {
    }

    @Test
    void ifHasValueShouldExecuteCallback() {
    }

    @Test
    void emptyValueShouldNotExecuteIfHasValueCallback() {
    }

    @Test
    void ifEmptyShouldExecuteCallback() {
    }

    @Test
    void valueShouldNotExecuteIfEmptyCallback() {
    }

    @Test
    void emptyValueMapShouldNotMapEmptyValue() {
    }

    @Test
    void emptyValueGetEmptyValueShouldThrowException() {
    }

    @Test
    void shouldBeEquals() {
    }

    @Test
    void toStringShouldRenderCorrectly() {
    }

    @Test
    void emptyValueStreamShouldCreateEmptyStream() {
    }

    @Test
    void streamShouldCreateAStream() {
    }
}
