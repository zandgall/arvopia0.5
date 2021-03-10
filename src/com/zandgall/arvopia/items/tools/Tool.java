package com.zandgall.arvopia.items.tools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.enviornment.Light;

public abstract class Tool {
	
	protected Handler game;
	
	public static enum tools {
		AXE, NONE;
	}
	
	public Tool(Handler game, boolean weapon) {
		this.game = game;
	}
	
	public abstract tools Type();
	
	public abstract void tick();
	
	public abstract void render(Graphics g, int x, int y);
	
	public abstract void custom1(int x, int y);

	public abstract void custom2(int i);
	
	public BufferedImage texture() {
		return null;
	}
	
	public abstract BufferedImage getFrame();

	public abstract int getYOffset();
	
	public abstract int getXOffset();
	
	public boolean hasLight() {
		return false;
	}
	
	public Light getLight() {
		return null;
	}
	
	public abstract boolean smashOrStab();

	public abstract void setFrame(int frameInt);
	
}
