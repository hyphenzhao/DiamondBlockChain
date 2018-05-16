package com.uts.DBC.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.KeyPair;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.uts.DBC.common.Constants;
import com.uts.DBC.common.RSAKeyTools;
import com.uts.DBC.common.VerificationTools;
import com.uts.DBC.core.BlockMiner;
import com.uts.DBC.core.CoreFunctions;
import com.uts.DBC.data.FileIO;
import com.uts.DBC.model.Account;

public class MainWindow extends JFrame{
	
	private JLabel addressStatus = new JLabel("Address: \t\t\t");
	private JTextArea balanceStatus = new JTextArea("Balance: \t\t\t");
	private JPanel mainPanel = new JPanel(new GridLayout(2,0));
	
	public MainWindow() {
		this.setName("Diamond Block Chain");
		this.setTitle("Diamond Block Chain");
		this.setSize(1000, 500);
		this.balanceStatus.setEditable(false);

		
		setupTabbedPane();
		setupStatusPane();
		
		this.setContentPane(mainPanel);
		this.setVisible(true);
		
	}
	
	protected void setupStatusPane() {
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.PAGE_AXIS));
		statusPanel.add(addressStatus);
		statusPanel.add(balanceStatus);
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton refreshButton = new JButton();
		refreshButton.setText("Refresh Balance");
		buttonPanel.add(refreshButton);
		JButton quitButton = new JButton();
		quitButton.setText("Quit");
		buttonPanel.add(quitButton);
		buttonPanel.setVisible(true);
		statusPanel.add(buttonPanel);
		statusPanel.setVisible(true);
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
		    {
		    	balanceStatus.setText("Balance: " + CoreFunctions.getBalance());
		    }
		});
		quitButton.addActionListener(new ActionListener() {
			@Override
		    public void actionPerformed(ActionEvent e)
		    {
		    	System.exit(0);
		    }
		});
		mainPanel.add(statusPanel);
	}
	
	protected void setupTabbedPane() {
		JTabbedPane tabbedPane = new JTabbedPane();
		JComponent walletPanel = generateWalletPanel();
		JComponent sendPanel = generateSendPanel();
		JComponent minePanel = generateMinePanel();
		JComponent checkPanel = generateSearchPanel();
		tabbedPane.addTab("Wallet", walletPanel);
		tabbedPane.addTab("Send", sendPanel);
		tabbedPane.addTab("Mine", minePanel);
		tabbedPane.addTab("Check Balance", checkPanel);
		tabbedPane.setVisible(true);
		mainPanel.add(tabbedPane);
	}
	
	protected JComponent generateSearchPanel() {
		JPanel panel = new JPanel(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JPanel addressPanel = new JPanel(false);
		addressPanel.setLayout(new FlowLayout());
		JLabel sendToLabel = new JLabel("Check balance of address: ");
		JTextField addressField = new JTextField(40);
		addressPanel.add(sendToLabel);
		addressPanel.add(addressField);
		panel.add(addressPanel);
		JLabel valueLabel = new JLabel("The balance will be shown here:");
		JTextArea values = new JTextArea(20, 5);
		panel.add(valueLabel);
		panel.add(values);
		JButton checkButton = new JButton();
		checkButton.setText("Check Now!");
		panel.add(checkButton);
		checkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String address = addressField.getText();
				String result = CoreFunctions.getBalance(address);
				values.setText(result);
			}
		});
		return panel;
	}
	
	protected JComponent generateMinePanel() {
		JPanel panel = new JPanel(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		JButton mineButton = new JButton();
		mineButton.setText("Mine Block Now!");
		panel.add(mineButton);
		mineButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CoreFunctions.mineBlock();
				balanceStatus.setText("Balance: " + CoreFunctions.getBalance());
			}
		});
		
		return panel;
	}
	
	protected JComponent generateSendPanel() {
		JPanel panel = new JPanel(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JPanel addressPanel = new JPanel(false);
		addressPanel.setLayout(new FlowLayout());
		JLabel sendToLabel = new JLabel("Send to: ");
		JTextField addressField = new JTextField(40);
		addressPanel.add(sendToLabel);
		addressPanel.add(addressField);
		panel.add(addressPanel);
		JLabel valueLabel = new JLabel("Please input the diamonds' no. you want to send, split with comma(,):");
		JTextArea values = new JTextArea(20, 5);
		panel.add(valueLabel);
		panel.add(values);
		JButton sendButton = new JButton();
		sendButton.setText("Send Now!");
		panel.add(sendButton);
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sendTo = addressField.getText();
				String[] diaNos = values.getText().split(",");
				ArrayList<String> diamonds = new ArrayList<String>();
				for(int i = 0; i < diaNos.length; i++) {
					diamonds.add(diaNos[i]);
				}
				if(CoreFunctions.transferTo(sendTo, diamonds)) {
					balanceStatus.setText("Balance: " + CoreFunctions.getBalance());
			    	JOptionPane.showMessageDialog(panel, "Diamonds Sent Successfully!", "Successful", JOptionPane.INFORMATION_MESSAGE);
			    	System.out.println("New Transaction: ");
			    	System.out.println(Constants.CURRENTTRANSACTIONS.getLast());
				} else {
					JOptionPane.showMessageDialog(panel, "You do not own all of the diamonds you sent.", "Failed", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		return panel;
	}
	
	protected JComponent generateWalletPanel() {
		JPanel panel = new JPanel(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		JButton newKey = new JButton();
		newKey.setText("Generate New Key");
		panel.add(newKey);
		
		JButton saveKey = new JButton();
		saveKey.setText("Save Key to File");
		panel.add(saveKey);
		
		JButton loadKey = new JButton();
		loadKey.setText("Load Key from File");
		panel.add(loadKey);
		
		JLabel publicKeyLabel = new JLabel("Your Public Key");
		panel.add(publicKeyLabel);
		
		JTextArea publicKeyArea = new JTextArea(5, 20);
		publicKeyArea.setLineWrap(true);
		publicKeyArea.setEditable(false);
		panel.add(publicKeyArea);
		
		newKey.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent e) {
				 KeyPair newKeys = RSAKeyTools.generateKeyPair();
				 Constants.CURRENTACCOUNT = new Account(newKeys);
				 System.out.println("New Keys Generated:");
				 System.out.println("Private Key: " + Constants.CURRENTACCOUNT.getPrivateKeyString());
				 System.out.println("Public Key: " + Constants.CURRENTACCOUNT.getPublicKeyString());
				 System.out.println("Address: " + Constants.CURRENTACCOUNT.getAddress());
				 publicKeyArea.setText(Constants.CURRENTACCOUNT.getPublicKeyString());
				 addressStatus.setText("Address: " + Constants.CURRENTACCOUNT.getAddress());
				 String priKey = Constants.CURRENTACCOUNT.getPrivateKeyString();
				 String info = "Your private key is:\n";
				 int step = 50;
				 for(int i = 0; i < priKey.length(); i += step) {
					 if(i + step < priKey.length())
						 info += priKey.substring(i, i + step) + "\n";
					 else
						 info += priKey.substring(i);
				 }
				 info += "\n Please remember and keep it secretly.";
				 JOptionPane.showMessageDialog(panel, info, "Private Key", JOptionPane.INFORMATION_MESSAGE);
				 balanceStatus.setText("Balance: " + CoreFunctions.getBalance());
			 }
		});
		
		saveKey.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent e) {
				 FileIO accountFile = new FileIO(Constants.ACCOUNTFILENAME);
				 accountFile.writeToFile(Constants.CURRENTACCOUNT.toString());
				 JOptionPane.showMessageDialog(panel, "Account Saved Successfully!", "Successful", JOptionPane.INFORMATION_MESSAGE);
			 }
		});
		
		loadKey.addActionListener(new ActionListener() {
			@Override
			 public void actionPerformed(ActionEvent e) {
				FileIO accountFile = new FileIO(Constants.ACCOUNTFILENAME);
				ArrayList<String> accountInfo = accountFile.loadFileText();
				Constants.CURRENTACCOUNT = new Account(accountInfo);
				System.out.println("Keys Loaded Successfully:");
				 System.out.println("Private Key: " + Constants.CURRENTACCOUNT.getPrivateKeyString());
				 System.out.println("Public Key: " + Constants.CURRENTACCOUNT.getPublicKeyString());
				 System.out.println("Address: " + Constants.CURRENTACCOUNT.getAddress());
				 publicKeyArea.setText(Constants.CURRENTACCOUNT.getPublicKeyString());
				 addressStatus.setText("Address: " + Constants.CURRENTACCOUNT.getAddress());
				 String priKey = Constants.CURRENTACCOUNT.getPrivateKeyString();
				 String info = "Your private key is:\n";
				 int step = 50;
				 for(int i = 0; i < priKey.length(); i += step) {
					 if(i + step < priKey.length())
						 info += priKey.substring(i, i + step) + "\n";
					 else
						 info += priKey.substring(i);
				 }
				 info += "\n Please remember and keep it secretly.";
				 JOptionPane.showMessageDialog(panel, info, "Private Key", JOptionPane.INFORMATION_MESSAGE);
				 String balance = CoreFunctions.getBalance();
				 balanceStatus.setText(balance);
			}
		});
		
		return panel;
	}
	
	
}
