package com.zandgall.arvopia.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.zandgall.arvopia.ArvopiaLauncher;
import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.Reporter;

public class ImageLoader {
	
	static Handler game = ArvopiaLauncher.game.handler;
	
	public static BufferedImage loadImage(String path) {
		
		try {
			System.out.println("		"+path+" loaded");
			return ImageIO.read(ImageLoader.class.getResource(path));
		} catch (Exception e) {
			game.logSilent(e.getLocalizedMessage());
			Reporter.quick("Could not load "+path+", this could be a big problem");
			return new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		}
		
	}
	
}
