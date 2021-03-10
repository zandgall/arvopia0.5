package com.zandgall.arvopia.enviornment;

import java.awt.Color;
import java.awt.Graphics;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.utils.Public;

public class Light {
	
	private int x, y, Strength, max;
	public Color color;
	
	private boolean on;
	
	private Handler game;
	
	public Light(Handler game, int x, int y, int Strength, int max, Color color) {
		this.x = x;
		this.y = y;
		this.Strength = Strength;
		this.max = max;
		this.color = color;
		this.game = game;
	}
	
	public void render(Graphics g) {
		for(int f = 0; f<game.getWidth(); f+=4) {
			for(int h = 0; h<game.getHeight(); h+=4) {
				
				int i;
				
				if(game.getWorld().getEnviornment().getHours() < 12) {
					i = (int) Public.range(0, 170, 255-game.getWorld().getEnviornment().getTotalMinutes()*0.75+250);
				} else {
					i = (int) Public.range(0, 170, game.getWorld().getEnviornment().getTotalMinutes()-1100);
				}
				
				int alpha = (int) Math.min(i, (170 - Public.dist(f,h,x, y)))/2;
				
				Color c;
				
				if(alpha > 0) {
					c = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
				} else {
					c = new Color(0,0,0,0);
				}
				if(alpha>0) {
					g.setColor(c);
					
					g.fillRect(f, h, 4, 4);
				}
			}
		}
	}
	
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void turnOn() {
		on = true;
	}
	
	public void turnOff() {
		on = false;
	}
	
	
	
	// Getters and Setters
	
	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
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

	public void setY(int y) {
		this.y = y;
	}

	public int getStrength() {
		return Strength;
	}

	public void setStrength(int strength) {
		Strength = strength;
	}
	
}
