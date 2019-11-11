package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Helper class for hashing strings.
 */
public class HashHelper {

    private static final String SALT = "xyz-driver-association-salt";

    /**
     * Salts & hashes the passed string.
     *
     * @param textToHash: The String to be hashed
     * @return the hashed String, or null if there was an error.
     */
    public static String hashString(String textToHash) {
        String saltedText = SALT + textToHash;
        return generateHash(saltedText);
    }

    private static String generateHash(String textToHash) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(textToHash.getBytes());
            char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
            for (int i = 0; i < hashedBytes.length; i++) {
                byte b = hashedBytes[i];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        return hash.toString();
    }
}
