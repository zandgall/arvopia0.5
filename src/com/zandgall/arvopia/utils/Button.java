package com.zandgall.arvopia.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;

public class Button {
	
	Handler game;
	
	private int x, y, width, height;
	
	private int mouseX, mouseY;
	private boolean mouseLeft, mouseRight;
	
	public boolean on, data, image;
	public boolean hovered;
	
	private String description, name;
	
	public Button(Handler game, int x, int y, int width, int height, String description, String name) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.game = game;
		this.description = description;
		this.name = name;
	}
	
	public void tick() {
		mouseX = game.getMouse().rMouseX();
		mouseY = game.getMouse().rMouseY();
		mouseLeft = game.getMouse().isLeft();
		mouseRight = game.getMouse().isRight();
		if (mouseX > x - 1 && mouseX < x + width + 1 && mouseY > y - 1 && mouseY < y + height + 1) {
			if(mouseLeft && game.getMouse().isClicked()) {
				on = true;
				data = false;
			} else if (mouseRight) {
				data = true;
				on = false;
			} else {
				hovered = true;
				on = false;
			}
		} else {
			hovered = false;
			on = false;
			data = false;
		}
		
		if(hovered)
			game.getMouse().changeCursor("HAND");
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void render(Graphics g) {
		if(on) {
			g.setColor(Color.lightGray);
			g.fillRect(x, y, width, height);
			g.setColor(Color.darkGray);
			g.fillRect(x+2, y+2, width-2, height-2);
			g.setColor(Color.black);
			g.drawRect(x, y, width, height);
				
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString(name, x+6, y+height-2);
		} else if (hovered) {
			g.setColor(Color.gray);
			g.fillRect(x, y, width, height);
			g.setColor(Color.lightGray);
			g.fillRect(x, y, width-2, height-2);
			g.setColor(Color.black);
			g.drawRect(x, y, width, height);
				
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString(name, x+6, y+height-2);
			g.setColor(new Color(200, 200, 200, 20));
			g.fillOval(mouseX-10, mouseY-10, 20, 20);
		} else {
			g.setColor(Color.darkGray);
			g.fillRect(x, y, width, height);
			g.setColor(Color.lightGray);
			g.fillRect(x, y, width-2, height-2);
			g.setColor(Color.gray);
			g.fillRect(x+2, y+2, width-4, height-4);
			g.setColor(Color.black);
			g.drawRect(x, y, width, height);
			
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString(name, x+6, y+height-2);
		}
		
		if(data) {
			g.setFont(new Font("Arial", Font.PLAIN, 12));
			g.setColor(Color.black);
			g.drawRect(mouseX+10, mouseY, (name.length()+description.length())*6, 14);
			g.setColor(Color.white);
			g.fillRect(mouseX+11, mouseY+1, (name.length()+description.length())*6-1, 13);
			g.setColor(Color.black);
			g.drawString(name + ": "+description, mouseX+12, mouseY+11);
		}
	}
	
}
