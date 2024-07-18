package aiims.cf.tms_api.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class CryptoUtil {

	private static final String ALGORITHM = "AES/CBC/PKCS5PADDING";
    private static final String KEY = "my-secret-key-as"; // Must be 16 bytes
    private static final String INIT_VECTOR = "1234567890123456"; // Must be 16 bytes

    public static String encrypt(String plainText) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encryptedText));
            return new String(original);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

