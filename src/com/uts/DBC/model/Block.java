package com.uts.DBC.model;

import java.util.ArrayList;

public class Block {
	private ArrayList<Transaction> transactions;
	private int index;
	private String preHash;
	private long proof;
	private double timestamp;
	
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
	public double getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}
	
	public void pushTransactions(Transaction transaction) {
		transactions.add(transaction);
	}
}
