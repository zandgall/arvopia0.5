package com.zandgall.arvopia.utils;

public class SoundPlayer implements Runnable{
	
	Sound sound;
	Thread thread;
	
	public SoundPlayer(Sound sound) {
		this.sound = sound;
		thread = new Thread(this);
	}
	
	public void setSong(Sound sound) {
		if(this.sound!=null)
			this.sound.Stop(false);
		this.sound = sound;
		this.sound.Start(0, true);
	}
	
	public Sound getSong() { 
		return sound;
	}
	
	public void play() {
//		thread.start();
	}
	
	public boolean done() {
		if(sound!=null) {
			return sound.hasEnded();
		}
		return true;
	}
	
	public void volume(double volume) {
		sound.setVolume((int) volume, true);
	}
	
	@SuppressWarnings("deprecation")
	public void stop() {
		sound.Stop(true);
		thread.stop();
	}
	
	@Override
	public void run() {
		while(true) {
			if(!sound.hasEnded())
				sound.tick(false);
		}
	}
	
}
