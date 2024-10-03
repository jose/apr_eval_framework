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

package com.amazonaws.encryptionsdk;

import static com.amazonaws.encryptionsdk.FastTestsOnlySuite.isFastTestSuiteActive;
import static com.amazonaws.encryptionsdk.TestUtils.assertThrows;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.encryptionsdk.caching.CachingCryptoMaterialsManager;
import com.amazonaws.encryptionsdk.caching.LocalCryptoMaterialsCache;
import com.amazonaws.encryptionsdk.exception.AwsCryptoException;
import com.amazonaws.encryptionsdk.exception.BadCiphertextException;
import com.amazonaws.encryptionsdk.internal.StaticMasterKey;
import com.amazonaws.encryptionsdk.internal.TestIOUtils;
import com.amazonaws.encryptionsdk.model.CiphertextType;
import com.amazonaws.encryptionsdk.model.DecryptionMaterials;
import com.amazonaws.encryptionsdk.model.DecryptionMaterialsRequest;
import com.amazonaws.encryptionsdk.model.EncryptionMaterials;
import com.amazonaws.encryptionsdk.model.EncryptionMaterialsRequest;

public class AwsCryptoTest {
    private StaticMasterKey masterKeyProvider;
    private AwsCrypto encryptionClient_;

    @Before
    public void init() {
        masterKeyProvider = spy(new StaticMasterKey("testmaterial"));

        encryptionClient_ = new AwsCrypto();
        encryptionClient_.setEncryptionAlgorithm(CryptoAlgorithm.ALG_AES_128_GCM_IV12_TAG16_HKDF_SHA256);
    }

    private void doEncryptDecrypt(final CryptoAlgorithm cryptoAlg, final int byteSize, final int frameSize) {
        final byte[] plaintextBytes = new byte[byteSize];

        final Map<String, String> encryptionContext = new HashMap<String, String>(1);
        encryptionContext.put("ENC1", "Encrypt-decrypt test with %d" + byteSize);

        encryptionClient_.setEncryptionAlgorithm(cryptoAlg);
        encryptionClient_.setEncryptionFrameSize(frameSize);

        final byte[] cipherText = encryptionClient_.encryptData(
                masterKeyProvider,
                plaintextBytes,
                encryptionContext).getResult();
        final byte[] decryptedText = encryptionClient_.decryptData(
                masterKeyProvider,
                cipherText
                ).getResult();

        assertArrayEquals("Bad encrypt/decrypt for " + cryptoAlg, plaintextBytes, decryptedText);
    }

    private void doTamperedEncryptDecrypt(final CryptoAlgorithm cryptoAlg, final int byteSize, final int frameSize) {
        final byte[] plaintextBytes = new byte[byteSize];

        final Map<String, String> encryptionContext = new HashMap<String, String>(1);
        encryptionContext.put("ENC1", "Encrypt-decrypt test with %d" + byteSize);

        encryptionClient_.setEncryptionAlgorithm(cryptoAlg);
        encryptionClient_.setEncryptionFrameSize(frameSize);

        final byte[] cipherText = encryptionClient_.encryptData(
                masterKeyProvider,
                plaintextBytes,
                encryptionContext).getResult();
        cipherText[cipherText.length - 2] ^= (byte) 0xff;
        try {
            encryptionClient_.decryptData(
                    masterKeyProvider,
                    cipherText
                    ).getResult();
            Assert.fail("Expected BadCiphertextException");
        } catch (final BadCiphertextException ex) {
            // Expected exception
        }
    }

    private void doEncryptDecryptWithParsedCiphertext(final int byteSize, final int frameSize) {
        final byte[] plaintextBytes = new byte[byteSize];

        final Map<String, String> encryptionContext = new HashMap<String, String>(1);
        encryptionContext.put("ENC1", "Encrypt-decrypt test with %d" + byteSize);

        encryptionClient_.setEncryptionFrameSize(frameSize);

        final byte[] cipherText = encryptionClient_.encryptData(
                masterKeyProvider,
                plaintextBytes,
                encryptionContext).getResult();
        ParsedCiphertext pCt = new ParsedCiphertext(cipherText);
        assertEquals(encryptionClient_.getEncryptionAlgorithm(), pCt.getCryptoAlgoId());
        assertEquals(CiphertextType.CUSTOMER_AUTHENTICATED_ENCRYPTED_DATA, pCt.getType());
        assertEquals(1, pCt.getEncryptedKeyBlobCount());
        assertEquals(pCt.getEncryptedKeyBlobCount(), pCt.getEncryptedKeyBlobs().size());
        assertEquals(masterKeyProvider.getProviderId(), pCt.getEncryptedKeyBlobs().get(0).getProviderId());
        for (Map.Entry<String, String> e : encryptionContext.entrySet()) {
            assertEquals(e.getValue(), pCt.getEncryptionContextMap().get(e.getKey()));
        }

        final byte[] decryptedText = encryptionClient_.decryptData(
                masterKeyProvider,
                pCt
                ).getResult();

        assertArrayEquals(plaintextBytes, decryptedText);
    }

    @Test
    public void encryptDecrypt() {
    }

    @Test
    public void encryptDecryptWithBadSignature() {
    }

    @Test
    public void encryptDecryptWithParsedCiphertext() {
    }

    @Test
    public void encryptDecryptWithCustomManager() throws Exception {
    }

    @Test
    public void whenCustomCMMIgnoresAlgorithm_throws() throws Exception {
    }

    @Test
    public void whenDecrypting_invokesMKPOnce() throws Exception {
    }

    private void doEstimateCiphertextSize(final CryptoAlgorithm cryptoAlg, final int inLen, final int frameSize) {
        final byte[] plaintext = TestIOUtils.generateRandomPlaintext(inLen);

        final Map<String, String> encryptionContext = new HashMap<String, String>(1);
        encryptionContext.put("ENC1", "Ciphertext size estimation test with " + inLen);

        encryptionClient_.setEncryptionAlgorithm(cryptoAlg);
        encryptionClient_.setEncryptionFrameSize(frameSize);

        final long estimatedCiphertextSize = encryptionClient_.estimateCiphertextSize(
                masterKeyProvider,
                inLen,
                encryptionContext);
        final byte[] cipherText = encryptionClient_.encryptData(masterKeyProvider, plaintext,
                encryptionContext).getResult();

        // The estimate should be close (within 16 bytes) and never less than reality
        final String errMsg = "Bad estimation for " + cryptoAlg + " expected: <" + estimatedCiphertextSize
                + "> but was: <" + cipherText.length + ">";
        assertTrue(errMsg, estimatedCiphertextSize - cipherText.length >= 0);
        assertTrue(errMsg, estimatedCiphertextSize - cipherText.length <= 16);
    }

    @Test
    public void estimateCiphertextSize() {
    }

    @Test
    public void estimateCiphertextSizeWithoutEncContext() {
    }

    @Test
    public void estimateCiphertextSize_usesCachedKeys() throws Exception {
    }

    @Test
    public void encryptDecryptWithoutEncContext() {
    }

    @Test
    public void encryptDecryptString() {
    }

    @Test
    public void encryptDecryptStringWithoutEncContext() {
    }

    @Test
    public void encryptBytesDecryptString() {
    }

    @Test
    public void encryptStringDecryptBytes() {
    }

    @Test
    public void emptyEncryptionContext() {
    }

    // Test that all the parameters that aren't allowed to be null (i.e. all of them) result in immediate NPEs if
    // invoked with null args
    @Test
    public void assertNullChecks() throws Exception {
    }

    @Test
    public void setValidFrameSize() throws IOException {
    }

    @Test //(expected = IllegalArgumentException.class)
    public void setNegativeFrameSize() throws IOException {
    }

    @Test
    public void setCryptoAlgorithm() throws IOException {
    }

}
