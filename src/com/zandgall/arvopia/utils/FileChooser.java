package com.zandgall.arvopia.utils;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class FileChooser extends JFrame {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField filename = new JTextField(), dir = new JTextField();
		
	
	public String[] extensions = new String[] {
			
	};
		 public String getFile(String directory) {
			 JFileChooser c = new JFileChooser(directory);
		      // Demonstrate "Open" dialog:
		      int rVal = c.showOpenDialog(FileChooser.this);
		      if (rVal == JFileChooser.APPROVE_OPTION) {
		    		  filename.setText(c.getSelectedFile().getName());
		        	dir.setText(c.getSelectedFile().getPath());
		      }
		      if (rVal == JFileChooser.CANCEL_OPTION) {
		        filename.setText("You pressed cancel");
		        dir.setText("");
		      }
		      
		      ArrayList<String> path = new ArrayList<String>();
		      for(int i=0; i<dir.getText().length(); i++) {
		    	  path.add(String.valueOf(dir.getText().toCharArray()[i]));
		      }
		      
		      String fin = "";
		      
		      for(int i = 0; i < path.size(); i++) {
		    	  fin+=path.get(i);
		      }
		      
		      System.out.println("World: "+fin);
		      
		      return dir.getText();
		 }
		 
		 public String saveFile(String directory) {
			 JFileChooser c = new JFileChooser(directory);
		      // Demonstrate "Save" dialog:
		      int rVal = c.showSaveDialog(FileChooser.this);
		      if (rVal == JFileChooser.APPROVE_OPTION) {
		    		  filename.setText(c.getSelectedFile().getName());
		        	dir.setText(c.getSelectedFile().getPath());
		      }
		      if (rVal == JFileChooser.CANCEL_OPTION) {
		        filename.setText("You pressed cancel");
		        dir.setText("");
		      }
		      
		      ArrayList<String> path = new ArrayList<String>();
		      for(int i=0; i<dir.getText().length(); i++) {
		    	  path.add(String.valueOf(dir.getText().toCharArray()[i]));
		      }
		      
		      String fin = "";
		      
		      for(int i = 0; i < path.size(); i++) {
		    	  fin+=path.get(i);
		      }
		      
		      System.out.println("World: "+fin);
		      
		      return dir.getText();
		 }
		 
		 public boolean accept(File file) {
			    if (file.isDirectory()) {
			      return true;
			    } else {
			      String path = file.getAbsolutePath().toLowerCase();
			      for (int i = 0, n = extensions.length; i < n; i++) {
			        String extension = extensions[i];
			        if ((path.endsWith(extension) && (path.charAt(path.length() 
			                  - extension.length() - 1)) == '.')) {
			          return true;
			        }
			      }
			    }
			    return false;
			}
}
