package blockchain;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block implements Serializable{
    
    private String hash;
    private String previousHash;
    private String data; // Our data will be a simple message.
    private long timeStamp; 
    private String merkleRoot; // Merkle Root
    
    
    // Block Constructor.
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
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
    
    public static Block fromString(String blockString, ArrayList<Block> blockchain) {
        try {
            // Remove the "Block{" and "}" from the beginning and end of the blockString
            blockString = blockString.replace("Block{", "").replace("}", "");
            
            // Split the blockString into parts based on ","
            String[] parts = blockString.split(", ");
            
            // Extract hash, previousHash, and data from the parts
            String hash = parts[0].substring(parts[0].indexOf("'") + 1, parts[0].lastIndexOf("'"));
            String previousHash = parts[1].substring(parts[1].indexOf("'") + 1, parts[1].lastIndexOf("'"));
            String data = parts[2].substring(parts[2].indexOf("'") + 1, parts[2].lastIndexOf("'"));
            

            // Extract the timestamp value correctly
            long timeStamp = Long.parseLong(parts[3].substring(parts[3].indexOf("=") + 1));
            
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
        return "Block{hash='" + hash + "', previousHash='" + previousHash + "', data='" + data + "', timeStamp=" + timeStamp + "'}";
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

    public String getMerkle() {
        return MerkleRoot;
    }

    public void setMerkle(String MerkleRoot) {
        this.MerkleRoot = MerkleRoot;
    }


    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    
 // Serialize the Block object to a binary file
    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            System.out.println("Block saved successfully to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving block to file: " + e.getMessage());
        }
    }

    // Deserialize the Block object from a binary file
    public static Block loadFromFile(String filename) {
        Block block = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            block = (Block) ois.readObject();
            System.out.println("Block loaded successfully from " + filename);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading block from file: " + e.getMessage());
        }
        return block;
    }
}
