package com.kumana.iotp.lite.util;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

@Component
public class TLSUtil {

    private static final String TEMPORARY_KEY_PASSWORD = "changeit";
    private static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERTIFICATE = "-----END CERTIFICATE-----";
    private static final String JKS = "JKS";
    private static final String CA_CERT_ALIAS = "caCert";
    private static final String CONCERT_IOTHUB_CERT_LOCATION = "concert.iothub.cert.location";
    private static final String TLS = "TLS";
    private static final String CONCERT_IOTHUB_CERT_PASSWORD = "concert.iothub.cert.password";
    private String certFileLocation = null;
    private static final Logger logger = LoggerFactory.getLogger(TLSUtil.class);
    private String iotHubeCertPassword = "abc123";

    private KeyStore keyStore = null;

    @Autowired
    private Environment env;

    @PostConstruct
    public void initialize(){
        final String iotHubCertLocation = env.getProperty(CONCERT_IOTHUB_CERT_LOCATION);
        if(iotHubCertLocation != null && !iotHubCertLocation.isEmpty()){
            File certFile = new File(iotHubCertLocation);
            if(certFile.exists() && certFile.isFile()){
                this.certFileLocation = iotHubCertLocation;
                logger.info("Using Cert file located in : {} for IOT Hub communications", iotHubCertLocation);
            }else {
                logger.error("Concert IOT Hub's Cert file cannot be found at location : {}", iotHubCertLocation);
                throw new RuntimeException("Concert IOT Hub's Cert file cannot be found");
            }
        }else{
            throw new IllegalArgumentException("Concert IOT Hub's certificate location entry cannot be null");
        }

        final String iotHubCertPWFromFile = env.getProperty(CONCERT_IOTHUB_CERT_PASSWORD);
        if(iotHubCertPWFromFile != null && !iotHubCertPWFromFile.isEmpty()){
            logger.debug("External password value for IOT Hub Cert detected. Using value : {}", iotHubCertPWFromFile);
            iotHubeCertPassword = iotHubCertPWFromFile;
        }else{
            logger.debug("Using default value of : {} for IOT Hub Cert password", iotHubeCertPassword);
        }
        generateKeyStore();
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public void generateKeyStore(){
        try{
            final String extractedCertEntry = extractCertEntry(certFileLocation);
            Certificate caCertificate = loadCertificate(extractedCertEntry);
            this.keyStore = KeyStore.getInstance(JKS);
            this.keyStore.load(null, TEMPORARY_KEY_PASSWORD.toCharArray());
            this.keyStore.setCertificateEntry(CA_CERT_ALIAS, caCertificate);
        }catch (GeneralSecurityException | IOException e){
            logger.error("Cannot generate JKS with provided Cert entry, error was : {}", e.getMessage());
            throw  new RuntimeException("Cannot generate JKS with provided Cert entry", e);
        }
    }

    private Certificate loadCertificate(String certificatePem) throws IOException, GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        final byte[] content = readPemContent(certificatePem);
        return certificateFactory.generateCertificate(new ByteArrayInputStream(content));
    }

    private byte[] readPemContent(String pem) throws IOException {
        final byte[] content;
        try (PemReader pemReader = new PemReader(new StringReader(pem))) {
            final PemObject pemObject = pemReader.readPemObject();
            content = pemObject.getContent();
        }
        return content;
    }

    private String readCertFile(final String certFileLocation) throws IOException {
        final byte[] certFileBytes = Files.readAllBytes(Paths.get(certFileLocation));
        final String certFileContent = new String(certFileBytes, StandardCharsets.UTF_8.name());
        return certFileContent;
    }

    private String extractCertEntry(final String certFileLocation) throws IOException {
        String certificateEntry = null;
        final String certFileContent = readCertFile(certFileLocation);
        final int beginCertificateIndex = certFileContent.indexOf(BEGIN_CERTIFICATE);
        final int endCertificateIndex = certFileContent.indexOf(END_CERTIFICATE);
        if(beginCertificateIndex != -1 && endCertificateIndex != -1){
            certificateEntry = certFileContent.substring(beginCertificateIndex, endCertificateIndex + END_CERTIFICATE.length());
        }else{
            logger.error("Invalid Cert file content has been detected");
            throw new IOException("Invalid Cert file content");
        }
        return certificateEntry;
    }

    public SSLSocketFactory getSocketFactoryWithX509Cert() {
        SSLSocketFactory ssf = null;
        try {
            final KeyStore ks = getKeyStore();
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, TEMPORARY_KEY_PASSWORD.toCharArray());
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            final SSLContext sc = SSLContext.getInstance(TLS);
            final TrustManager[] trustManagers = tmf.getTrustManagers();
            sc.init(kmf.getKeyManagers(), trustManagers, null);
            ssf = sc.getSocketFactory();
        } catch (Exception ex){
            throw new RuntimeException("Cannot create SSL Socket Factory", ex);
        }
        return ssf;
    }
}
