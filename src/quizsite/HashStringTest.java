/**
 * This is a static class used for manually hashing passwords. It should never be called in the website
 */

package quizsite;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashStringTest {
	public static final String salt = MyDBInfo.MYDB_LOGINSALT;
	
	public HashStringTest() {
	}
	
	public static String getHashedString(String password) {
		String saltpw = password + MyDBInfo.MYDB_LOGINSALT;
		return hexToString(hashString(saltpw));
	}
	
	/**
	 * Hash a string password with the SHA algorithm
	 * @param pwd
	 * @return
	 */
	private static byte[] hashString(String pwd) {
		byte[] hashBytes = null;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");

			// Load the string into the MessageDigest and hash it
			md.update(pwd.getBytes());
			hashBytes = md.digest();

		} catch (NoSuchAlgorithmException ignored) {};

		return hashBytes;
	}

	/**
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	 */
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	
	public static void main(String[] args) {
		String ginopw = "ginopw";
		String danpw = "danpw";
		String mattpw = "mattpw";
		System.out.println(ginopw + "\t->\t" + getHashedString(ginopw));
		System.out.println(mattpw + "\t->\t" + getHashedString(mattpw));
		System.out.println(danpw + "\t->\t" + getHashedString(danpw));
		
	}
}
