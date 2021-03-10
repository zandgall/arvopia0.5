package com.zandgall.arvopia.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.utils.Button;
import com.zandgall.arvopia.utils.LoaderException;
import com.zandgall.arvopia.utils.Public;
import com.zandgall.arvopia.utils.ToggleButton;
import com.zandgall.arvopia.utils.Utils;

public class Instructions extends State{
	
	public String[] instructions;
	
	
	private ToggleButton again;
	private Button back;
	
	private int scroll;
	
	public Instructions(Handler handler) {
		super(handler);
		
		scroll = 0;
		
		again = new ToggleButton(handler, handler.getWidth()-210, 40, 205, 20, new BufferedImage[] {}, "Show this page again at startup?", "Don't show this again");
		back = new Button(handler, handler.getWidth()-60, handler.getHeight()-30, 55, 20, "Brings you back to the main menu", "Back");
		
		try {
			instructions = LoaderException.streamToString(LoaderException.loadResource("/Instructions.txt"), 14).split("\\r");
		} catch (IOException e) {
			instructions = new String[] {"404 File not found: There was an error trying to load the instructions :( try again later" + System.lineSeparator() + "It seems that I've broken, this, it might be fixed by the next update... hmm, wonder why..."};
		}
	}

	@Override
	public void tick() {
		
		boolean preAgain = again.on;
		
		again.tick();
		back.tick();
		
		if(preAgain != again.on) {
			Utils.fileWriter("true", "C:\\Arvopia\\DontShowThisAgain\\");
			
			String string;
			string = LoaderException.readFile("C:\\Arvopia\\DontShowThisAgain");
			
			handler.log("Set to "+string);
		}
		
		if(back.on)
			State.setState(handler.getGame().menuState);
		
		if(handler.getKeyManager().up && scroll<10)
			scroll+=5;
		if(handler.getKeyManager().down && scroll > -145)
			scroll-=5;
		
		if(handler.getMouse().getMouseScroll()>0 && scroll>-145 || (handler.getMouse().getMouseScroll()<0 && scroll<10))
			scroll-=handler.getMouse().getMouseScroll()*10;
		
		if(scroll > 10)
			scroll = 10;
		if(scroll < -145)
			scroll = -145;
		
		handler.getMouse().changeCursor("");
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g2d.setTransform(handler.getGame().getDefaultTransform());
		
		g.setColor(new Color(120, 225, 255));
		
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		
		again.render(g);
		back.render(g);
		
		g2d.translate(0, scroll);
		
		
		
		g.setFont(Public.defaultFont);
		g.setColor(Color.black);
		for(int i = 0; i<instructions.length; i++)
			g.drawString(instructions[i], 15, i*13+10);
	}

	@Override
	public void init() {
		
	}
	
}
