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
	private String data;
	private List<String> dataMerkle;
	private long timeStamp;
	private String merkleRoot;

    
    // Block Constructor.
    public Block(String data, String previousHash, List<String> dataMerkle) {
        this.data = data;
        this.dataMerkle = dataMerkle;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.merkleRoot = computeMerkleRoot(dataMerkle);
        this.hash = calculateHash(); 
    }

    // Constructor without Merkle root
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.merkleRoot = null; 
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

            List<String> dataList = new ArrayList<>();
            String[] dataParts = data.split(",");
            for (String part : dataParts) {
                dataList.add(part.trim());  // Trim to remove any leading or trailing spaces
            }

            // Extract the timestamp value correctly
            long timeStamp = Long.parseLong(parts[3].substring(parts[3].indexOf("=") + 1));
            
            Block block = new Block(data, previousHash, dataList);
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<String> getDataMerkle() {
        return dataMerkle;
    }

    public void setDataMerkle(List<String> dataMerkle) {
        this.dataMerkle = dataMerkle;
        this.merkleRoot = computeMerkleRoot(dataMerkle); // Recalculate Merkle Root if data changes
        this.hash = calculateHash(); // Recalculate hash since data changed
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
