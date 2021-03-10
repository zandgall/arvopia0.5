package com.zandgall.arvopia.guis;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.utils.Public;

public class InventoryItem {
	
	private Handler game;
	
	private int x, y;

	int amount;

	int movedX;

	int movedY;
	
	boolean used;

	private int preX;

	private int preY;
	
	public int customCraftingMessage = 255, customCraftingInt;
	
	public static InventoryItem TAKEN;
	
	private BufferedImage texture;
	private boolean hovered;
	
	public InventoryItem(Handler game, BufferedImage texture, int x, int y, int amount) {
		this.texture = texture;
		this.x = x*20;
		this.y = y*20;
		preX=this.x;
		preY=this.y;
		
		this.amount = amount;
		hovered = false;
		
		this.game = game;
	}
	
	public void setPre(int x, int y) {
		preX = x;
		preY = y;
	}
	
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void tick(int amount) {
		this.amount = amount;
		
		boolean left = game.getMouse().fullLeft;
		int mouseX = game.getMouse().rMouseX();
		int mouseY = game.getMouse().rMouseY();
		
		hovered = (mouseX>x && mouseX<x+18 && mouseY>y && mouseY<y+18);
		
		if(left) {
			if(InventoryItem.TAKEN == this) {
				InventoryItem.TAKEN = this;
				setX(mouseX-9); y = mouseY-9;
				movedX=x;
				movedY=y;
			} else if(InventoryItem.TAKEN == null && hovered)
				InventoryItem.TAKEN = this;
		} else if(movedX==getX() && movedY==y) {
			
			if(InventoryItem.TAKEN == this)
				InventoryItem.TAKEN = null;
			
			x=(int) Public.range(0, game.getWidth(), preX);
			y=preY;
		} else {
			
			if(InventoryItem.TAKEN == this)
				InventoryItem.TAKEN = null;
			
//			preX=x;
//			preY=y;
		}
		
	}
	
	public void render(Graphics g) { 
		if(hovered) {
			g.setColor(Color.black);
			g.drawRect(getX(), y, 20, 20);
		}
			g.drawImage(texture, x, y, null);
			g.setColor(new Color(0, 0, 0, 200));
			g.setFont(new Font("Dialog", Font.BOLD, 12));
			g.drawString(""+amount, x+1, y+16);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
}
