package com.zandgall.arvopia.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.gfx.ImageLoader;
import com.zandgall.arvopia.utils.Button;

public class WorldLoaderState extends State{
	
	public Button world1, world2, world3, level1, level2, staircase, defaultworld, loadWorld, loadSave, back;
	
	int init = 0;
	
	private BufferedImage img;
	
	public WorldLoaderState(Handler handler) {
		super(handler);
		
		world1 = new Button(handler, 10, 10, 80, 25, "Platforms everywhere for 0.3", "World 1");
		world2 = new Button(handler, 10, 40, 80, 25, "A flat plain-like world for 0.4", "World 2");
		world3 = new Button(handler, 10, 70, 80, 25, "A new forest based level for 0.5", "World 3");
		level1 = new Button(handler, 10, 70, 80, 25, "Loads levelone", "World 3");
		level2 = new Button(handler, 10, 100, 80, 25, "Loads leveltwo", "World 4");
		staircase = new Button(handler, 10, 130, 80, 25, "Loads staircase.txt", "World 5");
		defaultworld = new Button(handler, 10, 160, 80, 25, "Loads DefaultWorld", "Default");
		loadWorld = new Button(handler, 10, handler.getHeight()-40, 115, 25, "Opens a file browser to select a file", "Open Other");
		loadSave = new Button(handler, 140, handler.getHeight()-40, 70, 25, "Opens a save file of your choice", "Saves");
		back = new Button(handler, 225, handler.getHeight()-40, 55, 25, "Takes you back to title screen", "Back");
		
	}

	@Override
	public void tick() {
		
		
		
		loadWorld.tick();
		
		loadSave.tick();
		
		back.tick();
		
		if(back.on)
			State.setState(handler.getGame().menuState);
		
		if(world1.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 0);
			
		} else if(world2.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 1);
			
		} else if(world3.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 2);
			
		} else if(level1.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 0);
			
		} else if(level2.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 1);
			
		} else if(defaultworld.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 2);
			
		} else if(staircase.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 5);
			
		} else if(loadWorld.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(true, 0);
			
		} else if(loadSave.on) {
			State.setState(handler.getGame().gameState);
			((GameState) handler.getGame().gameState).openSave();
		}
		
		world1.tick();
		world2.tick();
		world3.tick();
		
		if(back.hovered || loadWorld.hovered || loadSave.hovered || world1.hovered || world2.hovered || world3.hovered) handler.getMouse().changeCursor("HAND");
		else handler.getMouse().changeCursor("");
		
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g2d.setTransform(handler.getGame().getDefaultTransform());
		
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		
		g.setColor(Color.lightGray);
		g.fillRect(0, handler.getHeight()-50, handler.getWidth(), 51);
		g.setColor(Color.darkGray);
		g.fillRect(3, handler.getHeight()-47, handler.getWidth(), 51);
		g.setColor(Color.gray);
		g.fillRect(3, handler.getHeight()-47, handler.getWidth()-6, 44);
		g.setColor(Color.black);
		g.drawRect(0, handler.getHeight()-50, handler.getWidth(), 51);
		
		world1.render(g);
		world2.render(g);
		world3.render(g);
		
		loadWorld.render(g);
		loadSave.render(g);
		
		back.render(g);
		
		if(world1.on || world2.on || world3.on) {
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.setColor(Color.black);
			g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
			g.setColor(Color.white);
			
			g.drawString("Loading world...", 100, 100);

		}
		
	}

	@Override
	public void init() {
		
	}

}
