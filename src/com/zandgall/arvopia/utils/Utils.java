package com.zandgall.arvopia.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
	
	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static boolean parseBoolean(String bool) {
		if(bool.contains("true") || bool.contains("True") || bool.contains("TRUE")) {
			System.out.println(bool);
			return true;
		}
		
		System.out.println("False: "+bool);
		
		return false;
	}
	
	public static double parseDouble(String number) {
		try {
			return Double.parseDouble(number);
		} catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static long parseLong(String number) {
		try {
			return Long.parseLong(number);
		} catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
public static void fileWriter(String string, String fileName) {
		
		File file = new File(fileName);
		
		
		
	    BufferedWriter writer;
		try {
			
			if(file.exists())
				file.delete();
			
			if(file.createNewFile())
				System.out.println("File: "+file+" created!");
			
			writer = new BufferedWriter(new FileWriter(fileName));
			
			writer.write(string);
	     
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}

	public static void existWriter(String string, String fileName) {
		
		File file = new File(fileName);
		
	    BufferedWriter writer;
		try {
			
			if(!file.exists())
				if(file.createNewFile())
					System.out.println("File exception");
			
			writer = new BufferedWriter(new FileWriter(fileName));
			
			writer.write(string);
	     
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	public static void createDirectory(String fileName) {
		
		File file = new File(fileName);
		
		if(file.exists())
			return;
		
		System.out.println("Directory : " + file.mkdirs());
		
	}
}
