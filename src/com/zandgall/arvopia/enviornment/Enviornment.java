package com.zandgall.arvopia.enviornment;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.gfx.ImageLoader;
import com.zandgall.arvopia.state.OptionState;
import com.zandgall.arvopia.tiles.Tile;
import com.zandgall.arvopia.utils.Public;
import com.zandgall.arvopia.worlds.World;

public class Enviornment {
	// int thing, thingChange, thingSwing, thingTimer;
	// Thing, the thing that it is
	// ThingChange, how much it changes by
	// ThingSwing, how often it changes
	// ThingTimer, the private time it changes
	private double wind, windChange, windSwing, windTimer, maxWind, minWind; 
//	private double temp, tempChange, tempSwing;
	
	private long Time, totalTime;
	public int TimeChange, rohundo = 1, lapse = 1, collevti = 1, state = 0, preState=0;
	private Light moon, sun;
	private BufferedImage moonIm, sunIm;
	private LightManager lightManager;
	
	public int lightQuality;
	
	Font font;
	
	
	public LightManager getLightManager() {
		return lightManager;
	}

	private Handler game;
	
	public double getWind() {
		return wind;
	}
	public double getMaxWind() {
		return maxWind;
	}
	public double getMinWind() {
		return maxWind;
	}
	public double getWindChange() {
		return windChange;
	}
	
	public double getWindSwing() {
		return windSwing;
	}

	public Enviornment(Handler handler, double wind, double windChange, double windSwing) {
		this.wind = wind/10;
		maxWind = wind*0.5;
		minWind = wind*-0.2;
		this.windChange = windChange/10;
		this.windSwing = windSwing;
		windTimer = 0;
		
		lightQuality = 6;
		
		this.game = handler;
		
		
		font = Public.digital;
		
		
		game.log("Enviornment "+this+" initialized with wind: "+this.wind+" windChange: "+this.windChange+" windSwing: "+this.windSwing);
		
		
		sun = new Light(handler, 0,0,35, 1, Color.red);
		moon = new Light(handler, 0,0,10, 50, Color.white);
		lightManager = new LightManager(handler);
		lightManager.addLight(moon);
		lightManager.addLight(sun);
		sun.turnOn();
		moon.turnOn();
		
		moonIm = ImageLoader.loadImage("/textures/Moon.png");
		sunIm = ImageLoader.loadImage("/textures/Sun.png");
		
		OptionState o = (OptionState) game.getGame().optionState;
		TimeChange = o.timeSpeed.getValue();
		Time = 13*60*60;
	}
	
	public void setTimeSpeed(int timeSpeed) {
		TimeChange = timeSpeed;
	}
	
	public long getTime() {
		return Time;
	}
	
	public void setTime(long Time) {
		this.Time = Time;
	}
	
	public long getTotalTime() {
		return totalTime;
	}
	
	public int getHours() {
		return (int) (Time/60/60) + 1;
	}
	
	public int getMinutes() {
		return (int) (Time/60)%60;
	}
	
	public int getTotalMinutes() {
		return (getMinutes()+getHours()*60);
	}
	
	public void tick() {
		if(windTimer >= windSwing) {
			wind+=Math.random()*windChange*2 - windChange;
			windTimer = 0;
			if(wind<minWind) {
				wind+=windChange;
			} else if(wind>maxWind) {
				wind-=windChange;
			}
			game.log("Wind: "+(Math.round(wind*10)));
		} else {
			windTimer++;
		}
		
		
		Time+=TimeChange;
		totalTime += TimeChange;
		if(getHours() == 25) {
			Time = 0;
			rohundo++;
		}
		
//		if(rohundo==15&&state==preState) {
//			state++;
//		}
//		if(state == 19) state = 0;
		
		
		if(rohundo==32) {
			rohundo=1;
			collevti++;
//			state++;
			preState=state;
		}
		
		if(collevti==11) {
			collevti=1;
			lapse++;
		}
	}
	
	public void renderSunMoon(Graphics g) {
		
		int green = (int) Public.range(10, 200, (-0.00023*Math.pow(getTotalMinutes(), 2)+500));
		int red = (int) Public.range(25, 100, (-0.00023*Math.pow(getTotalMinutes(), 2)+400));
		int blue = (int) Public.range(100, 255, (-0.00023*Math.pow(getTotalMinutes(), 2)+600));
		if(getHours()<12) {
			green = (int) Public.range(10, 200, (-0.0006671*Math.pow(getTotalMinutes(), 2) + 10*getTotalMinutes() - 3500)/15);
			red = (int) Public.range(25, 100, (-0.0006671*Math.pow(getTotalMinutes(), 2) + 10*getTotalMinutes() - 3500)/17);
			blue = (int) Public.range(100, 255, (-0.0006671*Math.pow(getTotalMinutes(), 2) + 10*getTotalMinutes() - 3500)/10);
		}
		
		if(getHours()>=15) {
			red = (int) Math.max(red, Public.range(0, 255, (-0.007*Math.pow(getTotalMinutes()-1200, 2)+255)));
			green = (int) Math.max(green, Public.range(0, 255, (-0.007*Math.pow(getTotalMinutes()-1200, 2)+220)));
			blue = (int) Math.min(blue, Public.range(0, 255, (0.006*Math.pow(getTotalMinutes()-1300, 2)+120)));
		}
		
		if(getHours()>=5 && getHours()<12) {
//			red*=2;
//			green= (int) ((double) green * 1.5);
//			blue/=2;
		}
		
		g.setColor(new Color(red, green, blue));
		
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		if(true) {
			int y;
			double x;
			int totmin;
			if(getHours()<12) {
				y = (int) (Math.pow(getTotalMinutes()-120, 2) * 0.001)-100;
				totmin = getTotalMinutes();
			} else {
				y = (int) (Math.pow(getTotalMinutes()-1200-game.getWidth()/2, 2)*0.001)-100;
				totmin = getTotalMinutes()-(24*60);
			}
			
			x = ((totmin/14.99)/100)*(World.getWidth()*Tile.TILEWIDTH)*2+game.getWidth()/2;
			
			y -= game.getGameCamera().getyOffset();
			x -= game.getGameCamera().getxOffset();
			
			g.drawImage(moonIm, (int) x, y, null);
			
			moon.setY(y+10);
			moon.setX((int) x+10);
		}
		
		
		if(getHours() >= 0) {
			int x = (int) (getTotalMinutes()*1.6)-1050;
			int y = (int) (0.001*Math.pow((getTotalMinutes()*1.3)-game.getWidth()/2-750, 2));
			g.drawImage(sunIm, x, y, 30, 30, null);
			
			sun.setX(x);
			sun.setY(y + 15);
		}
	}
	
	private int[] stars;
	private int[] starsAlpha;
	
	public void setupStars() {
		stars = new int[(int) Public.random(10, 30)*2];
		starsAlpha = new int[stars.length];
		for(int i = 0; i <stars.length-1; i+=2) {
			stars[i] = (int) Public.random(0, game.getWidth());
			stars[i+1] = (int) Public.random(0, game.getHeight());
			starsAlpha[i/2]= (int) Public.random(170, 300);
		}
	}
	
	public void renderStars(Graphics g) {
		for(int i = 0; i < stars.length-1; i+=2) {
			int x = stars[i];
			int y = stars[i+1];
			int alpha1 = (int) Public.range(lightManager.getClosest(x, y).getMax(), Public.range(0, Public.range(0, 255, starsAlpha[i/2]), 255-getTotalMinutes()*0.75+250), (Public.dist(x,y,lightManager.getClosest(x, y).getX(), lightManager.getClosest(x, y).getY())  / (lightManager.getClosest(x, y).getStrength()/10)));
			int alpha2 = (int) Public.range(lightManager.getClosest(x, y).getMax(), Public.range(0, Public.range(0, 255, starsAlpha[i/2]), getTotalMinutes()-1100), (Public.dist(x,y,lightManager.getClosest(x, y).getX(), lightManager.getClosest(x, y).getY()) / (lightManager.getClosest(x, y).getStrength()/10)));
			
			x-=game.getGameCamera().getxOffset()/10;
			y-=game.getGameCamera().getyOffset()/10;
			
			if(getHours()<12) {
				g.setColor(new Color(255, 255, 255, alpha1));
			} else {
				g.setColor(new Color(255, 255, 255, alpha2));
			}
			g.drawRect(x, y, 1, 1);
		}
	}
	
	public void render(Graphics g) {
		
		
		
		for(int x = 0; x<game.getWidth(); x+=lightQuality) {
			for(int y = 0; y<game.getHeight(); y+=lightQuality) {
				
				int alpha1 = (int) Public.range(lightManager.getClosest(x, y).getMax(), Public.range(0, 170, 255-getTotalMinutes()*0.75+250), (Public.dist(x,y,lightManager.getClosest(x, y).getX(), lightManager.getClosest(x, y).getY())  / (lightManager.getClosest(x, y).getStrength()/10)));
				int alpha2 = (int) Public.range(lightManager.getClosest(x, y).getMax(), Public.range(0, 170, getTotalMinutes()-1150), (Public.dist(x,y,lightManager.getClosest(x, y).getX(), lightManager.getClosest(x, y).getY()) / (lightManager.getClosest(x, y).getStrength()/10)));
				
				
				if(getHours() < 12) {
					g.setColor(new Color(0, 0, 50, alpha1));
				} else if(getHours() >=12){
					g.setColor(new Color(0, 0, 50, alpha2));
				}
				
				g.fillRect(x, y, lightQuality, lightQuality);
			}
		}
		
		
		lightManager.render(g);
		
		
		int hours = getHours();
		
		String ampm;
		
		if(hours >= 12 && hours < 24) {
			ampm = " P M";
		} else {
			ampm = " A M";
		}
		
		if(hours > 12) {
			hours-=12;
		}
		
		g.setFont(font);
		g.setColor(Color.black);
		if(hours<10) {
			g.drawString("   "+hours, 10, game.getHeight()-20);
		} else {
			g.drawString("  "+hours, 10, game.getHeight()-20);
		}
		
		if(getMinutes() < 10) {
			g.drawString(":0"+getMinutes(), 40, game.getHeight()-20);
		} else {
			g.drawString(":"+getMinutes(), 40, game.getHeight()-20);
		}
		
		g.drawString("  "+rohundo+"-"+collevti+"-"+lapse, 0, game.getHeight()-50);
		
		g.drawString(ampm, 50, game.getHeight()-20);
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
