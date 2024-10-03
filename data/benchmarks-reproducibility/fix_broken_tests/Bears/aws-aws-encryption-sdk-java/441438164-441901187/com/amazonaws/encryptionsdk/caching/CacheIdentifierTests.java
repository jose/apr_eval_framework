package com.amazonaws.encryptionsdk.caching;

import static com.amazonaws.encryptionsdk.CryptoAlgorithm.ALG_AES_128_GCM_IV12_TAG16_HKDF_SHA256;
import static com.amazonaws.encryptionsdk.CryptoAlgorithm.ALG_AES_256_GCM_IV12_TAG16_HKDF_SHA384_ECDSA_P384;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import com.amazonaws.encryptionsdk.CryptoAlgorithm;
import com.amazonaws.encryptionsdk.CryptoMaterialsManager;
import com.amazonaws.encryptionsdk.model.DecryptionMaterialsRequest;
import com.amazonaws.encryptionsdk.model.EncryptionMaterialsRequest;
import com.amazonaws.encryptionsdk.model.KeyBlob;

public class CacheIdentifierTests {

    static String partitionName = "c15b9079-6d0e-42b6-8784-5e804b025692";
    static Map<String, String> contextEmpty = Collections.emptyMap();
    static Map<String, String> contextFull;
    static {
        contextFull = new HashMap<>();
        contextFull.put("this", "is");
        contextFull.put("a", "non-empty");
        contextFull.put("encryption", "context");
    }

    CachingCryptoMaterialsManager cmm;

    static List<KeyBlob> keyBlobs = Arrays.asList(
            new KeyBlob("this is a provider ID", "this is some key info".getBytes(UTF_8),
                        "super secret key, now with encryption!".getBytes(UTF_8)
                        ),
            new KeyBlob("another provider ID!", "this is some different key info".getBytes(UTF_8),
                        "better super secret key, now with encryption!".getBytes(UTF_8)
            )
    );

    @Test
    public void pythonTestVecs() throws Exception {
    }

    void assertDecryptId(String partitionName, CryptoAlgorithm algo, List<KeyBlob> blobs, Map<String, String> context, String expect) throws Exception {
        DecryptionMaterialsRequest request =
                DecryptionMaterialsRequest.newBuilder()
                                          .setAlgorithm(algo)
                                          .setEncryptionContext(context)
                                          .setEncryptedDataKeys(blobs)
                                          .build();

        byte[] id = getCacheIdentifier(getCMM(partitionName), request);

        assertEquals(expect, Base64.getEncoder().encodeToString(id));
    }

    void assertEncryptId(String partitionName, CryptoAlgorithm algo, Map<String, String> context, String expect) throws Exception {
        EncryptionMaterialsRequest request = EncryptionMaterialsRequest.newBuilder()
                                                                       .setContext(context)
                                                                       .setRequestedAlgorithm(algo)
                                                                       .build();

        byte[] id = getCacheIdentifier(getCMM(partitionName), request);

        assertEquals(expect, Base64.getEncoder().encodeToString(id));
    }

    @Test
    public void encryptDigestTestVector() throws Exception {
    }

    @Test
    public void decryptDigestTestVector() throws Exception {
    }

    private byte[] getCacheIdentifier(CachingCryptoMaterialsManager cmm, EncryptionMaterialsRequest request) throws Exception {
        Method m = CachingCryptoMaterialsManager.class.getDeclaredMethod("getCacheIdentifier", EncryptionMaterialsRequest.class);
        m.setAccessible(true);

        return (byte[])m.invoke(cmm, request);
    }

    private byte[] getCacheIdentifier(CachingCryptoMaterialsManager cmm, DecryptionMaterialsRequest request) throws Exception {
        Method m = CachingCryptoMaterialsManager.class.getDeclaredMethod("getCacheIdentifier", DecryptionMaterialsRequest.class);
        m.setAccessible(true);

        return (byte[])m.invoke(cmm, request);
    }

    private CachingCryptoMaterialsManager getCMM(final String partitionName) {
        return CachingCryptoMaterialsManager.newBuilder()
                                            .withCache(mock(CryptoMaterialsCache.class))
                                            .withBackingMaterialsManager(mock(CryptoMaterialsManager.class))
                                            .withMaxAge(1, TimeUnit.MILLISECONDS)
                                            .withPartitionId(partitionName)
                                            .build();
    }
}
