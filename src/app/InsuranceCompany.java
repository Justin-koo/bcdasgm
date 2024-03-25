package app;

import java.util.Map;
import java.util.HashMap;

public class InsuranceCompany {
    private Map<String, Boolean> insuranceClaims; // Key: claim ID, Value: approval status

    public InsuranceCompany() {
        this.insuranceClaims = new HashMap<>();
    }

    public void processInsuranceClaim(String claimID) {
        // Medical providers have already verified the claim before it reaches the insurance company
        // Simulate approval process
        boolean isApproved = Math.random() < 0.8; // 80% chance of approval
        insuranceClaims.put(claimID, isApproved);
        System.out.println("Claim ID: " + claimID + " processed. Approval status: " + (isApproved ? "Approved" : "Rejected"));
    }

    // Other methods as needed
}
