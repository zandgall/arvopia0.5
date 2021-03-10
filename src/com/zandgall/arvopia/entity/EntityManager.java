package com.zandgall.arvopia.entity;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.entity.*;
import com.zandgall.arvopia.entity.creatures.*;
import com.zandgall.arvopia.entity.moveableStatics.*;
import com.zandgall.arvopia.entity.statics.*;


public class EntityManager {

	private Handler handler;
	private Player player;

	private ArrayList<Entity> entities;
	private Comparator<Entity> sort;

	public EntityManager(Handler handler, Player player) {
		this.handler = handler;
		this.player = player;
		entities = new ArrayList<Entity>();
		addEntity(player, true);
		sort = new Comparator<Entity>() {
			public int compare(Entity a, Entity b) {
				if (a.layer < b.layer)
					return 1;
				return -1;
			}

		};
	}
	
	public String saveString() {
		
		String content;
		
		content = ""+(entities.size()-2)+System.lineSeparator();
		
		for(Entity e : entities) {
			
			if(e.getClass() == Bee.class) {
				Bee b = (Bee) e;
				
				content += "Bee " + b.x + " " + b.y + " " + "false " + b.prevTime;
			}
			
			if(e.getClass() == Butterfly.class) {
				Butterfly b = (Butterfly) e;
				
				content += "Butterfly " + b.x + " " + b.y + " " + "false " + b.prevTime;
			}
			
			if(e.getClass() == Cannibal.class) {
				Cannibal b = (Cannibal) e;
				
				content += "Cannibal " + (int) b.x + " " + (int) b.y + " " + b.walkSpeed + " " + b.lives + " " + b.alpha;
			}
			
			if(e.getClass() == Fox.class) {
				Fox b = (Fox) e;
				
				content += "Fox " + b.x + " " + b.y;
			}
			
			if(e.getClass() == Cloud.class) {
				Cloud b = (Cloud) e;
				
				content += "Cloud " + b.x + " " + b.y + " " + b.type + " " + b.speed;
			}
			
			if(e.getClass() == Flower.class) {
				Flower b = (Flower) e;
				
				content += "Flower " + b.x + " " + b.y + " " + b.type + " " + b.layer;
			}
			
			if(e.getClass() == Shrubbery.class) {
				Shrubbery b = (Shrubbery) e;
				
				content += "Shrubbery " + b.x + " " + b.y + " " + b.type;
			}
			
			if(e.getClass() == Stone.class) {
				Stone b = (Stone) e;
				
				content += "Stone " + b.x + " " + b.y + " " + b.type;
			}
			
			if(e.getClass() == Tree.class) {
				Tree b = (Tree) e;
				
				content += "Tree " + b.x + " " + b.y + " " + b.age;
			}
			
			content += System.lineSeparator();
			
		}
			
		
		return content;
	}

	public void tick() {
		for(int i = 0; i < entities.size(); i++) {
			if(i>=entities.size())
				return;
			entities.get(i).tick();
			if(i>=entities.size())
				return;
			if(entities.get(i).creature) {
				Creature c = (Creature) entities.get(i);
				c.regen();
			}
		}
		entities.sort(sort);
	}

	public void render(Graphics g, boolean tf) {
		for (Entity e : entities) {
			e.render(g);
			if (tf && e.creature) {
				e.showBox(g);
			}
		}
	}

	public void addEntity(Entity e, boolean tf) {
		entities.add(e);
		if(tf)
			handler.log("Entity " + e + " added at (" + e.x + ", " + e.y + ")");
	}

	// GETTERS AND SETTER

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		addEntity(player, true);
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

}
