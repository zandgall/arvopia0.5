package com.zandgall.arvopia.gfx;

import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Game;
import com.zandgall.arvopia.Handler;

public class Assets {
	
	private Game g;
	
	Handler game = new Handler(g);
	
	private static int width, height;
	BufferedImage sheetfile;
	static SpriteSheet sheet;
	
	public Assets(BufferedImage file, int Width, int Height, String name){
		sheet = new SpriteSheet(file);
		sheetfile = file;
		width = Width;
		height = Height;
		
		game.log("Assets created: " + name);
	}
	
	public BufferedImage get(int x, int y){
		return sheet.get(x*width,y*height,width,height);
	}
	
	public BufferedImage get() {
		return sheetfile;
	}
	
	public void reset(BufferedImage file, int Width, int Height, String name){
		sheet = new SpriteSheet(file);
		sheetfile = file;
		width = Width;
		height = Height;
		
		game.log("Assets re-created: " + name);
	}
}
