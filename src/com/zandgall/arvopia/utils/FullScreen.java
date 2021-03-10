package com.zandgall.arvopia.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.zandgall.arvopia.Handler;

public class FullScreen {
	
	static Dimension full = Toolkit.getDefaultToolkit().getScreenSize();
	static Dimension def = new Dimension(720, 400);
	
	
	public static void fullscreen(Handler handler) {
		handler.getGame().setDimension((int) full.getWidth(), (int) full.getHeight());
	}
	
	public static void defSize(Handler handler) {
		handler.getGame().setDimension((int) def.getWidth(), (int) def.getHeight());
	}
}
