package blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class Blockchain implements Serializable{

    private ArrayList<Block> blockchain;

    // Blockchain Constructor.
    public Blockchain() {
        this.blockchain = new ArrayList<>();
        // Create the genesis block (the first block in the blockchain).
//        if (blockchain.isEmpty()) {
//	        // If the blockchain is empty, create the genesis block
//	        Block genesisBlock = new Block("Genesis Block", "0");
//	        blockchain.add(genesisBlock);
//	    }
    }
    
    public Blockchain(ArrayList<Block> blocks) {
        this.blockchain = blocks;
    }
    
    public static Blockchain createGenesis() {
    	Blockchain blockchain = new Blockchain();
        Block genesisBlock = new Block("Genesis Block", "0");
        blockchain.addBlock(genesisBlock);
        return blockchain;
    }

    // Method to add a new block to the blockchain.
    public void addBlock(Block newBlock) {
        newBlock.setPreviousHash(getLatestBlock().getHash());
        
        newBlock.setHash(newBlock.calculateHash());
        blockchain.add(newBlock);
    }

    // Method to check if the blockchain is valid.
    public boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        // Iterate over the blockchain to check hashes.
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            // Compare registered hash and calculated hash.
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            // Compare previous hash and registered previous hash.
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            
            // Check if the timestamp of the current block is greater than or equal to the previous block.
            if (currentBlock.getTimeStamp() <= previousBlock.getTimeStamp()) {
                System.out.println("Timestamps are not in chronological order");
                return false;
            }
        }

        return true;
    }

    // Method to display the contents of the blockchain.
    public void displayBlockchain() {
        for (Block block : blockchain) {
            System.out.println("Hash: " + block.getHash());
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Data: " + block.getData());
            System.out.println("Timestamp: " + block.getReadableTimeStamp()); // Change here
            System.out.println("---------------------------------------");
        }
    }

    // Helper method to get the latest block in the blockchain.
    public Block getLatestBlock() {
        return blockchain.get(blockchain.size() - 1);
    }
    
    public static Blockchain fromString(String blockchainString) {
        Blockchain blockchain = new Blockchain();
        String[] blockStrings = blockchainString.split("\n");

        for (String blockString : blockStrings) {
            // Split each block string into its components
            String[] parts = blockString.split(", ");
            
            // Extracting hash and previous hash
            String hash = parts[0].substring(parts[0].indexOf(":") + 2);
            String previousHash = parts[1].substring(parts[1].indexOf(":") + 2);
            
            // Extracting data (which might be a JSON string)
            String data = parts[2].substring(parts[2].indexOf(":") + 2);

            // Extracting the timestamp
            String[] timeStampParts = parts[3].substring(parts[3].indexOf(":") + 2).split("Hash");
            long timeStamp = Long.parseLong(timeStampParts[0].trim());

            // Only add the block if it doesn't already exist in the blockchain
            Block block = Block.fromString(blockString, blockchain.getBlockchain());
            if (block != null) {
                blockchain.addBlock(block);
            }
        }

        return blockchain;
    }









    @Override
    public String toString() {
        StringBuilder blockchainString = new StringBuilder();
        for (Block block : blockchain) {
            blockchainString.append("Hash: ").append(block.getHash()).append(", ")
                            .append("Previous Hash: ").append(block.getPreviousHash()).append(", ")
                            .append("Data: ").append(block.getData()).append(", ")
                            .append("Timestamp: ").append(block.getTimeStamp())
                            .append("\n");
        }
        return blockchainString.toString();
    }

    
    public ArrayList<Block> getBlockchain() {
        return blockchain;
    }



    
    
}