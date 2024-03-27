package simulate;

import java.security.KeyPair;

import crypto.Asymmetric;

public class AsymmetricTest {
	public static void main(String[] args) {
        // Test key pair generation
        KeyPair keyPair = Asymmetric.generateKeyPair();

        // Test storing keys for a patient
        String name = "insurance_company";
        Asymmetric.storeKey(keyPair, name);

    }
}
