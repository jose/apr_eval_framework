/*
 * Copyright 2016-2018 the original author or authors.
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
package io.lettuce.core.dynamic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rx.Single;
import io.reactivex.Maybe;

/**
 * @author Mark Paluch
 */
class ReactiveTypeAdaptersUnitTests {

    private ConversionService conversionService = new ConversionService();

    @BeforeEach
    void before() {
        ReactiveTypeAdapters.registerIn(conversionService);
    }

    @Test
    void toWrapperShouldCastMonoToMono() {
    }

    @Test
    void toWrapperShouldConvertMonoToRxJava1Single() {
    }

    @Test
    void toWrapperShouldConvertMonoToRxJava2Single() {
    }

    @Test
    void toWrapperShouldConvertRxJava2SingleToMono() {
    }

    @Test
    void toWrapperShouldConvertRxJava2SingleToPublisher() {
    }

    @Test
    void toWrapperShouldConvertRxJava2MaybeToMono() {
    }

    @Test
    void toWrapperShouldConvertRxJava2MaybeToFlux() {
    }

    @Test
    void toWrapperShouldConvertRxJava2MaybeToPublisher() {
    }

    @Test
    void toWrapperShouldConvertRxJava2FlowableToMono() {
    }

    @Test
    void toWrapperShouldConvertRxJava2FlowableToFlux() {
    }

    @Test
    void toWrapperShouldCastRxJava2FlowableToPublisher() {
    }

    @Test
    void toWrapperShouldConvertRxJava2ObservableToMono() {
    }

    @Test
    void toWrapperShouldConvertRxJava2ObservableToFlux() {
    }

    @Test
    void toWrapperShouldConvertRxJava2ObservableToSingle() {
    }

    @Test
    void toWrapperShouldConvertRxJava2ObservableToMaybe() {
    }

    @Test
    void toWrapperShouldConvertRxJava2ObservableToPublisher() {
    }

    @Test
    void toWrapperShouldConvertMonoToFlux() {
    }
}
