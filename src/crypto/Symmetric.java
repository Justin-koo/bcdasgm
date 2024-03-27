package crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Symmetric {
    private static final String CRYPTO_FOLDER = "Crypto";
    private static final String SECRET_FOLDER = "Secret";

    private static final Map<String, SecretKey> keyMap = new HashMap<>();

    // Method to generate a new symmetric encryption key
    public static SecretKey generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to encrypt data using a specified key
    public static String encrypt(String data, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to decrypt data using a specified key
    public static String decrypt(String encryptedData, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to store a symmetric encryption key with a specified name
    public static void storeKey(String keyName, SecretKey key) {
        keyMap.put(keyName, key);
        
        File folder = new File(CRYPTO_FOLDER + File.separator + SECRET_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File keyFile = new File(folder, keyName + ".key");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(keyFile))) {
            oos.writeObject(key);
            System.out.println("Symmetric key '" + keyName + "' stored successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error storing symmetric key '" + keyName + "': " + e.getMessage());
        }
    }

    // Method to load a symmetric encryption key with a specified name
    public static SecretKey loadKey(String keyName) {
        if (!keyMap.containsKey(keyName)) {
            File keyFile = new File(CRYPTO_FOLDER + File.separator + SECRET_FOLDER, keyName + ".key");
            if (!keyFile.exists()) {
                System.err.println("Symmetric key file '" + keyName + ".key' not found.");
                return null;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(keyFile))) {
                SecretKey key = (SecretKey) ois.readObject();
                keyMap.put(keyName, key);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error loading symmetric key '" + keyName + "': " + e.getMessage());
                return null;
            }
        }
        return keyMap.get(keyName);
    }

}

