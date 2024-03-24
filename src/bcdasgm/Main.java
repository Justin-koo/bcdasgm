package bcdasgm;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Main {
   public static void main(String[] args) throws Exception {

      // Generate key
      SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
   
      // Original message
      String originalMessage = "Hello, world!";
   
      // Create Cipher instance and initialize it to ENCRYPT_MODE
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
   
      // Encrypt the message
      byte[] encryptedMessage = cipher.doFinal(originalMessage.getBytes(StandardCharsets.UTF_8));
   
      // Convert the encrypted message to Base64 encoded string
      String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessage);
   
      System.out.println("Original Message: " + originalMessage);
      System.out.println("Encrypted Message: " + encodedMessage);
   
      // Reinitialize the cipher to DECRYPT_MODE
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
   
      // Decrypt the message
      byte[] decryptedMessage = cipher.doFinal(Base64.getDecoder().decode(encodedMessage));
   
      System.out.println("Decrypted Message: " + new String(decryptedMessage, StandardCharsets.UTF_8));
   }
}
