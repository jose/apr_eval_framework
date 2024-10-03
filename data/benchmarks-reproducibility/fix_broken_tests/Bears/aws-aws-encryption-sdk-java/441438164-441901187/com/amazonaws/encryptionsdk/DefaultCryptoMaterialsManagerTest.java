package com.amazonaws.encryptionsdk;

import static com.amazonaws.encryptionsdk.multi.MultipleProviderFactory.buildMultiProvider;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.junit.Test;

import com.amazonaws.encryptionsdk.exception.AwsCryptoException;
import com.amazonaws.encryptionsdk.exception.CannotUnwrapDataKeyException;
import com.amazonaws.encryptionsdk.exception.NoSuchMasterKeyException;
import com.amazonaws.encryptionsdk.exception.UnsupportedProviderException;
import com.amazonaws.encryptionsdk.internal.Constants;
import com.amazonaws.encryptionsdk.internal.StaticMasterKey;
import com.amazonaws.encryptionsdk.internal.TrailingSignatureAlgorithm;
import com.amazonaws.encryptionsdk.model.DecryptionMaterialsRequest;
import com.amazonaws.encryptionsdk.model.DecryptionMaterials;
import com.amazonaws.encryptionsdk.model.EncryptionMaterials;
import com.amazonaws.encryptionsdk.model.EncryptionMaterialsRequest;

public class DefaultCryptoMaterialsManagerTest {
    private static final MasterKey<?> mk1 = new StaticMasterKey("mk1");
    private static final MasterKey<?> mk2 = new StaticMasterKey("mk2");

    @Test
    public void encrypt_testBasicFunctionality() throws Exception {
    }

    @Test
    public void encrypt_noSignatureKeyOnUnsignedAlgo() throws Exception {
    }

    @Test
    public void encrypt_hasSignatureKeyForSignedAlgo() throws Exception {
    }

    @Test
    public void encrypt_dispatchesMultipleMasterKeys() throws Exception {
    }

    @Test
    public void encrypt_forwardsPlaintextWhenAvailable() throws Exception {
    }

    @Test
    public void encrypt_forwardsPlaintextSizeWhenAvailable() throws Exception {
    }

    @Test
    public void encrypt_setsStreamingWhenNoSizeAvailable() throws Exception {
    }

    @Test //(expected = IllegalArgumentException.class)
    public void encrypt_whenECContextKeyPresent_throws() throws Exception {
    }

    @Test //(expected = IllegalArgumentException.class)
    public void encrypt_whenNoMasterKeys_throws() throws Exception {
    }

    private EncryptionMaterials easyGenMaterials(Consumer<EncryptionMaterialsRequest.Builder> customizer) {
        EncryptionMaterialsRequest.Builder request = EncryptionMaterialsRequest.newBuilder();

        customizer.accept(request);

        return new DefaultCryptoMaterialsManager(mk1).getMaterialsForEncrypt(request.build());
    }

    private DecryptionMaterialsRequest decryptReqFromMaterials(EncryptionMaterials result) {
        return DecryptionMaterialsRequest.newBuilder()
                                         .setEncryptionContext(result.getEncryptionContext())
                                         .setEncryptedDataKeys(result.getEncryptedDataKeys())
                                         .setAlgorithm(result.getAlgorithm())
                                         .build();
    }

    @Test
    public void decrypt_testSimpleRoundTrip() throws Exception {
    }

    @Test //(expected = CannotUnwrapDataKeyException.class)
    public void decrypt_onDecryptFailure() throws Exception {
    }

    @Test
    public void decrypt_whenTrailingSigMissing_throwsException() throws Exception {
    }
}
