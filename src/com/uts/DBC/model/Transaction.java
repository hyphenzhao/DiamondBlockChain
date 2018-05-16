package com.uts.DBC.model;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.uts.DBC.common.HashUtils;
import com.uts.DBC.common.RSAKeyTools;

public class Transaction implements Comparable{
	private long timestamp;
	private String sender;
	private String receiver;
	private ArrayList<String> diamonds = new ArrayList<String>();
	private ArrayList<String> preHashs = new ArrayList<String>();
	private String hash;
	private String signature;
	private Key publicKey;
	
	public Transaction(
			String sender,
			String receiver,
			ArrayList<String> diamonds,
			ArrayList<String> preHashs,
			Key privateKey,
			Key publicKey
			) {
		this.sender = sender;
		this.receiver = receiver;
		this.diamonds = diamonds;
		this.preHashs = preHashs;
		this.publicKey = publicKey;
		this.timestamp = (new Timestamp(System.currentTimeMillis())).getTime();
		String text = sender + receiver + Long.toString(timestamp);
		for(int i = 0; i < diamonds.size(); i++) {
			text += diamonds.get(i);
		}
		for(int i = 0; i < preHashs.size(); i++) {
			text += preHashs.get(i);
		}
		byte[] hashByte = HashUtils.calculateSha256(text);
		this.hash = HashUtils.bytesToHex(hashByte);
		this.signature = RSAKeyTools.signMessage(hash, privateKey);
	}
	
	public Transaction(String line) {
		int pos = line.indexOf("]");
		String preProcessed = line.substring(1, pos);
		String[] lines = preProcessed.split(";");
		for(int i = 0; i < lines.length; i++) {
			String[] phases = lines[i].split(":");
			switch(phases[0]) {
			case "Sender":
				this.sender = phases[1];
				break;
			case "Receiver":
				this.receiver = phases[1];
				break;
			case "Time Stamp":
				this.timestamp = Long.parseLong(phases[1]);
				break;
			case "Diamonds":
				String[] d = phases[1].split(",");
				for(int j = 0; j < d.length; j++)
					this.diamonds.add(d[j]);
				break;
			case "PreviousHashs":
				String[] h = phases[1].split(",");
				for(int j = 0; j < h.length; j++)
					this.preHashs.add(h[j]);
				break;
			case "Hash":
				this.hash = phases[1];
				break;
			case "Signature":
				this.signature = phases[1];
				break;
			case "PublicKey":
				byte[] bytePubKey = HashUtils.hexStringToByteArray(phases[1]);
				try {
					Key publicKey = (Key)KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytePubKey));
					this.publicKey = publicKey;
				} catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
			
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
		result += "]";
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

	@Override
	public int compareTo(Object o) {
		Transaction other = (Transaction) o;
		int result = (int)(this.timestamp - other.timestamp);
		return result;
	}
}
