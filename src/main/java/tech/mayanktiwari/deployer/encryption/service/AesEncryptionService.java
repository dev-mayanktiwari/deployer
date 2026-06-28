package tech.mayanktiwari.deployer.encryption.service;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;
import tech.mayanktiwari.deployer.common.exception.ErrorCode;
import tech.mayanktiwari.deployer.encryption.config.EncryptionProperties;

@Service
public class AesEncryptionService implements EncryptionService {

  private static final String AES_ALGORITHM = "AES";
  private static final String AES_GCM_ALGORITHM = "AES/GCM/NoPadding";
  private static final int IV_LENGTH = 12;
  private static final int AUTH_TAG_LENGTH = 128;
  private static final int AES_256_KEY_LENGTH = 32;

  private final SecureRandom secureRandom = new SecureRandom();
  private final SecretKey secretKey;

  public AesEncryptionService(EncryptionProperties encryptionProperties) {
    byte[] keyBytes = Base64.getDecoder().decode(encryptionProperties.getSecret());

    if (keyBytes.length != AES_256_KEY_LENGTH) {
      throw ErrorCode.INVALID_ENCRYPTION_CONFIGURATION.build();
    }

    this.secretKey = new SecretKeySpec(keyBytes, AES_ALGORITHM);
  }

  @Override
  public String encrypt(String plainText) {
    try {
      byte[] iv = new byte[IV_LENGTH];
      secureRandom.nextBytes(iv);

      GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(AUTH_TAG_LENGTH, iv);

      Cipher cipher = Cipher.getInstance(AES_GCM_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

      byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

      byte[] encryptPayload = new byte[iv.length + cipherText.length];
      System.arraycopy(iv, 0, encryptPayload, 0, iv.length);
      System.arraycopy(cipherText, 0, encryptPayload, iv.length, cipherText.length);

      return Base64.getEncoder().encodeToString(encryptPayload);
    } catch (GeneralSecurityException e) {
      throw ErrorCode.ENCRYPTION_FAILED.buildWithCause(e);
    }
  }

  @Override
  public String decrypt(String encryptedText) {
    try {
      byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
      byte[] iv = new byte[IV_LENGTH];
      byte[] cipherText = new byte[encryptedBytes.length - IV_LENGTH];

      System.arraycopy(encryptedBytes, 0, iv, 0, IV_LENGTH);
      System.arraycopy(encryptedBytes, IV_LENGTH, cipherText, 0, cipherText.length);

      GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(AUTH_TAG_LENGTH, iv);

      Cipher cipher = Cipher.getInstance(AES_GCM_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

      byte[] plainText = cipher.doFinal(cipherText);

      return new String(plainText, StandardCharsets.UTF_8);
    } catch (GeneralSecurityException e) {
      throw ErrorCode.DECRYPTION_FAILED.buildWithCause(e);
    }
  }
}
