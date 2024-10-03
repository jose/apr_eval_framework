package com.amazonaws.encryptionsdk.caching;

import static com.amazonaws.encryptionsdk.TestUtils.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.amazonaws.encryptionsdk.CryptoAlgorithm;
import com.amazonaws.encryptionsdk.CryptoMaterialsManager;
import com.amazonaws.encryptionsdk.caching.CryptoMaterialsCache.EncryptCacheEntry;
import com.amazonaws.encryptionsdk.caching.CryptoMaterialsCache.UsageStats;
import com.amazonaws.encryptionsdk.jce.JceMasterKey;
import com.amazonaws.encryptionsdk.model.DecryptionMaterialsRequest;
import com.amazonaws.encryptionsdk.model.DecryptionMaterials;
import com.amazonaws.encryptionsdk.model.EncryptionMaterials;
import com.amazonaws.encryptionsdk.model.EncryptionMaterialsRequest;

public class CachingCryptoMaterialsManagerTest {
    private static final String PARTITION_ID = "partition ID";
    @Mock private CryptoMaterialsCache cache;
    @Mock private CryptoMaterialsManager delegate;
    private CachingCryptoMaterialsManager cmm;
    private CachingCryptoMaterialsManager.Builder builder;
    private long maxAgeMs = 123456789;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(cache.putEntryForEncrypt(any(), any(), any(), any())).thenAnswer(
            invocation -> entryFor((EncryptionMaterials)invocation.getArguments()[1], UsageStats.ZERO)
        );
        when(delegate.getMaterialsForEncrypt(any())).thenThrow(new RuntimeException("Unexpected invocation"));
        when(delegate.decryptMaterials(any())).thenThrow(new RuntimeException("Unexpected invocation"));

        builder = CachingCryptoMaterialsManager.newBuilder().withBackingMaterialsManager(delegate)
                                               .withCache(cache)
                                               .withPartitionId(PARTITION_ID)
                                               .withMaxAge(maxAgeMs, TimeUnit.MILLISECONDS)
                                               .withByteUseLimit(200)
                                               .withMessageUseLimit(100);
        cmm = builder.build();
    }

    @Test
    public void whenCacheIsEmpty_performsCacheMiss() throws Exception {
    }

    @Test
    public void whenCacheMisses_correctHintAndUsagePassed() throws Exception {
    }

    @Test
    public void whenCacheHasEntry_performsCacheHit() throws Exception {
    }

    @Test
    public void whenAlgorithmIsUncachable_resultNotStoredInCache() throws Exception {
    }

    @Test
    public void whenInitialUsageExceedsLimit_cacheIsBypassed() throws Exception {
    }

    @Test
    public void whenCacheEntryIsExhausted_byMessageLimit_performsCacheMiss() throws Exception {
    }

    @Test
    public void whenEncryptCacheEntryIsExpired_performsCacheMiss() throws Exception {
    }

    @Test
    public void whenCacheEntryIsExhausted_byByteLimit_performsCacheMiss() throws Exception {
    }

    @Test
    public void whenStreaming_cacheMiss_withNoSizeHint_doesNotCache() throws Exception {
    }

    @Test
    public void whenDecrypting_cacheMiss() throws Exception {
    }

    @Test
    public void whenDecryptCacheMisses_correctHintPassed() throws Exception {
    }

    @Test
    public void whenDecrypting_cacheHit() throws Exception {
    }

    @Test
    public void whenDecrypting_andEntryExpired_cacheMiss() throws Exception {
    }

    @Test
    public void testBuilderValidation() throws Exception {
    }

    @Test
    public void whenBuilderReused_uniquePartitionSet() throws Exception {
    }

    @Test
    public void whenMKPPassed_itIsUsed() throws Exception {
    }

    private EncryptCacheEntry setupForCacheMiss(EncryptionMaterialsRequest request, EncryptionMaterials result) throws Exception {
        doReturn(result).when(delegate).getMaterialsForEncrypt(request);
        EncryptCacheEntry entry = entryFor(result, UsageStats.ZERO);
        doReturn(entry).when(cache).putEntryForEncrypt(any(), eq(result), any(), any());

        return entry;
    }

    private EncryptCacheEntry entryFor(
            EncryptionMaterials result,
            final UsageStats initialUsage
    ) throws Exception {
        return spy(new TestEncryptCacheEntry(result, initialUsage));
    }

    private static class TestEncryptCacheEntry implements EncryptCacheEntry {
        private final EncryptionMaterials result;
        private final UsageStats stats;

        public TestEncryptCacheEntry(EncryptionMaterials result, UsageStats initialUsage) {
            this.result = result;
            stats = initialUsage;
        }

        @Override public UsageStats getUsageStats() {
            return stats;
        }

        @Override public long getEntryCreationTime() {
            return System.currentTimeMillis();
        }

        @Override public EncryptionMaterials getResult() {
            return result;
        }

        @Override public void invalidate() {

        }
    }

    private class TestDecryptCacheEntry implements CryptoMaterialsCache.DecryptCacheEntry{
        private final DecryptionMaterials result;
        private long creationTime = System.currentTimeMillis();

        public TestDecryptCacheEntry(final DecryptionMaterials result) {
            this.result = result;
        }

        @Override public DecryptionMaterials getResult() {
            return result;
        }

        @Override public void invalidate() {

        }

        @Override public long getEntryCreationTime() {
            return creationTime;
        }
    }
}

