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

package com.amazonaws.encryptionsdk.jce;

import static com.amazonaws.encryptionsdk.internal.RandomBytesGenerator.generate;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.MasterKeyProvider;
import com.amazonaws.encryptionsdk.exception.CannotUnwrapDataKeyException;
import com.amazonaws.encryptionsdk.multi.MultipleProviderFactory;

@SuppressWarnings("deprecation")
public class KeyStoreProviderTest {
    private static final SecureRandom RND = new SecureRandom();
    private static final KeyPairGenerator KG;
    private static final byte[] PLAINTEXT = generate(1024);
    private static final char[] PASSWORD = "Password".toCharArray();
    private static final KeyStore.PasswordProtection PP = new PasswordProtection(PASSWORD);
    private KeyStore ks;

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KG = KeyPairGenerator.getInstance("RSA", "BC");
            KG.initialize(2048);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Before
    public void setup() throws Exception {
        ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, PASSWORD);
    }

    @Test
    public void singleKeyPkcs1() throws GeneralSecurityException {
    }

    @Test
    public void singleKeyOaepSha1() throws GeneralSecurityException {
    }

    @Test
    public void singleKeyOaepSha256() throws GeneralSecurityException {
    }

    @Test
    public void multipleKeys() throws GeneralSecurityException {
    }

    @Test //(expected = CannotUnwrapDataKeyException.class)
    public void encryptOnly() throws GeneralSecurityException {
    }

    @Test
    public void escrowAndSymmetric() throws GeneralSecurityException {
    }

    @Test
    public void escrowAndSymmetricSecondProvider() throws GeneralSecurityException {
    }

    @Test
    public void escrowCase() throws GeneralSecurityException, IOException {
    }

    @Test
    public void keystoreAndRawProvider() throws GeneralSecurityException, IOException {
    }

    private void addEntry(final String alias) throws GeneralSecurityException {
        final KeyPair pair = KG.generateKeyPair();
        // build a certificate generator
        final X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        final X500Principal dnName = new X500Principal("cn=" + alias);

        certGen.setSerialNumber(new BigInteger(256, RND));
        certGen.setSubjectDN(new X509Name("dc=" + alias));
        certGen.setIssuerDN(dnName); // use the same
        certGen.setNotBefore(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000));
        certGen.setPublicKey(pair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WithRSA");
        final X509Certificate cert = certGen.generate(pair.getPrivate(), "BC");

        ks.setEntry(alias, new KeyStore.PrivateKeyEntry(pair.getPrivate(), new X509Certificate[] { cert }), PP);
    }

    private void addPublicEntry(final String alias) throws GeneralSecurityException {
        final KeyPair pair = KG.generateKeyPair();
        // build a certificate generator
        final X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        final X500Principal dnName = new X500Principal("cn=" + alias);

        certGen.setSerialNumber(new BigInteger(256, RND));
        certGen.setSubjectDN(new X509Name("dc=" + alias));
        certGen.setIssuerDN(dnName); // use the same
        certGen.setNotBefore(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000));
        certGen.setPublicKey(pair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WithRSA");
        final X509Certificate cert = certGen.generate(pair.getPrivate(), "BC");

        ks.setEntry(alias, new KeyStore.TrustedCertificateEntry(cert), null);
    }

    private void copyPublicPart(final KeyStore src, final KeyStore dst, final String alias) throws KeyStoreException {
        Certificate cert = src.getCertificate(alias);
        dst.setCertificateEntry(alias, cert);
    }
}
