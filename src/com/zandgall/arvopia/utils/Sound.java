package com.zandgall.arvopia.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	
	Clip clip;
	URL url;
	FloatControl volume;
	String path;
	public Sound(String path) {
		try {
			this.path = path;
	        // Open an audio input stream.
			System.out.println("File: "+new File(path).toString()+" Loaded");
	        url = new File(path).toURI().toURL();
	        AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	        // Get a sound clip resource.
	        clip = AudioSystem.getClip();
	        
	        // Open audio clip and load samples from the audio input stream.
	        clip.open(audioIn);
	        
	        // Set's volume
	        volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	        volume.setValue(0f);
	      } catch (UnsupportedAudioFileException e) {
	        e.printStackTrace();
	      } catch (IOException e) {
	        e.printStackTrace();
	      } catch (LineUnavailableException e) {
	        e.printStackTrace();
	      }
	}
	
	public void Start(int loops, boolean print) {
		if(print)
			System.out.println("Started: " + path);
        clip.start();
        if(loops < 0) {
        	clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
        	clip.loop(loops);
        }
	}
	public void Stop(boolean print) {
		if(print)
			System.out.println("Stopped: " + path);
		clip.stop();
		clip.setFramePosition(0);
	}
	
	public void Close() {
		clip.close();
		System.out.println("Closed: " + path);
	}
	
	public void setPosition(int spot) {
		clip.setFramePosition(spot);
	}
	
	public void tick(boolean print) {
		if(clip.getFramePosition() >= clip.getFrameLength()-2) {
			Stop(print);
		}
	}
	
	public void setVolume(int volume, boolean print) {
		if(print) {
			System.out.println("Changed volume of: " + path + " to " + (volume+94));
			System.out.println("Full number: "+volume);
		}
		
		this.volume.setValue(volume);
	}
	
	public Clip getSound() {
		return clip;
	}
	
	public boolean hasEnded() {
		return clip.getFrameLength() <= clip.getFramePosition() || (!clip.isRunning());
	}
}