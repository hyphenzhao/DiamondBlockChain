package com.uts.DBC.model;

import java.util.ArrayList;

public class Chain {
	private ArrayList<Block> blocks = new ArrayList<Block>();
	
	public Chain() {
		
	}
	
	public Chain(ArrayList<String> text) {
		for(int i = 0; i < text.size(); i++) {
			blocks.add(new Block(text.get(i)));
		}
	}
	
	@Override
	public String toString() {
		String result = "";
		for(int i = 0; i < blocks.size(); i++) {
			result += blocks.get(i).toChain() + "\n";
		}
		return result;
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}
	
	public void pushBlock(Block block) {
		blocks.add(block);
	}
	
	public Block getBlock(int i) {
		return this.blocks.get(i);
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
