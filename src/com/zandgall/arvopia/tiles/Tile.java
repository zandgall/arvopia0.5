package com.zandgall.arvopia.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Tile {
	
	// TILE STATES

	public static Tile[] tiles = new Tile[256];
	public static Tile n0 = new EmptyTile(0); // null
	
	public static Tile g1 = new GrassTile(1, 0, 0); // Grass
	public static Tile g2 = new GrassTile(2, 1, 0);
	public static Tile g3 = new GrassTile(3, 2, 0);
	public static Tile g4 = new GrassTile(4, 0, 1);
	public static Tile g5 = new GrassTile(5, 1, 1);
	public static Tile g6 = new GrassTile(6, 2, 1);
	public static Tile g7 = new GrassTile(7, 0, 2);
	public static Tile g8 = new GrassTile(8, 1, 2);
	public static Tile g9 = new GrassTile(9, 2, 2);
	public static Tile g10 = new GrassTile(10, 0, 3);
	public static Tile g11 = new GrassTile(11, 1, 3);
	public static Tile g12 = new GrassTile(12, 2, 3);
	public static Tile g13 = new GrassTile(13, 3, 0);
	public static Tile g14 = new GrassTile(14, 3, 1);
	public static Tile g15 = new GrassTile(15, 3, 2);
	public static Tile g16 = new GrassTile(16, 4, 0);
	public static Tile g17 = new GrassTile(17, 3, 3);
	public static Tile g18 = new GrassTile(18, 4, 3);
	public static Tile g19 = new GrassTile(19, 4, 1);
	public static Tile g20 = new GrassTile(20, 4, 2);
	public static Tile g21 = new GrassTile(21, 5, 0);
	public static Tile g22 = new GrassTile(22, 5, 1);
	public static Tile g23 = new GrassTile(23, 5, 2);
	public static Tile g24 = new GrassTile(24, 5, 3);
	public static Tile g25 = new GrassTile(25, 6, 0);
	public static Tile g26 = new GrassTile(26, 6, 1);
	public static Tile g27 = new GrassTile(27, 6, 2);
	public static Tile g28 = new GrassTile(28, 6, 3);
	
	public static Tile b0 = new Bridge(2,29);
	public static Tile b1 = new Bridge(1,30);
	public static Tile b2 = new Bridge(0,31);
	public static Tile b3 = new Bridge(3,32);
	public static Tile b4 = new Bridge(4,33);
	public static Tile b5 = new Bridge(5,34);
	public static Tile b6 = new Bridge(6,35);
	public static Tile b7 = new Bridge(7,36);

	// TILE SUPERCLASS

	public static int TILEWIDTH = 18, TILEHEIGHT = 18;
	public static final int DEFAULT_WIDTH = 18, DEFAULT_HEIGHT = 18; 
	protected int x, y;

	protected final int id;
	
	private BufferedImage texture;

	public Tile(BufferedImage texture, int id) {
		this.id = id;

		this.texture = texture;
		
		tiles[id] = this;
	}

	public abstract void tick();
	
	public abstract void init();
	
	public abstract void reset();

	public void render(Graphics g, int x, int y) {
		g.drawImage(texture, x, y, TILEWIDTH, TILEHEIGHT, null);
	}

	public boolean isSolid() {
		return false;
	}
	
	public boolean varietable() {
		return false;
	}
	
	public boolean isTop() {
		return false;
	}

	public int getId() {
		return id;
	}
	
	
	
}
