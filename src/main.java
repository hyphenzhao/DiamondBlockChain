import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import com.uts.DBC.common.HashUtils;
import com.uts.DBC.common.RSAKeyTools;
import com.uts.DBC.common.VerificationTools;
import com.uts.DBC.data.FileIO;
import com.uts.DBC.exceptions.TransactionInvalidException;
import com.uts.DBC.model.Block;
import com.uts.DBC.model.Chain;
import com.uts.DBC.model.Transaction;

import java.util.ArrayList;
import java.util.Base64;

public class main {
	public static void main(String args[]) {
		KeyPair keys = RSAKeyTools.generateKeyPair();
		Key publicKey = keys.getPublic();
		Key privateKey = keys.getPrivate();
		
		byte[] publicKeyBytes = publicKey.getEncoded();  
        byte[] privateKeyBytes = privateKey.getEncoded();  
  
        System.out.println(HashUtils.bytesToHex(publicKeyBytes));
        System.out.println(HashUtils.bytesToHex(privateKeyBytes));
        
//        for(int i = 0; i < publicKeyBytes.length; i++) {
//        	System.out.print(publicKeyBytes[i]);
//        }
//        System.out.println();
//        byte[] rePublicKeyByte = HashUtils.hexStringToByteArray(HashUtils.bytesToHex(publicKeyBytes));
//        for(int i = 0; i < rePublicKeyByte.length; i++) {
//        	System.out.print(rePublicKeyByte[i]);
//        }
//        System.out.println();
        
        String sender = HashUtils.bytesToHex(HashUtils.calculateSha256(publicKeyBytes));
        String receiver = "469365caed9c3a744ae3fd0d95a223b2a0f722df589665cf7e0cc607f905fdf3";
        ArrayList<String> diamonds = new ArrayList<String> ();
        diamonds.add("1"); diamonds.add("2");
        ArrayList<String> preHashs = new ArrayList<String>();
        Transaction tra = new Transaction(
        		sender, receiver, diamonds, preHashs, privateKey, publicKey
        		);
        System.out.println(tra);
        System.out.println(new Transaction(tra.toBlock()));
        
        if(VerificationTools.verifyTransactionSignature(tra)) {
        	System.out.println("Transaction is valid.");
        } else {
        	System.out.println("Transaction is invalid.");
        }
        
        Chain testChain = new Chain();
        ArrayList<Transaction> tras = new ArrayList<Transaction>();
        tras.add(tra);
        try {
			Block blo = new Block(0, "0", tras, sender, privateKey, publicKey);
			System.out.println(new Block(blo.toChain()));
			System.out.println(blo.toChain());
			System.out.println(blo.toChain().length());
	        testChain.pushBlock(blo);
		} catch (TransactionInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
