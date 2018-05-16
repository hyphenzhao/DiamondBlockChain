package com.uts.DBC.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileIO {
	private String filename;
	private ArrayList<String> rawTextLines = new ArrayList<String>();
	
	public FileIO(String filename) {
		this.filename = filename;
	}
	
	public ArrayList<String> loadFileText() {
		try {
			File file = new File(this.filename);
			if (file.isFile() && file.exists())
	        {
	            InputStreamReader inputStream = new InputStreamReader(
	                    new FileInputStream(file));
	            BufferedReader bufferedReader = new BufferedReader(inputStream);
	            String lineText = null;

	            while ((lineText = bufferedReader.readLine()) != null)
	            {
	            	if(lineText != "")
	            		rawTextLines.add(lineText);
	            }
	            bufferedReader.close();
	            inputStream.close();
	        }
		}catch(Exception e){
			System.out.println("Input file error.");
			e.printStackTrace();
		}
		return this.rawTextLines;
	}
	
	public void writeToFile(String text) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename));
		    writer.write(text);
		    writer.close();
		}catch(Exception e){
			System.out.println("Output file error.");
			e.printStackTrace();
		}
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public boolean isEmptyFile() {
		try{
			File file = new File(this.filename);
			if (file.isFile() && file.exists())
	        {
	            return false;
	        } else {
	        	return true;
	        }
		} catch(Exception e){
			System.out.println("Input file error.");
			e.printStackTrace();
		}
		return rawTextLines.isEmpty();
	}
}
