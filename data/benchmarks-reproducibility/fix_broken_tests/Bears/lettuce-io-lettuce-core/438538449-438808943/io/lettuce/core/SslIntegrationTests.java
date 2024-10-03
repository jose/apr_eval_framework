/*
 * Copyright 2018 the original author or authors.
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
package io.lettuce.core;

import static io.lettuce.test.settings.TestSettings.host;
import static io.lettuce.test.settings.TestSettings.sslPort;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.masterslave.MasterSlave;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import io.lettuce.test.*;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.ssl.OpenSsl;

/**
 * Tests using SSL via {@link RedisClient}.
 *
 * @author Mark Paluch
 * @author Adam McElwee
 */
@ExtendWith(LettuceExtension.class)
class SslIntegrationTests extends TestSupport {

    private static final String KEYSTORE = "work/keystore.jks";
    private static final String TRUSTSTORE = "work/truststore.jks";
    private static final File TRUSTSTORE_FILE = new File(TRUSTSTORE);
    private static final int MASTER_SLAVE_BASE_PORT_OFFSET = 2000;

    private static final RedisURI URI_NO_VERIFY = sslURIBuilder(0) //
            .withVerifyPeer(false) //
            .build();

    private static final RedisURI URI_VERIFY = sslURIBuilder(1) //
            .withVerifyPeer(true) //
            .build();

    private static final RedisURI URI_CLIENT_CERT_AUTH = sslURIBuilder(2) //
            .withVerifyPeer(true) //
            .build();

    private static final List<RedisURI> MASTER_SLAVE_URIS_NO_VERIFY = sslUris(IntStream.of(0, 1),
            builder -> builder.withVerifyPeer(false));

    private static final List<RedisURI> MASTER_SLAVE_URIS_VERIFY = sslUris(IntStream.of(0, 1),
            builder -> builder.withVerifyPeer(true));

    private static final List<RedisURI> MASTER_SLAVE_URIS_WITH_ONE_INVALID = sslUris(IntStream.of(0, 1, 2),
            builder -> builder.withVerifyPeer(true));

    private static final List<RedisURI> MASTER_SLAVE_URIS_WITH_ALL_INVALID = sslUris(IntStream.of(2, 3),
            builder -> builder.withVerifyPeer(true));

    private final RedisClient redisClient;

    @Inject
    SslIntegrationTests(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @BeforeAll
    static void beforeClass() {

        assumeTrue(CanConnect.to(host(), sslPort()), "Assume that stunnel runs on port 6443");
        assertThat(TRUSTSTORE_FILE).exists();
    }

    @Test
    void standaloneWithSsl() {
    }

    @Test
    void standaloneWithJdkSsl() {
    }

    @Test
    void standaloneWithJdkSslUsingTruststoreUrl() throws Exception {
    }

    @Test
    void standaloneWithClientCertificates() {
    }

    @Test
    void standaloneWithClientCertificatesWithoutKeystore() {
    }

    @Test
    void standaloneWithJdkSslUsingTruststoreUrlWithWrongPassword() throws Exception {
    }

    @Test
    void standaloneWithJdkSslFailsWithWrongTruststore() {
    }

    @Test
    void standaloneWithOpenSsl() {
    }

    @Test
    void standaloneWithOpenSslFailsWithWrongTruststore() {
    }

    @Test
    void pingBeforeActivate() {
    }

    @Test
    void regularSslWithReconnect() {
    }

    @Test
    void sslWithVerificationWillFail() {
    }

    @Test
    void masterSlaveWithSsl() {
    }

    @Test
    void masterSlaveWithJdkSsl() {
    }

    @Test
    void masterSlaveWithJdkSslUsingTruststoreUrl() throws Exception {
    }

    @Test
    void masterSlaveWithJdkSslUsingTruststoreUrlWithWrongPassword() throws Exception {
    }

    @Test
    void masterSlaveWithJdkSslFailsWithWrongTruststore() {
    }

    @Test
    void masterSlavePingBeforeActivate() {
    }

    @Test
    void masterSlaveSslWithReconnect() {
    }

    @Test
    void masterSlaveSslWithVerificationWillFail() {
    }

    @Test
    void masterSlaveSslWithOneInvalidHostWillSucceed() {
    }

    @Test
    void masterSlaveSslWithAllInvalidHostsWillFail() {
    }

    @Test
    void pubSubSsl() {
    }

    @Test
    void pubSubSslAndBreakConnection() {
    }

    private static RedisURI.Builder sslURIBuilder(int portOffset) {
        return RedisURI.Builder.redis(host(), sslPort(portOffset)).withSsl(true);
    }

    private static List<RedisURI> sslUris(IntStream masterSlaveOffsets,
            Function<RedisURI.Builder, RedisURI.Builder> builderCustomizer) {

        return masterSlaveOffsets.map(it -> it + MASTER_SLAVE_BASE_PORT_OFFSET)
                .mapToObj(offset -> RedisURI.Builder.redis(host(), sslPort(offset)).withSsl(true)).map(builderCustomizer)
                .map(RedisURI.Builder::build).collect(Collectors.toList());
    }

    private URL truststoreURL() throws MalformedURLException {
        return TRUSTSTORE_FILE.toURI().toURL();
    }

    private void setOptions(SslOptions sslOptions) {
        ClientOptions clientOptions = ClientOptions.builder().sslOptions(sslOptions).build();
        redisClient.setOptions(clientOptions);
    }

    private void verifyConnection(RedisURI redisUri) {

        try (StatefulRedisConnection<String, String> connection = redisClient.connect(redisUri)) {
            connection.sync().ping();
        }
    }

    private void verifyMasterSlaveConnection(List<RedisURI> redisUris) {

        try (StatefulRedisConnection<String, String> connection = MasterSlave.connect(redisClient, StringCodec.UTF8, redisUris)) {
            connection.sync().ping();
        }
    }
}
