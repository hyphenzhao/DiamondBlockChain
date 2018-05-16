package com.uts.DBC.core;

import com.uts.DBC.model.Account;
import com.uts.DBC.model.Block;
import com.uts.DBC.model.TransactionList;
import com.uts.DBC.common.Constants;
import com.uts.DBC.data.FileIO;
import com.uts.DBC.exceptions.TransactionInvalidException;

public class BlockMiner {
	private Account miner;
	public BlockMiner() {
		
	}
	public BlockMiner(Account miner) {
		this.miner = miner;
	}
	
	public void startMining() {
		String preHash;
		int index;
		if(Constants.BLOCKCHAIN.isEmpty()) {
			preHash = "0";
			index = 0;
		} else {
			int lastPos = Constants.BLOCKCHAIN.getBlockLength() - 1;
			Block preBlock = Constants.BLOCKCHAIN.getBlock(lastPos);
			preHash = preBlock.getHash();
			index = preBlock.getIndex() + 1;
		}
		try {
			Block newBlock = new Block(index, preHash,
					Constants.CURRENTTRANSACTIONS.getTransactions(),
					Constants.CURRENTACCOUNT.getAddress(),
					Constants.CURRENTACCOUNT.getPrivateKey(),
					Constants.CURRENTACCOUNT.getPublicKey());
			Constants.BLOCKCHAIN.pushBlock(newBlock);
			Constants.CURRENTTRANSACTIONS = new TransactionList();
			FileIO blockFile = new FileIO(Constants.BLOCKFILENAME);
			blockFile.writeToFile(Constants.BLOCKCHAIN.toString());
			FileIO transFile = new FileIO(Constants.TRANSACTIONFILENAME);
			transFile.writeToFile(Constants.CURRENTTRANSACTIONS.toString());
		} catch (TransactionInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Account getMiner() {
		return miner;
	}
	public void setMiner(Account miner) {
		this.miner = miner;
	}
	
}
