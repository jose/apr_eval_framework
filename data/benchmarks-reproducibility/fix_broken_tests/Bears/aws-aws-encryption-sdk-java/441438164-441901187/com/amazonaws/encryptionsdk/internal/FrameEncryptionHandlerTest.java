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

package com.amazonaws.encryptionsdk.internal;

import static com.amazonaws.encryptionsdk.TestUtils.assertThrows;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.lang.reflect.Field;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoAlgorithm;
import com.amazonaws.encryptionsdk.model.CipherFrameHeaders;

public class FrameEncryptionHandlerTest {
    private final CryptoAlgorithm cryptoAlgorithm_ = AwsCrypto.getDefaultCryptoAlgorithm();
    private final byte[] messageId_ = RandomBytesGenerator.generate(Constants.MESSAGE_ID_LEN);
    private final byte nonceLen_ = cryptoAlgorithm_.getNonceLen();
    private final byte[] dataKeyBytes_ = RandomBytesGenerator.generate(cryptoAlgorithm_.getKeyLength());
    private final SecretKey encryptionKey_ = new SecretKeySpec(dataKeyBytes_, "AES");
    private final int frameSize_ = AwsCrypto.getDefaultFrameSize();

    private FrameEncryptionHandler frameEncryptionHandler_;

    @Before
    public void setUp() throws Exception {
        frameEncryptionHandler_ = new FrameEncryptionHandler(
                encryptionKey_,
                nonceLen_,
                cryptoAlgorithm_,
                messageId_,
                frameSize_
        );
    }

    @Test
    public void emptyOutBytes() {
    }

    @Test
    public void correctIVsGenerated() throws Exception {
    }

    @Test
    public void encryptionHandlerEnforcesFrameLimits() throws Exception {
    }

    private void assertHeaderNonce(byte[] expectedNonce, byte[] buf) {
        CipherFrameHeaders headers = new CipherFrameHeaders();
        headers.setNonceLength(cryptoAlgorithm_.getNonceLen());
        headers.deserialize(buf, 0);

        assertArrayEquals(
                expectedNonce,
                headers.getNonce()
        );
    }

    private void generateTestBlock(byte[] buf) {
        frameEncryptionHandler_.processBytes(
                new byte[frameSize_], 0, frameSize_, buf, 0
        );
    }
}
