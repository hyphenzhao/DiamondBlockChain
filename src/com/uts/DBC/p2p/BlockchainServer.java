package com.uts.DBC.p2p;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import com.uts.DBC.common.Constants;
import com.uts.DBC.core.CoreFunctions;

public class BlockchainServer implements Runnable {
	public void runServer() {
		try {
			ServerSocket server = new ServerSocket(Constants.PORT);
			System.out.println("Server: Server Established. Wait for connection...");
			Socket socket = server.accept();
			System.out.println("Server: Connection Established!");
			InputStream conIn = socket.getInputStream();
			ObjectInputStream objIn = new ObjectInputStream(conIn);
			String messageType = (String)objIn.readObject();
			System.out.println("Server: " + messageType);
			if(messageType.equals("GET")) {
				String ipv4 = Inet4Address.getLocalHost().getHostAddress().replace('.', '-');
				URL url = new URL("http://" + Constants.PUBIP + "/iplist.php?ip=" + ipv4);
				CoreFunctions.refreshIPBook(url, ipv4);
				CoreFunctions.multicast("both");
			} else {
				String message = "";
				ArrayList<String> result = new ArrayList<String>();
				do {
					message = (String) objIn.readObject();
					System.out.println("Server: " + message);
					if(!message.equals(Constants.SESSION_END)) {
						result.add(message);
					}
				}while(!message.equals(Constants.SESSION_END));
				CoreFunctions.resolveServerInput(messageType, result);
			}
			socket.close();
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			runServer();
		}
	}

}
