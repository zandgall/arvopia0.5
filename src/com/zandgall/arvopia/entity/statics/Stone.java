package com.zandgall.arvopia.entity.statics;

import java.awt.Graphics;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.gfx.PublicAssets;
import com.zandgall.arvopia.gfx.transform.Tran;
import com.zandgall.arvopia.utils.Public;

public class Stone extends StaticEntity{
	
	public int type;
	private int orType;
	int widthflip = 1;
	
	public Stone(Handler handler, double x, double y,int type) {
		super(handler, x, y, 18, 18, false, type*5, null);
		this.type = type;
		orType = type;
		
		if(Math.random()<0.5) {
			widthflip = -1;
		}
		
		if(type == 0) {
			layer = Public.random(-0.5, 0);
			bounds.x = 2;
			bounds.y = 8;
			bounds.width = 15;
			bounds.height = 10;
		} else if(type == 1) {
			layer = Public.random(-1, 0);
			bounds.x = 2;
			bounds.y = 4;
			bounds.width = 15;
			bounds.height = 14;
		} else {
			layer = Public.random(-4, 0);
			bounds.x = 2;
			bounds.width = 15;
			bounds.y = 3;
			bounds.height = 15;
		}
	}

	public int getType() {
		return type;
	}
	
	public int getOrType() {
		return orType;
	}

	public void setType(int type) {
		this.type = type;
		
		if(type == 0) {
			layer = Public.random(0, 1);
			bounds.x = 2;
			bounds.y = 8;
			bounds.width = 15;
			bounds.height = 10;
		} else if(type == 1) {
			layer = Public.random(-1, 1);
			bounds.x = 3;
			bounds.y = 4;
			bounds.width = 12;
			bounds.height = 16;
		} else {
			layer = Public.random(-1, 0);
			bounds.x = 3;
			bounds.width = 15;
			bounds.y = 3;
			bounds.height = 15;
		}
	}

	public void tick() {
		
	}
	
	@Override
	public void render(Graphics g) {
		if(variety)
			g.drawImage(Tran.flip(PublicAssets.stone[type], widthflip, 1), (int) (x - game.getGameCamera().getxOffset()), (int) (y - game.getGameCamera().getyOffset()), width, height, null);
		else g.drawImage(PublicAssets.stone[type], (int) (x - game.getGameCamera().getxOffset()), (int) (y - game.getGameCamera().getyOffset()), width, height, null);
		
	}
}
