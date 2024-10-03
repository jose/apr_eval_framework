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
import static org.junit.Assert.fail;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.reactivex.Observable;

/**
 * @author Mark Paluch
 */
class ConversionServiceUnitTests {

    private ConversionService sut = new ConversionService();

    @Test
    void getConverter() {
    }

    @Test
    void canConvert() {
    }

    @Test
    void convert() {
    }

    private class FluxToObservableConverter implements Function<Flux<?>, Observable<?>> {
        @Override
        public Observable<?> apply(Flux<?> source) {
            return null;
        }
    }

    private class MonoToObservableConverter implements Function<Mono<?>, Observable<?>> {
        @Override
        public Observable<?> apply(Mono<?> source) {
            return Observable.just("world");
        }
    }

}
