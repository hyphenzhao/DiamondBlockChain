package com.uts.DBC.model;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import com.uts.DBC.common.HashUtils;

public class Account {
	private Key publicKey;
	private Key privateKey;
	private byte[] publicKeyByte;
	private byte[] privateKeyByte;
	private String publicKeyString;
	private String privateKeyString;
	private String address;
	public Account() {
		
	}
	public Account(KeyPair keys) {
		this.publicKey = keys.getPublic();
		this.privateKey = keys.getPrivate();
		this.publicKeyByte = publicKey.getEncoded();
		this.privateKeyByte = privateKey.getEncoded();
		this.publicKeyString = HashUtils.bytesToHex(this.publicKeyByte);
		this.privateKeyString = HashUtils.bytesToHex(this.privateKeyByte);
		this.address = HashUtils.bytesToHex(HashUtils.calculateSha256(this.publicKeyString));
	}
	public Account(Key publicKey, Key privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.publicKeyByte = publicKey.getEncoded();
		this.privateKeyByte = privateKey.getEncoded();
		this.publicKeyString = HashUtils.bytesToHex(this.publicKeyByte);
		this.privateKeyString = HashUtils.bytesToHex(this.privateKeyByte);
		this.address = HashUtils.bytesToHex(HashUtils.calculateSha256(this.publicKeyString));
	}
	
	public Account(byte[] publicKeyBytes, byte[] privateKeyBytes) {
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			this.privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
			this.publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.publicKeyByte = publicKeyBytes;
		this.privateKeyByte = privateKeyBytes;
		this.publicKeyString = HashUtils.bytesToHex(this.publicKeyByte);
		this.privateKeyString = HashUtils.bytesToHex(this.privateKeyByte);
		this.address = HashUtils.bytesToHex(HashUtils.calculateSha256(this.publicKeyString));
	}
	public Account(String publicKeyString, String privateKeyString) {
		this.publicKeyString = publicKeyString;
		this.privateKeyString = privateKeyString;
		this.publicKeyByte = HashUtils.hexStringToByteArray(publicKeyString);
		this.privateKeyByte = HashUtils.hexStringToByteArray(privateKeyString);
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			this.privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));
			this.publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyByte));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.address = HashUtils.bytesToHex(HashUtils.calculateSha256(this.publicKeyString));
	}
	public Account(ArrayList<String> keys) {
		this.publicKeyString = keys.get(0);
		this.privateKeyString = keys.get(1);
		this.publicKeyByte = HashUtils.hexStringToByteArray(publicKeyString);
		this.privateKeyByte = HashUtils.hexStringToByteArray(privateKeyString);
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			this.privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));
			this.publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyByte));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.address = HashUtils.bytesToHex(HashUtils.calculateSha256(this.publicKeyString));
	}
	
	public Key getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(Key publicKey) {
		this.publicKey = publicKey;
	}
	public Key getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(Key privateKey) {
		this.privateKey = privateKey;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public byte[] getPublicKeyByte() {
		return publicKeyByte;
	}
	public void setPublicKeyByte(byte[] publicKeyByte) {
		this.publicKeyByte = publicKeyByte;
	}
	public byte[] getPrivateKeyByte() {
		return privateKeyByte;
	}
	public void setPrivateKeyByte(byte[] privateKeyByte) {
		this.privateKeyByte = privateKeyByte;
	}
	public String getPublicKeyString() {
		return publicKeyString;
	}
	public void setPublicKeyString(String publicKeyString) {
		this.publicKeyString = publicKeyString;
	}
	public String getPrivateKeyString() {
		return privateKeyString;
	}
	public void setPrivateKeyString(String privateKeyString) {
		this.privateKeyString = privateKeyString;
	}
	@Override
	public String toString() {
		String result = "";
		result += publicKeyString + "\n" + privateKeyString + "\n" + address;
		return result;
	}
	
}
