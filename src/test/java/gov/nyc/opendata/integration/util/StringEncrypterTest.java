package gov.nyc.opendata.integration.util;


import junit.framework.TestCase;
import gov.nyc.opendata.integration.util.StringEncrypter;
public class StringEncrypterTest extends TestCase {
	
	private StringEncrypter stringEncrypter;
	private String encryptionKey;
	
	public void setUp() throws Exception {
		super.setUp();
		encryptionKey = "socrata_pw_encryption_key";
		stringEncrypter = new StringEncrypter(encryptionKey);
	}
	
	public void testEncrypt() {
		String unencryptedString = "1234567890";
		String actual = stringEncrypter.encrypt(unencryptedString);
		assertFalse(unencryptedString.equals(actual));
	}
	
	public void testDecrypt() {
		String unencryptedString = "1234567890";
		String encryptedString = stringEncrypter.encrypt(unencryptedString);
		String actual = stringEncrypter.decrypt(encryptedString);
		assertEquals(unencryptedString, actual);
	}
	
}
