package com.zandgall.arvopia.state;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.gfx.PublicAssets;
import com.zandgall.arvopia.guis.Gui;
import com.zandgall.arvopia.guis.PlayerGui;
import com.zandgall.arvopia.utils.Button;
import com.zandgall.arvopia.utils.FileChooser;
import com.zandgall.arvopia.utils.Sound;
import com.zandgall.arvopia.worlds.World;

public class GameState extends State {
	private World world;

	public Gui u;

	private final String[] levels = new String[] { "/Worlds/LevelOne", "/Worlds/LevelTwo", "/Worlds/0.5Forest",
			"/Worlds/world1.txt", "/Worlds/world2.txt", "/Worlds/Staircase.txt" };

	private Sound SweetGuitar, StarsInTheNight, Playtime;

	private boolean songPlaying, loadingWorld;

	public boolean isSongPlaying() {
		return songPlaying;
	}

	private Button skip;

	public GameState(Handler handler) {
		super(handler);

		songPlaying = true;

		PublicAssets.init();

		SweetGuitar = new Sound("Songs/SweetGuitar.wav");
		StarsInTheNight = new Sound("Songs/StarsInTheNight.wav");
		Playtime = new Sound("Songs/Playtime.wav");

		world = new World(handler, "/Worlds/DefaultWorld", false, false);
		world.finish(false);
		handler.setWorld(world);

		u = new PlayerGui(handler);

		skip = new Button(handler, 100, 130, 80, 20, "Skips the resseting of FPS", "Skip");

	}

	public void openWorld(boolean open, int index) {

		world.reset();
		loadingWorld = true;

		if (open) {

			FileChooser fileGet = new FileChooser();

			String i = fileGet.getFile("C:\\Arvopia");

			if (i.length() > 0) {
				loadWorld(i, true);
			} else {
				State.setState(getPrev());
				handler.log("Couldn't load the world specified");
			}
		} else {
			loadWorld(levels[index], false);
		}
	}

	public void saveWorld() {
		FileChooser fileSet = new FileChooser();

		String i = fileSet.saveFile("C:\\Arvopia\\Saves");

		world.saveWorld(i + ".arv");
	}

	public void openSave() {
		FileChooser fileGet = new FileChooser();

		String i = fileGet.getFile("C:\\Arvopia\\Saves");

		if (i.length() > 0) {
			loadWorld(i, true);
		} else {
			State.setState(getPrev());
			handler.log("Couldn't load the world specified");
		}
	}

	public void loadWorld(String path, boolean tf) {
		handler.log("World: " + path + " loaded");
		world = new World(handler, path, tf, true);
		world.percentDone = 0;
		loadingWorld = true;

		handler.getGame().stable = false;
	}

	@Override
	public void tick() {
		if (!loadingWorld) {
			world.tick();
		}
		if (!ready) {
			world.finish(true);
			handler.getGame().forceRender();
		}
		u.tick();

		songPlaying = !State.songEnded();

		if (!songPlaying) {
			if (world.getEnviornment().getHours() > 9 && world.getEnviornment().getHours() < 19) {
				if (Math.random() < 2 && world.getEntityManager().getPlayer().health >= world.getEntityManager()
						.getPlayer().MAX_HEALTH) {
					Playtime.setVolume(-5, true);
					setSong(Playtime);
				} else {
					SweetGuitar.setVolume(-5, true);
					setSong(SweetGuitar);
				}
			} else {
				StarsInTheNight.setVolume(-5, true);
				setSong(StarsInTheNight);
			}

			handler.setVolume();
		}
	}

	boolean preWorked = false, ready = false;
	public boolean resettingFps = true;

	int loadingPhase = 0;

	public void setLoadingPhase(int loadingPhase) {
		this.loadingPhase = loadingPhase;
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		if (ready) {
			world.render(g, handler.getGame().get2D());
			g2d.setTransform(handler.getGame().getDefaultTransform());
			u.render(g);
		}

		if (!ready) {
			
			handler.getGame().setTps(60);

			handler.setWorld(world);
			resettingFps = false;
			loadingWorld = false;
			ready = true;
		}

	}

	@Override
	public void init() {

	}

}
