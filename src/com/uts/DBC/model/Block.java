package com.uts.DBC.model;

import java.security.Key;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.uts.DBC.common.HashUtils;
import com.uts.DBC.common.VerificationTools;

import com.uts.DBC.exceptions.TransactionInvalidException;

public class Block {
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	private int index;
	private String preHash;
	private long proof;
	private long timestamp;
	private String hash;
	
	public Block(int index,
			String preHash,
			ArrayList<Transaction> transactions,
			String winer,
			Key privateKey,
			Key publicKey
			) throws TransactionInvalidException {
		for(int i = 0; i < transactions.size(); i++) {
			if(!VerificationTools.verifyTransactionSignature(transactions.get(i)))
				throw new TransactionInvalidException();
		}
		this.transactions = transactions;
		ArrayList<String> diamonds = new ArrayList<String>();
		for(int i = index * 50; i < index * 50 + 50; i++) {
			diamonds.add(Integer.toString(i));
		}
		Transaction reward = new Transaction(
				"0", winer, diamonds, 
				new ArrayList<String>(),
				privateKey,publicKey
				);
		transactions.add(reward);
		this.timestamp = (new Timestamp(System.currentTimeMillis())).getTime();
		this.index = index;
		this.preHash = preHash;
		this.proof = (long) (100000L + Math.random() * (1000000L - 100000L));
		String text = Integer.toString(index) + preHash;
		for(int i = 0; i < transactions.size(); i++) {
			text += transactions.get(i).toBlock();
		}
		this.hash = HashUtils.bytesToHex(HashUtils.calculateSha256(text));
	}
	
	public Block(String line) {
		int pos = line.indexOf("}");
		String preProcessed = line.substring(1, pos);
		String[] lines = preProcessed.split("#");
		for(int i = 0; i < lines.length; i++) {
			String[] phases = lines[i].split("=");
			switch(phases[0]) {
			case "Block":
				this.index = Integer.parseInt(phases[1]);
				break;
			case "PreviousHash":
				this.preHash = phases[1];
				break;
			case "Proof":
				this.proof = Long.parseLong(phases[1]);
				break;
			case "TimeStamp":
				this.timestamp = Long.parseLong(phases[1]);
				break;
			case "Hash":
				this.hash = phases[1];
				break;
			case "Transactions":
				String[] traStrs = phases[1].split("!");
				for(int j = 0; j < traStrs.length; j++) {
					Transaction newTra = new Transaction(traStrs[j]);
					this.transactions.add(newTra);
				}
				break;
			}
		}
	}
	
	public String toChain() {
		String result = "";
		result += "{";
		result += "Block=" + this.index + "#";
		result += "PreviousHash=" + this.preHash + "#";
		result += "Proof=" + this.proof + "#";
		result += "TimeStamp=" + this.timestamp + "#";
		result += "Hash=" + this.hash + "#";
		result += "Transactions=";
		for(int i = 0; i < this.transactions.size(); i++) {
			result += this.transactions.get(i).toBlock() + "!";
		}
		result += "#";
		result += "}";
		return result;
	}
	
	@Override
	public String toString() {
		String result = "";
		result += "{\n";
		result += "Block=" + this.index + "#\n";
		result += "PreviousHash=" + this.preHash + "#\n";
		result += "Proof=" + this.proof + "#\n";
		result += "TimeStamp=" + this.timestamp + "#\n";
		result += "Hash=" + this.hash + "#\n";
		result += "Transactions=";
		for(int i = 0; i < this.transactions.size(); i++) {
			result += this.transactions.get(i).toBlock() + "!\n";
		}
		result += "#\n";
		result += "}\n";
		return result;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getPreHash() {
		return preHash;
	}
	public void setPreHash(String preHash) {
		this.preHash = preHash;
	}
	public long getProof() {
		return proof;
	}
	public void setProof(long proof) {
		this.proof = proof;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public void pushTransactions(Transaction transaction) {
		transactions.add(transaction);
	}
	
	public int getBlockSize() {
		return this.transactions.size();
	}
	
	public Transaction getTransaction(int i) {
		return this.transactions.get(i);
	}
}
