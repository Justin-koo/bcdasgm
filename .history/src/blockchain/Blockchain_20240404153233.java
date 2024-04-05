package blockchain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import crypto.MerkleTree;


public class Blockchain implements Serializable{

    private ArrayList<Block> blockchain;
    private static final String BLOCKCHAIN_FILE = "blockchain.bin";

    // Blockchain Constructor.
    public Blockchain() {
        this.blockchain = new ArrayList<>();
        File blockchainFile = new File(BLOCKCHAIN_FILE);

        if (blockchainFile.exists() && blockchainFile.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(blockchainFile))) {
                blockchain = (ArrayList<Block>) ois.readObject();
                System.out.println("Blockchain loaded successfully from " + BLOCKCHAIN_FILE);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading blockchain: " + e.getMessage());
            }
        } else {
            System.out.println("Blockchain file not found or empty. Creating new blockchain.");
            List<String> genesisData = new ArrayList<>();
            genesisData.add("Genesis Block");
            Block genesisBlock = new Block("Genesis Block", "0", genesisData);
            blockchain.add(genesisBlock);
        }
    }

    
    public Blockchain(ArrayList<Block> blocks) {
        this.blockchain = blocks;
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

            // Verify Merkle Root
            List<String> transactions = currentBlock.getData();
            String calculatedMerkleRoot = MerkleTree.calculateMerkleRoot(transactions);
            if (!calculatedMerkleRoot.equals(currentBlock.getMerkleRoot())) {
                System.out.println("Merkle Root mismatch in block: " + currentBlock.getHash());
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

            // Extracting the timestamp
            String[] timeStampParts = parts[3].substring(parts[3].indexOf(":") + 2).split("Hash");
            long timeStamp = Long.parseLong(timeStampParts[0].trim());

            // Extracting the data and parsing it as JSON
            String dataString = parts[2].substring(parts[2].indexOf(":") + 2);
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
    
    public static void saveBlockchain(Blockchain blockchain) {
        if (blockchain.isChainValid()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BLOCKCHAIN_FILE))) {
                oos.writeObject(blockchain.getBlockchain());
                System.out.println("Blockchain saved successfully to " + BLOCKCHAIN_FILE);
            } catch (IOException e) {
                System.err.println("Error saving blockchain: " + e.getMessage());
            }
        } else {
            System.err.println("Blockchain is not valid. Unable to save.");
        }
    }

}