package com.zandgall.arvopia.utils;

import java.awt.Color;
import java.awt.Graphics;

import com.zandgall.arvopia.Handler;

public class ArraySlider {
	private Handler game;

	private int min, val;
	private boolean hv, hovered;

	private double ratio;

	private int x, y;
	private int mouseX, mouseY;
	private boolean mouseLeft;
	private String name;
	
	private String[] array;
	
	public ArraySlider(Handler game, String[] array, boolean hv, String name) {
		this.game = game;
		
		this.array = array;
		
		min = 0;
		ratio = Math.max(array.length / 10, 0.1);
		this.val = (int) 0;
		this.hv = hv;
		this.name = name;

		x = 0;
		y = 0;
	}
	
	public void tick(int x, int y) {
		mouseX = game.getMouse().rMouseX();
		mouseY = game.getMouse().rMouseY();
		mouseLeft = game.getMouse().isLeft();
//		mouseRight = game.getMouse().isRight();

		this.x = x;
		this.y = y;
		
		if (mouseX > x - 1 && mouseX < x + 101 && mouseY > y - 5 && mouseY < y + 10) {
			hovered = true;
		} else {
			hovered = false;
		}
		
		if (mouseLeft && (game.getMouse().isDragged() || game.getMouse().isClicked())) {
			if (hv) {
				if (mouseX > x - 1 && mouseX < x + 101 && mouseY > y - 5 && mouseY < y + 10) {
					val = mouseX-x;
				}
			} else {
				if (mouseY > y - 1 && mouseY < y + 101 && mouseX > x - 5 && mouseX < x + 10) {
					val = mouseY-y;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		g.setFont(Public.defaultFont);
		if (hv) {
			g.setColor(Color.black);
			g.drawRect(x - 1, y - 1, 101, 16);
			g.setColor(Color.darkGray);
			g.fillRect(x, y, 100, 15);
			if(!hovered) {
				g.setColor(Color.lightGray);
			} else {
				g.setColor(Color.gray);
			}
			g.fillRect(x + 1, y + 1, 99, 14);
			if(!hovered) {
				g.setColor(Color.gray);
			} else {
				g.setColor(new Color(100, 100, 100));
			}
			g.fillRect(x + 1, y + 1, 97, 12);

			g.drawString(name + " " + getValue(), x + 10, y + 30);
		} else {
			g.setColor(Color.black);
			g.drawRect(x - 1, y - 1, 16, 101);
			g.setColor(Color.darkGray);
			g.fillRect(x, y, 15, 101);
			g.setColor(Color.lightGray);
			g.fillRect(x + 1, y + 1, 14, 99);
			g.setColor(Color.gray);
			g.fillRect(x + 1, y + 1, 12, 97);
		}

		if (val > -1 && val < 101) {
			g.setColor(Color.black);
			g.drawRect(x + val - 11, y - 3, 21, 21);
			g.setColor(Color.darkGray);
			g.fillRect(x + val - 10, y - 2, 20, 20);
			g.setColor(Color.lightGray);
			g.fillRect(x + val - 10, y - 2, 18, 18);
			g.setColor(Color.gray);
			g.fillRect(x + val - 9, y - 1, 17, 17);
		} else {
			game.log("Error on Slider: " + name);
		}
	}
	
	
	public String getValue() {
		return array[(int) Math.round((val*ratio)/10+min)];
	}
}
