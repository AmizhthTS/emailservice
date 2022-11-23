package com.amizhth.email.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AesUtil {
	private static int keySize;
	private static int iterationCount;
	private static Cipher cipher;
	private final String iv = "297A1646BC32E036CF3C4DE1B1926A71";
	private final String salt = "32E036CF3C4DE1B192297A1646BC6A71297A164DE1B1926A716BC32E036CF3C4";
	private final String passphrase = "varuvaaiencryption";
	public static int SECURITY_AES_KEYSIZE = 128;
	public static int SECURITY_AES_ITERATIONS = 1024;
	static Logger logger = LoggerFactory.getLogger(AesUtil.class);

	public String getIv() {
		return iv;
	}

	public String getSalt() {
		return salt;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public int getKeySize() {
		return keySize;
	}

	public int getIterationCount() {
		return iterationCount;
	}

	public AesUtil(int keySize, int iterationCount) {
		this.keySize = keySize;
		this.iterationCount = iterationCount;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			throw fail(e);
		} catch (NoSuchPaddingException e1) {
			throw fail(e1);
		}
	}

	public String encrypt(String plaintext) {
		try {
			SecretKey key = generateKey(salt, passphrase);
			byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
			return base64(encrypted);
		} catch (UnsupportedEncodingException e) {
			throw fail(e);
		}
	}

	public String encrypt(String salt, String iv, String passphrase, String plaintext) {
		try {
			SecretKey key = generateKey(salt, passphrase);
			byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
			return base64(encrypted);
		} catch (UnsupportedEncodingException e) {
			throw fail(e);
		}
	}

	public String decrypt(String ciphertext) {
		try {
			SecretKey key = generateKey(salt, passphrase);
			byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
			return new String(decrypted, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw fail(e);
		} catch (IllegalStateException e) {
			throw fail(e);
		} catch (Exception e) {
			throw fail(e);
		}
	}

	private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
		try {
			cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
			return cipher.doFinal(bytes);
		} catch (InvalidKeyException e) {
			throw fail(e);
		} catch (InvalidAlgorithmParameterException e1) {
			throw fail(e1);
		} catch (IllegalBlockSizeException e2) {
			throw fail(e2);
		} catch (BadPaddingException e3) {
			throw fail(e3);
		}
	}

	private SecretKey generateKey(String salt, String passphrase) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount, keySize);
			SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
			return key;
		} catch (NoSuchAlgorithmException e) {
			throw fail(e);
		} catch (InvalidKeySpecException e1) {
			throw fail(e1);
		}
	}

	public String random(int length) {
		byte[] salt = new byte[length];
		new SecureRandom().nextBytes(salt);
		return hex(salt);
	}

	public static String base64(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	public static byte[] base64(String str) {
		return Base64.decodeBase64(str);
	}

	public String hex(byte[] bytes) {
		return Hex.encodeHexString(bytes);
	}

	public byte[] hex(String str) {
		try {
			return Hex.decodeHex(str.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException(e);
		}
	}

	private static IllegalStateException fail(Exception e) {
		return new IllegalStateException(e);
	}

	public String decrypt(String salt, String iv, String passphrase, String ciphertext) {
		try {
			SecretKey key = generateKey(salt, passphrase);
			byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
			return new String(decrypted, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw fail(e);
		} catch (IllegalStateException e) {
			throw fail(e);
		} catch (Exception e) {
			throw fail(e);
		}
	}

	public static void main(String args[]) {
		try {
			//AesUtil util = new AesUtil(SECURITY_AES_KEYSIZE, SECURITY_AES_ITERATIONS);
			//String ciphertext = "";
			
			//Uncomment to Encrypt
			//System.out.println("encrypted :: " + util.encrypt(ciphertext));
			
			//Uncomment to Decrypt
			//System.out.println("decrypted :: " + util.decrypt(ciphertext));
		} catch (Exception e) {
			logger.error("e :: " + e.getMessage());
		}
	}

}
