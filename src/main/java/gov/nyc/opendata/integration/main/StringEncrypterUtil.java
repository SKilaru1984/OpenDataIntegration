package gov.nyc.opendata.integration.main;

/**
 * The StringEncrypterUtil class encrypts the string values provided.
 */
import gov.nyc.opendata.integration.util.PropertyLoaderUtil;


public class StringEncrypterUtil {
	static {
		PropertyLoaderUtil.init();
	}

	public static void main(String[] args) {
		if (args.length != 0) {
			for (String arg : args) {
				System.out.println("Original string: " +arg);
				System.out.println("Encrypted string: "
						+ PropertyLoaderUtil.STRING_ENCRYPTER.encrypt(arg));
			}
			
		} else {
			System.out
					.println("Please provide the string to encrypt in the argument...");
		}
	}

}