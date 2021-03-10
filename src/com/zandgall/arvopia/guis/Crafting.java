package com.zandgall.arvopia.guis;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.entity.creatures.Player;
import com.zandgall.arvopia.gfx.ImageLoader;
import com.zandgall.arvopia.state.GameState;

public class Crafting extends Gui {
	
	public static final int SPACEWIDTH = 20, SPACEHEIGHT = 20;

	private CraftOutput sword, torch, axe;
	
	private boolean[] loaded = {false, false, false, false, false, false};
	private int[] itemOutputs;
	private String[] inputKeyCode = new String[] {"metal", "petal", "honey", "foxFur", "butterflyWing", "wood"};
	private ArrayList<InventoryItem> items;
	private ArrayList<String> craftingInput;
	private ArrayList<CraftOutput> outputs;

	public Crafting(Handler game) {
		super(game);
				
		itemOutputs = new int[] {0, 0, 0};
		
		items = new ArrayList<InventoryItem>();
		
		craftingInput = new ArrayList<String>();
		
		sword = new CraftOutput(game, ImageLoader.loadImage("/textures/Inventory/Tools/Sword/SwordStab.png"), 120, 30, new ArrayList<String>(Arrays.asList("wood", "metal")));
		torch = new CraftOutput(game, ImageLoader.loadImage("/textures/Inventory/Tools/Torch/TorchStab.png").getSubimage(0, 0, 36, 21), 120, 30,  new ArrayList<String>(Arrays.asList("wood")));
		axe = new CraftOutput(game, ImageLoader.loadImage("/textures/Inventory/Tools/Axe/AxeSmash.png").getSubimage(0, 36, 36, 13), 120, 30, new ArrayList<String>(Arrays.asList("wood", "metal")));
		
		
		outputs = new ArrayList<CraftOutput>(Arrays.asList(sword, torch, axe));
	}
	
	
	@Override
	public void tick() {
		
		items = game.getWorld().getEntityManager().getPlayer().inventory.items; 
		
		for(InventoryItem i: items) {
			
			if(i.used && i.movedX==-10 && i.movedY==0) {
				// Do nothing
			} else if(i.movedX >= -10 && i.movedX <= 20 && i.movedY >= 39 && i.movedY <= 79 && InventoryItem.TAKEN!=i && i.amount>0 && !loaded[0]) {
				
				i.setPos(10, 50);
				i.movedX = - 10;
				i.movedY = 0;
				i.used = true;
				
				i.customCraftingMessage=255;
				
				craftingInput.add(inputKeyCode[items.indexOf(i)]);
				
				loaded[0]=true;
				
				i.customCraftingInt=0;
				
			} else if(i.movedX >= 20 && i.movedX <= 40 && i.movedY >= 39 && i.movedY <= 79 && InventoryItem.TAKEN!=i && i.amount>0 && !loaded[1]) {
				
				i.setPos(30, 50);
				i.movedX = - 10;
				i.movedY = 0;
				i.used = true;
				
				i.customCraftingMessage=255;
				
				craftingInput.add(inputKeyCode[items.indexOf(i)]);
				
				loaded[1]=true;
				i.customCraftingInt=1;
				
			} else if(i.movedX >= 40 && i.movedX <= 60 && i.movedY >= 39 && i.movedY <= 79 && InventoryItem.TAKEN!=i && i.amount>0 && !loaded[2]) {
				
				i.setPos(50, 50);
				i.movedX = - 10;
				i.movedY = 0;
				i.used = true;
				
				i.customCraftingMessage=255;
				
				craftingInput.add(inputKeyCode[items.indexOf(i)]);
				
				loaded[2]=true;
				i.customCraftingInt=2;
				
			} else if(i.movedX >= 60 && i.movedX <= 80 && i.movedY >= 39 && i.movedY <= 79 && InventoryItem.TAKEN!=i && i.amount>0 && !loaded[3]) {
				
				i.setPos(70, 50);
				i.movedX = - 10;
				i.movedY = 0;
				i.used = true;
				
				i.customCraftingMessage=255;
				
				craftingInput.add(inputKeyCode[items.indexOf(i)]);
				
				loaded[3]=true;
				i.customCraftingInt=3;
				
			} else if(i.movedX >= 80 && i.movedX <= 100 && i.movedY >= 39 && i.movedY <= 79 && InventoryItem.TAKEN!=i && i.amount>0 && !loaded[4]) {
				
				i.setPos(90, 50);
				i.movedX = - 10;
				i.movedY = 0;
				i.used = true;
				
				i.customCraftingMessage=255;
				
				craftingInput.add(inputKeyCode[items.indexOf(i)]);
				
				loaded[4]=true;
				i.customCraftingInt=4;
				
			} else if(i.movedX >= 100 && i.movedX <= 120 && i.movedY >= 39 && i.movedY <= 79 && InventoryItem.TAKEN!=i && i.amount>0 && !loaded[5]) {
				
				i.setPos(110, 50);
				i.movedX = - 10;
				i.movedY = 0;
				i.used = true;
				
				i.customCraftingMessage=255;
				
				craftingInput.add(inputKeyCode[items.indexOf(i)]);
				
				loaded[5]=true;
				i.customCraftingInt=5;
				
			} else if(InventoryItem.TAKEN != i){
				i.setPos(items.indexOf(i)*20+20, 20);
				if(i.used) {
					craftingInput.remove(inputKeyCode[items.indexOf(i)]);
				}
				
				if(i.customCraftingMessage==0)
//				i.customCraftingMessage=255;
				loaded[i.customCraftingInt]=false;
				if(craftingInput.isEmpty())
					for(int v = 0; v<=5; v++)
						loaded[v]=false;
			}
		}
		
		for(CraftOutput c : outputs) {
			if(c.craftable(craftingInput)) {
				c.tick();
				if(c.clicked && game.getMouse().isClicked()) {
					itemOutputs[outputs.indexOf(c)]++;
					
					Player p = game.getWorld().getEntityManager().getPlayer();
					
					p.swords = itemOutputs[0];
					p.torches = itemOutputs[1];
					p.axes = itemOutputs[2];
					
					
					
					if(craftingInput.contains("wood"))
						p.wood--;
					if(craftingInput.contains("metal"))
						p.metal--;
				}
			}
		}
		
		
	}
	
	public PlayerGui getGui() {
		GameState e = (GameState) game.getGame().gameState;
		return (PlayerGui) e.u;
	}

	@Override
	public void render(Graphics g) {
		
		g.setColor(new Color(0, 0, 200, 100));
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		
		for(int x = 10; x <= 110; x+=20) {
			
			g.setColor(Color.lightGray);
			g.fillRect(x, 50, 20, 20);
			g.setColor(Color.darkGray);
			g.fillRect(x+2, 52, 18, 18);
			g.setColor(Color.gray);
			g.fillRect(x+2, 52, 16, 16);
			
			g.setColor(Color.black);
			g.drawRect(x, 50, 20, 20);
			
		}
		
		
		for(InventoryItem i: items) {
			i.render(g);
			
			if(i.movedX >= -10 && i.movedX <= 139 && i.movedY >= 39 && i.movedY <= 79 && InventoryItem.TAKEN!=i) {
				
				g.setColor(new Color(0,0,0, i.customCraftingMessage));
				g.drawString("Needs 1 or more of this item to craft", i.movedX, i.movedY); 
				if(i.customCraftingMessage>0)
					i.customCraftingMessage--;	
			}
			
		}
		
		
		
		int i = 0;
		for(CraftOutput c : outputs) {
			if(c.craftable(craftingInput)) {
				c.x=c.ox+i*40;
				c.render(g);
				i++;
			}
		} 

		g.setColor(Color.black);
		g.drawString("Put stuff here ^", 10, 80);
	}

	@Override
	public void init() {
	}

}
