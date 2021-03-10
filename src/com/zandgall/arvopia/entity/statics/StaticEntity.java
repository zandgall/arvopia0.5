package com.zandgall.arvopia.entity.statics;

import java.awt.Color;
import java.awt.Graphics;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.entity.Entity;
import com.zandgall.arvopia.items.tools.Tool;
import com.zandgall.arvopia.utils.Public;

public abstract class StaticEntity extends Entity {

	public double health, maxHealth;
	
	public Tool.tools weakness;
	
	public StaticEntity(Handler handler, double x, double y, int width, int height, boolean solid, int health, Tool.tools weakness) {
		super(handler, x, y, width, height, solid, false, false, true);
		
		this.health = health*10;
		maxHealth = health*10;
		
		this.weakness = weakness;
	}
	
	public void reset() {
		
	}
	
	public void showBox(Graphics g) {
		if(maxHealth > 0) {
			g.setColor(Color.red);
			g.fillRect((int) Public.xO(x+bounds.x), (int) Public.yO(y+bounds.y-20), bounds.width, 10);
			
			g.setColor(Color.green);
			g.fillRect((int) Public.xO(x+bounds.x), (int) Public.yO(y+bounds.y-20), (int) (bounds.width*(health/maxHealth)), 10);
			
			g.setColor(Color.black);
			g.drawRect((int) Public.xO(x+bounds.x), (int) Public.yO(y+bounds.y-20), bounds.width, 10);
			
		}
		
		if (isSolid) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.green);
		}

		g.drawRect((int) (x + bounds.x - game.getGameCamera().getxOffset()),
				(int) (y + bounds.y - game.getGameCamera().getyOffset()), bounds.width, bounds.height);
	}
	
}
