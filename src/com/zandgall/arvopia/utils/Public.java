package com.zandgall.arvopia.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import com.zandgall.arvopia.Game;
import com.zandgall.arvopia.Handler;

public class Public {
	
	private static Handler game;
	
	public static double dist(double x1, double y1, double x2, double y2) {
		double d = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
		return Math.abs(d);
	}
	
	public static double difference(double num1, double num2) {
		return Math.max(num1, num2)-Math.min(num1, num2);
	}
	
	public static double range(double min, double max, double num) {
		return(Math.min(max, Math.max(num, min)));
	}
	
	public static Object identifyRange(double min, double max, double in, Object identifier) {
		if(range(min, max, in) != in) {
			return identifier;
		}
		return in;
	}
	
	public static int grid(double in, double every, double offset) {
		return (int) ((Math.floor((in)/every)));
	}
	
	public static double zoomX(double num) {
		return ((num-game.getWidth()/2)/Game.scale+(game.getWidth()/2));
	}
	
	public static double zoomY(double num) {
		return ((num-game.getHeight()/2)/Game.scale+(game.getHeight()/2));
	}
	
	public static Color blend( Color c1, Color c2, float ratio ) {
	    if ( ratio > 1f ) ratio = 1f;
	    else if ( ratio < 0f ) ratio = 0f;
	    float iRatio = 1.0f - ratio;

	    int i1 = c1.getRGB();
	    int i2 = c2.getRGB();

	    int a1 = (i1 >> 24 & 0xff);
	    int r1 = ((i1 & 0xff0000) >> 16);
	    int g1 = ((i1 & 0xff00) >> 8);
	    int b1 = (i1 & 0xff);

	    int a2 = (i2 >> 24 & 0xff);
	    int r2 = ((i2 & 0xff0000) >> 16);
	    int g2 = ((i2 & 0xff00) >> 8);
	    int b2 = (i2 & 0xff);

	    int a = (int)((a1 * iRatio) + (a2 * ratio));
	    int r = (int)((r1 * iRatio) + (r2 * ratio));
	    int g = (int)((g1 * iRatio) + (g2 * ratio));
	    int b = (int)((b1 * iRatio) + (b2 * ratio));

	    return new Color( a << 24 | r << 16 | g << 8 | b );
	}
	
	public static double random(double min, double max) {
		return Math.random()*(max-min+0.9999)+min;
	}
	
	public static Font digital;
	public static Font defaultFont;
	
	public static int Map(int Input, int InputHigh, int InputLow, int OutputHigh, int OutputLow) {
		return ((Input - InputLow) / (InputHigh - InputLow)) * (OutputHigh - OutputLow) + OutputLow;
	}
	
	public static double Map(double Input, double InputHigh, double InputLow, double OutputHigh, double OutputLow) {
		return ((Input - InputLow) / (InputHigh - InputLow)) * (OutputHigh - OutputLow) + OutputLow;
	}
	
	public static void init(Handler handler) {
		
		game = handler;
		
		try {
			digital = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/Digital-Regular.ttf"));
			digital = digital.deriveFont(Font.BOLD,12);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			digital = new Font("Arial", Font.BOLD, 12);
		} 
		defaultFont = new Font("Arial", Font.PLAIN, 12);
	}
	
	public static double xO (double x) {
		return x - game.getGameCamera().getxOffset();
	}
	
	public static double yO(double y) {
		return y - game.getGameCamera().getyOffset();
	}
	
	//Math.PI/180 = 1
	public static double[] angle(double rotation) {
		double[] output = new double[] {0, 0};
		output[0] = Math.cos(rotation*(Math.PI/180));
		output[1] = Math.sin(rotation*(Math.PI/180));
		return output;
	}
	
	public static double reflect(double rotation, double normal) {
		normal*=2;
		double y = Math.abs(rotation+180 - normal+180);
		return 180 - y;
	}
	
}
