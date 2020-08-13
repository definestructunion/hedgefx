package hedge.fx.io.serialization.json;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public final class JsonEncrypt {
	private static final String encryptKey = "Bar12345Bar0234i";
	private static final String algorithm = "AES";

	public static String encrypt(String json) {
		try {
			Key aesKey = new SecretKeySpec(encryptKey.getBytes(), algorithm);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(json.getBytes());
			byte[] encryptedByteValue = Base64.getEncoder().encode(encrypted);
			return new String(encryptedByteValue);
		} catch(Exception e) { e.printStackTrace(); }

		//Logger.getClient().add(LogLevel.WARN, "Json encryption failed.");
		return json;
	}

	public static String decrypt(String encryptedJson) {
		try {
			Key aesKey = new SecretKeySpec(encryptKey.getBytes(), algorithm);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			byte[] decodedValue = Base64.getDecoder().decode(encryptedJson.getBytes());
			byte[] decryptedValue = cipher.doFinal(decodedValue);
			return new String(decryptedValue);
		} catch(Exception e) { e.printStackTrace(); }

		//Logger.getClient().add(LogLevel.WARN, "Json decryption failed.");
		return encryptedJson;
	}
}
