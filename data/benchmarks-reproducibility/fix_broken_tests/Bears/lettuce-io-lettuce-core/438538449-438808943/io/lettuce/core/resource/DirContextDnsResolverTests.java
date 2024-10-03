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
package io.lettuce.core.resource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Mark Paluch
 */

class DirContextDnsResolverTests {

    private DirContextDnsResolver resolver;

    @BeforeEach
    void before() {

        System.getProperties().remove(DirContextDnsResolver.PREFER_IPV4_KEY);
        System.getProperties().remove(DirContextDnsResolver.PREFER_IPV6_KEY);
    }

    @AfterEach
    void tearDown() throws Exception {

        if (resolver != null) {
            resolver.close();
        }
    }

    @Test
    @Disabled("Requires guarding against IPv6 absence")
    void shouldResolveDefault() throws Exception {
    }

    @Test
    void shouldResolvePreferIpv4WithProperties() throws Exception {
    }

    @Test
    @Disabled("Requires guarding against IPv6 absence")
    void shouldResolveWithDnsServer() throws Exception {
    }

    @Test
    void shouldPreferIpv4() throws Exception {
    }

    @Test
    void shouldPreferIpv4AndNotIpv6() throws Exception {
    }

    @Test
    @Disabled("Requires guarding against IPv6 absence")
    void shouldPreferIpv6AndNotIpv4() throws Exception {
    }

    @Test
    void shouldFailWithUnknownHost() {
    }

    @Test
    void shouldResolveCname() throws Exception {
    }

    @Test
    void shouldResolveWithoutSubdomain() throws Exception {
    }

    @Test
    void shouldWorkWithIpv4Address() throws Exception {
    }

    @Test
    void shouldWorkWithIpv6Addresses() throws Exception {
    }
}
