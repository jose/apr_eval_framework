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
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import io.lettuce.core.RedisCommandExecutionException;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class RedisCommandsIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    RedisCommandsIntegrationTests(StatefulRedisConnection<String, String> connection) {
        this.redis = connection.sync();
    }

    @Test
    void verifierShouldCatchMisspelledDeclarations() {
    }

    @Test
    void disabledVerifierDoesNotReportTypo() {
    }

    @Test
    void doesNotFailIfCommandRetrievalFails() {
    }

    @Test
    void verifierShouldCatchTooFewParametersDeclarations() {
    }

    private interface TooFewParameters extends Commands {

        String get();

    }

    private interface WithTypo extends Commands {

        String gat(String key);

    }
}
