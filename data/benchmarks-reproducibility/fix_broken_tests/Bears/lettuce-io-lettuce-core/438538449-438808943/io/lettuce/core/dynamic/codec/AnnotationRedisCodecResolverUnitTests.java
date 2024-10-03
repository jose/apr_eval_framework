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
package io.lettuce.core.dynamic.codec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.lettuce.core.Range;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.dynamic.CommandMethod;
import io.lettuce.core.dynamic.DeclaredCommandMethod;
import io.lettuce.core.dynamic.annotation.Key;
import io.lettuce.core.dynamic.annotation.Value;
import io.lettuce.core.dynamic.support.ReflectionUtils;

/**
 * @author Mark Paluch
 */
class AnnotationRedisCodecResolverUnitTests {

    private List<RedisCodec<?, ?>> codecs = Arrays.asList(new StringCodec(), new ByteArrayCodec());

    @Test
    void shouldResolveFullyHinted() {
    }

    @Test
    void shouldResolveHintedKey() {
    }

    @Test
    void shouldResolveHintedValue() {
    }

    @Test
    void shouldResolveWithoutHints() {
    }

    @Test
    void shouldResolveHintedByteArrayValue() {
    }

    @Test
    void resolutionShouldFail() {
    }

    @Test
    void shouldDiscoverCodecTypesFromWrappers() {
    }

    RedisCodec<?, ?> resolve(Method method) {

        CommandMethod commandMethod = DeclaredCommandMethod.create(method);
        AnnotationRedisCodecResolver resolver = new AnnotationRedisCodecResolver(codecs);

        return resolver.resolve(commandMethod);
    }

    private static interface CommandMethods {

        String stringOnly(@Key String key, @Value String value);

        String annotatedKey(@Key String key, String value);

        String annotatedValue(String key, @Value String value);

        String annotatedByteArrayValue(String key, @Value byte[] value);

        String nothingAnnotated(String key, String value);

        String mixedTypes(@Key String key, @Value byte[] value);

        String withWrappers(@Value Range<String> range, @Value io.lettuce.core.Value<Number> value);

        String withMap(Map<Integer, String> map);
    }

}
