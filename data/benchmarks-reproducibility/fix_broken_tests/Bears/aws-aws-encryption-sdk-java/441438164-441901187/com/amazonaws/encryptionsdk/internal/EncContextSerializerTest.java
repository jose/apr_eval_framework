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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.amazonaws.encryptionsdk.exception.AwsCryptoException;

public class EncContextSerializerTest {

    @Test
    public void nullContext() {
    }

    @Test
    public void emptyContext() {
    }

    @Test
    public void singletonContext() {
    }

    @Test
    public void contextOrdering() throws Exception {
    }

    @Test
    public void smallContext() {
    }

    @Test
    public void largeContext() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void overlyLargeContext() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void overlyLargeKey() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void overlyLargeValue() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void overlyLargeContextBytes() {
    }

    @Test //(expected = IllegalArgumentException.class)
    public void contextWithBadUnicodeKey() {
    }

    @Test //(expected = IllegalArgumentException.class)
    public void contextWithBadUnicodeValue() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithEmptyKey() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithEmptyValue() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithEmptyKeyAndValue() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithNullKey() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithNullValue() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithNullKeyAndValue() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithLargeKey() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithShortKey() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithNegativeKey() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithLargeValue() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithShortValue() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithNegativeValue() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithNegativeCount() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithZeroCount() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithInvalidCount() {
    }

    @Test //(expected = IllegalArgumentException.class)
    public void contextWithInvalidCharacters() {
    }

    @Test //(expected = AwsCryptoException.class)
    public void contextWithDuplicateEntries() {
    }

    private void testMap(final Map<String, String> ctx) {
        final byte[] ctxBytes = EncryptionContextSerializer.serialize(Collections.unmodifiableMap(ctx));
        final Map<String, String> result = EncryptionContextSerializer.deserialize(ctxBytes);
        assertEquals(ctx, result);
    }
}
