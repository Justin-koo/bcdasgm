package blockchain;

import java.util.Date;

import app.InsuranceClaim;

public class Block {
    public int index;
    public long timestamp;
    public InsuranceClaim data;
    public String previousHash;
    public String hash;

    // Constructor
    public Block(int index, InsuranceClaim data, String previousHash) {
        this.index = index;
        this.timestamp = new Date().getTime();
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    // Method to calculate the hash of the block
    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                Long.toString(timestamp) +
                Integer.toString(index) +
                data.toJson()
        );
    }
}
