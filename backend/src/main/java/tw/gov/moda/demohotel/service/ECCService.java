package tw.gov.moda.demohotel.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.NamedParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

/**
 * 繁體中文註解：提供 ECC 加解密功能，使用 X25519 進行金鑰交換並搭配 ChaCha20-Poly1305 對稱加密。
 */
@Service
public class ECCService {

  private static final String KEY_AGREEMENT_ALGORITHM = "X25519";
  private static final String SYMMETRIC_ALGORITHM = "ChaCha20-Poly1305";
  private static final String KDF_ALGORITHM = "SHA-256";
  private static final int SYMMETRIC_KEY_SIZE = 32;
  private static final int NONCE_LENGTH = 12;

  /**
   * 繁體中文註解：產生 ECC 金鑰對，回傳陣列索引 0 為公鑰、索引 1 為私鑰，皆為 Base64 字串。
   */
  public String[] generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_AGREEMENT_ALGORITHM);
    keyPairGenerator.initialize(new NamedParameterSpec("X25519"), new SecureRandom());
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    return new String[] {publicKeyBase64, privateKeyBase64};
  }

  /**
   * 繁體中文註解：使用對方公鑰加密資料，最終資料格式為「公鑰長度(4 bytes) + 臨時公鑰 + nonce + 密文」再轉 Base64。
   */
  public String encrypt(String plaintext, String receiverPublicKeyBase64) throws Exception {
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_AGREEMENT_ALGORITHM);
    PublicKey receiverPublicKey = keyFactory.generatePublic(
        new java.security.spec.X509EncodedKeySpec(Base64.getDecoder().decode(receiverPublicKeyBase64)));

    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_AGREEMENT_ALGORITHM);
    keyPairGenerator.initialize(new NamedParameterSpec("X25519"), new SecureRandom());
    KeyPair ephemeralKeyPair = keyPairGenerator.generateKeyPair();

    KeyAgreement keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALGORITHM);
    keyAgreement.init(ephemeralKeyPair.getPrivate());
    keyAgreement.doPhase(receiverPublicKey, true);
    byte[] sharedSecret = keyAgreement.generateSecret();

    SecretKey secretKey = deriveKey(sharedSecret);

    byte[] nonce = new byte[NONCE_LENGTH];
    new SecureRandom().nextBytes(nonce);
    IvParameterSpec iv = new IvParameterSpec(nonce);

    Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
    byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

    byte[] ephemeralPublicKeyBytes = ephemeralKeyPair.getPublic().getEncoded();
    byte[] keyLengthBytes = intToBytes(ephemeralPublicKeyBytes.length);
    byte[] output = new byte[4 + ephemeralPublicKeyBytes.length + nonce.length + ciphertext.length];

    int offset = 0;
    System.arraycopy(keyLengthBytes, 0, output, offset, keyLengthBytes.length);
    offset += keyLengthBytes.length;
    System.arraycopy(ephemeralPublicKeyBytes, 0, output, offset, ephemeralPublicKeyBytes.length);
    offset += ephemeralPublicKeyBytes.length;
    System.arraycopy(nonce, 0, output, offset, nonce.length);
    offset += nonce.length;
    System.arraycopy(ciphertext, 0, output, offset, ciphertext.length);

    return Base64.getEncoder().encodeToString(output);
  }

  /**
   * 繁體中文註解：使用私鑰解密加密資料，若 MAC 驗證失敗會丟出 SecurityException。
   */
  public String decrypt(String encryptedBase64, String privateKeyBase64) throws Exception {
    byte[] input = Base64.getDecoder().decode(encryptedBase64);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_AGREEMENT_ALGORITHM);
    PrivateKey privateKey = keyFactory.generatePrivate(
        new java.security.spec.PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyBase64)));

    byte[] keyLengthBytes = new byte[4];
    System.arraycopy(input, 0, keyLengthBytes, 0, keyLengthBytes.length);
    int ephemeralKeyLength = bytesToInt(keyLengthBytes);

    byte[] ephemeralPublicKeyBytes = new byte[ephemeralKeyLength];
    byte[] nonce = new byte[NONCE_LENGTH];
    byte[] ciphertext = new byte[input.length - 4 - ephemeralKeyLength - NONCE_LENGTH];

    int offset = 4;
    System.arraycopy(input, offset, ephemeralPublicKeyBytes, 0, ephemeralKeyLength);
    offset += ephemeralKeyLength;
    System.arraycopy(input, offset, nonce, 0, NONCE_LENGTH);
    offset += NONCE_LENGTH;
    System.arraycopy(input, offset, ciphertext, 0, ciphertext.length);

    PublicKey ephemeralPublicKey = keyFactory.generatePublic(
        new java.security.spec.X509EncodedKeySpec(ephemeralPublicKeyBytes));

    KeyAgreement keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALGORITHM);
    keyAgreement.init(privateKey);
    keyAgreement.doPhase(ephemeralPublicKey, true);
    byte[] sharedSecret = keyAgreement.generateSecret();

    SecretKey secretKey = deriveKey(sharedSecret);

    Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(nonce));
    byte[] decrypted = cipher.doFinal(ciphertext);
    return new String(decrypted, StandardCharsets.UTF_8);
  }

  private SecretKey deriveKey(byte[] sharedSecret) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance(KDF_ALGORITHM);
    byte[] derived = digest.digest(sharedSecret);
    return new SecretKeySpec(derived, 0, SYMMETRIC_KEY_SIZE, "ChaCha20");
  }

  private byte[] intToBytes(int value) {
    return new byte[] {
        (byte) (value >> 24),
        (byte) (value >> 16),
        (byte) (value >> 8),
        (byte) value
    };
  }

  private int bytesToInt(byte[] bytes) {
    return ((bytes[0] & 0xFF) << 24)
        | ((bytes[1] & 0xFF) << 16)
        | ((bytes[2] & 0xFF) << 8)
        | (bytes[3] & 0xFF);
  }
}
