package com.amazonaws.encryptionsdk.multi;

import static com.amazonaws.encryptionsdk.internal.RandomBytesGenerator.generate;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.MasterKey;
import com.amazonaws.encryptionsdk.MasterKeyProvider;
import com.amazonaws.encryptionsdk.internal.StaticMasterKey;
import com.amazonaws.encryptionsdk.jce.JceMasterKey;

public class MultipleMasterKeyTest {
    private static final String WRAPPING_ALG = "AES/GCM/NoPadding";
    private static final byte[] PLAINTEXT = generate(1024);
    
    @Test
    public void testMultipleJceKeys() {
    }

    @Test
    public void testMultipleJceKeysSingleDecrypt() {
    }
    
    @Test
    public void testMixedKeys() {
    }
    
    @Test
    public void testMixedKeysSingleDecrypt() {
    }
    
    private void assertMultiReturnsKeys(MasterKeyProvider<?> mkp, MasterKey<?>... mks) {
        for (MasterKey<?> mk : mks) {
            assertEquals(mk, mkp.getMasterKey(mk.getKeyId()));
            assertEquals(mk, mkp.getMasterKey(mk.getProviderId(), mk.getKeyId()));
        }
    }
}
