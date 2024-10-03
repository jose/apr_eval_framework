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
package io.lettuce.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * @author Mark Paluch
 */
class HostAndPortUnitTests {

    @Test
    void testFromStringWellFormed() {
    }

    @Test
    void testFromStringBadDefaultPort() {
    }

    @Test
    void testFromStringUnusedDefaultPort() {
    }

    @Test
    void testFromStringBadPort() {
    }

    @Test
    void testFromStringUnparseableNonsense() {
    }

    @Test
    void testFromStringParseableNonsense() {
    }

    @Test
    void shouldCreateHostAndPortFromParts() {
    }

    @Test
    void shouldCompare() {
    }

    @Test
    void shouldApplyCompatibilityParsing() {
    }

    private static void checkFromStringCase(String hpString, int defaultPort, String expectHost, int expectPort,
            boolean expectHasExplicitPort) {
        HostAndPort hp;
        try {
            hp = HostAndPort.parse(hpString);
        } catch (IllegalArgumentException e) {
            // Make sure we expected this.
            assertThat(expectHost).isNull();
            return;
        }
        assertThat(expectHost).isNotNull();

        // Apply withDefaultPort(), yielding hp2.
        final boolean badDefaultPort = (defaultPort < 0 || defaultPort > 65535);

        // Check the pre-withDefaultPort() instance.
        if (expectHasExplicitPort) {
            assertThat(hp.hasPort()).isTrue();
            assertThat(hp.getPort()).isEqualTo(expectPort);
        } else {
            assertThat(hp.hasPort()).isFalse();
            try {
                hp.getPort();
                fail("Expected IllegalStateException");
            } catch (IllegalStateException expected) {
            }
        }
        assertThat(hp.getHostText()).isEqualTo(expectHost);
    }

    private static void checkFromCompatCase(String hpString, String expectHost, int expectPort) {

        HostAndPort hostAndPort = HostAndPort.parseCompat(hpString);
        assertThat(hostAndPort.getHostText()).isEqualTo(expectHost);
        assertThat(hostAndPort.getPort()).isEqualTo(expectPort);

    }
}
