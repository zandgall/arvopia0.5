package com.zandgall.arvopia;

import java.awt.Cursor;

import com.zandgall.arvopia.display.Display;
import com.zandgall.arvopia.gfx.GameCamera;
import com.zandgall.arvopia.input.KeyManager;
import com.zandgall.arvopia.input.MouseManager;
import com.zandgall.arvopia.state.OptionState;
import com.zandgall.arvopia.state.State;
import com.zandgall.arvopia.utils.Public;
import com.zandgall.arvopia.worlds.World;

public class Handler {

	private Log player;
	private Log worldl;
	private Log keyEvent;
	private Log fpsLogger;

	private Game game;
	private World world;

	public Game getGame() {
		return game;
	}
	
	public void setCursor(Cursor cursor) {
		game.getDisplay().getFrame().setCursor(cursor);
	}
	
	public OptionState options() {
		return (OptionState) game.optionState;
	}
	
	public void setVolume() {
		State.setVolume(Public.range(-80, 6, getVolume()));
	}

	public int getWidth() {
		return game.getWidth();
	}

	public int getHeight() {
		return game.getHeight();
	}

	public GameCamera getGameCamera() {
		return game.getGameCamera();
	}
	
	public float xOffset() {
		return game.getGameCamera().getxOffset();
	}
	
	public float yOffset() {
		return game.getGameCamera().getyOffset();
	}

	public KeyManager getKeyManager() {
		return game.getKeyManager();
	}

	public MouseManager getMouse() {
		return game.getMouse();
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public World getWorld() {
		return world;
	}
	
	public double getWind() {
		return world.getEnviornment().getWind();
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Handler(Game game) {
		this.game = game;
	}

	public void log(String string) {
		Game.log.log(string);
	}
	
	public Display display() {
		return game.getDisplay();
	}

	public double getVolume() {
		OptionState o = (OptionState) game.optionState;
		
		return o.volume.getValue()-80;
	}
	
	public void init() {
//		if(!player.exists())
			player = new Log("C:\\Arvopia\\logs\\Player\\player.txt", "Player");
//		if(!worldl.exists())
			worldl = new Log("C:\\Arvopia\\logs\\World\\world.txt", "World");
//		if(!keyEvent.exists())
			keyEvent = new Log("C:\\Arvopia\\logs\\Key Events\\keyEvent.txt", "Keys");
			
		fpsLogger = new Log("C:\\Arvopia\\logs\\FPSLogs\\Fps.txt", "Fps");
	}

	public void logPlayer(String string) {
		player.log(string);
	}

	public void logWorld(String string) {
		worldl.logSilent(string);
	}

	public void logKeys(String string) {
		keyEvent.log(string);
	}

	public void logWorldSilent(String string) {
		worldl.logSilent(string);
	}

	public void logSilent(String message) {
		Game.log.logSilent(message);
	}
	
	public void saveFps(int fps) {
		fpsLogger.logSilent("FPS: "+fps);
	}
}
