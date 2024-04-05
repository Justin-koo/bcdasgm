package crypto;

import javax.crypto.*;
import java.io.*;
import java.security.*;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

public class MerkleTree {

	public static List<String> splitData(String data, String delimiter) {
	    List<String> dataList = new ArrayList<>();
	    String[] parts = data.split(delimiter);
	    for (String part : parts) {
	        dataList.add(part);
	    }
	    List<String> list = dataList;
	    
	    return list;
	}
	
	// Convert single data string to list with a single element
	public static List<String> convertDataToList(String data) {
        List<String> datasplit = splitData(data, ":"); 
        
        return datasplit;
    }
    
    
    public static String calculateMerkleRoot(List<String> jsonData) {
    	System.out.println("datalist" + jsonData);
        ArrayList<String> tempTreeLayer = new ArrayList<>(jsonData);
        ArrayList<String> treeLayer = tempTreeLayer;
    	System.out.println("datalist" + treeLayer + ",,," + treeLayer.size());
    	System.out.println("datalist" + tempTreeLayer + ",,," + tempTreeLayer.size());
        while (treeLayer.size() > 1) {
            treeLayer = new ArrayList<>();
            for (int i = 0; i < tempTreeLayer.size(); i += 2) {
                // For simplicity, pair each two adjacent elements, or duplicate the last if odd number of elements
                String left = tempTreeLayer.get(i);
                String right = i + 1 < tempTreeLayer.size() ? tempTreeLayer.get(i + 1) : left;
                treeLayer.add(applySha1(left + right));
            }
            tempTreeLayer = treeLayer;
        }
        return treeLayer.size() == 1 ? treeLayer.get(0) : "";
    }

    
 // SHA-1 hash function
    private static String applySha1(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}