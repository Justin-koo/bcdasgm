package blockchain;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class Blockchain {

    private ArrayList<Block> blockchain;

    // Blockchain Constructor.
    public Blockchain() {
        this.blockchain = new ArrayList<>();
        // Create the genesis block (the first block in the blockchain).
        Block genesisBlock = new Block("Genesis Block", "0");
        blockchain.add(genesisBlock);
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
        }

        return true;
    }

    // Method to display the contents of the blockchain.
    public void displayBlockchain() {
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);
    }

    // Helper method to get the latest block in the blockchain.
    public Block getLatestBlock() {
        return blockchain.get(blockchain.size() - 1);
    }
}
