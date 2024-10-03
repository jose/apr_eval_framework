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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoAlgorithm;
import com.amazonaws.encryptionsdk.exception.AwsCryptoException;

public class BlockDecryptionHandlerTest {
    private static final SecureRandom RND = new SecureRandom();
    private final CryptoAlgorithm cryptoAlgorithm_ = AwsCrypto.getDefaultCryptoAlgorithm();
    private final byte[] messageId_ = new byte[Constants.MESSAGE_ID_LEN];
    private final byte nonceLen_ = cryptoAlgorithm_.getNonceLen();
    private final byte[] dataKeyBytes_ = new byte[cryptoAlgorithm_.getKeyLength()];
    private final SecretKey dataKey_ = new SecretKeySpec(dataKeyBytes_, "AES");

    private final BlockDecryptionHandler blockDecryptionHandler_ = new BlockDecryptionHandler(
            dataKey_,
            nonceLen_,
            cryptoAlgorithm_,
            messageId_);

    @Before
    public void setup() {
        RND.nextBytes(messageId_);
        RND.nextBytes(dataKeyBytes_);
    }

    @Test
    public void estimateOutputSize() {
    }

    @Test
    public void decryptWithoutHeaders() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void decryptMaxContentLength() {
    }
}
