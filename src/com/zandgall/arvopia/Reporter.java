package com.zandgall.arvopia;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.zandgall.arvopia.utils.LoaderException;
import com.zandgall.arvopia.utils.Utils;

public class Reporter {
	
	public static ArrayList<String> scannedLines = new ArrayList<String>();
	
	public static Scanner sc = new Scanner(System.in);
	
	public static boolean sentUser;
	
	public static String user;
	
	public Reporter() {
		File check = new File("C:\\Arvopia\\username.txt");
		sentUser = check.exists();
		
		if(sentUser)
			user = LoaderException.readFile("C:\\Arvopia\\username.txt");
		else {
			addUser();
		}
	}
	
	public static void out(String error) {
		String toEmail, fromEmail, subject, message;
		toEmail = "Alexanderdgall@gmail.com";
		fromEmail = "messanger.zandgall@gmail.com";
		
		if(!sentUser) {
			System.out.print("What's your username? : ");
			user = sc.nextLine();
			sentUser = true;
			Utils.fileWriter(user, "C:\\Arvopia\\username.txt");
		} else {
			user = LoaderException.readFile("C:\\Arvopia\\username.txt");
		}
		
		
		subject = "Bug report: "+user;
		message = "Bug report from: "+user+System.lineSeparator()+System.lineSeparator();
		message+= "Error: "+error+System.lineSeparator()+System.lineSeparator();
		System.out.print("Anything you want to say? (Leave blank for no) : ");
		String custom = sc.nextLine();
		if(custom.length() > 1)
			message+=user+": "+custom+System.lineSeparator()+System.lineSeparator();
		message+= " ~ End of bug report";
		
		
		try {
			
			
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			  // Get a Properties object
			     Properties props = System.getProperties();
			     props.setProperty("mail.smtp.host", "smtp.gmail.com");
			     props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			     props.setProperty("mail.smtp.socketFactory.fallback", "false");
			     props.setProperty("mail.smtp.port", "465");
			     props.setProperty("mail.smtp.socketFactory.port", "465");
			     props.put("mail.smtp.auth", "true");
			     props.put("mail.debug", "true");
			     props.put("mail.store.protocol", "pop3");
			     props.put("mail.transport.protocol", "smtp");
			     final String username = "messanger.zandgall@gmail.com";//
			     final String password = "openpass";
			     Session session = Session.getDefaultInstance(props, 
			                          new Authenticator(){
			                             protected PasswordAuthentication getPasswordAuthentication() {
			                                return new PasswordAuthentication(username, password);
			                             }});
			     
			
			Message m = new MimeMessage(session);
			System.out.println("Sending message "+subject+": "+message+" from "+fromEmail+" to "+toEmail);
			m.setFrom(new InternetAddress(fromEmail));
			m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			m.setSubject(subject);
			m.setText(message);
			
			Transport.send(m);
			System.out.println("Message sent!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(System.lineSeparator()+System.lineSeparator()+System.lineSeparator()+System.lineSeparator()+System.lineSeparator());
		}
	}
	
	public static void sendMessage(String subject, String message) {
		String toEmail, fromEmail;
		toEmail = "Alexanderdgall@gmail.com";
		fromEmail = "messanger.zandgall@gmail.com";
		
		if(!sentUser) {
			System.out.print("What's your username? : ");
			user = sc.nextLine();
			sentUser = true;
			Utils.fileWriter(user, "C:\\Arvopia\\username.txt");
		} else {
			user = LoaderException.readFile("C:\\Arvopia\\username.txt");
		}
		
		
		try {
			
			
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			  // Get a Properties object
			     Properties props = System.getProperties();
			     props.setProperty("mail.smtp.host", "smtp.gmail.com");
			     props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			     props.setProperty("mail.smtp.socketFactory.fallback", "false");
			     props.setProperty("mail.smtp.port", "465");
			     props.setProperty("mail.smtp.socketFactory.port", "465");
			     props.put("mail.smtp.auth", "true");
			     props.put("mail.debug", "true");
			     props.put("mail.store.protocol", "pop3");
			     props.put("mail.transport.protocol", "smtp");
			     final String username = "messanger.zandgall@gmail.com";//
			     final String password = "openpass";
			     Session session = Session.getDefaultInstance(props, 
			                          new Authenticator(){
			                             protected PasswordAuthentication getPasswordAuthentication() {
			                                return new PasswordAuthentication(username, password);
			                             }});
			     
			
			Message m = new MimeMessage(session);
			System.out.println("Sending message "+subject+": "+message+" from "+fromEmail+" to "+toEmail);
			m.setFrom(new InternetAddress(fromEmail));
			m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			m.setSubject(subject);
			m.setText("Message from: " + user + System.lineSeparator() +message);
			
			Transport.send(m);
			System.out.println("Message sent!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(System.lineSeparator()+System.lineSeparator()+System.lineSeparator()+System.lineSeparator()+System.lineSeparator());
		}
	}
	
	public static void addUser() {
		String toEmail, fromEmail, subject, message, user;
		toEmail = "Alexanderdgall@gmail.com";
		fromEmail = "messanger.zandgall@gmail.com";
			System.out.print("What's your username? : ");
			user = sc.nextLine();
			sentUser = true;
			Utils.fileWriter(user, "C:\\Arvopia\\username.txt");
		subject = "New user!";
		message = user+" has joined the world of Arvopia" + System.lineSeparator();
			String extra;
			System.out.print("Anything you want to say about yourself? (Leave blank for no) : ");
			extra = sc.nextLine();
			if(extra.length()>1)
				message+=user+" says: "+extra;
		
		
		try {
			
			
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			  // Get a Properties object
			     Properties props = System.getProperties();
			     props.setProperty("mail.smtp.host", "smtp.gmail.com");
			     props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			     props.setProperty("mail.smtp.socketFactory.fallback", "false");
			     props.setProperty("mail.smtp.port", "465");
			     props.setProperty("mail.smtp.socketFactory.port", "465");
			     props.put("mail.smtp.auth", "true");
			     props.put("mail.debug", "true");
			     props.put("mail.store.protocol", "pop3");
			     props.put("mail.transport.protocol", "smtp");
			     final String username = "messanger.zandgall@gmail.com";//
			     final String password = "openpass";
			     Session session = Session.getDefaultInstance(props, 
			                          new Authenticator(){
			                             protected PasswordAuthentication getPasswordAuthentication() {
			                                return new PasswordAuthentication(username, password);
			                             }});
			     
			
			Message m = new MimeMessage(session);
			System.out.println("Sending message "+subject+": "+message+" from "+fromEmail+" to "+toEmail);
			m.setFrom(new InternetAddress(fromEmail));
			m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			m.setSubject(subject);
			m.setText(message);
			
			Transport.send(m);
			System.out.println("Message sent!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(System.lineSeparator()+System.lineSeparator()+System.lineSeparator()+System.lineSeparator()+System.lineSeparator());
		}
	}
	
	public static void quick(String error) {
		String toEmail, fromEmail, subject, message, user;
		toEmail = "Alexanderdgall@gmail.com";
		fromEmail = "messanger.zandgall@gmail.com";
		if(!sentUser) {
			System.out.print("What's your username? : ");
			user = sc.nextLine();
			sentUser = true;
			Utils.fileWriter("", "C:\\Arvopia\\username.txt");
		} else {
			user = LoaderException.readFile("C:\\Arvopia\\username.txt");
		}
		subject = "Bug report: "+user;
		message = "Bug report from: "+user+System.lineSeparator()+System.lineSeparator();
		message+= "Error: "+error+System.lineSeparator()+System.lineSeparator();
		message+= " ~ End of bug report";
		
		
		try {
			
			
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			  // Get a Properties object
			     Properties props = System.getProperties();
			     props.setProperty("mail.smtp.host", "smtp.gmail.com");
			     props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			     props.setProperty("mail.smtp.socketFactory.fallback", "false");
			     props.setProperty("mail.smtp.port", "465");
			     props.setProperty("mail.smtp.socketFactory.port", "465");
			     props.put("mail.smtp.auth", "true");
			     props.put("mail.debug", "true");
			     props.put("mail.store.protocol", "pop3");
			     props.put("mail.transport.protocol", "smtp");
			     final String username = "messanger.zandgall@gmail.com";//
			     final String password = "openpass";
			     Session session = Session.getDefaultInstance(props, 
			                          new Authenticator(){
			                             protected PasswordAuthentication getPasswordAuthentication() {
			                                return new PasswordAuthentication(username, password);
			                             }});
			     
			
			Message m = new MimeMessage(session);
			System.out.println("Sending message "+subject+": "+message+" from "+fromEmail+" to "+toEmail);
			m.setFrom(new InternetAddress(fromEmail));
			m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			m.setSubject(subject);
			m.setText(message);
			
			Transport.send(m);
			System.out.println("Message sent!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(System.lineSeparator()+System.lineSeparator()+System.lineSeparator()+System.lineSeparator()+System.lineSeparator());
		}
	}
	
}
