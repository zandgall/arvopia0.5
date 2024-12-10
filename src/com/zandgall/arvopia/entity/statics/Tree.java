package com.zandgall.arvopia.entity.statics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.gfx.PublicAssets;
import com.zandgall.arvopia.gfx.transform.Tran;
import com.zandgall.arvopia.items.tools.Tool;
import com.zandgall.arvopia.utils.Public;

public class Tree extends StaticEntity {

	BufferedImage[][] tree = PublicAssets.tree;

	long[] timings = { 2000, 4000, 10000, 20000, 30000, 50000, 100000, 200000, 350000, 700000, 1000000, 2000000,
			4000000, 5000000, 7000000, 10000000 }; //How long each age lasts

	int[] xoff = { 16, 15, 14, 14, 12, 11, 10, 9, 5, 5, 2, 2, 2, -4, -11, -17},
			yoff = { 140, 138, 134, 128, 125, 118, 112, 108, 101, 93, 84, 79, 69, 50, 22, 9 },
			width = { 3, 4, 9, 9, 12, 13, 17, 18, 28, 29, 34, 34, 34, 43, 60, 66 },
			height = { 4, 6, 10, 16, 19, 26, 32, 36, 43, 51, 60, 68, 77, 94, 122, 135 };

	public int age = 0;

	int growthTime;
	long lastTime = 0;
	
	
	int widthflip = 1;
	
	public Tree(Handler handler, double x, double y, int age) {
		super(handler, x, y, 36, 144, false, age*2, Tool.tools.AXE);

		layer = Public.random(-10, 10);

		this.age = age;

		bounds.x = xoff[age];
		bounds.y = yoff[age];
		bounds.width = width[age];
		bounds.height = height[age];
		
		if(Math.random()<0.5) {
			widthflip = -1;
		}
		
		growthTime = (int) Public.random(-1000, 5000);
		
	}

	public void tick() {
		if (game.getWorld().getEnviornment().getTotalTime() - lastTime >= timings[age] + growthTime) {

			lastTime = game.getWorld().getEnviornment().getTotalTime();
			age++;
			growthTime = (int) Public.random(-5000, 10000);
			
			bounds.x = xoff[age];
			bounds.y = yoff[age];
			bounds.width = width[age];
			bounds.height = height[age];
			
			if (age == 16) {
				game.getWorld().kill(this);
				return;
			}

			if (age < 6 && (game.getWorld().getEnviornment().getState() != 0
					&& game.getWorld().getEnviornment().getState() < 16)) {
				game.getWorld().kill(this);
			}

		}

	}

	@Override
	public void render(Graphics g) {

		double newX = 0;

		if (age > 12)
			newX = 18;
		
		if(variety)
			g.drawImage(Tran.flip(getFrame(), widthflip, 1), (int) (x - game.xOffset() - newX), (int) (y - game.yOffset()), null);
		else g.drawImage(getFrame(), (int) (x - game.xOffset() - newX), (int) (y - game.yOffset()), null);

	}

	private BufferedImage getFrame() {

		return tree[(int) Public.range(0, 20, game.getWorld().getEnviornment().getState())][age];
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
