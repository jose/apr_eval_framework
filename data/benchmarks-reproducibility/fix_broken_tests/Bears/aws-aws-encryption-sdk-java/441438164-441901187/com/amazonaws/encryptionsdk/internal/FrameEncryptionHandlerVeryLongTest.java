package com.amazonaws.encryptionsdk.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import javax.crypto.spec.SecretKeySpec;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.encoders.HexTranslator;
import org.junit.Test;

import com.amazonaws.encryptionsdk.CryptoAlgorithm;
import com.amazonaws.encryptionsdk.model.CipherFrameHeaders;

/*
 * This test exhaustively encrypts a 2^32 frame message, which takes approximately 2-3 hours on my hardware. Because of
 * this long test time, this test is not run as part of the normal suites.
 */
public class FrameEncryptionHandlerVeryLongTest {
    @Test
    public void exhaustiveIVCheck() throws Exception {
    }
}
