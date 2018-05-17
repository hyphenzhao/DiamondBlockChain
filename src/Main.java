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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

public class Main {
	public static void init() {
		/*
		 * Read blockchain and transactions list from file.
		 * */
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
		
		
		/*
		 * Try to get a ip list from remote server with public ip
		 * Register this node online
		 * */
		Constants.IPBOOK = new ArrayList<String>();
		try {
			System.out.println("IPv4: " + Inet4Address.getLocalHost().getHostAddress());
			String ipv4 = Inet4Address.getLocalHost().getHostAddress().replace('.', '-');
			System.out.println("Output IPv4: " + ipv4);
			URL url = new URL("http://" + Constants.PUBIP + "/iplist.php?ip=" + ipv4);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				if(!inputLine.equals("Connection failed.") &&
						!inputLine.equals(ipv4))
				Constants.IPBOOK.add(inputLine.replace('-', '.'));
				System.out.println("HTTP GET:" + inputLine);
			}
			in.close();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		/*
		 * Start the blockchain server.
		 * */
		Thread serverThread = new Thread(new BlockchainServer());
		serverThread.start();
		
		/*
		 * Get current trasactions and blockchain status from other devices.
		 * */
		for(int i = 0; i < Constants.IPBOOK.size(); i++) {
			try {
				Socket initSocket = new Socket();
				initSocket.connect(
						new InetSocketAddress(Constants.IPBOOK.get(i), Constants.PORT), 
						1000);
				OutputStream out = initSocket.getOutputStream();
				ObjectOutputStream obj = new ObjectOutputStream(out);
				obj.flush();
				obj.writeObject("GET");
				initSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
