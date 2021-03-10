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

public class Changelog extends State {
	
	public String[] changelog;
	
	private Button back;
	
	private int scroll;
	
	public Changelog(Handler handler) {
		super(handler);
		
		scroll = 0;
		
		back = new Button(handler, handler.getWidth()-60, handler.getHeight()-30, 55, 20, "Brings you back to the main menu", "Back");
		
		try {
			changelog = LoaderException.streamToString(LoaderException.loadResource("/Changelog.txt"), 14).split("\\r");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			changelog = new String[] {"404 File not found: There was an error trying to load the changelog :( try again later"};
		}
	}

	@Override
	public void tick() {
		
		back.tick();
		if(back.on)
			State.setState(handler.getGame().menuState);
		
		if(handler.getKeyManager().up && scroll<10)
			scroll+=5;
		if(handler.getKeyManager().down && scroll > -790)
			scroll-=5;
		
		if(handler.getMouse().getMouseScroll()>0 && scroll>-790 || (handler.getMouse().getMouseScroll()<0 && scroll<10))
			scroll-=handler.getMouse().getMouseScroll()*10;
		
		if(scroll > 10)
			scroll = 10;
		if(scroll < -790)
			scroll = -790;
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g2d.setTransform(handler.getGame().getDefaultTransform());
		
		g.setColor(new Color(120, 225, 255));
		
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		
		back.render(g);
		
		g2d.translate(0, scroll);
		
		
		
		g.setFont(Public.defaultFont);
		g.setColor(Color.black);
		for(int i = 0; i<changelog.length; i++)
			g.drawString(changelog[i], 15, i*13+10);
	}

	@Override
	public void init() {
		
	}
	
}
