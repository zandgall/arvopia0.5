package com.zandgall.arvopia.guis;

import java.awt.Graphics;

import com.zandgall.arvopia.Handler;

public abstract class Gui {
	
	protected Handler game;
	
	public Gui(Handler game) {
		this.game = game;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
	public abstract void init();
	
}
