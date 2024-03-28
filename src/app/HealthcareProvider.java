package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import crypto.Asymmetric;
import crypto.DigitalSignature;
import crypto.Symmetric;
import javax.crypto.SecretKey;

public class HealthcareProvider {
    private Map<String, String> treatmentData; // Key: patient's medical record number, Value: treatment data
    private static final String TREATMENT_FILE = "treatment_records.txt";
    private static final String CLAIMS_FILE = "insurance_claims.txt";
    private static final String SIGNED_FILE = "signatures.txt";
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
            SecretKey loadedKey = Symmetric.loadKey("MedicalRecord");
            String encryptedData = Symmetric.encrypt(jsonRecord, loadedKey);
            
            writer.write(encryptedData + "\n");
            System.out.println("Treatment data updated and saved to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }
    
    public void verifyHealthInsuranceClaim() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CLAIMS_FILE))) {
            System.out.println("\nHealth Insurance Claims:");
            String line;
            List<InsuranceClaim> claims = new ArrayList<>();
            PrivateKey privateKey = Asymmetric.loadPrivateKey("HealthcareProvider");
            SecretKey loadedKey = Symmetric.loadKey("InsuranceClaim");
            int i = 0;

            while ((line = reader.readLine()) != null) {
//                String decryptedData = Asymmetric.decrypt(line, privateKey);
                String decryptedData = Symmetric.decrypt(line, loadedKey);
                InsuranceClaim claim = new Gson().fromJson(decryptedData, InsuranceClaim.class);
                
                if(claim.getClaimStatus().equals("Pending")) {
                	System.out.println((i + 1) + ". " + claim + "\n");
	                claims.add(claim);
	                i++;
                }
                
            }
            if(claims.size() > 0) {
            	// Select a claim to sign
	            System.out.print("Enter the number of the claim you want to sign: ");
	            int claimNumber = Integer.parseInt(scanner.nextLine());
	            
	            InsuranceClaim selectedClaim = claims.get(claimNumber - 1);            
	            String claimJson = new Gson().toJson(selectedClaim);
	            
	            // Sign the selected claim
	            String signature = DigitalSignature.sign(claimJson, privateKey);
	
	            System.out.println("Claim " + selectedClaim.getClaimID() + " signed successfully.");
	            
	            storeClaimWithSignature(selectedClaim.getClaimID(), signature);
            } else {
            	System.out.println("No available claims");
            }
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    private void storeClaimWithSignature(String claimID, String signature) {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(SIGNED_FILE, true))) {
    		
    		PublicKey insuranceCompanyPublicKey = Asymmetric.loadPublicKey("InsuranceCompany");
    		String encryptedClaimID  = Asymmetric.encrypt(claimID, insuranceCompanyPublicKey);
    		
            // Create a JSON object for the signed claim
            JsonObject signedClaim = new JsonObject();
            signedClaim.addProperty("claimID", encryptedClaimID);
            signedClaim.addProperty("signature", signature);

            // Convert the JSON object to a string
            String signedClaimJson = signedClaim.toString();
            
            // Write the signed claim JSON string to the file
            writer.write(signedClaimJson + "\n");
            System.out.println("Claim stored with digital signature successfully.");
        } catch (IOException e) {
            System.err.println("Error storing claim with digital signature: " + e.getMessage());
        }
    }

    public String getTreatmentData(String medicalRecord) {
        return treatmentData.getOrDefault(medicalRecord, "No treatment data available.");
    }

    // Other methods as needed
}
