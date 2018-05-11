package com.uts.DBC.model;

import java.security.Key;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.uts.DBC.common.HashUtils;
import com.uts.DBC.common.VerificationTools;

import com.uts.DBC.exceptions.TransactionInvalidException;

public class Block {
	private ArrayList<Transaction> transactions;
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
				new ArrayList<Integer>(),
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
}
