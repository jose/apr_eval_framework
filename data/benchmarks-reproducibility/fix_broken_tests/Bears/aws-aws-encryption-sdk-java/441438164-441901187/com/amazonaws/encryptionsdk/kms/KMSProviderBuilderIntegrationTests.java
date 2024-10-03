package com.amazonaws.encryptionsdk.kms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.amazonaws.AbortedException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Request;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoAlgorithm;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.MasterKeyProvider;
import com.amazonaws.encryptionsdk.exception.CannotUnwrapDataKeyException;
import com.amazonaws.encryptionsdk.internal.VersionInfo;
import com.amazonaws.encryptionsdk.model.KeyBlob;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.http.exception.HttpRequestTimeoutException;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;

public class KMSProviderBuilderIntegrationTests {
    @Test
    public void whenBogusRegionsDecrypted_doesNotLeakClients() throws Exception {
    }

    @Test
    public void whenOperationSuccessful_clientIsCached() {
    }

    @Test
    public void whenConstructedWithoutArguments_canUseMultipleRegions() throws Exception {
    }

    @SuppressWarnings("deprecation") @Test //(expected = CannotUnwrapDataKeyException.class)
    public void whenLegacyConstructorsUsed_multiRegionDecryptIsNotSupported() throws Exception {
    }

    @Test
    public void whenHandlerConfigured_handlerIsInvoked() throws Exception {
    }

    @Test
    public void whenShortTimeoutSet_timesOut() throws Exception {
    }

    @Test
    public void whenCustomCredentialsSet_theyAreUsed() throws Exception {
    }

    @Test
    public void whenBuilderCloned_credentialsAndConfigurationAreRetained() throws Exception {
    }

    @Test
    public void whenBuilderCloned_clientBuilderCustomizationIsRetained() throws Exception {
    }

    @Test //(expected = IllegalArgumentException.class)
    public void whenBogusEndpointIsSet_constructionFails() throws Exception {
    }

    @Test
    public void whenUserAgentsOverridden_originalUAsPreserved() throws Exception {
    }

    @Test
    public void whenDefaultRegionSet_itIsUsedForBareKeyIds() throws Exception {
    }
}

