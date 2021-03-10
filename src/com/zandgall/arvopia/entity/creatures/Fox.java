package com.zandgall.arvopia.entity.creatures;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.gfx.PublicAssets;
import com.zandgall.arvopia.gfx.transform.Tran;
import com.zandgall.arvopia.tiles.Tile;

public class Fox extends Creature {

	private int widthFlip;

	private long sitTimer, moveTimer;

	private boolean u, d, l, r;

	private boolean rChange, lChange;

	private int downTimer;

	public Fox(Handler handler, double x, double y) {
		super(handler, x, y, 54, 36, true, Creature.DEFAULT_SPEED, Creature.DEFAULT_ACCELERATION, 3, false, false, 2,
				0.02, "Fox");
		sitTimer = 0;
		
		MAX_HEALTH = 5;

		health = 5;

		creature = true;

		rChange = false;
		lChange = false;

		downTimer = 0;

		layer = Math.random();
		bounds.x = 18;
		bounds.y = 19;
		bounds.width = 26;
		bounds.height = 16;

		widthFlip = 1;
		moveTimer = 0;
	}

	public void tick() {
		
		foxWalk.tick();
		foxSit.tick();
		foxStill.tick();

		if (widthFlip == 1) {
			int tx = (int) ((x) + getxMove() + bounds.x) / Tile.TILEWIDTH;
			if ((collisionWithTile(tx, (int) ((y) + bounds.y) / Tile.TILEHEIGHT)
					|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)
					|| checkCollision(getxMove(), 0f))) {
				bounds.x = 18;
			}
		} else {
			int tx = (int) ((x) + getxMove() + bounds.x + bounds.width) / Tile.TILEWIDTH;
			if ((collisionWithTile(tx, (int) ((y) + bounds.y) / Tile.TILEHEIGHT)
					|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)
					|| checkCollision(getxMove(), 0f))) {
				bounds.x = 5;
			}
		}

		if (moveTimer >= 100) {

			if (d) {
				downTimer++;
			}
			if (downTimer >= 10) {
				d = false;
				downTimer = 0;
			}

			if (Math.random() < 0.5) {
				if (rChange) {
					l = true;
					r = false;
				} else {
					r = true;
					l = false;
				}
				int tx = (int) ((x) + getxMove() + bounds.x + bounds.width) / Tile.TILEWIDTH;
				if ((collisionWithTile(tx, (int) ((y) + bounds.y) / Tile.TILEHEIGHT)
						|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)
						|| checkCollision(getxMove(), 0f))) {
					u = true;
					rChange = true;
				} else {
					u = false;
					rChange = false;
				}
			} else if (Math.random() > 0.5) {
				if (lChange) {
					r = true;
					l = false;
				} else {
					l = true;
					r = false;
				}
				int tx = (int) ((x) + getxMove() + bounds.x) / Tile.TILEWIDTH;
				if (collisionWithTile(tx, (int) ((y) + bounds.y) / Tile.TILEHEIGHT)
						|| collisionWithTile(tx, (int) ((y) + bounds.y + bounds.height) / Tile.TILEHEIGHT)
						|| checkCollision(getxMove(), 0f)) {
					u = true;
					lChange = true;
				} else {
					u = false;
					lChange = false;
				}
			} else {
				u = false;
			}

			if (Math.random() < 0.05) {
				d = true;
			}

			moveTimer = 0;
		} else {
			moveTimer++;
		}

		aiMove();

		move();
	}

	private void aiMove() {
		setxMove(0);
		yMove = 0;

		if (xVol > 0)
			// xMove += Math.round(speed + xVol);
			setxMove((float) (getxMove() + (speed + xVol)));
		if (xVol < 0)
			// xMove += Math.round(-speed + xVol);
			setxMove((float) (getxMove() + (-speed + xVol)));

		if (l) {
			if (!d) {
				if (xVol < maxSpeed)
					if (xVol > 0)
						xVol -= FRICTION * 2;
				xVol -= acceleration;
			} else {
				xVol = 0;
			}
			widthFlip = 1;
		} else if (r) {
			widthFlip = -1;
			if (!d) {
				if (xVol < maxSpeed)
					if (xVol < 0)
						xVol += FRICTION * 2;
				xVol += acceleration;
			} else {
				xVol = 0;
			}
		} else {
			float txv = (float) xVol;
			if (xVol < 0)
				xVol += acceleration + FRICTION;
			else if (xVol > 0)
				xVol -= acceleration + FRICTION;
			if ((txv > 0 && xVol < 0) || (txv < 0 && xVol > 0)) {
				xVol = 0;
				game.log("X Volocity reset for player");
			}

		}

		if (u) {
			if (bottoms && yVol > -DEFAULT_JUMP_FORCE + (GRAVITY * 2)) {
				if (!d && yVol > -DEFAULT_JUMP_FORCE / 2) {
					yVol -= DEFAULT_JUMP_FORCE;
				}
			} else if (yVol < 0) {
				yVol -= DEFAULT_JUMP_CARRY;
			}
			
			if (l) {
				if (!left) {
					lChange = false;
				} else {
					lChange = true;
				}
			}

			if (r) {
				if (!right) {
					rChange = false;
				} else {
					rChange = true;
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Tran.flip(getFrame(), widthFlip, 1), (int) (x - game.getGameCamera().getxOffset()),
				(int) (y - game.getGameCamera().getyOffset()), null);
		
		if(health < MAX_HEALTH) {
			showHealthBar(g);
		}
	}

	private BufferedImage getFrame() {
		if (Math.round(yMove - speed + 0.49) > 0) {
			return PublicAssets.fox[5];
		} else if (!bottoms) {
			return PublicAssets.fox[4];
		} else if (d) {
			return foxSit.getFrame();
		} else if (Math.round(Math.abs(getxMove()) - speed + 0.49) > 0) {
			sitTimer = 0;
			return foxWalk.getFrame();
		} else if (sitTimer < 1000) {
			sitTimer++;
			return foxStill.getFrame();
		} else {
			return foxSit.getFrame();
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
