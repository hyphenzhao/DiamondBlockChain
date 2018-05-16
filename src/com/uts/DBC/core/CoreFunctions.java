package com.uts.DBC.core;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.uts.DBC.common.Constants;
import com.uts.DBC.common.VerificationTools;
import com.uts.DBC.data.FileIO;
import com.uts.DBC.model.Block;
import com.uts.DBC.model.Transaction;

public class CoreFunctions {
	public static void multicast(String type) {
		for(int i = 0; i < Constants.IPBOOK.size(); i++) {
			String ip = Constants.IPBOOK.get(i);
			try {
				Socket socket = new Socket(ip, Constants.PORT);
				OutputStream out = socket.getOutputStream();
				ObjectOutputStream obj = new ObjectOutputStream(out);
				obj.flush();
				switch(type) {
				case "blockchain":
					obj.writeObject("BLOCKCHAIN");
					for(int j = 0; j < Constants.BLOCKCHAIN.getBlockLength(); j++) {
						obj.writeObject(Constants.BLOCKCHAIN.getBlock(j).toChain());
					}
					obj.writeObject(Constants.SESSION_END);
					break;
				case "transactions":
					obj.writeObject("TRANSACTIONS");
					for(int j = 0; j < Constants.CURRENTTRANSACTIONS.getLength(); j++) {
						obj.writeObject(Constants.CURRENTTRANSACTIONS.getTransaction(j).toBlock());
					}
					obj.writeObject(Constants.SESSION_END);
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static String getBalance(String address) {
		boolean[] result = 
    			VerificationTools.getAccountBalance(
    					address,
    					Constants.BLOCKCHAIN,
    					Constants.CURRENTTRANSACTIONS);
    	String balance = "";
    	int sum = 0;
    	for(int i = 0; i < result.length; i++) {
    		if(result[i]) {
    			balance += i + ",";
    			sum++;
    		}
    		if(sum > 20) {
    			balance += "\n";
    			sum = 0;
    		}
    	}
    	return balance;
	}
	public static String getBalance() {
		boolean[] result = 
    			VerificationTools.getAccountBalance(
    					Constants.CURRENTACCOUNT.getAddress(),
    					Constants.BLOCKCHAIN,
    					Constants.CURRENTTRANSACTIONS);
    	String balance = "";
    	int sum = 0;
    	for(int i = 0; i < result.length; i++) {
    		if(result[i]) {
    			balance += i + ",";
    			sum++;
    		}
    		if(sum > 20) {
    			balance += "\n";
    			sum = 0;
    		}
    	}
    	return balance;
	}
	public static void mineBlock() {
		BlockMiner newMiner = new BlockMiner();
		newMiner.startMining();
		multicast("blockchain");
	}
	public static boolean transferTo(String address, ArrayList<String> diamonds) {
		boolean[] ownedDias = 
				VerificationTools.getAccountBalance(
						Constants.CURRENTACCOUNT.getAddress(), 
						Constants.BLOCKCHAIN,
						Constants.CURRENTTRANSACTIONS);
		for(int i = 0; i < diamonds.size(); i++) {
			int d = Integer.parseInt(diamonds.get(i));
			if(!ownedDias[d]) {
				return false;
			}
		}
		ArrayList<Transaction> allTransactions = new ArrayList<Transaction>();
		for(int i = 0; i < Constants.BLOCKCHAIN.getBlockLength(); i++) {
			Block block = Constants.BLOCKCHAIN.getBlock(i);
			allTransactions.addAll(block.getTransactions());
		}
		allTransactions.addAll(Constants.CURRENTTRANSACTIONS.getTransactions());
		ArrayList<String> preHashs = new ArrayList<String>();
		for(int i = 0; i < diamonds.size(); i++) {
			String d = diamonds.get(i);
			for(int j = allTransactions.size() - 1; j >= 0; j--) {
				Transaction t = allTransactions.get(j);
				if(t.getReceiver().equals(Constants.CURRENTACCOUNT.getAddress())) {
					if(t.getDiamonds().contains(d)) {
						if(!preHashs.contains(t.getHash())) {
							preHashs.add(t.getHash());
						}
						break;
					}
				}
			}
		}
		Transaction newTra = new Transaction(
				Constants.CURRENTACCOUNT.getAddress(),
				address, diamonds, preHashs,
				Constants.CURRENTACCOUNT.getPrivateKey(),
				Constants.CURRENTACCOUNT.getPublicKey()
				);
		Constants.CURRENTTRANSACTIONS.pushTransaction(newTra);
		FileIO transactionFile = new FileIO(Constants.TRANSACTIONFILENAME);
		transactionFile.writeToFile(Constants.CURRENTTRANSACTIONS.toString());
		multicast("transactions");
		return true;
	}
}
