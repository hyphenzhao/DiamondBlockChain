package com.uts.DBC.model;

import java.util.ArrayList;

public class TransactionList {
	ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	
	public TransactionList() {
		
	}
	public TransactionList(ArrayList<String> tras) {
		for(int i = 0; i < tras.size(); i++) {
			this.transactions.add(new Transaction(tras.get(i)));
		}
	}
	public int getLength() {
		return this.transactions.size();
	}
	public boolean isEmpty() {
		return this.transactions.isEmpty();
	}
	public Transaction getTransaction(int i) {
		return this.transactions.get(i);
	}
	public void pushTransaction(Transaction tra) {
		this.transactions.add(tra);
	}
	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}
	public Transaction getLast() {
		int i = this.transactions.size() - 1;
		return this.transactions.get(i);
	}
	@Override
	public String toString() {
		String result = "";
		for(int i = 0; i < this.transactions.size(); i++) {
			result += this.transactions.get(i).toBlock() + "\n";
		}
		return result;
	}
}
