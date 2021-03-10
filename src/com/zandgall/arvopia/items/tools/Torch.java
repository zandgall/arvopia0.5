package com.zandgall.arvopia.items.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.enviornment.Light;
import com.zandgall.arvopia.gfx.Animation;
import com.zandgall.arvopia.gfx.PublicAssets;

public class Torch extends Tool{
	
	
	private Animation torchAn, stab;
	private Light torch;
	
	private boolean one = false;
	
	public Light getLight() {
		return torch;
	}
	
	public boolean hasLight() {
		return true;
	}
	
	public Torch(Handler game) {
		super(game, false);
		
		one = false;
		
		torchAn = new Animation(150, new BufferedImage[] {PublicAssets.torch.getSubimage(0, 0, 18, 45), PublicAssets.torch.getSubimage(18, 0, 18, 45)}, "", "Torch");
		stab = new Animation(150, new BufferedImage[] {PublicAssets.torchStab.getSubimage(0, 0, 36, 21), PublicAssets.torchStab.getSubimage(36, 0, 36, 21)}, "Stab", "Torch");
		torch = new Light(game, 100, 100, 15, 1, Color.orange);
	}

	@Override
	public void tick() {
		if(!one) {
			game.getWorld().getEnviornment().getLightManager().addLight(torch);
			one = true;
			torch.turnOn();
		}
		
		torchAn.tick();
		stab.tick();
	}

	public void custom1(int x, int y) {
		torch.setX(x-9);
		torch.setY(y+7);
	}
	
	public BufferedImage texture() {
		return torchAn.getFrame();
	}

	public BufferedImage getFrame() {
		return stab.getFrame();
	}

	public void render(Graphics g, int x, int y) {
		
	}

	public int getYOffset() {
		return 22;
	}
	
	public int getXOffset() {
		return torchAn.getFrame().getWidth()/2;
	}

	public boolean smashOrStab() {
		return false;
	}

	@Override
	public void setFrame(int frameInt) {
		
	}

	@Override
	public tools Type() {
		return tools.NONE;
	}

	@Override
	public void custom2(int i) {
		
	}

}
