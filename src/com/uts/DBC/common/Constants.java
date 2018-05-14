package com.uts.DBC.common;

import java.util.ArrayList;

import com.uts.DBC.model.Chain;
import com.uts.DBC.model.Transaction;

public class Constants {
	public static Chain BLOCKCHAIN;
	public static ArrayList<Transaction> CURRENTTRANSACTIONS;
	public static String BLOCKFILENAME = "blocks.data";
	public static String TRANSACTIONFILENAME = "transactions.data";
	public static String ACCOUNTFILENAME = "accounts.data";
}
