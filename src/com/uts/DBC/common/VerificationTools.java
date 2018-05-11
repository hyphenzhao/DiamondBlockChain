package com.uts.DBC.common;

import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;

import com.uts.DBC.model.Transaction;

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
}
