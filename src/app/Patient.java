package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.crypto.SecretKey;

import crypto.Symmetric;
import com.google.gson.*;

public class Patient {
	private String patientID;
	private Scanner scanner;

    public Patient(String patientID) {
    	this.patientID = patientID;
    }
    
    public void viewMedicalRecords() {
    	SecretKey loadedKey = Symmetric.loadKey();
    	
        try (BufferedReader reader = new BufferedReader(new FileReader("treatment_records.txt"))) {
            String line;
            System.out.println("Medical Records:");
            while ((line = reader.readLine()) != null) {
            	String decryptedData = Symmetric.decrypt(line, loadedKey);
            	
                processDecryptedData(decryptedData);
            }
        } catch (IOException e) {
            System.err.println("Error reading medical records: " + e.getMessage());
        }
    }
    
    public void fileHealthInsuranceClaim(String diagnosis, String treatment, String[] medications) {
        try (FileWriter writer = new FileWriter("insurance_claims.txt", true)) {
        	
        	JsonObject claimObject = new JsonObject();
            claimObject.addProperty("patientID", patientID);
            claimObject.addProperty("diagnosis", diagnosis);
            claimObject.addProperty("treatment", treatment);
            JsonArray medicationsArray = new JsonArray();
            for (String medication : medications) {
                medicationsArray.add(medication);
            }
            claimObject.add("medications", medicationsArray);
            claimObject.addProperty("claimStatus", "Pending");

            // Write the insurance claim details to the file
            writer.write(claimObject.toString() + "\n");
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
    
    private void processDecryptedData(String decryptedData) {
//    	System.out.println("Decrypted medical record: " + decryptedData);
    	try {
            // Parse the decrypted JSON data using Gson
            Gson gson = new Gson();
            TreatmentRecord treatmentRecord = gson.fromJson(decryptedData, TreatmentRecord.class);

            // Extract the patient ID from the TreatmentRecord object
            String medicalRecordPatientID = treatmentRecord.getPatientID();

            // Check if the medical record belongs to the target patient
            if (medicalRecordPatientID.equals(patientID)) {
                // Process the decrypted data (e.g., display it)
                System.out.println("Decrypted medical record: " + decryptedData);
            } else {
                // Skip processing if the medical record doesn't belong to the target patient
                System.out.println("Medical record does not belong to the target patient.");
            }
        } catch (JsonSyntaxException e) {
            // Handle JSON parsing errors
            System.err.println("Error parsing decrypted JSON data: " + e.getMessage());
        }
    }
}
