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
	
	public int getLength() {
		return blocks.size();
	}
}
