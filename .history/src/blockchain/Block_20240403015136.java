package blockchain;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {
    
    private String hash;
    private String previousHash;
    private List<String> data; // Now a list of data
    private long timeStamp; 
    private String merkleRoot;
    
    // Block Constructor.
    public Block(List<String> data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.merkleRoot = computeMerkleRoot(data);
        this.hash = calculateHash(); // Include Merkle Root in hash calculation
    }

    // Calculate new hash based on blocks contents using SHA-1
    public String calculateHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            String input = previousHash + Long.toString(timeStamp) + merkleRoot;
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Simplified Merkle Root calculation
    public static String computeMerkleRoot(List<String> data) {
        ArrayList<String> tempTreeLayer = new ArrayList<>(data);
        ArrayList<String> treeLayer = tempTreeLayer;
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

    
    // Convert timestamp to human-readable format
    public String getReadableTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(this.timeStamp);
        return sdf.format(date);
    }
    
    public static Block fromString(String blockString, ArrayList<Block> blockchain) {
        try {
            String[] parts = blockString.split(", ");
            String hash = parts[0].substring(parts[0].indexOf("'") + 1, parts[0].lastIndexOf("'"));
            String previousHash = parts[1].substring(parts[1].indexOf("'") + 1, parts[1].lastIndexOf("'"));
            
            // Assuming the data string should be split into multiple items.
            // This is a placeholder; you'll need to adjust it based on your actual data format.
            List<String> dataList = new ArrayList<>();
            String dataPart = parts[2].substring(parts[2].indexOf("'") + 1, parts[2].lastIndexOf("'"));
            dataList.add(dataPart); // This is a simplification. You might want to split the data string into multiple parts.
    
            // Extract the timestamp value correctly
            long timeStamp = Long.parseLong(parts[3].substring(parts[3].indexOf("=") + 1, parts[3].lastIndexOf("}")));
            
            Block block = new Block(dataList, previousHash);
            block.setHash(hash);
            block.setTimeStamp(timeStamp);
            
            return block;
        } catch (Exception e) {
            System.err.println("Error parsing block: " + e.getMessage());
            return null;
        }
    }
    





    @Override
    public String toString() {
        return "Block{hash='" + hash + "', previousHash='" + previousHash + "', data='" + data + "', timeStamp=" + timeStamp + ", merkleRoot='" + merkleRoot + "'}";
    }


    // Getters and setters:

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
        this.merkleRoot = computeMerkleRoot(data); // Recalculate Merkle Root if data changes
        this.hash = calculateHash(); // Recalculate hash since data changed
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    


}
