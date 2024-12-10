package com.zandgall.arvopia.guis;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.entity.creatures.Player;
import com.zandgall.arvopia.gfx.ImageLoader;

public class PlayerGui extends Gui{
	
	Player p;
	
	private BufferedImage player, sword, torch, fists, axe, crafting, inventory;
	
	public boolean activeSword, activeTorch, activeAxe;
	
	public PlayerGui(Handler game) {
		super(game);
		p = game.getWorld().getEntityManager().getPlayer();
		
		player = ImageLoader.loadImage("/textures/Player/Player.png").getSubimage(9, 7, 18, 18);
		
		sword = ImageLoader.loadImage("/textures/Inventory/Tools/Sword/SwordStab.png");
		torch = ImageLoader.loadImage("/textures/Inventory/Tools/Torch/TorchStab.png").getSubimage(0, 0, 36, 21);
		fists = ImageLoader.loadImage("/textures/Player/PlayerPunch.png").getSubimage(49, 19, 18, 18);
		axe = ImageLoader.loadImage("/textures/Inventory/Tools/Axe/AxeSmash.png").getSubimage(0, 36, 36, 13);
		crafting = ImageLoader.loadImage("/textures/CraftingIcon.png");
		inventory = ImageLoader.loadImage("/textures/InventoryIcon.png");
	}

	@Override
	public void tick() {
		if(p != game.getWorld().getEntityManager().getPlayer()) {
			p = game.getWorld().getEntityManager().getPlayer();
		}
		
		activeSword=(p.swords>0);
		activeTorch=(p.torches>0);
		activeAxe=(p.axes>0);
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 18, 18);
		g.drawImage(player, 0, 0, null);
		g.setColor(Color.black);
		g.drawRect(0, 0, 18, 18);
		g.setFont(new Font("Dialog", Font.BOLD, 12));
		g.drawString("Lives: "+p.lives, 20, 18);
		
		g.drawImage(fists, 80, 0, null);
		g.drawString("1", 80, 10);
		if(activeSword) {
			g.drawImage(sword, 100, 2, null);
			g.drawString("2", 100, 10);
		} 
		if(activeTorch) {
			g.drawImage(torch, 141, 2, null);
			g.drawString("3", 141, 10);
		}
		if(activeAxe) {
			g.drawImage(axe, 182, 2, null);
			g.drawString("4", 182, 10);
		}
		
		g.drawImage(inventory, 100, game.getHeight()-20, null);
		g.drawImage(crafting, 190, game.getHeight()-20, null);
		
		
	}

	@Override
	public void init() {
		
	}
	
}
