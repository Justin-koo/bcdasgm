package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Patient {
	private Scanner scanner;

    public Patient() {
        this.scanner = new Scanner(System.in);
    }
    
    public void viewMedicalRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader("treatment_records.txt"))) {
            String line;
            System.out.println("Medical Records:");
            while ((line = reader.readLine()) != null) {
                // Display each treatment record
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading medical records: " + e.getMessage());
        }
    }
    
    public void fileHealthInsuranceClaim() {
        try (FileWriter writer = new FileWriter("insurance_claims.txt", true)) {
            // Collect input for the insurance claim
            System.out.print("Enter your insurance claim details: ");
            String claimDetails = scanner.nextLine();

            // Write the claim details to the file
            writer.write(claimDetails + "\n");
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
