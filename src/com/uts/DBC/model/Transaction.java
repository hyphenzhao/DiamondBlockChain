package com.uts.DBC.model;

import java.security.Key;
import java.util.ArrayList;

import com.uts.DBC.common.HashUtils;
import com.uts.DBC.common.RSAKeyTools;

public class Transaction {
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
		String text = sender + receiver;
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
	
	@Override
	public String toString() {
		String strPubKey = HashUtils.bytesToHex(publicKey.getEncoded());
		String result = "";
		result += "[\n";
		result += "sender: " + this.sender + "\n";
		result += "receiver: " + this.receiver + "\n";
		result += "diamonds: ";
		for(int i = 0; i < this.diamonds.size(); i++) {
			result += this.diamonds.get(i) + ",";
		}
		result += "\n";
		result += "Previous Indexs: 0\n";
		result += "Previous Hashs: 0\n";
		result += "Hash: " + this.hash + "\n";
		result += "Signature: " + this.signature + "\n";
		result += "Public Key:" + strPubKey + "\n";
		result += "]\n";
		return result;
	}
	
	// Getters and Setters
	public String getSender() {
		return sender;
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
