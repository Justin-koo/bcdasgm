package app;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class InsuranceClaim {
    private String claimID;
    private String patientID;
    private String diagnosis;
    private String treatment;
    private String[] medications;
    private String claimStatus;
    private Double totalCost;
    private String dateOfService;

    public InsuranceClaim(String claimID, String patientID, String diagnosis, String treatment, String[] medications, String claimStatus, Double totalCost, String dateOfService) {
        this.claimID = claimID;
        this.patientID = patientID;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.medications = medications;
        this.claimStatus = claimStatus;
        this.totalCost = totalCost;
        this.dateOfService = dateOfService;
    }

    public String getClaimID() {
        return claimID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public String[] getMedications() {
        return medications;
    }

    public String getClaimStatus() {
        return claimStatus;
    }
    
    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }
    
    public String toJson() {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("claimID", claimID);
        jsonObject.addProperty("patientID", patientID);
        jsonObject.addProperty("diagnosis", diagnosis);
        jsonObject.addProperty("treatment", treatment);
        JsonArray medicationsArray = gson.toJsonTree(medications).getAsJsonArray();
        jsonObject.add("medications", medicationsArray);
        jsonObject.addProperty("claimStatus", claimStatus);
        return gson.toJson(jsonObject);
    }
    
    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getDateOfService() {
        return dateOfService;
    }

    public void setDateOfService(String dateOfService) {
        this.dateOfService = dateOfService;
    }

    public static InsuranceClaim fromJson(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String claimID = jsonObject.get("claimID").getAsString();
        String patientID = jsonObject.get("patientID").getAsString();
        String diagnosis = jsonObject.get("diagnosis").getAsString();
        String treatment = jsonObject.get("treatment").getAsString();
        JsonArray medicationsArray = jsonObject.get("medications").getAsJsonArray();
        String[] medications = gson.fromJson(medicationsArray, String[].class);
        String claimStatus = jsonObject.get("claimStatus").getAsString();
        double totalCost = jsonObject.get("totalCost").getAsDouble();
        String dateOfService = jsonObject.get("dateOfService").getAsString();
        return new InsuranceClaim(claimID, patientID, diagnosis, treatment, medications, claimStatus, totalCost, dateOfService);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Claim ID: ").append(claimID).append("\n");
        sb.append("Patient ID: ").append(patientID).append("\n");
        sb.append("Diagnosis: ").append(diagnosis).append("\n");
        sb.append("Treatment: ").append(treatment).append("\n");
        sb.append("Medications: ");
        for (int i = 0; i < medications.length; i++) {
            sb.append(medications[i]);
            if (i < medications.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("\nTotal Cost: ").append(totalCost).append("\n");
        sb.append("Date of Service: ").append(dateOfService).append("\n");
        sb.append("Claim Status: ").append(claimStatus);
        return sb.toString();
    }

}
