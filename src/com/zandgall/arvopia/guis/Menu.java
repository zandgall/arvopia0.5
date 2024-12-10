package com.zandgall.arvopia.guis;

import java.awt.Graphics;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.state.GameState;
import com.zandgall.arvopia.state.State;
import com.zandgall.arvopia.utils.*;

public class Menu extends Gui{
	
	private Button returnToGame, titleScreen, options, exit, save;
	
	
	public Menu(Handler game) {
		super(game);
		
		exit = new Button(game, game.getWidth()/2 - 10, game.getHeight()/2 - 10, 45, 20, "Quits the application", "Quit");
		save = new Button(game, game.getWidth()/2 - 14, game.getHeight()/2 - 40, 54, 20, "Saves the current world", "Save");
		options = new Button(game, game.getWidth()/2 - 30, game.getHeight()/2-70, 80, 20, "Takes you to the options menu", "Options");
		returnToGame = new Button(game, game.getWidth()/2-65, game.getHeight()/2-130, 150, 20, "Takes you back the the current game", "Return to Game");
		titleScreen = new Button(game, game.getWidth()/2-50, game.getHeight()/2-100, 120, 20, "Takes you to the title screen", "Title Screen");
	}

	@Override
	public void tick() {
		
		if(save.on) {
			
			GameState g = (GameState) game.getGame().gameState;
			
			g.saveWorld();
		}
		
		if(exit.on)
			game.getGame().stop();
		
		if(returnToGame.on) {
			game.getGame().unPause();
			game.getWorld().getEntityManager().getPlayer().viewMenu = false;
		}
		
		if(options.on) {
			State.setState(game.getGame().optionState);
			game.getGame().unPause();
			game.getWorld().getEntityManager().getPlayer().viewMenu = false;
		}
		
		if(titleScreen.on) {
			State.setState(game.getGame().menuState);
			game.getGame().unPause();
			game.getWorld().getEntityManager().getPlayer().viewMenu = false;
		}
		
		exit.tick();
		options.tick();
		returnToGame.tick();
		titleScreen.tick();
		save.tick();
	}

	@Override
	public void render(Graphics g) {
		exit.render(g);
		options.render(g);
		returnToGame.render(g);
		titleScreen.render(g);
		save.render(g);
	}

	@Override
	public void init() {
		
	}

}
