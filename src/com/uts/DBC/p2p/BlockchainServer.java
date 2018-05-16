package com.uts.DBC.p2p;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.uts.DBC.common.Constants;

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
			String message = "";
			do {
				if(!message.equals(""))
					System.out.println(message);
				message = (String) objIn.readObject();
				System.out.println("Server: " + message);
			}while(!message.equals(Constants.SESSION_END));
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
