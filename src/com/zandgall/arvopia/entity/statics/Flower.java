package com.zandgall.arvopia.entity.statics;

import java.awt.Graphics;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.gfx.PublicAssets;
import com.zandgall.arvopia.gfx.transform.Tran;
import com.zandgall.arvopia.items.tools.Tool;
import com.zandgall.arvopia.utils.Public;

public class Flower extends StaticEntity{
	
	public int type, randomSpeed, widthflip;
	
	public Flower(Handler handler, double x, double y,int type, double layer) {
		super(handler, x, y, 18, 18, false, 1, null);
		this.type = type;
		
		this.layer = layer;
		
		if(Math.random()<0.5) {
			widthflip = -1;
		}
		
		randomSpeed = (int) Public.random(-100, 100);
		
		bounds.x = -4;
		bounds.width = 7;
		bounds.y = 5;
		bounds.height = 13;
	}

	public void tick() {
		
		PublicAssets.flowerFinal[type].setSpeed((int) Math.abs((400+randomSpeed)/game.getWind()));
		
		PublicAssets.flowerFinal[type].tick();
	}
	
	@Override
	public void render(Graphics g) {
		if(variety)
			g.drawImage(Tran.flip(PublicAssets.flowerFinal[type].getFrame(), widthflip, 1), (int) (x - game.getGameCamera().getxOffset()-9), (int) (y - game.getGameCamera().getyOffset()), width, height, null);
		else g.drawImage(PublicAssets.flowerFinal[type].getFrame(), (int) (x - game.getGameCamera().getxOffset()-9), (int) (y - game.getGameCamera().getyOffset()), width, height, null);
		
	}

	public int getType() {
		return type;
	}
	
}
