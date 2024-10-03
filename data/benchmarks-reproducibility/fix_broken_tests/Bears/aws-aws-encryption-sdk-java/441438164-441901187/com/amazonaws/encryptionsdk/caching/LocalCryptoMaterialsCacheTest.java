package com.amazonaws.encryptionsdk.caching;

import static com.amazonaws.encryptionsdk.TestUtils.assertThrows;
import static com.amazonaws.encryptionsdk.caching.CacheTestFixtures.createDecryptRequest;
import static com.amazonaws.encryptionsdk.caching.CacheTestFixtures.createMaterialsResult;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.encryptionsdk.caching.CryptoMaterialsCache.UsageStats;
import com.amazonaws.encryptionsdk.model.DecryptionMaterials;
import com.amazonaws.encryptionsdk.model.EncryptionMaterials;

public class LocalCryptoMaterialsCacheTest {
    public static final String PARTTION_NAME = "foo";
    FakeClock clock;
    LocalCryptoMaterialsCache cache;
    CryptoMaterialsCache.CacheHint hint = () -> 1000; // maxAge = 1000

    @Before
    public void setUp() throws Exception {
        clock = new FakeClock();
        cache = new LocalCryptoMaterialsCache(5);
        cache.clock = clock;
    }

    @Test
    public void whenNoEntriesInCache_noEntriesReturned() throws Exception {
    }

    @Test
    public void whenEntriesAddedToDecryptCache_correctEntriesReturned() throws Exception {
    }

    @Test
    public void whenManyDecryptEntriesAdded_LRURespected() throws Exception {
    }

    @Test
    public void whenEncryptEntriesAdded_theyCanBeRetrieved() throws Exception {
    }

    @Test
    public void whenInitialUsagePassed_itIsRetained() throws Exception {
    }

    @Test
    public void whenManyEncryptEntriesAdded_LRUIsRespected() throws Exception {
    }

    @Test
    public void whenManyEncryptEntriesAdded_andEntriesTouched_LRUIsRespected() throws Exception {
    }

    @Test
    public void whenManyEncryptEntriesAdded_andEntryInvalidated_LRUIsRespected() throws Exception {
    }

    @Test
    public void testCacheEntryBehavior() throws Exception {
    }

    @Test
    public void whenTTLExceeded_encryptEntriesAreEvicted() throws Exception {
    }

    @Test
    public void whenManyEntriesExpireAtOnce_expiredEncryptEntriesStillNotReturned() throws Exception {
    }

    @Test
    public void whenAccessed_encryptEntryTTLNotReset() throws Exception {
    }

    @Test
    public void whenTTLExceeded_decryptEntriesAreEvicted() throws Exception {
    }

    @Test
    public void whenAccessed_decryptEntryTTLNotReset() throws Exception {
    }

    @Test
    public void whenManyEntriesExpireAtOnce_expiredDecryptEntriesStillNotReturned() throws Exception {
    }

    @Test
    public void testDecryptInvalidate() throws Exception {
    }

    @Test
    public void testDecryptEntryCreationTime() throws Exception {
    }

    @Test
    public void whenIdentifiersDifferInLowOrderBytes_theyAreNotConsideredEquivalent() throws Exception {
    }

    @Test
    public void testUsageStatsCtorValidation() throws Exception {
    }

    private EncryptionMaterials createResult() {
        return CacheTestFixtures.createMaterialsResult(CacheTestFixtures.createMaterialsRequest(0));
    }

    private void assertEncryptEntry(byte[] cacheId, EncryptionMaterials expectedResult) {
        CryptoMaterialsCache.EncryptCacheEntry entry = cache.getEntryForEncrypt(cacheId, UsageStats.ZERO);
        EncryptionMaterials actual = entry == null ? null : entry.getResult();

        assertEquals(expectedResult, actual);
    }

    private Map getCacheMap(LocalCryptoMaterialsCache cache) throws Exception {
        Field field = LocalCryptoMaterialsCache.class.getDeclaredField("cacheMap");
        field.setAccessible(true);

        return (Map)field.get(cache);
    }

    private static final class FakeClock implements MsClock {
        long now = 0x1_0000_0000L;
        
        @Override public long timestamp() {
            return now;
        }
    }
}

