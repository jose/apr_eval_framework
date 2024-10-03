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
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.Test;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.exception.AwsCryptoException;
import com.amazonaws.encryptionsdk.exception.BadCiphertextException;
import com.amazonaws.encryptionsdk.internal.Constants;
import com.amazonaws.encryptionsdk.internal.RandomBytesGenerator;
import com.amazonaws.encryptionsdk.internal.TestIOUtils;

public class CipherFrameHeadersTest {
    final int nonceLen_ = AwsCrypto.getDefaultCryptoAlgorithm().getNonceLen();
    final int testSeqNum_ = 1;
    final int testFrameContentLen_ = 4096;
    byte[] nonce_ = RandomBytesGenerator.generate(nonceLen_);

    @Test
    public void serializeDeserializeTest() {
    }

    @Test
    public void serializeDeserializeStreamingTest() {
    }

    private byte[] createHeaderBytes(final boolean includeContentLen, final boolean isFinalFrame) {
        final CipherFrameHeaders cipherFrameHeaders = new CipherFrameHeaders(
                testSeqNum_,
                nonce_,
                testFrameContentLen_,
                isFinalFrame);
        cipherFrameHeaders.includeFrameSize(includeContentLen);

        return cipherFrameHeaders.toByteArray();
    }

    private CipherFrameHeaders deserialize(final boolean parseContentLen, final byte[] headerBytes) {
        final CipherFrameHeaders reconstructedHeaders = new CipherFrameHeaders();
        reconstructedHeaders.setNonceLength((short) nonceLen_);
        reconstructedHeaders.includeFrameSize(parseContentLen);
        reconstructedHeaders.deserialize(headerBytes, 0);

        return reconstructedHeaders;
    }

    private boolean serializeDeserialize(final boolean includeContentLen, final boolean isFinalFrame) {
        final byte[] headerBytes = createHeaderBytes(includeContentLen, isFinalFrame);
        final CipherFrameHeaders reconstructedHeaders = deserialize(includeContentLen, headerBytes);

        final byte[] reconstructedHeaderBytes = reconstructedHeaders.toByteArray();

        return Arrays.equals(headerBytes, reconstructedHeaderBytes) ? true : false;
    }

    private boolean serializeDeserializeStreaming(final boolean includeContentLen, final boolean isFinalFrame) {
        final byte[] headerBytes = createHeaderBytes(includeContentLen, isFinalFrame);

        final CipherFrameHeaders reconstructedHeaders = new CipherFrameHeaders();
        reconstructedHeaders.setNonceLength((short) nonceLen_);
        reconstructedHeaders.includeFrameSize(includeContentLen);

        int totalParsedBytes = 0;
        int bytesToParseLen = 1;
        int bytesParsed;

        while (reconstructedHeaders.isComplete() == false) {
            final byte[] bytesToParse = new byte[bytesToParseLen];
            System.arraycopy(headerBytes, totalParsedBytes, bytesToParse, 0, bytesToParse.length);

            bytesParsed = reconstructedHeaders.deserialize(bytesToParse, 0);
            if (bytesParsed == 0) {
                bytesToParseLen++;
            } else {
                totalParsedBytes += bytesParsed;
                bytesToParseLen = 1;
            }
        }

        final byte[] reconstructedHeaderBytes = reconstructedHeaders.toByteArray();

        return Arrays.equals(headerBytes, reconstructedHeaderBytes) ? true : false;
    }

    @Test
    public void deserializeNull() {
    }

    @Test
    public void checkNullNonce() {
    }

    @Test
    public void checkNonce() {
    }

    @Test
    public void checkSeqNum() {
    }

    @Test
    public void checkFrameLen() {
    }

    @Test //(expected = BadCiphertextException.class)
    public void invalidFrameLen() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void nullNonce() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void invalidNonce() {
    }

    @Test
    public void byteFormatCheck() {
    }

    @Test
    public void byteFormatCheckforFinalFrame() {
    }
}
