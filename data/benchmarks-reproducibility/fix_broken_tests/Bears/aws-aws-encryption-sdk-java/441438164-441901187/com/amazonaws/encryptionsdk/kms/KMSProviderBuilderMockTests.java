package com.amazonaws.encryptionsdk.kms;

import static com.amazonaws.encryptionsdk.multi.MultipleProviderFactory.buildMultiProvider;
import static com.amazonaws.regions.Region.getRegion;
import static com.amazonaws.regions.Regions.fromName;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.RequestClientOptions;
import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.MasterKeyProvider;
import com.amazonaws.encryptionsdk.internal.VersionInfo;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider.RegionalClientSupplier;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.model.CreateAliasRequest;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;

public class KMSProviderBuilderMockTests {
    @Test
    public void testBareAliasMapping() {
    }

    @Test
    public void testBareAliasMapping_withLegacyCtor() {
    }

    @Test
    public void testGrantTokenPassthrough_usingMKsetCall() throws Exception {
    }

    @Test
    public void testGrantTokenPassthrough_usingMKPWithers() throws Exception {
    }

    @Test
    public void testLegacyGrantTokenPassthrough() throws Exception {
    }

    @Test
    public void testUserAgentPassthrough() throws Exception {
    }

    private String getUA(AmazonWebServiceRequest request) {
        // Note: This test may break in future versions of the AWS SDK, as Marker is documented as being for internal
        // use only.
        return request.getRequestClientOptions().getClientMarker(RequestClientOptions.Marker.USER_AGENT);
    }
}
