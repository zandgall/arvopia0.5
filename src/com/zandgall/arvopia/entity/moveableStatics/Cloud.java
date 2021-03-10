package com.zandgall.arvopia.entity.moveableStatics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.gfx.PublicAssets;
import com.zandgall.arvopia.gfx.transform.Tran;
import com.zandgall.arvopia.tiles.Tile;
import com.zandgall.arvopia.worlds.World;

public class Cloud extends MoveableStatic{

	public double speed;
	public int type;
	
	private BufferedImage cloud;
	
	int widthflip = 1, heightflip;
	
	public Cloud(Handler handler, double x, double y, int type, double speed) {
		super(handler, x, y, 54, 36, false);
		this.speed = speed;
		this.type = type;
		
		if(Math.random()<0.5) {
			widthflip = -1;
		}
		
		if(Math.random()<0.25 || Math.random() > 0.75) {
			heightflip = -1;
		}
		
		this.layer = (Math.random()*4-2);
		
		cloud = PublicAssets.cloud[type];
	}

	@Override
	public void tick() {
		x+=speed+game.getWorld().getEnviornment().getWind();
		if(x>World.getWidth()*Tile.TILEWIDTH+game.getWidth()/2) {
			x = -54;
		}
	}

	@Override
	public void init() {
		
	}
	
	public void render(Graphics g) {
		if(variety)
			g.drawImage(Tran.flip(cloud, widthflip, heightflip), (int) (x - game.getGameCamera().getxOffset()), (int) (y - game.getGameCamera().getyOffset()), null);
		else g.drawImage(cloud, (int) (x - game.getGameCamera().getxOffset()), (int) (y - game.getGameCamera().getyOffset()), null);
	}

}
