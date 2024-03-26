package app;

import java.io.Serializable;

public class TreatmentRecord {
	private String medicalRecord;
    private String treatment;
    private String date;
    private String patientID;

    public TreatmentRecord(String medicalRecord, String treatment, String date, String patientID) {
        this.medicalRecord = medicalRecord;
        this.treatment = treatment;
        this.date = date;
        this.patientID = patientID;
    }
    
    public String getMedicalRecord() {
        return medicalRecord;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getDate() {
        return date;
    }

    public String getPatientID() {
        return patientID;
    }
}

