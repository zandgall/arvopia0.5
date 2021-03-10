package com.zandgall.arvopia.state;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.utils.Sound;
import com.zandgall.arvopia.utils.SoundPlayer;

public abstract class State {
	
	protected Handler handler;
	static Handler handlerStatic;
	
	static Sound currentSong;
	static SoundPlayer player = new SoundPlayer(currentSong);
	
	private static State currentState = null, prevState = null;
	
	public static void setState(State state){
		prevState = currentState;
		currentState = state;
		handlerStatic.log("State is now: "+state);
	}
	
	public static State getState(){
		return currentState;
	}
	
	public static State getPrev() {
		return prevState;
	}
	//CLASS
	
	public State(Handler handler) {
		this.handler = handler;
		handlerStatic = handler;
	}
	
	public void openWorld(boolean open, int index) {
		
	}
	
	public void setSong(Sound song) {
		currentSong = song;
		player.setSong(currentSong);
		player.play();
	}
	
	public static void setVolume(double volume) {
		player.volume(volume);
	}
	
	public static boolean songEnded() {
		Sound sound = player.getSong();
		if(sound!=null) {
			return sound.hasEnded();
		}
		return true;
	}
	
	public static Sound getSong() {
		return player.getSong();
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g, Graphics2D g2d);
	
	public abstract void init();
	
	
}
