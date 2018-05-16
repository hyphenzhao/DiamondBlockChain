package com.uts.DBC.common;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;

import com.uts.DBC.model.Block;
import com.uts.DBC.model.Chain;
import com.uts.DBC.model.Transaction;
import com.uts.DBC.model.TransactionList;

public class VerificationTools {
	public static boolean verifyTransactionSignature(Transaction transaction) {
		Key publicKey = transaction.getPublicKey();
		String signature = transaction.getSignature();
		String hash = transaction.getHash();
		try {
			Cipher cipher = Cipher.getInstance("RSA");  
	        cipher.init(Cipher.DECRYPT_MODE, publicKey);    
	        byte[] b1 = HashUtils.hexStringToByteArray(signature);  
	        byte[] b = cipher.doFinal(b1); 
	        byte[] bHash = hash.getBytes();
	        return Arrays.equals(b, bHash);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean verifyAddress(String publicKey, String address) {
		String sampleAddr = HashUtils.bytesToHex(HashUtils.calculateSha256(publicKey));
		if(address.equals(sampleAddr)) return true;
		return false;
	}
	public static boolean[] getAccountBalance(String accountAddr, Chain blockChain, TransactionList transactions) {
		int sum = 50 * blockChain.getBlockLength() + 1;
		boolean[] diamonds = new boolean[sum];
		for(int i = 0; i < diamonds.length; i++) 
			diamonds[i] = false;
		for(int i = 0; i < blockChain.getBlockLength(); i++) {
			Block cBlock = blockChain.getBlock(i);
			for(int j = 0; j < cBlock.getBlockSize(); j++) {
				Transaction cTransaction = cBlock.getTransaction(j);
				if(accountAddr.equals(cTransaction.getReceiver())) {
					ArrayList<String> trades = cTransaction.getDiamonds();
					for(int k = 0; k < trades.size(); k++) {
						int diaNo = Integer.parseInt(trades.get(k));
						diamonds[diaNo] = true;
					}
				}else if(accountAddr.equals(cTransaction.getSender())) {
					ArrayList<String> trades = cTransaction.getDiamonds();
					for(int k = 0; k < trades.size(); k++) {
						int diaNo = Integer.parseInt(trades.get(k));
						diamonds[diaNo] = false;
					}
				}
			}
		}
		for(int i = 0; i < transactions.getLength(); i++) {
			Transaction cTransaction = transactions.getTransaction(i);
			if(accountAddr.equals(cTransaction.getReceiver())) {
				ArrayList<String> trades = cTransaction.getDiamonds();
				for(int k = 0; k < trades.size(); k++) {
					int diaNo = Integer.parseInt(trades.get(k));
					diamonds[diaNo] = true;
				}
			}else if(accountAddr.equals(cTransaction.getSender())) {
				ArrayList<String> trades = cTransaction.getDiamonds();
				for(int k = 0; k < trades.size(); k++) {
					int diaNo = Integer.parseInt(trades.get(k));
					diamonds[diaNo] = false;
				}
			}
		}
		return diamonds;
	}
}
