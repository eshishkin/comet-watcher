package org.eshishkin.edu.cometwatcher.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eshishkin.edu.cometwatcher.service.VaultRepository;

@ApplicationScoped
public class EncryptionUtils {

    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String CIPHER_ALGORITHM = "AES";
    private static final int KEY_LENGTH = 16;

    private String encryptionKey;

    @Inject
    VaultRepository vault;

    @PostConstruct
    void init() {
        encryptionKey = vault.readSingleConfigValue("application.encryption_key").orElseThrow(
            () -> new IllegalStateException("Encryption key is not found in Vault")
        );
    }

    public String encryptAndEncode(String value) {
        return Base64.getUrlEncoder().encodeToString(encrypt(value));
    }

    public String encryptAndDecode(String value) {
        byte[] decoded = Base64.getUrlDecoder().decode(value);

        return new String(decrypt(decoded), StandardCharsets.UTF_8);
    }

    public byte[] encrypt(String value) {
        Key key = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), CIPHER_ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[KEY_LENGTH]));
            return cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | IllegalBlockSizeException
                | BadPaddingException
                | InvalidAlgorithmParameterException
                | InvalidKeyException e) {
            throw new RuntimeException("Unable to encrypt a string", e);
        }
    }

    public byte[] decrypt(byte[] input) {
        Key key = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), CIPHER_ALGORITHM);

        try {
            Cipher c = Cipher.getInstance(CIPHER_TRANSFORMATION);
            c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[KEY_LENGTH]));

            return c.doFinal(input);
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | IllegalBlockSizeException
                | BadPaddingException
                | InvalidAlgorithmParameterException
                | InvalidKeyException e) {

            throw new RuntimeException("Unable to decrypt a string", e);
        }
    }
}
