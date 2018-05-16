import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import com.uts.DBC.common.Constants;
import com.uts.DBC.common.HashUtils;
import com.uts.DBC.common.RSAKeyTools;
import com.uts.DBC.common.VerificationTools;
import com.uts.DBC.data.FileIO;
import com.uts.DBC.exceptions.TransactionInvalidException;
import com.uts.DBC.gui.MainWindow;
import com.uts.DBC.model.Block;
import com.uts.DBC.model.Chain;
import com.uts.DBC.model.Transaction;
import com.uts.DBC.model.TransactionList;
import com.uts.DBC.p2p.BlockchainServer;

import java.util.ArrayList;
import java.util.Base64;

public class Main {
	public static void init() {
		FileIO blockChainFile = new FileIO(Constants.BLOCKFILENAME);
		FileIO transactionFile = new FileIO(Constants.TRANSACTIONFILENAME);
		if(!blockChainFile.isEmptyFile()) {
			ArrayList<String> text = blockChainFile.loadFileText();
			Constants.BLOCKCHAIN = new Chain(text);
		} else {
			Constants.BLOCKCHAIN = new Chain();
		}
		if(!transactionFile.isEmptyFile()) {
			ArrayList<String> text = transactionFile.loadFileText();
			Constants.CURRENTTRANSACTIONS = new TransactionList(text);
		} else {
			Constants.CURRENTTRANSACTIONS = new TransactionList();
		}
		Constants.IPBOOK = new ArrayList<String>();
		Constants.IPBOOK.add("localhost");
		Thread serverThread = new Thread(new BlockchainServer());
		serverThread.start();
	}
	public static void printBasicInformation() {
		System.out.println("Block Chain Status:");
		if(Constants.BLOCKCHAIN.isEmpty()) {
			System.out.println("Empty");
		} else {
			for(int i = 0; i < Constants.BLOCKCHAIN.getBlockLength(); i++) {
				Block b = Constants.BLOCKCHAIN.getBlock(i);
				System.out.println(b);
			}
		}
		System.out.println("Transaction Status:");
		if(Constants.CURRENTTRANSACTIONS.isEmpty()) {
			System.out.println("Empty");
		} else {
			for(int i = 0; i < Constants.CURRENTTRANSACTIONS.getLength(); i++) {
				Transaction t = Constants.CURRENTTRANSACTIONS.getTransaction(i);
				System.out.println(t);
			}
		}
	}
	public static void main(String args[]) {
        init();
        printBasicInformation();
        MainWindow mainWindow = new MainWindow();
	}
}
