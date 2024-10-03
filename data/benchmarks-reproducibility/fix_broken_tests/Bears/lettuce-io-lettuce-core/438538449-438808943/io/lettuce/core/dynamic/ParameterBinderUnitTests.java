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
package io.lettuce.core.dynamic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Base64Utils;
import org.springframework.util.ReflectionUtils;

import io.lettuce.core.*;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.dynamic.segment.CommandSegment;
import io.lettuce.core.dynamic.segment.CommandSegments;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;

/**
 * @author Mark Paluch
 */
@ExtendWith(MockitoExtension.class)
class ParameterBinderUnitTests {

    private ParameterBinder binder = new ParameterBinder();
    private CommandSegments segments = new CommandSegments(Collections.singletonList(CommandSegment.constant("set")));

    @Test
    void bindsNullValueAsEmptyByteArray() {
    }

    @Test
    void bindsStringCorrectly() {
    }

    @Test
    void bindsStringArrayCorrectly() {
    }

    @Test
    void bindsIntArrayCorrectly() {
    }

    @Test
    void bindsValueCorrectly() {
    }

    @Test
    void rejectsEmptyValue() {
    }

    @Test
    void bindsKeyValueCorrectly() {
    }

    @Test
    void rejectsEmptyKeyValue() {
    }

    @Test
    void bindsScoredValueCorrectly() {
    }

    @Test
    void rejectsEmptyScoredValue() {
    }

    @Test
    void bindsLimitCorrectly() {
    }

    @Test
    void bindsRangeCorrectly() {
    }

    @Test
    void bindsUnboundedRangeCorrectly() {
    }

    @Test
    void rejectsStringLowerValue() {
    }

    @Test
    void rejectsStringUpperValue() {
    }

    @Test
    void bindsValueRangeCorrectly() {
    }

    @Test
    void bindsUnboundedValueRangeCorrectly() {
    }

    @Test
    void bindsGeoCoordinatesCorrectly() {
    }

    @Test
    void bindsProtocolKeywordCorrectly() {
    }

    private CommandArgs<String, String> bind(Object object) {
        CommandMethod commandMethod = DeclaredCommandMethod.create(ReflectionUtils.findMethod(MyCommands.class, "justObject",
                Object.class));
        return bind(commandMethod, object);
    }

    private CommandArgs<String, String> bind(CommandMethod commandMethod, Object object) {
        DefaultMethodParametersAccessor parametersAccessor = new DefaultMethodParametersAccessor(commandMethod.getParameters(),
                object);

        CommandArgs<String, String> args = new CommandArgs<>(new StringCodec());
        binder.bind(args, StringCodec.UTF8, segments, parametersAccessor);

        return args;
    }

    private interface MyCommands {

        void justObject(Object object);

        void valueRange(@io.lettuce.core.dynamic.annotation.Value Range<String> value);
    }
}
