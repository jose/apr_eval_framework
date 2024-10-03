/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except
 * in compliance with the License. A copy of the License is located at
 * 
 * http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.amazonaws.encryptionsdk.kms;

import static com.amazonaws.encryptionsdk.CryptoAlgorithm.ALG_AES_128_GCM_IV12_TAG16_NO_KDF;
import static com.amazonaws.encryptionsdk.internal.RandomBytesGenerator.generate;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.MasterKey;
import com.amazonaws.encryptionsdk.MasterKeyProvider;
import com.amazonaws.encryptionsdk.MasterKeyRequest;
import com.amazonaws.encryptionsdk.jce.JceMasterKey;
import com.amazonaws.encryptionsdk.multi.MultipleProviderFactory;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;

public class LegacyKMSMasterKeyProviderTests {
    private static final String WRAPPING_ALG = "AES/GCM/NoPadding";
    private static final byte[] PLAINTEXT = generate(1024);

    @Test
    public void testExplicitCredentials() throws Exception {
    }

    @Test
    public void testNoKeyMKP() throws Exception {
    }

    @Test
    public void testMultipleKmsKeys() {
    }

    @Test
    public void testMultipleKmsKeysSingleDecrypt() {
    }

    @Test
    public void testMultipleRegionKmsKeys() {
    }


    @Test
    public void testMixedKeys() {
    }

    @Test
    public void testMixedKeysSingleDecrypt() {
    }

    private KmsMasterKeyProvider legacyConstruct(final AWSKMS client, String... keyIds) {
        return legacyConstruct(client, Region.getRegion(Regions.DEFAULT_REGION), keyIds);
    }

    private KmsMasterKeyProvider legacyConstruct(final AWSKMS client, final Region region, String... keyIds) {
        return new KmsMasterKeyProvider(client, region, Arrays.asList(keyIds));
    }

    private void assertMultiReturnsKeys(MasterKeyProvider<?> mkp, MasterKey<?>... mks) {
        for (MasterKey<?> mk : mks) {
            assertEquals(mk, mkp.getMasterKey(mk.getKeyId()));
            assertEquals(mk, mkp.getMasterKey(mk.getProviderId(), mk.getKeyId()));
        }
    }

    private void assertExplicitCredentialsUsed(final MasterKeyProvider<KmsMasterKey> mkp) {
        try {
            MasterKeyRequest mkr = MasterKeyRequest.newBuilder()
                                                   .setEncryptionContext(Collections.emptyMap())
                                                   .setStreaming(true)
                                                   .build();
            mkp.getMasterKeysForEncryption(mkr)
               .forEach(mk -> mk.generateDataKey(ALG_AES_128_GCM_IV12_TAG16_NO_KDF, Collections.emptyMap()));

            fail("Expected exception");
        } catch (UsedExplicitCredentials e) {
            // ok
        }
    }

    private static class UsedExplicitCredentials extends RuntimeException {}

    private static class ThrowingCredentials implements AWSCredentials {
        @Override public String getAWSAccessKeyId() {
            throw new UsedExplicitCredentials();
        }

        @Override public String getAWSSecretKey() {
            throw new UsedExplicitCredentials();
        }
    }
}
