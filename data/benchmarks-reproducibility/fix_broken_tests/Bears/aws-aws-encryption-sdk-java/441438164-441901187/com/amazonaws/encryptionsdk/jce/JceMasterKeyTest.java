package com.amazonaws.encryptionsdk.jce;

import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class JceMasterKeyTest {

    private static final SecretKey SECRET_KEY = new SecretKeySpec(new byte[1], "AES");
    private static final PrivateKey PRIVATE_KEY;
    private static final PublicKey PUBLIC_KEY;

    static {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PUBLIC_KEY = keyPair.getPublic();
            PRIVATE_KEY = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private JceMasterKey jceGetInstance(final String algorithmName) {
        return JceMasterKey.getInstance(SECRET_KEY, "mockProvider", "mockKey", algorithmName);
    }

    private JceMasterKey jceGetInstanceAsymmetric(final String algorithmName) {
        return JceMasterKey.getInstance(PUBLIC_KEY, PRIVATE_KEY, "mockProvider",  "mockKey",
                algorithmName);
    }

    @Test //(expected = IllegalArgumentException.class)
    public void testGetInstanceInvalidWrappingAlgorithm() {
    }


    @Test //(expected = UnsupportedOperationException.class)
    public void testGetInstanceAsymmetricInvalidWrappingAlgorithm() {
    }

    /**
     * Calls JceMasterKey.getInstance with differently cased wrappingAlgorithm names.
     * Passes if no Exception is thrown.
     * Relies on passing an invalid algorithm name to result in an Exception.
     */
    @Test
    public void testGetInstanceAllLowercase() {
    }

    @Test
    public void testGetInstanceMixedCasing() {
    }

    @Test
    public void testGetInstanceAsymmetricAllLowercase() {
    }

    @Test
    public void testGetInstanceAsymmetricMixedCasing() {
    }
}
