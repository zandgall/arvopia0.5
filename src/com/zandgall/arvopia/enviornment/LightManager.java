package com.zandgall.arvopia.enviornment;

import java.awt.Graphics;
import java.util.ArrayList;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.utils.Public;

public class LightManager {
	
	private ArrayList<Light> light;
	
	private Handler game;
	
	public LightManager(Handler game) {
		light = new ArrayList<Light>();
		this.game = game;
	}
	
	public void tick() {
		
	}
	
	public Light getClosest(int x, int y) {
		Light i;
		i = light.get(0);
		for(Light l: light) {
			int x1 = (int) Public.range(l.getMax(), 255, Public.dist(x, y, l.getX(), l.getY())/(l.getStrength()/10));
			int x2 = (int) Public.range(i.getMax(), 255, Public.dist(x, y, i.getX(), i.getY())/(i.getStrength()/10));
			if(x1<x2) {
				i = l;
			}
		}
		
		return i;
	}
	
	public void render(Graphics g) {
		for(Light l: light) {
			l.render(g);
		}
	}
	
	public void addLight(Light light) {
		this.light.add(light);
		game.log("Light: "+light+" Added");
	}
	
	public ArrayList<Light> getList() {
		return light;
	}
	
	
}
