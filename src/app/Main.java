package app;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static HealthcareProvider provider = new HealthcareProvider();
    private static InsuranceCompany insuranceCompany = new InsuranceCompany();

    public static void main(String[] args) {
        displayRoleSelectionMenu();
    }

    private static void displayRoleSelectionMenu() {
        System.out.println("\nWelcome to the Healthcare System CLI");
        System.out.println("Please select your role:");
        System.out.println("1. Patient/Consumer");
        System.out.println("2. Healthcare Provider");
        System.out.println("3. Insurance Company");
        System.out.println("4. Exit");

        int choice = getUserChoice();
        switch (choice) {
            case 1:
            	System.out.print("Enter your patient ID: ");
                String patientID = scanner.nextLine();
                Patient patient = new Patient(patientID);
                
                displayPatientMenu(patient);
                break;
            case 2:
                displayHealthcareProviderMenu(provider);
                break;
            case 3:
                displayInsuranceCompanyMenu();
                break;
            case 4:
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayRoleSelectionMenu();
        }
    }

    private static int getUserChoice() {
    	int choice = 0;
        while (true) {
            try {
                System.out.print("Enter your choice: ");
                choice = Integer.parseInt(scanner.nextLine());
                break; // Break the loop if the input is successfully parsed
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }

    private static void displayPatientMenu(Patient patient) {
        System.out.println("\nPatient/Consumer Menu");
        System.out.println("1. View medical records");
        System.out.println("2. File health insurance claim");
        System.out.println("3. View insurance claim status");
        System.out.println("4. Go back");

        int choice = getUserChoice();
        
        switch (choice) {
            case 1:
            	patient.viewMedicalRecords();
            	
            	displayPatientMenu(patient);
                break;
            case 2:
            	System.out.print("Enter diagnosis: ");
                String diagnosis = scanner.nextLine();
                
                System.out.print("Enter treatment: ");
                String treatment = scanner.nextLine();
                
                System.out.print("Enter medications (separated by comma): ");
                String[] medications = scanner.nextLine().split(",");

                // File the health insurance claim with the collected details
                patient.fileHealthInsuranceClaim(diagnosis, treatment, medications);
            	
            	displayPatientMenu(patient);
                break;
            case 3:
            	patient.viewInsuranceClaimStatus();
            	
            	displayPatientMenu(patient);
                break;
            case 4:
                displayRoleSelectionMenu();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayPatientMenu(patient);
        }
    }

    private static void displayHealthcareProviderMenu(HealthcareProvider provider) {
        System.out.println("\nHealthcare Provider Menu");
        System.out.println("1. Update treatment data");
        System.out.println("2. Verify health insurance claim");
        System.out.println("3. Go back");

        int choice = getUserChoice();
        switch (choice) {
            case 1:
//            	need to add more parameters: digital signature, who wrote this
                System.out.print("Enter patient's medical record number: ");
                String medicalRecord = scanner.nextLine();

                System.out.print("Enter treatment data: ");
                String treatmentData = scanner.nextLine();

//                change this to auto get current data in the correct format
                System.out.print("Enter date (YYYY-MM-DD): ");
                String date = scanner.nextLine();

                System.out.print("Enter patient ID: ");
                String patientID = scanner.nextLine();

                provider.updateTreatmentData(medicalRecord, treatmentData, date, patientID);
                System.out.println("Treatment data updated successfully.");

                displayHealthcareProviderMenu(provider);
                break;
            case 2:
            	provider.verifyHealthInsuranceClaim();
            	
            	displayHealthcareProviderMenu(provider);
            	break;
            case 3:
                displayRoleSelectionMenu();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayHealthcareProviderMenu(provider);
        }
    }

    private static void displayInsuranceCompanyMenu() {
        System.out.println("\nInsurance Company Menu");
        System.out.println("1. Process insurance claim");
        System.out.println("2. Go back");

        int choice = getUserChoice();
        switch (choice) {
            case 1:
                insuranceCompany.processInsuranceClaim();
                
            	displayInsuranceCompanyMenu();
                break;
            case 2:
                displayRoleSelectionMenu();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayInsuranceCompanyMenu();
        }
    }
}


