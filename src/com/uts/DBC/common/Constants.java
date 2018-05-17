package com.uts.DBC.common;

import java.util.ArrayList;

import com.uts.DBC.model.Account;
import com.uts.DBC.model.Chain;
import com.uts.DBC.model.Transaction;
import com.uts.DBC.model.TransactionList;

public class Constants {
	public static Chain BLOCKCHAIN;
	public static TransactionList CURRENTTRANSACTIONS;
	public static Account CURRENTACCOUNT;
	public static String BLOCKFILENAME = "blocks.data";
	public static String TRANSACTIONFILENAME = "transactions.data";
	public static String ACCOUNTFILENAME = "accounts.data";
	public static int PORT = 5000;
	public static ArrayList<String> IPBOOK;
	public static String SESSION_END = "SESSION END.";
	public static String PUBIP = "13.211.117.39";
}
