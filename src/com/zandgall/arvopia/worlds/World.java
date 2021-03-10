package com.zandgall.arvopia.worlds;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;

import com.zandgall.arvopia.Game;
import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.entity.*;
import com.zandgall.arvopia.entity.creatures.*;
import com.zandgall.arvopia.entity.moveableStatics.*;
import com.zandgall.arvopia.entity.statics.*;
import com.zandgall.arvopia.enviornment.Enviornment;
import com.zandgall.arvopia.items.Item;
import com.zandgall.arvopia.items.ItemManager;
import com.zandgall.arvopia.state.GameState;
import com.zandgall.arvopia.state.State;
import com.zandgall.arvopia.tiles.Tile;
import com.zandgall.arvopia.utils.Button;
import com.zandgall.arvopia.utils.LoaderException;
import com.zandgall.arvopia.utils.Public;
import com.zandgall.arvopia.utils.Utils;

public class World {

	private Enviornment enviornment;

	private Handler handler;

	private static int width;

	private static int height;
	private int spawnx, spawny;
	private static int[][] tiles;

	public int bee, butterfly, fox, stone0, stone1, stone2, flower0, flower1, flower2, youngTrees, midTrees, oldTrees,
			cloud0, cloud1, cloud2, cloud3, cloudY, cannibalTribes, minPerTribe, maxPerTribe;
	private int maxBee, maxButterfly, maxFox, maxStone, maxFlower, maxTrees, maxCannibalTribes;

	int rencount = 0;

	boolean waitingForCreature;

	// Respawn
	private Button respawn;
	private boolean dead;

	// Entities
	private EntityManager entityManager;

	private Entity center;
	private boolean Box = false;

	private boolean loading;
	public double percentDone = 0;

	private ArrayList<ArrayList<Integer>> heights;

	// Items
	private ItemManager itemManager;

	// Save vs Load
	public boolean save = false;
	
	public World(Handler handler, String path, boolean isPath, boolean beginning) {
		this.handler = handler;
		percentDone = 0;

		entityManager = new EntityManager(handler, new Player(handler, 100, 0, false, 2, 3));
		itemManager = new ItemManager(handler);

		center = entityManager.getPlayer();
		waitingForCreature = false;

		loading = true;

		respawn = new Button(handler, handler.getWidth() / 2 - 50, handler.getHeight() / 2 - 25, 100, 25,
				"Respawns the character", "Respawn");

		Creature.init();
		
		loadWorld(path, isPath, beginning);
		
		enviornment.setupStars();

		highestTile();
		
		if(save) {
			
			entityManager.getPlayer().setX(spawnx);
			entityManager.getPlayer().setY(spawny);
			
			return;
		}
		
		if(beginning) {
			GameState gset = (GameState) (State.getState());
			gset.setLoadingPhase(3);
		}
		
		addShrubbery(10);
		
		addTrees(youngTrees, 0, 5);
		addTrees(midTrees, 6, 10);
		addTrees(oldTrees, 11, 15);
		
		
		addCloud(cloud0, 0);
		addCloud(cloud1, 1);
		addCloud(cloud2, 2);
		addCloud(cloud3, 3);
		addFox(fox);
		addBee(bee, 100000);
		addButterfly(butterfly, 100000);
		addStone(stone0, 0);
		addStone(stone1, 1);
		addStone(stone2, 2);
		addFlower(flower0, 2);
		addFlower(flower1, 1);
		addFlower(flower2, 0);
		entityManager.getPlayer().setX(spawnx);
		entityManager.getPlayer().setY(spawny);
	}
	
	public void finish(boolean beginning) {
		handler.logWorld("Finished loading world");
		percentDone=-5;
		ren();
		percentDone = 0;
		
		if(beginning) {
			GameState gset = (GameState) (State.getState());
			gset.setLoadingPhase(4);
		}
	}

	public void reset() {
		entityManager.getEntities().clear();
		tiles = new int[][] { {} };
		dead = true;
		center = null;
		handler = null;
	}

	public void tick() {

		if (handler.getKeyManager().b) {
			if (Box) {
				Box = false;
			} else {
				Box = true;
			}
		}

		if (dead) {
			respawn.tick();
		}

		entityManager.tick();
		itemManager.tick();
		enviornment.tick();

		if (respawn.on) {
			if (entityManager.getPlayer() != null) {
				entityManager.getPlayer().kill();
			}
			entityManager.setPlayer(new Player(handler, 100, 0, false, 2, 3));
			entityManager.getPlayer().setHealth(entityManager.getPlayer().MAX_HEALTH);
			entityManager.getPlayer().setX(spawnx);
			entityManager.getPlayer().setY(spawny);
			respawn.tick();
			center = entityManager.getPlayer();
			dead = false;
		}

		if (waitingForCreature) {
			for (Entity e2 : entityManager.getEntities()) {
				if (e2.creature) {
					center = e2;
					handler.logWorld("Centered on: " + e2.getClass());
					waitingForCreature = false;
					return;
				}
			}
		}
		
		if (loading) {
			loading = false;
		}
	}
	
	public void spawing() {
		if (Math.random() < 0.001 && flower0 + flower1 + flower2 < maxFlower) {
			if (Math.random() < 1 / 3) {
				addFlower(2, 0);
				flower0 += 2;
			} else if (Math.random() < 0.5) {
				addFlower(2, 1);
				flower1 += 2;
			} else {
				addFlower(2, 2);
				flower2 += 2;
			}
		}

		if (Math.random() < 0.0001 && stone0 + stone1 + stone2 < maxStone) {
			if (Math.random() < 1 / 3) {
				addStone(2, 0);
				stone0 += 2;
			} else if (Math.random() < 0.5) {
				addStone(2, 1);
				stone1 += 2;
			} else {
				addStone(1, 2);
				stone2++;
			}
		}

		if (Math.random() < 0.0001 && youngTrees + midTrees + oldTrees < maxTrees) {

			youngTrees++;

			addTrees(1, 0, 0);

		}

		if (Math.random() < 0.0005 && fox < maxFox) {
			addFox(2);
			fox += 2;
		}

		if (Math.random() < 0.01 && bee < maxBee) {
			addBee(1, 10000);
			bee++;
		}

		if (Math.random() < 0.01 && butterfly < maxButterfly) {
			addButterfly(1, 10000);
			butterfly++;
		}

		if (Math.random() < 0.0025 && cannibalTribes < maxCannibalTribes) {
			addCannibalTribe((int) Public.random(minPerTribe, maxPerTribe), (int) Public.random(5, width - 5));
			cannibalTribes++;
		}
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void render(Graphics g, Graphics2D g2d) {

		g2d.translate(-(Game.scale - 1) * handler.getWidth() / 2, -(Game.scale - 1) * handler.getHeight() / 2);
		g2d.scale(Game.scale, Game.scale);

		handler.getGameCamera().centerOnEntity(center);

		enviornment.renderSunMoon(g);
		enviornment.renderStars(g);

		if (rencount == 0)
			resetGraphics();

		int xStart = (int) (Math.max(handler.getGameCamera().getxOffset() / (Tile.TILEWIDTH), 0));
		int xEnd = (int) (Math.min(width,
				(handler.getGameCamera().getxOffset() + handler.getWidth()) / Tile.TILEWIDTH + 1));
		int yStart = (int) (Math.max(handler.getGameCamera().getyOffset() / (Tile.TILEHEIGHT), 0));
		int yEnd = (int) (Math.min(height,
				(handler.getGameCamera().getyOffset() + handler.getHeight()) / Tile.TILEHEIGHT + 1));

		for (int y = yStart; y < yEnd; y++) {
			for (int x = xStart; x < xEnd; x++) {
				getTile(x, y).render(g, (int) (x * Tile.TILEWIDTH - (handler.getGameCamera().getxOffset())),
						(int) (y * Tile.TILEHEIGHT - handler.getGameCamera().getyOffset()));
			}
		}
		entityManager.render(g, Box);
		itemManager.render(g, false);
		if (dead) {
			respawn.render(g);
		}

		rencount++;
		
		g2d.setTransform(handler.getGame().getDefaultTransform());
		
		
		enviornment.render(g);

		

		entityManager.getPlayer().renScreens(g);

	}

	public ItemManager getItemManager() {
		return itemManager;
	}

	public void setItemManager(ItemManager itemManager) {
		this.itemManager = itemManager;
	}

	private void resetGraphics() {
		for (int i = entityManager.getEntities().size() - 1; i > 0; i--) {
			Entity e = entityManager.getEntities().get(i);
			e.reset();
		}

	}

	public static Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height)
			return Tile.n0;

		Tile t = Tile.tiles[tiles[x][y]];
		if (t == null)
			return Tile.n0;

		return t;
	}

	private void ren() {
//		handler.getGame().forceRender();
	}
	
	private void loadWorld(String path, boolean isPath, boolean beginning) {
		
		GameState gset = null;
		
		if(beginning) {
			gset = (GameState) (State.getState());
			gset.setLoadingPhase(0);
			State.setState(gset);
		}
		
		ren();
		
		String file = null;
		if (isPath) {
			file = LoaderException.readFile(path);
		} else {
			try {
				file = LoaderException.streamToString(LoaderException.loadResource(path), path.length());
			} catch (IOException e) {
				e.printStackTrace();
				handler.logWorldSilent("Error loading world: "+e.getMessage());
				path = "/Worlds/DefaultWorld";
				try {
					file = LoaderException.streamToString(LoaderException.loadResource(path), path.length());
				} catch (IOException e1) {
					e1.printStackTrace();
					handler.logWorldSilent("Error loading backup world: "+e.getMessage());
					file = null;
				}
			}
		}

		String[] tokens = file.split("\\s+");
		
		if(tokens[0].contains("Save")) {
			save = true;
			loadSave(path);
		} else {
			
			percentDone = 0.1;
			ren();
			
			width = Utils.parseInt(tokens[0]);
			height = Utils.parseInt(tokens[1]);
			
			percentDone = 0.2;
			ren();
			
			spawnx = Utils.parseInt(tokens[2]) * 18;
			spawny = Utils.parseInt(tokens[3]) * 18;
	
			percentDone = 0.3;
			ren();
			
			stone0 = Utils.parseInt(tokens[4]);
			stone1 = Utils.parseInt(tokens[5]);
			stone2 = Utils.parseInt(tokens[6]);
			flower0 = Utils.parseInt(tokens[7]);
			flower1 = Utils.parseInt(tokens[8]);
			flower2 = Utils.parseInt(tokens[9]);
			youngTrees = Utils.parseInt(tokens[10]);
			midTrees = Utils.parseInt(tokens[11]);
			oldTrees = Utils.parseInt(tokens[12]);
			
			percentDone = 0.5;
			ren();
	
			bee = Utils.parseInt(tokens[13]);
			butterfly = Utils.parseInt(tokens[14]);
			fox = Utils.parseInt(tokens[15]);
			cannibalTribes = Utils.parseInt(tokens[16]);
			minPerTribe = Utils.parseInt(tokens[17]);
			maxPerTribe = Utils.parseInt(tokens[18]);
			
			percentDone = 0.6;
			ren();
	
			maxStone = Utils.parseInt(tokens[19]);
			maxFlower = Utils.parseInt(tokens[20]);
			maxTrees = Utils.parseInt(tokens[21]);
	
			percentDone = 0.7;
			ren();
			
			maxBee = Utils.parseInt(tokens[22]);
			maxButterfly = Utils.parseInt(tokens[23]);
			maxFox = Utils.parseInt(tokens[24]);
			maxCannibalTribes = Utils.parseInt(tokens[25]);
			
			percentDone = 0.8;
			ren();
			
			enviornment = new Enviornment(handler, Utils.parseDouble(tokens[26]), Utils.parseDouble(tokens[27]),
					Utils.parseDouble(tokens[28]));
	
			percentDone = 0.9;
			ren();
			
			cloud0 = Utils.parseInt(tokens[29]);
			cloud1 = Utils.parseInt(tokens[30]);
			cloud2 = Utils.parseInt(tokens[31]);
			cloud3 = Utils.parseInt(tokens[32]);
			cloudY = Utils.parseInt(tokens[33]);
			
			percentDone = 1;
			ren();
			
			if(beginning) {
				gset = (GameState) (State.getState());
				gset.setLoadingPhase(1);
				State.setState(gset);
			}
			
			tiles = new int[width][height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					tiles[x][y] = Utils.parseInt(tokens[(x + y * width) + 34]);
					
					percentDone = (y/height)*0.9;
					percentDone += (x/width)*0.1;
					ren();
				}
			}
	
			if (tokens.length <= width * height + 34) {
				percentDone = 1;
				ren();
				return;
			} else if (tokens.length > width * height + 34) {
	
				handler.logWorld("Adding custom entities... " + (tokens.length > width * height + 34));
				
				int contInt = width * height + 33;
				
				if(beginning) {
					gset = (GameState) (State.getState());
					gset.setLoadingPhase(2);
					State.setState(gset);
				}
				
				while(contInt < tokens.length-6) {
					
					percentDone = contInt/tokens.length-6;
					ren();
					
					String s = tokens[contInt+1];
					
					if (s.contains("Tree")) {
						entityManager.addEntity(new Tree(handler, Utils.parseDouble(tokens[contInt + 2]),
								Utils.parseDouble(tokens[contInt + 3]), Utils.parseInt(tokens[contInt + 4])), true);
	
						contInt += 4;
					} else if (s.contains("Flower")) {
						entityManager.addEntity(new Flower(handler, Utils.parseDouble(tokens[contInt + 2]),
								Utils.parseDouble(tokens[contInt + 3]), Utils.parseInt(tokens[contInt + 4]),
								Utils.parseDouble(tokens[contInt + 5])), true);
	
						contInt += 5;
					} else if (s.contains("Stone")) {
						entityManager.addEntity(new Stone(handler, Utils.parseDouble(tokens[contInt+ 2]),
								Utils.parseDouble(tokens[contInt + 3]), Utils.parseInt(tokens[contInt  + 4])), true);
	
						contInt += 4;
					} else if (s.contains("Cannibal")) {
						entityManager.addEntity(new Cannibal(handler, Utils.parseDouble(tokens[contInt + 2]),
								Utils.parseDouble(tokens[contInt + 3]), Utils.parseDouble(tokens[contInt  + 4]),
								Utils.parseInt(tokens[contInt + 5]), Utils.parseBoolean(tokens[contInt + 6])), true);
	
						contInt += 6;
					} else if (s.contains("Shrubbery")) {
						entityManager.addEntity(new Shrubbery(handler, Utils.parseDouble(tokens[contInt+2]), Utils.parseDouble(tokens[contInt+3]), Utils.parseInt(tokens[contInt+4])), true);
						
						contInt += 4;
					} else if (s.contains("Butterfly")) {
						entityManager.addEntity(new Butterfly(handler, Utils.parseDouble(tokens[contInt+2]), Utils.parseDouble(tokens[contInt+3]), false, 100000), true);
						
						contInt += 3;
					} else if (s.contains("Bee")) {
						entityManager.addEntity(new Bee(handler, Utils.parseDouble(tokens[contInt+2]), Utils.parseDouble(tokens[contInt+3]), false, 100000), true);
						
						contInt += 3;
					} else if (s.contains("Fox")) {
						entityManager.addEntity(new Fox(handler, Utils.parseDouble(tokens[contInt+2]), Utils.parseDouble(tokens[contInt+3])), true);
						
						contInt+=3;
					} else if (s.contains("Cloud")) {
						entityManager.addEntity(new Cloud(handler, Utils.parseDouble(tokens[contInt+2]), Utils.parseDouble(tokens[contInt+3]), Utils.parseInt(tokens[contInt+4]), Utils.parseDouble(tokens[contInt+5])), true);
						
						contInt+=5;
					} else {
						contInt++;
					}
				}
			}
		}

	}
	
	public void loadSave(String path) {
		String file = LoaderException.readFile(path);
		
		String[] tokens = file.split("\\s+");
		
		width = Utils.parseInt(tokens[1]);
		height = Utils.parseInt(tokens[2]);

		spawnx = (int) Utils.parseDouble(tokens[3]);
		spawny = (int) Utils.parseDouble(tokens[4]);

		stone0 = Utils.parseInt(tokens[5]);
		stone1 = Utils.parseInt(tokens[6]);
		stone2 = Utils.parseInt(tokens[7]);
		flower0 = Utils.parseInt(tokens[8]);
		flower1 = Utils.parseInt(tokens[9]);
		flower2 = Utils.parseInt(tokens[10]);
		youngTrees = Utils.parseInt(tokens[11]);
		midTrees = Utils.parseInt(tokens[12]);
		oldTrees = Utils.parseInt(tokens[13]);

		bee = Utils.parseInt(tokens[14]);
		butterfly = Utils.parseInt(tokens[15]);
		fox = Utils.parseInt(tokens[16]);
		cannibalTribes = Utils.parseInt(tokens[17]);
		minPerTribe = Utils.parseInt(tokens[18]);
		maxPerTribe = Utils.parseInt(tokens[19]);

		maxStone = Utils.parseInt(tokens[20]);
		maxFlower = Utils.parseInt(tokens[21]);
		maxTrees = Utils.parseInt(tokens[22]);

		maxBee = Utils.parseInt(tokens[23]);
		maxButterfly = Utils.parseInt(tokens[24]);
		maxFox = Utils.parseInt(tokens[25]);
		maxCannibalTribes = Utils.parseInt(tokens[26]);

		enviornment = new Enviornment(handler, Utils.parseDouble(tokens[27]), Utils.parseDouble(tokens[28]),
				Utils.parseDouble(tokens[29]));

		cloud0 = Utils.parseInt(tokens[30]);
		cloud1 = Utils.parseInt(tokens[31]);
		cloud2 = Utils.parseInt(tokens[32]);
		cloud3 = Utils.parseInt(tokens[33]);
		cloudY = Utils.parseInt(tokens[34]);

		tiles = new int[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[x][y] = Utils.parseInt(tokens[(x + y * width) + 35]);
			}
		}
		
		int offset = width*height+35, off = offset;
		
		handler.logWorld(tokens[off] + " Entities saved to load");
		
		for(off = offset; off < tokens.length-7;) {
			
			handler.logWorld("Currently loading a "+tokens[off+1]);
			
			handler.logWorld("Offset number "+off+ " Out of " + (tokens.length-7));
			
			if(tokens[off+1].contains("Bee")) {
				
				entityManager.addEntity(new Bee(handler, Utils.parseDouble(tokens[off+2]), Utils.parseDouble(tokens[off+3]), Utils.parseBoolean(tokens[off+4]), Utils.parseLong(tokens[off+5])), false);
				
				off+=5;
			}

			if(tokens[off+1].contains("Butterfly")) {
				
				entityManager.addEntity(new Butterfly(handler, Utils.parseDouble(tokens[off+2]), Utils.parseDouble(tokens[off+3]), Utils.parseBoolean(tokens[off+4]), Utils.parseLong(tokens[off+5])), false);
				
				off+=5;
			}
			
			if(tokens[off+1].contains("Cannibal")) {
				
				entityManager.addEntity(new Cannibal(handler, Utils.parseDouble(tokens[off+2]), Utils.parseDouble(tokens[off+3]), Utils.parseDouble(tokens[off+4]), Utils.parseInt(tokens[off+5]), Utils.parseBoolean(tokens[off+6])), false);
				
				off+=6;
			}
			
			if(tokens[off+1].contains("Fox")) {
				
				entityManager.addEntity(new Fox(handler, Utils.parseDouble(tokens[off+2]), Utils.parseDouble(tokens[off+3])), false);
				
				off+=3;
			}
			
			if(tokens[off+1].contains("Cloud")) {
				
				entityManager.addEntity(new Cloud(handler, Utils.parseDouble(tokens[off+2]), Utils.parseDouble(tokens[off+3]), Utils.parseInt(tokens[off+4]), Utils.parseDouble(tokens[off+5])), false);
				
				off+=5;
			}
			
			if(tokens[off+1].contains("Flower")) {
				
				entityManager.addEntity(new Flower(handler, Utils.parseDouble(tokens[off+2]), Utils.parseDouble(tokens[off+3]), Utils.parseInt(tokens[off+4]), Utils.parseDouble(tokens[off+5])), false);
				
				off+=5;
			}
			
			if(tokens[off+1].contains("Shrubbery")) {
				
				entityManager.addEntity(new Shrubbery(handler, Utils.parseDouble(tokens[off+2]), Utils.parseDouble(tokens[off+3]), Utils.parseInt(tokens[off+4])), false);
				
				off+=4;
			}
			
			if(tokens[off+1].contains("Stone")) {
				
				entityManager.addEntity(new Stone(handler, Utils.parseDouble(tokens[off+2]), Utils.parseDouble(tokens[off+3]), Utils.parseInt(tokens[off+4])), false);
				
				off+=4;
			}
			
			if(tokens[off+1].contains("Tree")) {
				
				entityManager.addEntity(new Tree(handler, Utils.parseDouble(tokens[off+2]), Utils.parseDouble(tokens[off+3]), Utils.parseInt(tokens[off+4])), false);
				
				off+=4;
			} else {
				off++;
			}
			
		}
		
		for(int i = width*height+35; i < tokens.length; i++)
			if(tokens[i].contains("Continue")) {
				offset = i;
				continue;
			}
		
		Player p = entityManager.getPlayer();
		
		p.metal = Utils.parseInt(tokens[offset+1]);
		p.wood = Utils.parseInt(tokens[offset+2]);
		p.petals = Utils.parseInt(tokens[offset+3]);
		p.honey = Utils.parseInt(tokens[offset+4]);
		p.foxFur = Utils.parseInt(tokens[offset+5]);
		p.butterflyWing = Utils.parseInt(tokens[offset+6]);
		
		p.swords = Utils.parseInt(tokens[offset+7]);
		p.torches = Utils.parseInt(tokens[offset+8]);
		p.axes = Utils.parseInt(tokens[offset+9]);
		
		Enviornment e = enviornment;
		
		e.setTime(Utils.parseLong(tokens[offset+10]));
		e.rohundo = Utils.parseInt(tokens[offset+11]);
		e.collevti = Utils.parseInt(tokens[offset+12]);
		e.lapse = Utils.parseInt(tokens[offset+13]);
		
	}
	
	public void saveWorld(String path) {
		String content = "Save "+System.lineSeparator()+width + " ";
		content += height + System.lineSeparator();

		content += entityManager.getPlayer().getX()+" "+entityManager.getPlayer().getY() + System.lineSeparator();
		
		
		// ADD LINE SEPERATORS AND SPACES
		content += stone0+ " ";
		content += stone1+ " ";
		content += stone2+ " ";
		content += flower0+ " ";
		content += flower1+ " ";
		content += flower2+ " ";
		content += youngTrees+" ";
		content += midTrees+" ";
		content += oldTrees+System.lineSeparator();
		
		content += bee+ " ";
		content += butterfly+ " ";
		content += fox+ " ";
		content += cannibalTribes+ " ";
		content += minPerTribe+ " ";
		content += maxPerTribe+ System.lineSeparator();
		
		content += maxStone+ " ";
		content += maxFlower+ " ";
		content += maxTrees+ System.lineSeparator();
		
		content += maxBee+ " ";
		content += maxButterfly+ " ";
		content += maxFox+ " ";
		content += maxCannibalTribes+System.lineSeparator();
		
		content += enviornment.getWind()+ " "; 
		content += enviornment.getWindChange()+ " ";
		content += enviornment.getWindSwing()+System.lineSeparator();
		
		content += cloud0+ " ";
		content += cloud1+ " ";
		content += cloud2+ " ";
		content += cloud3+System.lineSeparator();
		content += cloudY;

		for (int y = 0; y < height; y++) {
			content += System.lineSeparator();
			for (int x = 0; x < width; x++) {
				content += getTile(x, y).getId() + " ";
			}
		}
		content += System.lineSeparator();
		
		content += entityManager.saveString();
		
		content += System.lineSeparator();
		
		content += "Continue";
		
		content += System.lineSeparator();
		
		Player p = entityManager.getPlayer();
		
		content += p.metal + " " + p.wood + " " + p.petals +  " " + p.honey + " " + p.foxFur + " " + p.butterflyWing;
		
		content += System.lineSeparator();
		
		content += p.swords + " " + p.torches + " " + p.axes + System.lineSeparator();
		
		Enviornment e = enviornment;
		
		content += e.getTime() + " " + e.rohundo + " " + e.collevti + " " + e.lapse;
		
		Utils.fileWriter(content, path);
	}

	public Enviornment getEnviornment() {
		return enviornment;
	}

	public int getSpawnx() {
		return spawnx;
	}

	public int getSpawny() {
		return spawny;
	}

	public void outOfBounds(Entity e) {
		if (e.getY() > (height + 10) * Tile.TILEHEIGHT || e.getX() > (width + 10) * Tile.TILEWIDTH
				|| e.getX() < (-10 * Tile.TILEWIDTH) || e.getY() < (-15 * Tile.TILEHEIGHT)) {
			if (e.getClass() == Player.class) {
				Player p = (Player) e;
				if (p.lives == 0) {
					kill(e);
				} else {
					p.lives--;
					p.setX(spawnx);
					p.setY(spawny);
					p.setHealth(p.MAX_HEALTH);
					handler.logWorld("Player lives: " + p.lives);
				}
			} else if (e.getClass() != Cloud.class && (e.getClass() == Flower.class || e.getClass() == Stone.class)) {
				e.kill();
			} else if (e.getClass() != Cloud.class) {
				kill(e);
			}
		}
	}

	public void kill(Entity e) {
		if (e.getClass() != Stone.class && e.getClass() != Player.class) {
			e.dead = true;
			e.kill();
		}
		handler.logWorld("Killed: " + e);

		if (e.getClass() == Flower.class) {
			Flower f = (Flower) e;

			if (f.getType() == 0) {
				flower0--;
				for (int i = 0; i < (int) Math.ceil(Math.random() * 3); i++) {
					handler.getWorld().getItemManager()
							.addItem(Item.whitePetal.createNew((int) f.getX(), (int) f.getY()));
				}
			} else if (f.getType() == 1) {
				flower1--;
				for (int i = 0; i < (int) Math.ceil(Math.random() * 3); i++) {
					handler.getWorld().getItemManager()
							.addItem(Item.pinkPetal.createNew((int) f.getX(), (int) f.getY()));
				}
			} else {
				flower2--;
				for (int i = 0; i < (int) Math.ceil(Math.random() * 3); i++) {
					handler.getWorld().getItemManager()
							.addItem(Item.bluePetal.createNew((int) f.getX(), (int) f.getY()));
				}
			}

		} else if (e.getClass() == Stone.class) {
			Stone s = (Stone) e;
			int i = 1;
			if (s.getType() == 0) {
				if (s.getOrType() == 0) {
					stone0--;
				} else if (s.getOrType() == 1) {
					stone1--;
				} else {
					stone2--;
				}
				s.kill();
				i = 1;
			} else if (s.getType() == 1) {
				s.setType(s.getType() - 1);
				i = 2;
			} else {
				s.setType(s.getType() - 1);
				i = 3;
			}

			if (!loading) {
				for (int b = 0; b < i; b++) {
					itemManager.addItem(Item.metal.createNew((int) (s.getX()), (int) (s.getY() - 36)));
				}
			}

		} else if (e.getClass() == Butterfly.class) {
			butterfly--;
		} else if (e.getClass() == Bee.class) {
			bee--;
		} else if (e.getClass() == Fox.class) {
			fox--;
		} else if (e.getClass() == Tree.class) {
			
			youngTrees--;
			
			Tree t = (Tree) e;
			
			for (int b = 0; b < (t.getAge()/2-3)*2; b++) {
				itemManager.addItem(Item.wood.createNew((int) (e.getX()+e.getbounds().x), (int) (e.getY()+e.getbounds().y - 36)));
			}
		}
		if (e.getClass() == Player.class) {
			Player p = (Player) e;
			if (p.lives == 0) {
				e.kill();
				e.dead = true;
				dead = true;
			} else {
				p.lives--;
				p.setX(spawnx);
				p.setY(spawny);
				p.setHealth(p.MAX_HEALTH);
				handler.logWorld("Player lives: " + p.lives);
			}
		}

		if (e.getClass() == center.getClass() && e.dead) {
			for (Entity e2 : entityManager.getEntities()) {
				if (e2.creature) {
					center = e2;
					handler.logWorld("Centered on: " + e2);
					return;
				}
			}
			if (entityManager.getEntities().size() > 0) {
				center = entityManager.getEntities().get(0);
				handler.logWorld("Centered on: " + center);
				waitingForCreature = true;
			} else {
				handler.logWorld("No more entities to center on");
				waitingForCreature = true;
			}
		}
	}

	public static int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		World.width = width;
	}

	public static int getHeight() {
		return height;
	}

	public static void setHeight(int height) {
		World.height = height;
	}

	public void highestTile() {
		handler.logWorld("Getting highest tiles...");
		heights = new ArrayList<ArrayList<Integer>>();
		for (int x = 0; x < width + 2; x++) {
			handler.logWorldSilent("Tile chosen: " + x);
			heights.add(new ArrayList<Integer>());
			for (int y = 0; y < height + 1; y++) {
				handler.logWorldSilent("Checking tile: (" + x + ", " + y + ")");
				int v = getTile(x, y).getId();
				if (v == 1 || v == 2 || v == 3 || v == 10 || v == 11 || v == 12 || v == 13 || v == 16) {
					handler.logWorldSilent("Tile (" + x + ", " + y + ") is solid");
					heights.get(x).add(y);
					
					if(!save)
						if (v == 1 || v == 10 || v == 13 || v == 16)
							entityManager
									.addEntity(new Shrubbery(handler, x * Tile.TILEWIDTH, (y - 1) * Tile.TILEHEIGHT, 0), false);
						else if (v == 2 || v == 11)
							entityManager
									.addEntity(new Shrubbery(handler, x * Tile.TILEWIDTH, (y - 1) * Tile.TILEHEIGHT, 1), false);
						else if (v == 3 || v == 12)
							entityManager
									.addEntity(new Shrubbery(handler, x * Tile.TILEWIDTH, (y - 1) * Tile.TILEHEIGHT, 2), false);
					
					heights.add(new ArrayList<Integer>());
				}
			}

			if (heights.get(x).size() <= 0) {
				handler.logWorldSilent("404: No tile found");
				heights.get(x).add(-Tile.TILEHEIGHT * 2);
			}
		}
	}

	public boolean checkCollision(int x, int y) {
		x = (int) x / Tile.TILEWIDTH;
		y = (int) y / Tile.TILEHEIGHT;
		if (getTile(x, y).isSolid()) {
			return true;
		} else {
			return false;
		}
	}

	private void addFlower(int amount, int type) {
		for (int i = 0; i < amount; i++) {
			int x = (int) (Math.random() * width * Tile.TILEWIDTH + 1);
			double l = Public.random(-2, 0);
			handler.logWorld("Layer = " + l);
			entityManager.addEntity(new Flower(handler, x,
					(heights.get(x / Tile.TILEWIDTH)
							.get((int) Public.random(0, heights.get(x / Tile.TILEWIDTH).size() - 1)) - 1)
							* Tile.TILEHEIGHT,
					type, l), true);
		}
	}

	private void addStone(int amount, int type) {
		for (int i = 0; i < amount; i++) {
			int x = (int) (Math.random() * width + 1) * Tile.TILEWIDTH;
			entityManager.addEntity(new Stone(handler, x,
					(heights.get(x / Tile.TILEWIDTH)
							.get((int) Public.random(0, heights.get(x / Tile.TILEWIDTH).size() - 1)) - 1)
							* Tile.TILEHEIGHT,
					type), true);
		}
	}

	private void addTrees(int amount, int agemin, int agemax) {
		for (int i = 0; i < amount; i++) {
			int x = (int) (Math.random() * width * Tile.TILEWIDTH + 1);
			entityManager.addEntity(new Tree(handler, x-18,
					(heights.get(x / Tile.TILEWIDTH)
							.get((int) Public.random(0, heights.get(x / Tile.TILEWIDTH).size() - 1)) - 8)
							* Tile.TILEHEIGHT,
					(int) Public.random(agemin, agemax)), true);
		}
	}

	private void addBee(int amount, long timer) {
		for (int i = 0; i < amount; i++) {
			int x = (int) (Math.random() * width + 1);
			int y = (int) (Math.random() * height + 1);

			while (getTile(x, y).isSolid()) {
				x = (int) (Math.random() * width + 1);
				y = (int) (Math.random() * height + 1);
			}

			entityManager.addEntity(new Bee(handler, x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT, false, timer), true);
		}
	}

	private void addButterfly(int amount, long timer) {
		for (int i = 0; i < amount; i++) {
			int x = (int) (Math.random() * width + 1);
			int y = (int) (Math.random() * height + 1);

			while (getTile(x, y).isSolid()) {
				x = (int) (Math.random() * width + 1);
				y = (int) (Math.random() * height + 1);
			}

			entityManager.addEntity(new Butterfly(handler, x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT, false, timer), true);
		}
	}

	private void addFox(int amount) {
		for (int i = 0; i < amount; i++) {
			int x = (int) (Math.random() * width + 1);

			while (getTile(x, 0).isSolid()) {
				x = (int) (Math.random() * width + 1);
			}

			entityManager.addEntity(new Fox(handler, x * Tile.TILEWIDTH, 0), true);
		}
	}

	public void addConBee(int x, int y, long timer) {
		if (bee < maxBee) {
			entityManager.addEntity(new Bee(handler, x, y, false, timer), true);
		} else {
			handler.logWorld("Too many bees!");
		}
	}

	public void addConButterfly(int x, int y, long timer) {
		if (butterfly < maxButterfly) {
			entityManager.addEntity(new Butterfly(handler, x, y, false, timer), true);
		} else {
			handler.logWorld("Too many butterflies!");
		}
	}

	public void addCloud(int amount, int type) {
		for (int i = 0; i < amount; i++) {
			int y = (int) (Math.random() * -handler.getHeight() + cloudY * Tile.TILEHEIGHT);
			double x = Math.random() * (Tile.TILEWIDTH * (width + 8)) - (Tile.TILEWIDTH * 4);
			entityManager.addEntity(new Cloud(handler, x, y, type, Math.random() / 2), true);
		}
	}

	private void addCannibalTribe(int amount, int groupX) {
		entityManager.addEntity(new Cannibal(handler, groupX,
				heights.get(groupX / Tile.TILEWIDTH)
						.get((int) Public.random(0, heights.get(groupX / Tile.TILEWIDTH).size() - 1)) - 2,
				Public.random(0.1, 0.6), 1, true), true);
		for (int i = 0; i < amount - 1; i++) {
			int x = (int) Public.random(groupX - 2, groupX + 2);
			int y = heights.get(x / Tile.TILEWIDTH)
					.get((int) Public.random(0, heights.get(x / Tile.TILEWIDTH).size() - 1)) - 2;

			entityManager.addEntity(
					new Cannibal(handler, x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT, Public.random(0.51, 0.8), 1, false), true);
		}
	}

	private void addShrubbery(int amount) {
		for (int i = 0; i < amount; i++) {
			int x = (int) Public.random(0, width);
			int y = heights.get(x).get((int) Public.random(0, heights.get(x).size() - 1)) - 1;

			entityManager.addEntity(
					new Shrubbery(handler, x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT, (int) Public.random(3, 4)), true);
		}
	}

}
