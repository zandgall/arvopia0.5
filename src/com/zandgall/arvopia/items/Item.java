package com.zandgall.arvopia.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.entity.Entity;
import com.zandgall.arvopia.gfx.Animation;
import com.zandgall.arvopia.gfx.ImageLoader;
import com.zandgall.arvopia.tiles.Tile;
import com.zandgall.arvopia.worlds.World;

public class Item {
	// Handler
	
		public static Item[] items = new Item[256];
		
		public static Item whitePetal = new Item("White Petal", 0);
		public static Item pinkPetal = new Item("Pink Petal", 1);
		public static Item bluePetal = new Item("Blue Petal", 2);
		
		public static Item metal = new Item("Metal", 3);
		
		public static Item wood = new Item("Wood", 4);
		
		// Class
		
		public static final int ITEMWIDTH = 18, ITEMHEIGHT = 18;
		
		protected Handler game;
		protected BufferedImage texture;
		protected String name;
		protected final int id;
		
		protected Rectangle bounds;
		
		// Smart movement
		public boolean left, right, top, bottom, down;
		public boolean lefts, rights, tops, bottoms;
		
		protected double x, y;
		protected int count;
		protected boolean pickedUp = false;
		
		// Particles
		double xMove, yMove;
		public boolean downed;
		private static ArrayList<Animation> anim = new ArrayList<Animation>();
		private static ArrayList<BufferedImage> dead = new ArrayList<BufferedImage>();
		private long timer;
		private static BufferedImage[] white, pink, blue, metalArray, woodArray;
		
		private boolean direction;
		
		public Item(String name, int id){
			this.name = name;
			this.id = id;
			count = 1;
			
			int width, height;
			if(id == 0 || id == 1 || id == 2) {
				width = 6;
				height = 6;
			} else {
				width = ITEMWIDTH;
				height = ITEMHEIGHT;
			}
			if(id == 3 || id == 4) {
				bounds = new Rectangle((int) x, (int) y, width, height);
				
				
				yMove =  -(Math.random()*5);
				
				timer = 0;
				
				if(Math.random()<0.5) {
					direction = false;
				} else {
					direction = true;
				}
			} else {
				bounds = new Rectangle((int)x, (int) y, width, height);
			}
			
			items[id] = this;
		}
		
		public static void init() {
			BufferedImage p1 = ImageLoader.loadImage("/textures/Statics/Flowers/PetalParticlesWhite.png");
			white = new BufferedImage[] { p1.getSubimage(0, 0, 6, 6),
					p1.getSubimage(0, 6, 6, 6), p1.getSubimage(0, 12, 6, 6), p1.getSubimage(0, 18, 6, 6),
					p1.getSubimage(0, 12, 6, 6), p1.getSubimage(0, 6, 6, 6), p1.getSubimage(0, 0, 6, 6) };
			anim.add(new Animation(250, white, "Alive", "White Petals"));
			dead.add(ImageLoader.loadImage("/textures/Statics/Flowers/WhitePetal.png"));
			p1 = ImageLoader.loadImage("/textures/Statics/Flowers/PetalParticlesPink.png");
			pink = new BufferedImage[] { p1.getSubimage(0, 0, 6, 6),
					p1.getSubimage(0, 6, 6, 6), p1.getSubimage(0, 12, 6, 6), p1.getSubimage(0, 18, 6, 6),
					p1.getSubimage(0, 12, 6, 6), p1.getSubimage(0, 6, 6, 6), p1.getSubimage(0, 0, 6, 6) };
			anim.add(new Animation(250,pink, "Alive", "Pink Petals"));
			dead.add(ImageLoader.loadImage("/textures/Statics/Flowers/PinkPetal.png"));
			p1 = ImageLoader.loadImage("/textures/Statics/Flowers/PetalParticlesBlue.png");
			blue = new BufferedImage[] { p1.getSubimage(0, 0, 6, 6),
					p1.getSubimage(0, 6, 6, 6), p1.getSubimage(0, 12, 6, 6), p1.getSubimage(0, 18, 6, 6),
					p1.getSubimage(0, 12, 6, 6), p1.getSubimage(0, 6, 6, 6), p1.getSubimage(0, 0, 6, 6) };
			anim.add(new Animation(250, blue, "Alive", "Blue Petals"));
			dead.add(ImageLoader.loadImage("/textures/Statics/Flowers/BluePetal.png"));
			p1 = ImageLoader.loadImage("/textures/Inventory/Metal.png");
			metalArray = new BufferedImage[] {p1};
			anim.add(new Animation(250, metalArray, "Dormant", "Metal"));
			dead.add(p1);
			
			p1 = ImageLoader.loadImage("/textures/Inventory/Wood.png");
			metalArray = new BufferedImage[] {p1};
			anim.add(new Animation(1000, metalArray, "Dormant", "Wood"));
			dead.add(p1);
		}
		
		public void tick(){
			int width, height;
			if(id == 0 || id == 1 || id == 2) {
				width = 6;
				height = 6;
			} else {
				width = ITEMWIDTH;
				height = ITEMHEIGHT;
			}
			if(game.getWorld().getEntityManager().getPlayer().getCollision(0f, 0f).intersects(new Rectangle((int) x, (int) y, width, height)) || (new Rectangle((int)(game.getMouse().getMouseX()+game.getGameCamera().getxOffset()), (int) (game.getMouse().getMouseY()+game.getGameCamera().getyOffset()), 10, 20).intersects(new Rectangle((int) x, (int) y, width, height)))){
				pickedUp = true;
				if(id == 3) 
					game.getWorld().getEntityManager().getPlayer().metal++;
				if(id == 4)
					game.getWorld().getEntityManager().getPlayer().wood++;
				if(id == 0 || id == 1 || id == 2)
					game.getWorld().getEntityManager().getPlayer().petals++;
			}
			
			checkCol();
			
			if(id == 0 || id == 1 || id == 2) {
				BufferedImage[] aliveArray;
				if(id==0) {
					aliveArray = white;
				} else if(id==1) {
					aliveArray = pink;
				} else {
					aliveArray = blue;
				}
				
				
				if(downed) {
					timer++;
					if(timer>1000) {
						pickedUp = true;
					}
				} else if(!bottom) {
					anim.get(id).tick();
					if(anim.get(id).getFrame()==aliveArray[0]) {
						yMove=Math.random()/3;
						xMove=Math.random()/10;
					} else if(anim.get(id).getFrame()==aliveArray[1]) {
						yMove=Math.random()/5;
						xMove=Math.random()/5;
					} else if(anim.get(id).getFrame()==aliveArray[2]) {
						yMove=Math.random()/10;
						xMove=Math.random()/5;
					} else if(anim.get(id).getFrame()==aliveArray[3]) {
						yMove=-Math.random()/10;
						xMove=Math.random()/10;
					} else if(anim.get(id).getFrame()==aliveArray[4]) {
						yMove=Math.random()/5;
						xMove=-Math.random()/5;
					} else if(anim.get(id).getFrame()==aliveArray[5]) {
						yMove=Math.random()/5;
						xMove=-Math.random()/5;
					} else if(anim.get(id).getFrame()==aliveArray[6]) {
						yMove=-Math.random()/10;
						xMove=-Math.random()/10;
					} 
					if(xMove+game.getWind()/10>0) {
						if (!right) {
							x+=game.getWind()/10+xMove;
						}
					} else if(xMove+game.getWind()/10<0) {
						if (!left) {
							x+=game.getWind()/10+xMove;
						}
					}
					
					if(right) {
						x--;
					}
					if(left) {
						x++;
					}
					
					yMove+=0.001;
					
					if(!bottom) {
						this.y+=yMove;
					}
				} else {
					downed = true;
				}
			} else if(id == 3 || id == 4) {
				if(checkFloor()) {
					downed = true;
				}
				
				if(downed) {
					if(timer>=10000) {
						pickedUp = true;
					} else {
						timer++;
					}
				} else {
					if(direction) {
						int tx = (int) ((x) + bounds.width) / Tile.TILEWIDTH;
						if (!collisionWithTile(tx, (int) (y) / Tile.TILEHEIGHT)
								&& !collisionWithTile(tx, (int) (y + bounds.height) / Tile.TILEHEIGHT)) {
							x++;
						}
					} else {
						int tx = (int) (x) / Tile.TILEWIDTH;
						if (!collisionWithTile(tx, (int) (y) / Tile.TILEHEIGHT)
								&& !collisionWithTile(tx, (int) (y + bounds.height) / Tile.TILEHEIGHT)) {
							x--;
						}
					}
					this.y+=yMove;
					yMove+=0.1;
				}
			}
		}
		
		public void checkCol() {

			down = false;
			left = false;
			right = false;
			top = false;
			bottom = false;
			lefts = false;
			rights = false;
			tops = false;
			bottoms = false;

			int ty = (int) ((y + yMove + bounds.y + bounds.height) / Tile.TILEHEIGHT);
			if (collisionWithTile((int) ((x + bounds.x+2) / Tile.TILEWIDTH), ty)
					|| collisionWithTile((int) ((x + bounds.x + bounds.width-2) / Tile.TILEWIDTH), ty)) {
				bottom = true;
			} else if (collisionWithDown((int) ((x + bounds.x+2) / Tile.TILEWIDTH), ty)
					|| collisionWithDown((int) ((x + bounds.x + bounds.width-2) / Tile.TILEWIDTH), ty)) {
				if (y + bounds.y + bounds.height < ty * Tile.TILEHEIGHT + 4) {
					
					down = (yMove>=0);
				}
				
				if (y + bounds.y + bounds.height <= ty * Tile.TILEHEIGHT+1 && yMove>=0) {
					bottoms = true;
					bottom = true;
				}
			}
			ty = (int) ((y + yMove + bounds.y + bounds.height + 2) / Tile.TILEHEIGHT);
			if (collisionWithTile((int) ((x + bounds.x+2) / Tile.TILEWIDTH), ty)
					|| collisionWithTile((int) ((x + bounds.x + bounds.width-2) / Tile.TILEWIDTH), ty)
					|| ((collisionWithDown((int) ((x + bounds.x+2) / Tile.TILEWIDTH), ty)
							|| collisionWithDown((int) ((x + bounds.x + bounds.width-2) / Tile.TILEWIDTH), ty)) && y + bounds.y + bounds.height <= ty * Tile.TILEHEIGHT+1)) {
				bottoms = true;
			}

			ty = (int) ((y + yMove + bounds.y) / Tile.TILEHEIGHT);
			if (collisionWithTile((int) ((x + bounds.x+2) / Tile.TILEWIDTH), ty)
					|| collisionWithTile((int) ((x + bounds.x + bounds.width-2) / Tile.TILEWIDTH), ty)) {
				top = true;
			}
			ty = (int) ((y + yMove + bounds.y - 2) / Tile.TILEHEIGHT);
			if (collisionWithTile((int) ((x + bounds.x+2) / Tile.TILEWIDTH), ty)
					|| collisionWithTile((int) ((x + bounds.x + bounds.width-2) / Tile.TILEWIDTH), ty)) {
				tops = true;
			}

			int tx = (int) ((x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH);
			if (collisionWithTile(tx, (int) (y + bounds.y+2) / Tile.TILEHEIGHT)
					|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height-2) / Tile.TILEHEIGHT)) {
				right = true;
			}
			tx = (int) ((x + xMove + bounds.x + bounds.width + 2) / Tile.TILEWIDTH);
			if (collisionWithTile(tx, (int) (y + bounds.y+2) / Tile.TILEHEIGHT)
					|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height-2) / Tile.TILEHEIGHT)) {
				rights = true;
			}

			tx = (int) ((x + xMove + bounds.x) / Tile.TILEWIDTH);
			if (collisionWithTile(tx, (int) (y + bounds.y+2) / Tile.TILEHEIGHT)
					|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height-2) / Tile.TILEHEIGHT)) {
				left = true;
			}
			tx = (int) ((x + xMove + bounds.x - 2) / Tile.TILEWIDTH);
			if (collisionWithTile(tx, (int) (y + bounds.y+2) / Tile.TILEHEIGHT)
					|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height-2) / Tile.TILEHEIGHT)) {
				lefts = true;
			}
		}
		
		protected boolean collisionWithDown(int x, int y) {
			return (World.getTile(x, y).isSolid() || World.getTile(x, y).isTop());
		}
		
		public boolean checkFloor() {
			int ty = (int) ((y + yMove + bounds.height)) / Tile.TILEHEIGHT;
			return (collisionWithDown((int) (x + bounds.width) / Tile.TILEWIDTH, ty)
					&& collisionWithDown((int) (x) / Tile.TILEWIDTH, ty));
		}
		
		protected boolean collisionWithTile(int x, int y) {
			return World.getTile(x, y).isSolid();
		}
		
		public boolean checkCollision(float xOffset, float yOffset) {
			for (Entity e : game.getWorld().getEntityManager().getEntities()) {
				if (e.isSolid && e.getCollision(0f, 0f).intersects(getCollision(xOffset, yOffset))) {
					return true;
				}
			}
			return false;
		}
		
		public Rectangle getCollision(float xOffset, float yOffset) {
			return new Rectangle((int) (x + bounds.x + xOffset), (int) (y + bounds.y + yOffset), bounds.width,
					bounds.height);
		}
		
		public void render(Graphics g, boolean box){
			if(game == null)
				return;
			render(g, (int) (x - game.getGameCamera().getxOffset()), (int) (y - game.getGameCamera().getyOffset()));
			
			if(box) {
				showBox(g, (int) (x - game.getGameCamera().getxOffset()), (int) (y - game.getGameCamera().getyOffset()));
			}
		}
		
		public void showBox(Graphics g, int x, int y) { 
			g.setColor(Color.orange);
			
			g.drawRect(x, y, bounds.width, bounds.height);
			g.drawRect(game.getMouse().getMouseX(), game.getMouse().getMouseY(), 10, 20);
		}
		
		public void render(Graphics g, int x, int y){
			if(id == 0 || id == 1 || id == 2) {
				g.drawImage(getFrame(), x, y, 6, 6, null);
			} else {
				g.drawImage(anim.get(id).getFrame(), x, y, ITEMWIDTH, ITEMHEIGHT, null);
			}
		}
		
		public Item createNew(int x, int y){
			Item i = new Item(name, id);
			i.setPosition(x, y);
			return i;
		}
		
		public void setPosition(int x, int y){
			this.x = x;
			this.y = y;
			bounds.x = x;
			bounds.y = y;
		}
		
		// Getters and Setters
		
		public Handler getHandler() {
			return game;
		}

		public void setHandler(Handler handler) {
			this.game = handler;
		}

		public BufferedImage getTexture() {
			return texture;
		}

		public void setTexture(BufferedImage texture) {
			this.texture = texture;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getId() {
			return id;
		}

		public boolean isPickedUp() {
			return pickedUp;
		}
		
		public BufferedImage getFrame() {
			if(downed) {
				return dead.get(id);
			} else {
				return anim.get(id).getFrame();
			}
		}
}
