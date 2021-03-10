package com.zandgall.arvopia.entity.statics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.gfx.PublicAssets;
import com.zandgall.arvopia.gfx.transform.Tran;
import com.zandgall.arvopia.utils.Public;

public class Shrubbery extends StaticEntity {

	public BufferedImage image;
	
	public int type;
	int widthflip = 1;
	
	public Shrubbery(Handler handler, double x, double y, int type) {
		super(handler, x, y, 18, 18, false, 0, null);
		
		if(Math.random()<0.5) {
			widthflip = -1;
		}
		
		layer = Public.random(-0.5, -1);
		
		image = PublicAssets.shrubbery[type];
		
		this.type = type;
		
	}

	@Override
	public void render(Graphics g) {
		
		if(variety && type != 0 && type != 2)
			g.drawImage(Tran.flip(image, widthflip, 1), (int) (x-game.xOffset()), (int) (y-game.yOffset()), null);
		else g.drawImage(image, (int) (x-game.xOffset()), (int) (y-game.yOffset()), null);
		
	}

}
