package gov.nyc.opendata.integration.util;

import java.security.Security;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * StringEncrypter class - To encrypt and decrypt the passwords
 * This class has to be passed with the encryption key for initialization.
 */
public class StringEncrypter {
//	private final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	private final String UNICODE_FORMAT = "UTF8";
	private KeySpec keySpec;
	private SecretKeyFactory keyFactory;
	private Cipher cipher;

	
	public StringEncrypter(String encryptionKey) {
		String DESEDE_ENCRYPTION_SCHEME = "DESede";
		if (encryptionKey == null || encryptionKey.trim().length() == 0) {
			throw new IllegalArgumentException(
					"encryptionKey cannot be null or empty");
		}

		byte[] encryptionKeyBytes;
		try {
			Security.addProvider(new BouncyCastleProvider());
			encryptionKeyBytes = encryptionKey.getBytes(UNICODE_FORMAT);
			keySpec = new DESedeKeySpec(encryptionKeyBytes);
			keyFactory = SecretKeyFactory.getInstance(DESEDE_ENCRYPTION_SCHEME);
			cipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * 
	 * encrypts the password
	 * 
	 * @param unencryptedString
	 *            the string to be encrypted
	 *           
	 *  @return the encrypted password       
	 *          
	 */
	public String encrypt(String unencryptedString) {
		if (unencryptedString == null || unencryptedString.trim().length() == 0) {
			throw new IllegalArgumentException(
					"unencryptedString cannot be null or empty");
		}

		try {
			SecretKey key = keyFactory.generateSecret(keySpec);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] ciphertext = cipher.doFinal(cleartext);
			BASE64Encoder base64encoder = new BASE64Encoder();
			return base64encoder.encode(ciphertext);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * decrypts the password
	 * 
	 * @param encryptedString
	 *            the string to be decrypted
	 *           
	 *  @return the decrypted password       
	 *          
	 */
	public String decrypt(String encryptedString) {
		if (encryptedString == null || encryptedString.trim().length() <= 0) {
			throw new IllegalArgumentException(
					"encryptedString cannot be null or empty");
		}

		try {
			SecretKey key = keyFactory.generateSecret(keySpec);
			cipher.init(Cipher.DECRYPT_MODE, key);
			BASE64Decoder base64decoder = new BASE64Decoder();
			byte[] cleartext = base64decoder.decodeBuffer(encryptedString);
			byte[] ciphertext = cipher.doFinal(cleartext);
			return bytesToString(ciphertext);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * 
	 * Convert from bytes to String
	 * 
	 * @param bytes
	 *           byte array
	 *           
	 *  @return String      
	 *          
	 */
	private static String bytesToString(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder();
		
		for (byte b : bytes) {
	
			stringBuilder.append((char) b);
		}
		return stringBuilder.toString();
	}
}