package bcdasgm;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class MedicalRecordEncryption {

    // Symmetric encryption key (16 bytes for AES-128)
    private static final String SECRET_KEY = "mySecretKey123456";

    // Encrypt medical record using AES algorithm
    public static String encryptMedicalRecord(String medicalRecord) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(medicalRecord.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt medical record using AES algorithm
    public static String decryptMedicalRecord(String encryptedMedicalRecord) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMedicalRecord);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        try {
            // Patient's medical record
            String medicalRecord = "Patient's medical record goes here...";

            // Encrypt medical record
            String encryptedMedicalRecord = encryptMedicalRecord(medicalRecord);
            System.out.println("Encrypted Medical Record: " + encryptedMedicalRecord);

            // Decrypt medical record
            String decryptedMedicalRecord = decryptMedicalRecord(encryptedMedicalRecord);
            System.out.println("Decrypted Medical Record: " + decryptedMedicalRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
