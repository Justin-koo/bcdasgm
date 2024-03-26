package app;

import java.util.Map;
import java.io.*;
import com.google.gson.Gson;
import crypto.Symmetric;
import javax.crypto.SecretKey;

public class HealthcareProvider {
    private Map<String, String> treatmentData; // Key: patient's medical record number, Value: treatment data
    private static final String TREATMENT_FILE = "treatment_records.txt";
    
    public HealthcareProvider() {
    }

    public void updateTreatmentData(String medicalRecord, String treatment, String date, String patientID) {
        try (FileWriter writer = new FileWriter(TREATMENT_FILE, true)) {
            Gson gson = new Gson();
            
            TreatmentRecord record = new TreatmentRecord(medicalRecord, treatment, date, patientID);
            String jsonRecord = gson.toJson(record);
            
         // Load the symmetric encryption key
            SecretKey loadedKey = Symmetric.loadKey();
            String encryptedData = Symmetric.encrypt(jsonRecord, loadedKey);
            
            writer.write(encryptedData + "\n");
            System.out.println("Treatment data updated and saved to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    public String getTreatmentData(String medicalRecord) {
        return treatmentData.getOrDefault(medicalRecord, "No treatment data available.");
    }

    // Other methods as needed
}
