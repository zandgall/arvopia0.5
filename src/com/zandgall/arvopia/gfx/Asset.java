package com.zandgall.arvopia.gfx;

import java.awt.image.BufferedImage;

public class Asset {
	
	public BufferedImage file;
	
	public Asset(String file){
		
		this.file = ImageLoader.loadImage(file);
		
	}
	
	public BufferedImage get(){
		return file;
	}
}
