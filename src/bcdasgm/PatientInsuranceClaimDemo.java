package bcdasgm;

import java.util.*;

//Define a class for representing a Patient
class Patient {
 private String name;
 private String medicalRecord;
 private List<HealthInsuranceClaim> insuranceClaims;

 // Constructor to initialize a patient with name
 public Patient(String name) {
     this.name = name;
     this.medicalRecord = ""; // Initialize an empty medical record
     this.insuranceClaims = new ArrayList<>();
 }

 // Method to access medical records securely
 public String accessMedicalRecords() {
     // Simulate the process of accessing medical records securely
     return medicalRecord;
 }

 // Method to file a health insurance claim electronically
 public void fileInsuranceClaim(HealthInsuranceClaim claim) {
     // Add the claim to the list of insurance claims filed by the patient
     insuranceClaims.add(claim);
     // Simulate the process of filing a health insurance claim electronically
     System.out.println("Health insurance claim filed successfully for patient: " + name);
 }

 // Method to view the status of insurance claims
 public List<HealthInsuranceClaim> viewInsuranceClaimsStatus() {
     // Simulate the process of viewing the status of insurance claims
     return insuranceClaims;
 }
}

//Define a class for representing a Health Insurance Claim
class HealthInsuranceClaim {
 private String claimDetails;
 private boolean claimStatus; // True for approved, false for denied

 // Constructor to initialize a health insurance claim with details
 public HealthInsuranceClaim(String claimDetails) {
     this.claimDetails = claimDetails;
     this.claimStatus = false; // Initialize claim status as denied
 }

 // Method to get the claim details
 public String getClaimDetails() {
     return claimDetails;
 }

 // Method to set the claim status
 public void setClaimStatus(boolean status) {
     this.claimStatus = status;
 }

 // Method to get the claim status
 public boolean getClaimStatus() {
     return claimStatus;
 }
}

//Main class to demonstrate the functionality of patients accessing medical records and filing insurance claims
public class PatientInsuranceClaimDemo {
 public static void main(String[] args) {
     // Create a patient
     Patient patient = new Patient("John Doe");

     // Simulate updating the patient's medical record
     patient.accessMedicalRecords();

     // Simulate filing a health insurance claim
     HealthInsuranceClaim claim1 = new HealthInsuranceClaim("Treatment for flu");
     patient.fileInsuranceClaim(claim1);

     // Simulate viewing the status of insurance claims
     List<HealthInsuranceClaim> claimsStatus = patient.viewInsuranceClaimsStatus();
     for (HealthInsuranceClaim claim : claimsStatus) {
         System.out.println("Claim details: " + claim.getClaimDetails());
         System.out.println("Claim status: " + (claim.getClaimStatus() ? "Approved" : "Denied"));
     }
 }
}
