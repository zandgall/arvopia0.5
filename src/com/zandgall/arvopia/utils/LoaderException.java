package com.zandgall.arvopia.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Scanner;


public class LoaderException {
//	@WillNotClose //this is a JSR 305 annotation
	public static InputStream loadResource(final String resourcePath)
		    throws IOException
		{	
		    final URL url = LoaderException.class.getResource(resourcePath);
		    if (url == null)
		        throw new IOException(resourcePath + ": resource not found");
		    return url.openStream();
		}
	
	public static String streamToString(final InputStream is, final int bufferSize) {
	    final char[] buffer = new char[bufferSize];
	    final StringBuilder out = new StringBuilder();
	    try (Reader in = new InputStreamReader(is, "UTF-8")) {
	        for (;;) {
	            int rsz = in.read(buffer, 0, buffer.length);
	            if (rsz < 0)
	                break;
	            out.append(buffer, 0, rsz);
	        }
	    }
	    catch (UnsupportedEncodingException ex) {
	        /* ... */
	    }
	    catch (IOException ex) {
	        /* ... */
	    }
	    return out.toString();
	}
	
	@SuppressWarnings("resource")
	public static String readFile(String path) {
		String output = null;
		
		try {
			File file = new File(path); 
			System.out.println("File chosen: "+file.getAbsolutePath()+" is "+file.exists());
			Scanner sc = new Scanner(file);
			
			sc.useDelimiter("//Z");
			
			output = sc.next();
			
			System.out.println("File loaded: "+output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return output;
	}
}
