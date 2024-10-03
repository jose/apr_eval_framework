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
package io.lettuce.core.cluster;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.lettuce.core.RedisURI;
import io.lettuce.core.internal.LettuceLists;
import io.lettuce.test.resource.FastShutdown;
import io.lettuce.test.resource.TestClientResources;
import io.lettuce.test.settings.TestSettings;

/**
 * @author Mark Paluch
 */
class RedisClusterClientFactoryTests {

    private static final String URI = "redis://" + TestSettings.host() + ":" + TestSettings.port();
    private static final RedisURI REDIS_URI = RedisURI.create(URI);
    private static final List<RedisURI> REDIS_URIS = LettuceLists.newList(REDIS_URI);

    @Test
    void withStringUri() {
    }

    @Test
    void withStringUriNull() {
    }

    @Test
    void withUri() {
    }

    @Test
    void withUriUri() {
    }

    @Test
    void withUriIterable() {
    }

    @Test
    void withUriIterableNull() {
    }

    @Test
    void clientResourcesWithStringUri() {
    }

    @Test
    void clientResourcesWithStringUriNull() {
    }

    @Test
    void clientResourcesNullWithStringUri() {
    }

    @Test
    void clientResourcesWithUri() {
    }

    @Test
    void clientResourcesWithUriNull() {
    }

    @Test
    void clientResourcesWithUriUri() {
    }

    @Test
    void clientResourcesWithUriIterable() {
    }

    @Test
    void clientResourcesWithUriIterableNull() {
    }

    @Test
    void clientResourcesNullWithUriIterable() {
    }

    @Test
    void clientWithDifferentSslSettings() {
    }

    @Test
    void clientWithDifferentTlsSettings() {
    }

    @Test
    void clientWithDifferentVerifyPeerSettings() {
    }
}
