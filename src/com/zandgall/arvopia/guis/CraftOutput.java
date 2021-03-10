package com.zandgall.arvopia.guis;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.zandgall.arvopia.Handler;

public class CraftOutput {
	
	BufferedImage image;
	
	int x, y, ox;
	
	Handler game;
	
	boolean clicked;
	
	ArrayList<String> craftList;
	
	public CraftOutput(Handler handler, BufferedImage image, int x, int y, ArrayList<String> craftList) {
		this.image= image;
		
		this.craftList = craftList;
		
		this.x = x;
		this.y = y;
		ox = x;
		
		
		game = handler;
	}
	
	public void tick() {
		clicked = false;
		if(game.getMouse().isLeft()) {
			if(game.getMouse().rMouseX() > x && game.getMouse().rMouseX() < x+image.getWidth() && game.getMouse().rMouseY() > y && game.getMouse().rMouseY()<y+image.getHeight()) {
				clicked = true;
			}
		}
	}
	
	public boolean craftable(ArrayList<String> list) {
		for(String i: craftList) {
			if(!list.contains(i)) {
				return false;
			}
		}
		
		if(list.size() != craftList.size()) {
			return false;
		}
		
		return true;
	}
	
	public void render(Graphics g) {
		g.drawImage(image, x, y, null);
	}
	
}
