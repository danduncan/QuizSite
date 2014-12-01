package quizsite;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword {
	
	public static String getHash(String pwd) {
		String hashpw = pwd;
		if (!MyDBInfo.MYDB_LOGINSKIPHASH) {
			String saltpw = pwd + MyDBInfo.MYDB_LOGINSALT;
			hashpw = hexToString(hashString(saltpw));
		}
		return hashpw;
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
	private static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/**
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	private static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String pwd = "danpw";
		System.out.println("\tPassword = " + pwd);
		System.out.println("\tHash = " + getHash(pwd));
	}

}
