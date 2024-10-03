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

package com.amazonaws.encryptionsdk.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.encryptionsdk.CryptoAlgorithm;
import com.amazonaws.encryptionsdk.DataKey;
import com.amazonaws.encryptionsdk.exception.AwsCryptoException;
import com.amazonaws.encryptionsdk.internal.Constants;
import com.amazonaws.encryptionsdk.internal.RandomBytesGenerator;
import com.amazonaws.encryptionsdk.internal.StaticMasterKey;

public class KeyBlobTest {
    private static CryptoAlgorithm ALGORITHM = CryptoAlgorithm.ALG_AES_128_GCM_IV12_TAG16_NO_KDF;
    final String providerId_ = "Test Key";
    final String providerInfo_ = "Test Info";
    private StaticMasterKey masterKeyProvider_;

    @Before
    public void init() {
        masterKeyProvider_ = new StaticMasterKey("testmaterial");
    }

    private byte[] createKeyBlobBytes() {
        final Map<String, String> encryptionContext = new HashMap<String, String>(1);
        encryptionContext.put("ENC", "Test Encryption Context");
        final DataKey<StaticMasterKey> mockDataKey_ = masterKeyProvider_.generateDataKey(ALGORITHM, encryptionContext);

        final KeyBlob keyBlob = new KeyBlob(
                providerId_,
                providerInfo_.getBytes(StandardCharsets.UTF_8),
                mockDataKey_.getEncryptedDataKey());

        return keyBlob.toByteArray();
    }

    private KeyBlob deserialize(final byte[] keyBlobBytes) {
        final KeyBlob reconstructedKeyBlob = new KeyBlob();
        reconstructedKeyBlob.deserialize(keyBlobBytes, 0);
        return reconstructedKeyBlob;
    }

    @Test
    public void serializeDeserialize() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void overlyLargeKeyProviderIdLen() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void overlyLargeKeyProviderInfoLen() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void overlyLargeKey() {
    }

    @Test
    public void deserializeNull() {
    }

    @Test
    public void checkKeyProviderIdLen() {
    }

    @Test
    public void checkKeyProviderId() {
    }

    @Test
    public void checkKeyProviderInfoLen() {
    }

    @Test
    public void checkKeyProviderInfo() {
    }

    @Test
    public void checkKeyLen() {
    }

    private KeyBlob generateRandomKeyBlob(int idLen, int infoLen, int keyLen) {
        final byte[] idBytes = new byte[idLen];
        Arrays.fill(idBytes, (byte) 'A');

        final byte[] infoBytes = RandomBytesGenerator.generate(infoLen);
        final byte[] keyBytes = RandomBytesGenerator.generate(keyLen);

        return new KeyBlob(new String(idBytes, StandardCharsets.UTF_8), infoBytes, keyBytes);
    }

    private void assertKeyBlobsEqual(KeyBlob b1, KeyBlob b2) {
        assertArrayEquals(b1.getProviderId().getBytes(StandardCharsets.UTF_8),
                          b2.getProviderId().getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(b1.getProviderInformation(), b2.getProviderInformation());
        assertArrayEquals(b1.getEncryptedDataKey(), b2.getEncryptedDataKey());
    }
    
    @Test
    public void checkKeyProviderIdLenUnsigned() {
    }

    @Test
    public void checkKeyProviderInfoLenUnsigned() {
    }

    @Test
    public void checkKeyLenUnsigned() {
    }
}
