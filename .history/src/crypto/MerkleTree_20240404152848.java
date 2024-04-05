package crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {

    // Convert single data string to list with a single element
    private List<String> convertDataToList(String data) {
        List<String> dataList = new ArrayList<>();
        dataList.add(data);
        return dataList;
    }

    public static String calculateMerkleRoot(String data) {
        List<String> dataList = convertDataToList(data);
        List<String> tree = new ArrayList<>(dataList);

        while (tree.size() > 1) {
            List<String> newTree = new ArrayList<>();

            // Calculate hashes pairwise and add them to the new tree
            for (int i = 0; i < tree.size(); i += 2) {
                String left = tree.get(i);
                String right = (i + 1 < tree.size()) ? tree.get(i + 1) : left; // Handle odd number of transactions

                // Concatenate and hash left and right transaction data
                String concatenatedHash = left + right;
                String hash = calculateSHA1Hash(concatenatedHash);

                newTree.add(hash);
            }

            tree = newTree;
        }

        // Return the Merkle Root
        return tree.get(0);
    }

    private static String calculateSHA1Hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = digest.digest(data.getBytes());

            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Handle error
        }
    }
}
