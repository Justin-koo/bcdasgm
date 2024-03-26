package simulate;

import javax.crypto.SecretKey;

import crypto.Symmetric;

public class createSecret {
	public static void main(String[] args) {
        // Generate a symmetric encryption key
		SecretKey key = Symmetric.generateKey();
        Symmetric.storeKey(key);

        // Load the symmetric encryption key
        SecretKey loadedKey = Symmetric.loadKey();

        if (loadedKey != null) {
            // Test encryption and decryption with the loaded key
            String originalData = "Hello, world!";
            System.out.println("Original data: " + originalData);

            String encryptedData = Symmetric.encrypt(originalData, loadedKey);
            System.out.println("Encrypted data: " + encryptedData);

            String decryptedData = Symmetric.decrypt(encryptedData, loadedKey);
            System.out.println("Decrypted data: " + decryptedData);
        } else {
            System.out.println("Failed to load symmetric key.");
        }
    }
}
