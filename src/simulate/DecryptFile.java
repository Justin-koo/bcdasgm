package simulate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.crypto.SecretKey;
import crypto.Symmetric;

public class DecryptFile {
    private static final String ENCRYPTED_FILE = "insurance_claims.txt"; // Change to your encrypted file path
    private static SecretKey key = Symmetric.loadKey("InsuranceClaim");
    
    public static void main(String[] args) {
        try {
        	// Read the encrypted content from the file
            String decryptedContent = readEncryptedFile(ENCRYPTED_FILE);

            // Process the decrypted content (e.g., print it)
            System.out.println("Decrypted Content:");
            System.out.println(decryptedContent);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static String readEncryptedFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	String decryptedLine  = Symmetric.decrypt(line, key);
            	
                content.append(decryptedLine ).append("\n");
            }
        }
        return content.toString();
    }
}
