package com.uts.DBC.model;

import java.security.Key;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.uts.DBC.common.HashUtils;
import com.uts.DBC.common.RSAKeyTools;

public class Transaction {
	private long timestamp;
	private String sender;
	private String receiver;
	private ArrayList<String> diamonds;
	private ArrayList<Integer> preIndexs;
	private ArrayList<String> preHashs;
	private String hash;
	private String signature;
	private Key publicKey;
	
	public Transaction(
			String sender,
			String receiver,
			ArrayList<String> diamonds,
			ArrayList<Integer> preIndexs,
			ArrayList<String> preHashs,
			Key privateKey,
			Key publicKey
			) {
		this.sender = sender;
		this.receiver = receiver;
		this.diamonds = diamonds;
		this.preIndexs = preIndexs;
		this.preHashs = preHashs;
		this.publicKey = publicKey;
		this.timestamp = (new Timestamp(System.currentTimeMillis())).getTime();
		String text = sender + receiver + Long.toString(timestamp);
		for(int i = 0; i < diamonds.size(); i++) {
			text += diamonds.get(i);
		}
		for(int i = 0; i < preIndexs.size(); i++) {
			text += preIndexs.get(i).toString();
		}
		for(int i = 0; i < preHashs.size(); i++) {
			text += preHashs.get(i);
		}
		byte[] hashByte = HashUtils.calculateSha256(text);
		this.hash = HashUtils.bytesToHex(hashByte);
		this.signature = RSAKeyTools.signMessage(hash, privateKey);
	}
	
	public String toBlock() {
		String result = "";
		String strPubKey = HashUtils.bytesToHex(publicKey.getEncoded());
		result += "[";
		result += "Sender:" + this.sender + ";";
		result += "Receiver:" + this.receiver + ";";
		result += "Time Stamp:" + Long.toString(this.timestamp) + ";";
		result += "Diamonds:";
		if(this.diamonds.size() == 0)
			result += "0;";
		for(int i = 0; i < this.diamonds.size(); i++) {
			if(i < this.diamonds.size() - 1)
				result += this.diamonds.get(i) + ",";
			else
				result += this.diamonds.get(i) + ";";
		}
		result += "PreviousIndexs:";
		if(this.preIndexs.size() == 0)
			result += "0;";
		for(int i = 0; i < this.preIndexs.size(); i++) {
			if(i < this.preIndexs.size() - 1)
				result += preIndexs.get(i).toString() + ",";
			else
				result += preIndexs.get(i).toString() + ";";
		}
		result += "PreviousHashs:";
		if(this.preHashs.size() == 0)
			result += "0;";
		for(int i = 0; i < this.preHashs.size(); i++) {
			if(i < this.preHashs.size() - 1)
				result += preHashs.get(i).toString() + ",";
			else
				result += preHashs.get(i).toString() + ";";
		}
		result += "Hash:" + this.hash + ";";
		result += "Signature:" + this.signature + ";";
		result += "PublicKey:" + strPubKey + ";";
		result += "]\n";
		return result;
	}
	
	@Override
	public String toString() {
		String result = "";
		String strPubKey = HashUtils.bytesToHex(publicKey.getEncoded());
		result += "[\n";
		result += "Sender:" + this.sender + "\n";
		result += "Receiver:" + this.receiver + "\n";
		result += "Time Stamp:" + Long.toString(this.timestamp) + "\n";
		result += "Diamonds:";
		if(this.diamonds.size() == 0)
			result += "0\n";
		for(int i = 0; i < this.diamonds.size(); i++) {
			if(i < this.diamonds.size() - 1)
				result += this.diamonds.get(i) + ",";
			else
				result += this.diamonds.get(i) + "\n";
		}
		result += "Previous Indexs:";
		if(this.preIndexs.size() == 0)
			result += "0\n";
		for(int i = 0; i < this.preIndexs.size(); i++) {
			if(i < this.preIndexs.size() - 1)
				result += preIndexs.get(i).toString() + ",";
			else
				result += preIndexs.get(i).toString() + "\n";
		}
		result += "Previous Hashs:";
		if(this.preHashs.size() == 0)
			result += "0\n";
		for(int i = 0; i < this.preHashs.size(); i++) {
			if(i < this.preHashs.size() - 1)
				result += preHashs.get(i).toString() + ",";
			else
				result += preHashs.get(i).toString() + "\n";
		}
		result += "Hash:" + this.hash + "\n";
		result += "Signature:" + this.signature + "\n";
		result += "Public Key:" + strPubKey + "\n";
		result += "]\n";
		return result;
	}
	
	// Getters and Setters
	
	public String getSender() {
		return sender;
	}
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Key getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(Key publicKey) {
		this.publicKey = publicKey;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public ArrayList<String> getDiamonds() {
		return diamonds;
	}
	public void setDiamonds(ArrayList<String> diamonds) {
		this.diamonds = diamonds;
	}
	public ArrayList<Integer> getPreIndexs() {
		return preIndexs;
	}
	public void setPreIndexs(ArrayList<Integer> preIndexs) {
		this.preIndexs = preIndexs;
	}
	public ArrayList<String> getPreHashs() {
		return preHashs;
	}
	public void setPreHashs(ArrayList<String> preHashs) {
		this.preHashs = preHashs;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public void pushDiamnonds(String diamond) {
		diamonds.add(diamond);
	}
	public void pushPreHash(String preHash) {
		preHashs.add(preHash);
	}
}
