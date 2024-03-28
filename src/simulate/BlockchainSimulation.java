package simulate;

import app.InsuranceClaim;
import blockchain.Block;
import blockchain.Blockchain;

public class BlockchainSimulation {

    public static void main(String[] args) {
        // Create some sample insurance claims
        InsuranceClaim claim1 = new InsuranceClaim("claim1", "patient1", "diagnosis1", "treatment1", new String[]{"medication1"}, "Pending");
        InsuranceClaim claim2 = new InsuranceClaim("claim2", "patient2", "diagnosis2", "treatment2", new String[]{"medication2"}, "Approved");

        // Create a new blockchain
        Blockchain blockchain = new Blockchain();

        // Create and add blocks to the blockchain
        blockchain.addBlock(new Block("Genesis Block", "0"));
        blockchain.addBlock(new Block(claim1.toJson(), blockchain.getLatestBlock().getHash()));
        blockchain.addBlock(new Block(claim2.toJson(), blockchain.getLatestBlock().getHash()));

        // Validate the blockchain
        boolean isValid = blockchain.isChainValid();
        System.out.println("Is the blockchain valid? " + isValid);

        // Print the blockchain
        System.out.println("\nBlockchain:");
        System.out.println(blockchain);
    }
}
