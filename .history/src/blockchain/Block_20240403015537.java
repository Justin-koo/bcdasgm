package blockchain;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {
    
    private String hash;
    private String previousHash;
    private String data; // Our data will be a simple message.
    private List<String> dataMerkle; // Now a list of data
    private long timeStamp; 
    private String merkleRoot;
    
    // Block Constructor.
    public Block(String data, String previousHash, List<String> dataMerkle) {
        this.data = data;
        this.dataMerkle = dataMerkle;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.merkleRoot = computeMerkleRoot(data);
        this.hash = calculateHash(); // Making sure we do this after we set the other values.
    }

    // Calculate new hash based on blocks contents using SHA-1.
    public String calculateHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            
            // Concatenate data, previousHash, and timeStamp to form input string.
            String input = previousHash + Long.toString(timeStamp) + data;
            
            // Compute hash.
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
    
    // Convert timestamp to human-readable format
    public String getReadableTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(this.timeStamp);
        return sdf.format(date);
    }
    
    // Convert string representation of a block back to a Block object
    public static Block fromString(String blockString, ArrayList<Block> blockchain) {
        try {
            String[] parts = blockString.split(", ");
            String hash = parts[0].substring(parts[0].indexOf("'") + 1, parts[0].lastIndexOf("'"));
            String previousHash = parts[1].substring(parts[1].indexOf("'") + 1, parts[1].lastIndexOf("'"));
            String data = parts[2].substring(parts[2].indexOf("'") + 1, parts[2].lastIndexOf("'"));
            
            // Extract the timestamp value correctly
            long timeStamp = Long.parseLong(parts[3].substring(parts[3].indexOf("=") + 1, parts[3].lastIndexOf("}")));
            
            Block block = new Block(data, previousHash);
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
        return "Block{hash='" + hash + "', previousHash='" + previousHash + "', data='" + data + "', timeStamp=" + timeStamp + "}";
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
