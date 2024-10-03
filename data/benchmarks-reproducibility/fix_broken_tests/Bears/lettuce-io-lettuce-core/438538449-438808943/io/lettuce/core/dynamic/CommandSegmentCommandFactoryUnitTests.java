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
import static org.junit.Assert.fail;

import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

import io.lettuce.core.ScanArgs;
import io.lettuce.core.SetArgs;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.dynamic.annotation.Command;
import io.lettuce.core.dynamic.annotation.Param;
import io.lettuce.core.dynamic.annotation.Value;
import io.lettuce.core.dynamic.domain.Timeout;
import io.lettuce.core.dynamic.output.CodecAwareOutputFactoryResolver;
import io.lettuce.core.dynamic.output.OutputRegistry;
import io.lettuce.core.dynamic.output.OutputRegistryCommandOutputFactoryResolver;
import io.lettuce.core.dynamic.segment.AnnotationCommandSegmentFactory;
import io.lettuce.core.dynamic.segment.CommandSegmentFactory;
import io.lettuce.core.dynamic.support.ReflectionUtils;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.protocol.RedisCommand;

/**
 * @author Mark Paluch
 */
class CommandSegmentCommandFactoryUnitTests {

    @Test
    void setKeyValue() {
    }

    @Test
    void setKeyValueWithByteArrayCodec() {
    }

    @Test
    void setKeyValueWithHintedValue() {
    }

    @Test
    void lowercaseCommandResolvesToStringCommand() {
    }

    @Test
    void setWithArgs() {
    }

    @Test
    void varargsMethodWithParameterIndexAccess() {
    }

    @Test
    void clientSetname() {
    }

    @Test
    void annotatedClientSetname() {
    }

    @Test
    void asyncWithTimeout() {
    }

    @Test
    void syncWithTimeout() {
    }

    @Test
    void resolvesUnknownCommandToStringBackedCommandType() {
    }

    private CommandMethod methodOf(Class<?> commandInterface, String methodName, Class... args) {
        return DeclaredCommandMethod.create(ReflectionUtils.findMethod(commandInterface, methodName, args));
    }

    @SuppressWarnings("unchecked")
    private RedisCommand<?, ?, ?> createCommand(CommandMethod commandMethod, RedisCodec<?, ?> codec, Object... args) {

        CommandSegmentFactory segmentFactory = new AnnotationCommandSegmentFactory();
        CodecAwareOutputFactoryResolver outputFactoryResolver = new CodecAwareOutputFactoryResolver(
                new OutputRegistryCommandOutputFactoryResolver(new OutputRegistry()), codec);
        CommandSegmentCommandFactory factory = new CommandSegmentCommandFactory(
                segmentFactory.createCommandSegments(commandMethod), commandMethod, codec, outputFactoryResolver);

        return factory.createCommand(args);
    }

    @SuppressWarnings("unchecked")
    private String toString(RedisCommand<?, ?, ?> command) {

        StringBuilder builder = new StringBuilder();

        builder.append(command.getType().name());

        String commandString = command.getArgs().toCommandString();

        if (!commandString.isEmpty()) {
            builder.append(' ').append(commandString);
        }

        return builder.toString();
    }

    private interface Commands {

        boolean set(String key, String value);

        @Command("SET")
        boolean set2(String key, @Value String value);

        @Command("set")
        boolean set3(String key, @Value String value);

        boolean set(String key, String value, SetArgs setArgs);

        boolean clientSetname(String connectionName);

        @Command("CLIENT SETNAME :connectionName")
        boolean methodWithNamedParameters(@Param("connectionName") String connectionName);

        @Command("MGET ?1 ?0")
        String varargsWithParamIndexes(ScanArgs scanArgs, String... keys);

        @Command("XYZ")
        boolean unknownCommand();
    }

    private static interface MethodsWithTimeout {

        Future<String> async(String key, Timeout timeout);

        String sync(String key, Timeout timeout);
    }
}
