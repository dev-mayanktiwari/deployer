package tech.mayanktiwari.deployer.encryption.service;

public interface EncryptionService {

  String encrypt(String plainText);

  String decrypt(String encryptedText);
}
