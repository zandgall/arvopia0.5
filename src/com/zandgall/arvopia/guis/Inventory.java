package com.zandgall.arvopia.guis;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.entity.creatures.Player;
import com.zandgall.arvopia.gfx.ImageLoader;

public class Inventory extends Gui{
	
	public static final int SPACEWIDTH = 20, SPACEHEIGHT = 20;
	
	private InventoryItem metal, petal, honey, foxFur, butterflyWing, wood;
	
	public ArrayList<InventoryItem> items;
	
	private int[] itemNumbers;
	
	public Inventory(Handler game) {
		super(game);
		
		metal = new InventoryItem(game, ImageLoader.loadImage("/textures/Inventory/Metal.png"), 1, 1, 0);
		petal = new InventoryItem(game, ImageLoader.loadImage("/textures/Inventory/Petals.png"), 2, 1, 0);
		honey = new InventoryItem(game, ImageLoader.loadImage("/textures/Inventory/Honey.png"), 3, 1, 0);
		foxFur = new InventoryItem(game, ImageLoader.loadImage("/textures/Inventory/FoxFur.png"), 4, 1, 0);
		butterflyWing = new InventoryItem(game, ImageLoader.loadImage("/textures/Inventory/ButterflyWing.png"), 5, 1, 0);
		wood = new InventoryItem(game, ImageLoader.loadImage("/textures/Inventory/Wood.png"), 6, 1, 0);
		items = new ArrayList<InventoryItem>();
		items.add(metal);
		items.add(petal);
		items.add(honey);
		items.add(foxFur);
		items.add(butterflyWing);
		items.add(wood);
	}

	@Override
	public void tick() {
		Player p = game.getWorld().getEntityManager().getPlayer();
		
		itemNumbers = new int[] {p.metal, p.petals, p.honey, p.foxFur, p.butterflyWing, p.wood};
		
		for(InventoryItem i: items) {
			i.tick(itemNumbers[items.indexOf(i)]);
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(new Color(0, 0, 200, 100));
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		metal.render(g);
		petal.render(g);
		honey.render(g);
		foxFur.render(g);
		butterflyWing.render(g);
		wood.render(g);
	}

	@Override
	public void init() {
		
	}

}
