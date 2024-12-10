package com.zandgall.arvopia.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.zandgall.arvopia.Handler;

public class TextEditor {
	
	Handler game;
	
	private int x, y, width, height;
	String content;
	
	private boolean selected = false;
	
	public TextEditor(Handler game, int x, int y, int width, int rows) {
		this.game = game;
		
		content = "";
		
		this.x = x;
		this.y = y;
		this.width = width;
		height = rows*12;
	}
	
	public TextEditor(Handler game, int x, int y, int width, int rows, String charset) {
		this.game = game;
		
		content = charset;
		
		this.x = x;
		this.y = y;
		this.width = width;
		height = rows*12;
	}
	
	boolean trippin;
	long trippedtimer;
	
	public void tick() {
		boolean[] keys = game.getKeyManager().keys;
		
		if(game.getMouse().fullLeft) {
			int x = game.getMouse().getMouseX();
			int y = game.getMouse().getMouseY();
			if(x>=this.x && x<=this.x+width && y>=this.y && y<=this.y + height)
				selected = true;
			else selected = false;
		}
		
		if(game.getKeyManager().preTyped && selected)
			if(keys[KeyEvent.VK_BACK_SPACE] && content.length()>=1)
				content = content.substring(0, content.length()-1);
			else if(game.getKeyManager().keys[KeyEvent.VK_ENTER] && getGameContent().length<height)
				content+="\n";
			else if(Character.isDefined(game.getKeyManager().getNameOfKey()) && !keys[KeyEvent.VK_BACK_SPACE])
				content+=(game.getKeyManager().getNameOfKey().toString());
		
		if(trippedtimer>=30 && selected) {
			if(trippin) trippin=false;
			else trippin=true;
			
			trippedtimer = 0;
		} else trippedtimer++;
		
		if(selected)
			game.getMouse().changeCursor("TYPE");
	}
	
	public void render(Graphics g) {
		if(selected) g.setColor(Color.white);
		else g.setColor(Color.lightGray);
		g.fillRect(x, y, width, height);
		if(selected) g.setColor(Color.lightGray);
		else g.setColor(Color.gray);
		g.fillRect(x+2, y+2, width-2, height-2);
		g.setColor(Color.black);
		g.drawRect(x, y, width, height);
		for(int i = 0; i<getGameContent().length; i++)
			g.drawString(getGameContent()[i], x+1, y+11+(i*12));
	}
	
	
	
	public String[] getGameContent() {
		
		ArrayList<String> newString = new ArrayList<String>();
		
		newString.add("");
		
		for(int i = 0; i<content.length(); i++)
			if(content.charAt(i) == '\n') {
				newString.add("");
			} else
				newString.set(newString.size()-1, newString.get(newString.size()-1)+content.charAt(i));
		
		if(trippin) {
			newString.set(newString.size()-1, newString.get(newString.size()-1)+'|');
		}
			
		String[] array = new String[newString.size()];
		newString.toArray(array);
			
			
		return array;
	}
	
	public String getContent() {
		return content;
	}
	
}
