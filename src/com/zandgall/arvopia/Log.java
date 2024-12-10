package com.zandgall.arvopia;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.zandgall.arvopia.utils.Utils;

public class Log {
	
	public String file;
	public String log;
	public boolean exists = false;
	
	public Log(String file, String source) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
		Date date = new Date();
		
		String dateString = formatter.format(date);
		
		this.file = file+" "+dateString + " ("+System.currentTimeMillis()+").txt";
		
		log+="Logger Initiated for "+source + " at " + dateString + System.lineSeparator();
		
		Utils.fileWriter("Logger Initiated for "+source + " at " + dateString, this.file);
		
		System.out.println("Logger Initiated for "+source + " at " + dateString);
		
		exists = true;
		
	}
	
	public boolean exists() {
		if(exists==true)
			return exists;
		else return false;
	}

	public void log(String log) {
		
		System.out.println(log);
		
		this.log+=System.lineSeparator()+log;
		
		Utils.existWriter(this.log, file);
		
	}

	public void out(String string) {
		System.out.println(string);
	}

	public void logSilent(String log) {
		this.log+=System.lineSeparator()+log;
		
		Utils.existWriter(this.log, file);
		
	}
}
