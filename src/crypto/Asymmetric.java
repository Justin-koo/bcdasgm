package crypto;

import javax.crypto.*;
import java.io.*;
import java.security.*;
import java.util.Base64;

public class Asymmetric {
	private static final String CRYPTO_FOLDER = "Crypto";
	
    // Method to generate a public-private key pair using RSA algorithm
    public static KeyPair generateKeyPair() {
        try {
            // Create a KeyPairGenerator object for RSA algorithm
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

            keyGen.initialize(2048);

            // Generate the key pair
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to perform encryption using RSA algorithm and public key
    public static String encrypt(String data, PublicKey publicKey) {
        try {
            // Create a Cipher object for RSA encryption
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // Encrypt the data
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to perform decryption using RSA algorithm and private key
    public static String decrypt(String encryptedData, PrivateKey privateKey) {
        try {
            // Create a Cipher object for RSA decryption
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // Decrypt the data
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
 // Method to store a key pair for a specific patient
    public static void storeKey(KeyPair keyPair, String userID) {
        File patientFolder = new File(CRYPTO_FOLDER, userID);
        if (!patientFolder.exists()) {
            if (!patientFolder.mkdirs()) {
                System.err.println("Failed to create folder for patient: " + userID);
                return;
            }
        }

        File publicKeyFile = new File(patientFolder, "public.key");
        File privateKeyFile = new File(patientFolder, "private.key");

        try {
            // Save the public key to a file
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(publicKeyFile))) {
                oos.writeObject(keyPair.getPublic());
            }

            // Save the private key to a file
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(privateKeyFile))) {
                oos.writeObject(keyPair.getPrivate());
            }

            System.out.println("Keys for patient " + userID + " stored successfully.");
        } catch (IOException e) {
            System.err.println("Error storing keys for patient " + userID + ": " + e.getMessage());
        }
    }
    
 // Method to load the public key
    public static PublicKey loadPublicKey(String userID) {
        File publicKeyFile = new File(CRYPTO_FOLDER + File.separator + userID, "public.key");
        if (!publicKeyFile.exists()) {
            System.err.println("Public key file not found for user: " + userID);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(publicKeyFile))) {
            return (PublicKey) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to load the private keyt
    public static PrivateKey loadPrivateKey(String userID) {
        File privateKeyFile = new File(CRYPTO_FOLDER + File.separator + userID, "private.key");
        if (!privateKeyFile.exists()) {
            System.err.println("Private key file not found for user: " + userID);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(privateKeyFile))) {
            return (PrivateKey) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
