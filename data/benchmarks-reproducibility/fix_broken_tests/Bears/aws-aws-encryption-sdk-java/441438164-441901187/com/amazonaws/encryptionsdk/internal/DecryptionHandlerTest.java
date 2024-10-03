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

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoAlgorithm;
import com.amazonaws.encryptionsdk.DefaultCryptoMaterialsManager;
import com.amazonaws.encryptionsdk.MasterKey;
import com.amazonaws.encryptionsdk.exception.AwsCryptoException;
import com.amazonaws.encryptionsdk.exception.BadCiphertextException;
import com.amazonaws.encryptionsdk.model.CiphertextType;
import com.amazonaws.encryptionsdk.model.EncryptionMaterialsRequest;
import com.amazonaws.encryptionsdk.model.EncryptionMaterials;

public class DecryptionHandlerTest {
    private StaticMasterKey masterKeyProvider_;

    @Before
    public void init() {
        masterKeyProvider_ = new StaticMasterKey("testmaterial");
    }

    @Test //(expected = NullPointerException.class)
    public void nullMasterKey() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void invalidLenProcessBytes() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void maxLenProcessBytes() {
    }

    @Test //(expected = BadCiphertextException.class)
    public void headerIntegrityFailure() {
    }

    @Test //(expected = BadCiphertextException.class)
    public void invalidVersion() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void invalidCMK() {
    }

    private byte[] getTestHeaders() {
        final CryptoAlgorithm cryptoAlgorithm_ = AwsCrypto.getDefaultCryptoAlgorithm();
        final int frameSize_ = AwsCrypto.getDefaultFrameSize();
        final Map<String, String> encryptionContext = Collections.<String, String> emptyMap();

        final EncryptionMaterialsRequest encryptionMaterialsRequest = EncryptionMaterialsRequest.newBuilder()
                                                                                                .setContext(encryptionContext)
                                                                                                .setRequestedAlgorithm(cryptoAlgorithm_)
                                                                                                .build();

        final EncryptionMaterials encryptionMaterials = new DefaultCryptoMaterialsManager(masterKeyProvider_)
                .getMaterialsForEncrypt(encryptionMaterialsRequest);

        final EncryptionHandler encryptionHandler = new EncryptionHandler(frameSize_, encryptionMaterials);

        // create the ciphertext headers by calling encryption handler.
        final byte[] in = new byte[0];
        final int ciphertextLen = encryptionHandler.estimateOutputSize(in.length);
        final byte[] ciphertext = new byte[ciphertextLen];
        encryptionHandler.processBytes(in, 0, in.length, ciphertext, 0);
        return ciphertext;
    }

    @Test //(expected = AwsCryptoException.class)
    public void invalidOffsetProcessBytes() {
    }
}
