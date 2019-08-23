package com.kumana.iotp.utils;

import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;


@Service
public class Utillities {

    public static class KeyStorePasswordPair {
        public KeyStore keyStore;
        public String keyPassword;

        public KeyStorePasswordPair(KeyStore keyStore, String keyPassword) {
            this.keyStore = keyStore;
            this.keyPassword = keyPassword;
        }
    }


    public static KeyStorePasswordPair getKeyStorePasswordPair(String certificateFile, String privateKeyFile) {
        return getKeyStorePasswordPair(certificateFile, privateKeyFile, null);
    }

    public static KeyStorePasswordPair getKeyStorePasswordPair(String certificateFile, String privateKeyFile,
                                                               String keyAlgorithm) {
        if (certificateFile == null || privateKeyFile == null) {
            System.out.println("Certificate or private key file missing");
            return null;
        }

        Certificate certificate = loadCertificateFromFile(certificateFile);
        PrivateKey privateKey = loadPrivateKeyFromFile(privateKeyFile, keyAlgorithm);
        if (certificate == null || privateKey == null) {
            return null;
        }

        return getKeyStorePasswordPair(certificate, privateKey);
    }

    private static Certificate loadCertificateFromFile(String filename) {
        Certificate certificate = null;
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Certificate file not found: " + filename);
            return null;
        }
        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file))) {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            certificate = certFactory.generateCertificate(stream);
        } catch (IOException | CertificateException e) {
            System.out.println("Failed to load certificate file " + filename);
        }

        return certificate;
    }

    private static PrivateKey loadPrivateKeyFromFile(String filename, String algorithm) {
        PrivateKey privateKey = null;
        //System.out.println(filename);
        //URL url = ClassLoader.getSystemClassLoader().getResource(filename);
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Private key file not found: " + filename);
            return null;
        }
        try (DataInputStream stream = new DataInputStream(new FileInputStream(file))) {
            privateKey = PrivateKeyReader.getPrivateKey(stream, algorithm);
        } catch (IOException | GeneralSecurityException e) {
            System.out.println("Failed to load private key from file " + filename);
        }

        return privateKey;
    }


    public static KeyStorePasswordPair getKeyStorePasswordPair(Certificate certificate, PrivateKey privateKey) {
        KeyStore keyStore = null;
        String keyPassword = null;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            keyStore.setCertificateEntry("alias", certificate);

            // randomly generated key password for the key in the KeyStore
            keyPassword = new BigInteger(128, new SecureRandom()).toString(32);
            keyStore.setKeyEntry("alias", privateKey, keyPassword.toCharArray(), new Certificate[]{certificate});
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            System.out.println("Failed to create key store");
            return null;
        }

        return new KeyStorePasswordPair(keyStore, keyPassword);
    }


}
