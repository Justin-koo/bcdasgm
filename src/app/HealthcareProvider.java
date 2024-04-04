package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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
    private static final String CLAIMS_FILE = "insurance_claims.txt";
    private static final String SIGNED_FILE = "signatures.txt";
    private Scanner scanner;
    
    public HealthcareProvider() {
    	this.scanner = new Scanner(System.in);
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
                int claimNumber;
                try {
                    claimNumber = Integer.parseInt(scanner.nextLine());
                    if (claimNumber < 1 || claimNumber > claims.size()) {
                        System.out.println("Invalid claim number. Please enter a valid claim number.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    return;
                }
                
                InsuranceClaim selectedClaim = claims.get(claimNumber - 1);    
                selectedClaim.setClaimStatus("Verified");
                
                // Prompt the healthcare provider to enter total cost and date of service
                double totalCost;
                try {
                    System.out.print("Enter the total cost of service: ");
                    totalCost = Double.parseDouble(scanner.nextLine());
                    if (totalCost < 0) {
                        System.out.println("Invalid total cost. Please enter a valid amount.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    return;
                }
                
                System.out.print("Enter the date of service (YYYY-MM-DD): ");
                String dateOfService = scanner.nextLine();
                
                if (!isValidDate(dateOfService)) {
                    System.out.println("Invalid date. Please enter a valid date in YYYY-MM-DD format.");
                    return;
                }

                // Update the selected claim with entered values
                selectedClaim.setTotalCost(totalCost);
                selectedClaim.setDateOfService(dateOfService);

                String claimJson = new Gson().toJson(selectedClaim);
                
                // Rewrite the entire file with updated claim information
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(CLAIMS_FILE))) {
                    for (InsuranceClaim claim : claims) {
                        String encryptedData = Symmetric.encrypt(claimJson, loadedKey);
                        writer.write(encryptedData + "\n");
                    }
                    System.out.println("Claims file updated successfully.");
                } catch (IOException e) {
                    System.err.println("Error updating claims file: " + e.getMessage());
                }
                
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

    private boolean isValidDate(String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    private boolean isValidDateFormat(String date) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        return date.matches(regex);
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
}
