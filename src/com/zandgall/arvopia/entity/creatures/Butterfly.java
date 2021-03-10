package com.zandgall.arvopia.entity.creatures;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.tiles.Tile;

public class Butterfly extends Creature{
	public long  prevTime, prevMoveTime = System.currentTimeMillis();
	private long moveTimer, timer;

	public Butterfly(Handler handler, double x, double y, boolean direction, long timer) {
		super(handler, x, y, 18, 18, direction, 0.5, Creature.DEFAULT_ACCELERATION,
				5, true, false, 0, 0, "Butterfly");
		
		health = 2;
		MAX_HEALTH = 2;
		
		this.timer = timer;
		prevTime = System.currentTimeMillis();
		
		layer = Math.random()-5;
		moveTimer = 0;
	}

	public void tick() {
		
		if(System.currentTimeMillis()-prevTime >= timer) {
			game.getWorld().kill(this);
		} else {
		
			butterflyForward.tick();
			butterflyBackward.tick();
			
			move();
			
			if(System.currentTimeMillis() - prevMoveTime >= moveTimer) {
				prevMoveTime = System.currentTimeMillis();
				
				xVol += (float) (Math.random()*speed - speed/2);
				yVol += (float) (Math.random()*speed*4 - speed*2);
				
				if(Math.abs(xVol)>MAX_SPEED) {
					xVol -= xVol/2;
				}
			
				if(Math.abs(yVol)>MAX_SPEED) {
					yVol -= yVol/2;
				}
			}
			
			yMove = yVol;
			
			if(xVol > 0) {
				setxMove((float) (speed + xVol + game.getWind()));
			} else  if(xVol<0){
				setxMove((float) (-speed + xVol + game.getWind()));
			}
		}
}

	@Override
	public void render(Graphics g) {
		g.drawImage(getFrame(), (int) (x - game.getGameCamera().getxOffset()),
				(int) (y - game.getGameCamera().getyOffset()), null);
		
		if(health < MAX_HEALTH) {
			showHealthBar(g);
		}
	}

	private BufferedImage getFrame() {
		if (Math.round(getxMove() - speed + 0.49) > 0) {
			return butterflyForward.getFrame();
		}  else if (Math.round(Math.abs(getxMove()) - speed + 0.49) > 0) {
			return butterflyBackward.getFrame();
		} else {
			return butterflyForward.getFrame();
		}
	}

	@Override
	public void checkCol() {

		down = false;
		left = false;
		right = false;
		top = false;
		bottom = false;
		lefts = false;
		rights = false;
		tops = false;
		bottoms = false;

		int ty = (int) ((y + yMove + bounds.y + bounds.height) / Tile.TILEHEIGHT);
		if (collisionWithTile((int) ((x + bounds.x+2) / Tile.TILEWIDTH), ty)
				|| collisionWithTile((int) ((x + bounds.x + bounds.width-2) / Tile.TILEWIDTH), ty)
				|| checkCollision(0f, yMove)) {
			bottom = true;
		} else if (collisionWithDown((int) ((x + bounds.x+2) / Tile.TILEWIDTH), ty)
				|| collisionWithDown((int) ((x + bounds.x + bounds.width-2) / Tile.TILEWIDTH), ty)) {
			if (y + bounds.y + bounds.height < ty * Tile.TILEHEIGHT + 4) {
				
				down = true;
			}
			
			if (y + bounds.y + bounds.height <= ty * Tile.TILEHEIGHT+1 && yMove>=0) {
				bottoms = true;
				bottom = true;
			}
		}
		ty = (int) ((y + yMove + bounds.y + bounds.height + 2) / Tile.TILEHEIGHT);
		if (collisionWithTile((int) ((x + bounds.x+2) / Tile.TILEWIDTH), ty)
				|| collisionWithTile((int) ((x + bounds.x + bounds.width-2) / Tile.TILEWIDTH), ty)
				|| checkCollision(0f, yMove + 1) || ((collisionWithDown((int) ((x + bounds.x+2) / Tile.TILEWIDTH), ty)
						|| collisionWithDown((int) ((x + bounds.x + bounds.width-2) / Tile.TILEWIDTH), ty)) && y + bounds.y + bounds.height <= ty * Tile.TILEHEIGHT+1)) {
			bottoms = true;
		}

		ty = (int) ((y + yMove + bounds.y) / Tile.TILEHEIGHT);
		if (collisionWithTile((int) ((x + bounds.x+2) / Tile.TILEWIDTH), ty)
				|| collisionWithTile((int) ((x + bounds.x + bounds.width-2) / Tile.TILEWIDTH), ty)
				|| checkCollision(0f, yMove)) {
			top = true;
		}
		ty = (int) ((y + yMove + bounds.y - 2) / Tile.TILEHEIGHT);
		if (collisionWithTile((int) ((x + bounds.x+2) / Tile.TILEWIDTH), ty)
				|| collisionWithTile((int) ((x + bounds.x + bounds.width-2) / Tile.TILEWIDTH), ty)
				|| checkCollision(0f, yMove - 1)) {
			tops = true;
		}

		int tx = (int) ((x + getxMove() + bounds.x + bounds.width) / Tile.TILEWIDTH);
		if (collisionWithTile(tx, (int) (y + bounds.y+2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height-2) / Tile.TILEHEIGHT)
				|| checkCollision(getxMove() + 1, 0f)) {
			right = true;
		}
		tx = (int) ((x + getxMove() + bounds.x + bounds.width + 2) / Tile.TILEWIDTH);
		if (collisionWithTile(tx, (int) (y + bounds.y+2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height-2) / Tile.TILEHEIGHT)
				|| checkCollision(getxMove() + 1, 0f)) {
			rights = true;
		}

		tx = (int) ((x + getxMove() + bounds.x) / Tile.TILEWIDTH);
		if (collisionWithTile(tx, (int) (y + bounds.y+2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height-2) / Tile.TILEHEIGHT)
				|| checkCollision(getxMove(), 0f)) {
			left = true;
		}
		tx = (int) ((x + getxMove() + bounds.x - 2) / Tile.TILEWIDTH);
		if (collisionWithTile(tx, (int) (y + bounds.y+2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height-2) / Tile.TILEHEIGHT)
				|| checkCollision(getxMove() - 1, 0f)) {
			lefts = true;
		}
	}
}
