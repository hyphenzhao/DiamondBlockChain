package com.uts.DBC.model;

import java.util.ArrayList;

public class Chain {
	private ArrayList<Block> blocks;

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}
	
	public void pushBlock(Block block) {
		blocks.add(block);
	}
	
	public int getBlockLength() {
		return blocks.size();
	}
	public int getTransactionLength() {
		int sum = 0;
		for(int i = 0; i < blocks.size(); i++) {
			sum += blocks.get(i).getBlockSize();
		}
		return sum;
	}
}
