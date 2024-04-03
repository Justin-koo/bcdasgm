package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import javax.crypto.SecretKey;

import crypto.Symmetric;

public class Patient {
	private String patientID;

    public Patient(String patientID) {
    	this.patientID = patientID;
    }
    
    public void fileHealthInsuranceClaim(String diagnosis, String treatment, String[] medications) {
        try (FileWriter writer = new FileWriter("insurance_claims.txt", true)) {
        	// Generate a unique claim ID
        	String claimID = UUID.randomUUID().toString();
        	
        	InsuranceClaim claim = new InsuranceClaim(claimID, patientID, diagnosis, treatment, medications, "Pending", null, null);

        	String claimJson = claim.toJson();
        	
        	SecretKey loadedKey = Symmetric.loadKey("InsuranceClaim");
            String encryptedClaim = Symmetric.encrypt(claimJson.toString(), loadedKey);
            
            // Write the insurance claim details to the file
            writer.write(encryptedClaim + "\n");
            System.out.println("Health insurance claim filed successfully.");

        } catch (IOException e) {
            System.err.println("Error filing health insurance claim: " + e.getMessage());
        }
    }

    public void viewInsuranceClaimStatus() {
        try (BufferedReader reader = new BufferedReader(new FileReader("insurance_claims.txt"))) {
            String line;
            System.out.println("Insurance Claim Status:");
            while ((line = reader.readLine()) != null) {
                // Display each insurance claim and its status
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading insurance claim status: " + e.getMessage());
        }
    }
}
