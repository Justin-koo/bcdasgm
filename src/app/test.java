package app;

import crypto.Symmetric;
import javax.crypto.*;
import java.security.*;
import java.util.*;

public class test {
	public static void main(String[] args) {
		SecretKey encryptionKey = Symmetric.generateKey();
        // Create a patient
        Patient patient = new Patient("John Doe");

        // Create a healthcare provider
        HealthcareProvider healthcareProvider = new HealthcareProvider("Dr. Smith", encryptionKey);

        // Healthcare provider inputs treatment data and updates patient records
        healthcareProvider.inputTreatmentData(patient, "Prescribed medication: Paracetamol, Dosage: 500mg");
        healthcareProvider.inputTreatmentData(patient, "Follow-up appointment scheduled for next week");

        // Display the updated medical record
        System.out.println("Updated Medical Record:");
        System.out.println(patient.accessMedicalRecords());
    }
}
