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
package io.lettuce.core.dynamic.codec;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.lettuce.core.KeyValue;
import io.lettuce.core.Range;
import io.lettuce.core.Value;
import io.lettuce.core.dynamic.codec.AnnotationRedisCodecResolver.ParameterWrappers;
import io.lettuce.core.dynamic.parameter.Parameter;
import io.lettuce.core.dynamic.support.ReflectionUtils;
import io.lettuce.core.dynamic.support.TypeInformation;

/**
 * @author Mark Paluch
 */
class ParameterWrappersUnitTests {

    @Test
    void shouldReturnValueTypeForRange() {
    }

    @Test
    void shouldReturnValueTypeForValue() {
    }

    @Test
    void shouldReturnValueTypeForKeyValue() {
    }

    @Test
    void shouldReturnValueTypeForArray() {
    }

    @Test
    void shouldNotSupportByteArray() {
    }

    @Test
    void shouldReturnValueTypeForList() {
    }

    @Test
    void shouldReturnValueTypeForMap() {
    }

    private static interface CommandMethods {

        String range(Range<String> range);

        String value(Value<String> range);

        String keyValue(KeyValue<Integer, String> range);

        String array(String[] values);

        String byteArray(byte[] values);

        String withWrappers(Range<String> range, io.lettuce.core.Value<Number> value,
                io.lettuce.core.KeyValue<Integer, Long> keyValue);

        String withList(List<String> map);

        String withMap(Map<Integer, String> map);
    }

}
