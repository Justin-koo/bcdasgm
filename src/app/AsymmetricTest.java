package app;

import java.security.KeyPair;

import crypto.Asymmetric;

public class AsymmetricTest {
	public static void main(String[] args) {
        // Test key pair generation
        KeyPair keyPair = Asymmetric.generateKeyPair();

        // Test storing keys for a patient
        String patientID = "Patient123";
        Asymmetric.storeKeyForPatient(keyPair, patientID);

        // Additional testing (encryption, decryption, etc.) can be performed here
    }
}
