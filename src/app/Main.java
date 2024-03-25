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
        System.out.println("Welcome to the Healthcare System CLI");
        System.out.println("Please select your role:");
        System.out.println("1. Patient/Consumer");
        System.out.println("2. Healthcare Provider");
        System.out.println("3. Insurance Company");
        System.out.println("4. Exit");

        int choice = getUserChoice();
        switch (choice) {
            case 1:
                displayPatientMenu();
                break;
            case 2:
                displayHealthcareProviderMenu();
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

    private static void displayPatientMenu() {
        System.out.println("Patient/Consumer Menu");
        System.out.println("1. View medical records");
        System.out.println("2. File health insurance claim");
        System.out.println("3. View insurance claim status");
        System.out.println("4. Go back");

        int choice = getUserChoice();
        switch (choice) {
            case 1:
                // Implement viewing medical records functionality
                break;
            case 2:
                // Implement filing health insurance claim functionality
                break;
            case 3:
                // Implement viewing insurance claim status functionality
                break;
            case 4:
                displayRoleSelectionMenu();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayPatientMenu();
        }
    }

    private static void displayHealthcareProviderMenu() {
        System.out.println("Healthcare Provider Menu");
        System.out.println("1. Update treatment data");
        System.out.println("2. Go back");

        int choice = getUserChoice();
        switch (choice) {
            case 1:
            	HealthcareProvider provider = new HealthcareProvider();

                System.out.print("Enter patient's medical record number: ");
                String medicalRecord = scanner.nextLine();

                System.out.print("Enter treatment data: ");
                String treatmentData = scanner.nextLine();

                System.out.print("Enter date (YYYY-MM-DD): ");
                String date = scanner.nextLine();

                System.out.print("Enter patient ID: ");
                String patientID = scanner.nextLine();

                provider.updateTreatmentData(medicalRecord, treatmentData, date, patientID);
                System.out.println("Treatment data updated successfully.");

                displayHealthcareProviderMenu();
                break;
            case 2:
                displayRoleSelectionMenu();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayHealthcareProviderMenu();
        }
    }

    private static void displayInsuranceCompanyMenu() {
        System.out.println("Insurance Company Menu");
        System.out.println("1. Process insurance claim");
        System.out.println("2. Go back");

        int choice = getUserChoice();
        switch (choice) {
            case 1:
                // Implement processing insurance claim functionality
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


