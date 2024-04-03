package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.crypto.SecretKey;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import crypto.Asymmetric;
import crypto.DigitalSignature;
import crypto.Symmetric;
import blockchain.Block;
import blockchain.Blockchain;

public class InsuranceCompany {
    private static final String SIGNED_FILE = "signatures.txt";
    private static final String CLAIMS_FILE = "insurance_claims.txt";
    private SecretKey loadedKey;
    private Scanner scanner;
    List<InsuranceClaim> claims;
    protected Blockchain blockchain;
    protected Block block;

    public InsuranceCompany() {
        loadedKey = Symmetric.loadKey("InsuranceClaim");
        this.scanner = new Scanner(System.in);
        claims = new ArrayList<>();
        blockchain = new Blockchain();
        
    }

    public void processInsuranceClaim() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CLAIMS_FILE))){

            System.out.println("\nProcessing Insurance Claims:");
            String line;
            int i = 0;
            List<InsuranceClaim> verifiedClaims = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String decryptedData = Symmetric.decrypt(line, loadedKey);
                InsuranceClaim claim = new Gson().fromJson(decryptedData, InsuranceClaim.class);
                claims.add(claim);
                
                String signature = retrieveSignature(claim.getClaimID());
                if (signature != null) {
                    PublicKey publicKey = Asymmetric.loadPublicKey("HealthcareProvider");
                    String claimJson = new Gson().toJson(claim);

                    if (DigitalSignature.verify(claimJson, signature, publicKey)) {
                        System.out.println((i + 1) + ". " + claim + "\n");
                        verifiedClaims.add(claim);
                        i++;
                    }
                }
            }
            
            if(verifiedClaims.size() > 0) {
            	while(true) {
            		try {
		            	System.out.print("Enter the number of the claim you want to approve (or type '0' to cancel): ");
			            int claimNumber = Integer.parseInt(scanner.nextLine());
			
			            if(claimNumber == 0) {
			            	return;
			            }
			            
			            if (claimNumber <= verifiedClaims.size()) {
	                        InsuranceClaim selectedClaim = verifiedClaims.get(claimNumber - 1);
	                        approveClaim(selectedClaim);
	                        
	                     // Update the digital signature
	                        String claimJson = new Gson().toJson(selectedClaim);
	                        String newSignature = DigitalSignature.sign(claimJson, Asymmetric.loadPrivateKey("InsuranceCompany"));
	                        updateSignature(selectedClaim.getClaimID(), newSignature);
           
	                        break;
	                        
	                    } else {
	                        System.out.println("Invalid claim number. Please try again.");
	                    }
			            
		            } catch (NumberFormatException e) {
		            	System.out.println("Invalid input. Please enter a number or '0' to cancel.");
		            }
            	}
            	
            } else {
            	System.out.println("No claim is signed yet");
            }
            
            
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error processing insurance claims: " + e.getMessage());
        }
    }
    
    
    private void addClaimToBlockchain(InsuranceClaim claim, List<String> dataMerkle) {
        String merkleRoot = calculateMerkleRoot(dataMerkle); // Calculate Merkle root
        Block newBlock = new Block(claim.toJson(), blockchain.getLatestBlock().getHash(), Block.computeMerkleRoot(claim.toJson()));
        blockchain.addBlock(newBlock);
        System.out.println("Claim added to blockchain successfully.");
        Blockchain.saveBlockchain(blockchain);  // Save the updated blockchain
    }
    
    private void updateSignature(String claimID, String newSignature) {
        // Read existing signatures from the file
        StringBuilder updatedSignatures = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(SIGNED_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JsonObject signatureObject = new Gson().fromJson(line, JsonObject.class);
                String encryptedClaimID = signatureObject.get("claimID").getAsString();

                // Decrypt the claim ID using insurance company's private key
                String decryptedClaimID = Asymmetric.decrypt(encryptedClaimID, Asymmetric.loadPrivateKey("InsuranceCompany"));

                // Check if this is the claim ID we want to update
                if (decryptedClaimID.equals(claimID)) {
                    // Update the signature for the matching claim ID
                    signatureObject.addProperty("signature", newSignature);
                }

                // Append the updated or unchanged signature to the string builder
                updatedSignatures.append(signatureObject.toString()).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading signatures file: " + e.getMessage());
            return; // Exit if an error occurs while reading the file
        }

        // Write back the updated signatures to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SIGNED_FILE))) {
            writer.write(updatedSignatures.toString());
            System.out.println("Signature updated successfully for claim ID: " + claimID);
        } catch (IOException e) {
            System.err.println("Error updating signature: " + e.getMessage());
        }
    }

    private String retrieveSignature(String claimID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(SIGNED_FILE))) {
            PrivateKey privateKey = Asymmetric.loadPrivateKey("InsuranceCompany");
            String line;

            while ((line = reader.readLine()) != null) {
                JsonObject signatureObject = new Gson().fromJson(line, JsonObject.class);
                String encryptedClaimID = signatureObject.get("claimID").getAsString();
                String encryptedSignature = signatureObject.get("signature").getAsString();
                String decryptedClaimID = Asymmetric.decrypt(encryptedClaimID, privateKey);

                if (decryptedClaimID.equals(claimID)) {
                    return encryptedSignature;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading signatures file: " + e.getMessage());
        }
        return null;
    }

    private void approveClaim(InsuranceClaim selectedClaim) {
        try {
            System.out.println("\nClaim Details:");
            System.out.println(selectedClaim.toString() + "\n");

            String approval;
            boolean validInput = false;

            // Loop until valid input is provided
            while (!validInput) {
                System.out.print("Do you want to approve this claim? (Y/N): ");
                approval = scanner.nextLine().trim().toUpperCase();

                if (approval.equals("Y")) {
                    selectedClaim.setClaimStatus("Approved");
                    System.out.println("Claim approved.");
                    validInput = true; // Exit the loop

                    // Add the claim to the blockchain
                    addClaimToBlockchain(selectedClaim);

                    // Update the digital signature
                    String claimJson = new Gson().toJson(selectedClaim);
                    String newSignature = DigitalSignature.sign(claimJson, Asymmetric.loadPrivateKey("InsuranceCompany"));
                    updateSignature(selectedClaim.getClaimID(), newSignature);

                    break;

                } else if (approval.equals("N")) {
                    selectedClaim.setClaimStatus("Rejected");
                    System.out.println("Claim rejected.");
                    validInput = true; // Exit the loop
                } else {
                    System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                }
            }

            // Update the status of the selected claim directly in the list
            for (InsuranceClaim claim : claims) {
                if (claim.getClaimID().equals(selectedClaim.getClaimID())) {
                    claim.setClaimStatus(selectedClaim.getClaimStatus());
                    break; // Exit the loop after updating the status
                }
            }

            // Rewrite the claims file with the updated list
            rewriteClaimsToFile(claims);
        } catch (IOException e) {
            System.err.println("Error approving/rejecting claim: " + e.getMessage());
        }
    }

    private void rewriteClaimsToFile(List<InsuranceClaim> claims) throws IOException {
        try (FileWriter writer = new FileWriter(CLAIMS_FILE)) {
            for (InsuranceClaim claim : claims) {
                String claimJson = new Gson().toJson(claim);
                String encryptedClaim = Symmetric.encrypt(claimJson, loadedKey);
                writer.write(encryptedClaim + "\n");
            }
            System.out.println("Claims file updated successfully.");
        }
    }

}