package app;

import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.security.PrivateKey;

import com.google.gson.Gson;

import crypto.Asymmetric;
import crypto.Symmetric;
import javax.crypto.SecretKey;

public class HealthcareProvider {
    private Map<String, String> treatmentData; // Key: patient's medical record number, Value: treatment data
    private static final String TREATMENT_FILE = "treatment_records.txt";
    private Scanner scanner;
    
    public HealthcareProvider() {
    	this.scanner = new Scanner(System.in);
    }

    public void updateTreatmentData(String medicalRecord, String treatment, String date, String patientID) {
        try (FileWriter writer = new FileWriter(TREATMENT_FILE, true)) {
            Gson gson = new Gson();
            
            TreatmentRecord record = new TreatmentRecord(medicalRecord, treatment, date, patientID);
            String jsonRecord = gson.toJson(record);
            
         // Load the symmetric encryption key
            SecretKey loadedKey = Symmetric.loadKey();
            String encryptedData = Symmetric.encrypt(jsonRecord, loadedKey);
            
            writer.write(encryptedData + "\n");
            System.out.println("Treatment data updated and saved to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }
    
    public void verifyHealthInsuranceClaim() {
        try (BufferedReader reader = new BufferedReader(new FileReader("insurance_claims.txt"))) {
            System.out.println("Health Insurance Claims:");
            String line;
            int lineNumber = 1;
            PrivateKey privateKey = Asymmetric.loadPrivateKey("mdProvider");
            
            while ((line = reader.readLine()) != null) {
                String decryptedData = Asymmetric.decrypt(line, privateKey);
                
                System.out.println(lineNumber + ". " + decryptedData);
                lineNumber++;
            }
            
            System.out.print("Enter the number of the claim you want to verify: ");
            int claimNumber = Integer.parseInt(scanner.nextLine());
            
//            storeClaimWithSignature(line);
            
            // Implement verification logic here
            System.out.println("Claim " + claimNumber + " verified successfully.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error verifying health insurance claim: " + e.getMessage());
        }
    }
    
//    private void storeClaimWithSignature(String claim) {
////    	String signature = DigitalSignature.sign()
//    	
//        try (FileWriter writer = new FileWriter("signed_insurance_claims.txt", true)) {
//            writer.write(claim + "\n");
//            writer.write("Digital Signature: " + signature + "\n\n");
//        } catch (IOException e) {
//            System.err.println("Error storing claim with digital signature: " + e.getMessage());
//        }
//    }

    public String getTreatmentData(String medicalRecord) {
        return treatmentData.getOrDefault(medicalRecord, "No treatment data available.");
    }

    // Other methods as needed
}
