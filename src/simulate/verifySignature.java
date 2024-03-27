package simulate;

import java.security.PublicKey;

import crypto.Asymmetric;
import crypto.DigitalSignature;

public class verifySignature {
	public static void main(String[] args) {
	    // Load the public key of the medical provider
	    PublicKey publicKey = Asymmetric.loadPublicKey("mdProvider");
	    
	    // Assume the following as an example claim data
	    String originalClaimData = "{\"claimID\":\"f4c4ab38-1f22-4fa8-8edb-d22ee5bd78f7\",\"patientID\":\"222\",\"diagnosis\":\"2\",\"treatment\":\"2\",\"medications\":[\"2\"],\"claimStatus\":\"Pending\"}";
	    
	    // Assume the following as an example signature
	    String signature = "JDMPdj6HvSd6xL5UmRWM6MkrJQRzI70KVRAkhYSbfz+wOxBACgAnuYzJqFyljeGZomMYsruKgdxDQx0UAg50o6rylw6fIF+FKy40VhlV6XgKzvp/d8O9UHEEJ3KH4Kth36/i9diJ1pS35uzGnPW+q25bf13wxiTcMOeBGBNE6aTJBvBakoxILcHSCv8tYU9OVYLth1GG+J/Gtp2eqqBGQsE3JW9mQYBjItkvfGskgyM/DtAWPkPHxa2ut39t8dfeMaDIrbPof5r2yhpqnNPZs3IKv985r54wcFd6f8OndihBMOHHfib+vPmm178SxnVlqf5F5FEzKKpjcrPLanKBhg==";
	    
	    // Verify the digital signature
	    boolean signatureValid = DigitalSignature.verify(originalClaimData, signature, publicKey);
	    
	    if (signatureValid) {
	        System.out.println("Digital signature verification passed. The claim data is authentic.");
	    } else {
	        System.out.println("Digital signature verification failed. The claim data may have been tampered with.");
	    }
	}

}
