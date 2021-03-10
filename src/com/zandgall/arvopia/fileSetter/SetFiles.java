package com.zandgall.arvopia.fileSetter;

import java.io.File;
import java.io.IOException;

import com.zandgall.arvopia.utils.Utils;

public class SetFiles {
	
	public static File file;
	
	public static void fileSet() {
		
		file = new File("C:\\Arvopia\\Options.txt");
		if(!file.exists())
			try {
				if(file.createNewFile())
					System.out.println("Options File Created");
				Utils.existWriter("60 2 5 6 60 false", "C:\\Arvopia\\Options.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		file = new File("C:\\Arvopia\\DontShowThisAgain");
		if(!file.exists())
			try {
				if(file.createNewFile())
					System.out.println("DontShow File Created");
				
				Utils.existWriter("false", "C:\\Arvopia\\DontShowThisAgain");
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
	
}
