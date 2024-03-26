package crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Symmetric {
	private static final String CRYPTO_FOLDER = "Crypto";
    private static final String SECRET_FOLDER = "Secret";
    private static final String KEY_FILE = "symmetric.key";
    
    // Method to generate a symmetric encryption key using AES algorithm
    public static SecretKey generateKey() {
        try {
            // Create a KeyGenerator object for AES algorithm
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");

            // Generate a 128-bit AES key
            keyGen.init(128);

            // Generate the key
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to encrypt data using AES algorithm and the provided key
    public static String encrypt(String data, SecretKey key) {
        try {
            // Create a Cipher object for AES encryption
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // Encrypt the data
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to decrypt data using AES algorithm and the provided key
    public static String decrypt(String encryptedData, SecretKey key) {
        try {
            // Create a Cipher object for AES decryption
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            // Decrypt the data
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
 // Method to store the symmetric encryption key in a file
    public static void storeKey(SecretKey key) {
        File folder = new File(CRYPTO_FOLDER + File.separator + SECRET_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(folder, KEY_FILE)))) {
            oos.writeObject(key);
            System.out.println("Symmetric key stored successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error storing symmetric key: " + e.getMessage());
        }
    }

    // Method to load the symmetric encryption key from a file
    public static SecretKey loadKey() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CRYPTO_FOLDER + File.separator + SECRET_FOLDER + File.separator + KEY_FILE))) {
            return (SecretKey) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error loading symmetric key: " + e.getMessage());
            return null;
        }
    }
}

