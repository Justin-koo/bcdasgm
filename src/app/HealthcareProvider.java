package app;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import crypto.Symmetric;
import javax.crypto.*;

public class HealthcareProvider {
    private Map<String, String> treatmentData; // Key: patient's medical record number, Value: treatment data
    private static final String TREATMENT_FILE = "treatment_data.txt";
    
    public HealthcareProvider() {
        this.treatmentData = new HashMap<>();
    }

    public void updateTreatmentData(String medicalRecord, String treatment) {
//        treatmentData.put(medicalRecord, treatment);
    	try (FileWriter writer = new FileWriter(TREATMENT_FILE, true)) {
            writer.write("Medical Record Number: " + medicalRecord + "\n");
            writer.write("Treatment Data: " + treatment + "\n\n");
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
