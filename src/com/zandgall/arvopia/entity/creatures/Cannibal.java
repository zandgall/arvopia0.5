package com.zandgall.arvopia.entity.creatures;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.gfx.Animation;
import com.zandgall.arvopia.gfx.PublicAssets;
import com.zandgall.arvopia.gfx.transform.Tran;
import com.zandgall.arvopia.items.tools.*;
import com.zandgall.arvopia.tiles.Tile;
import com.zandgall.arvopia.utils.Public;

public class Cannibal extends Creature {

	// Items
	private Torch torch;
	private Sword sword;
	
	private Tool currentTool;

	// Fall Damage
	private double lastHeight;
	// Attacking
	private boolean attackReady;

	private Rectangle attack;
	private boolean attacking, primed, chasing;
	private int attackDelay, damage, delayRange;

	// Timing
	private long timer;
	
	private long attackTimer;

	// Create animations
	private Animation jump;
	private Animation still;
	private Animation crouch;
	private Animation walk;
	 private Animation punch;
	 private Animation hold;
	 private Animation stab;
	 private BufferedImage airKick;
	private boolean jumping = false;

	int renderCount = 0;
	int widthFlip = 1;

	public int lives;

	private boolean rChange, lChange;
	private boolean r, l, d;
	public boolean alpha;
	
	
	public double walkSpeed;

	public Cannibal(Handler handler, double x, double y, double speed, int lives, boolean alpha) {
		super(handler, x, y, DEFAULT_CREATURE_WIDTH, 47, true, speed, DEFAULT_ACCELERATION, (int) MAX_SPEED, false,
				false, DEFAULT_JUMP_FORCE, DEFAULT_JUMP_CARRY, "Cannibal");
		
		walkSpeed = speed;
		
		attack = new Rectangle((int) x - width / 2, (int) y, 72, 54);

		health = 10;
		MAX_HEALTH = 10;

		layer = Public.random(-3, -1);

		attackReady = true;
		attacking = false;
		attackDelay = 60;

		timer = 0;
		attackTimer = 0;
		
		if(alpha) {
			currentTool = torch;
			delayRange = 15;
			attackDelay = 15;
			damage = 1;
		} else if(Math.random() < 0.2) {
			currentTool = sword;
			delayRange = 15;
			attackDelay = 15;
			damage = 2;
		} else {
			currentTool = null;
			delayRange = 15;
			attackDelay = 15;
			damage = 1;
		} 

		this.lives = lives;

		torch = new Torch(handler);
		sword = new Sword(handler);
		
		bounds.x = 12;
		bounds.y = 8;
		bounds.width = 10;
		bounds.height = 45;

		// Initiate animations
		jump = PublicAssets.cjump;
		still = PublicAssets.cstill;
		walk = PublicAssets.cwalk;
		crouch = PublicAssets.ccrouch;
		
		punch = PublicAssets.cpunch;
		airKick = PublicAssets.cairKick;
		
		hold = PublicAssets.chold;
		
		stab = PublicAssets.cstab;
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
		if (collisionWithTile((int) ((x + bounds.x + 2) / Tile.TILEWIDTH), ty)
				|| collisionWithTile((int) ((x + bounds.x + bounds.width - 2) / Tile.TILEWIDTH), ty)
				|| checkCollision(0f, yMove)) {
			bottom = true;
		} else if (collisionWithDown((int) ((x + bounds.x + 2) / Tile.TILEWIDTH), ty)
				|| collisionWithDown((int) ((x + bounds.x + bounds.width - 2) / Tile.TILEWIDTH), ty)) {
			if (y + bounds.y + bounds.height < ty * Tile.TILEHEIGHT + 4) {

				down = true;
			}

			if (y + bounds.y + bounds.height <= ty * Tile.TILEHEIGHT + 1 && yMove >= 0) {
				bottoms = true;
				bottom = true;
			}
		}
		ty = (int) ((y + yMove + bounds.y + bounds.height + 2) / Tile.TILEHEIGHT);
		if (collisionWithTile((int) ((x + bounds.x + 2) / Tile.TILEWIDTH), ty)
				|| collisionWithTile((int) ((x + bounds.x + bounds.width - 2) / Tile.TILEWIDTH), ty)
				|| checkCollision(0f, yMove + 1)
				|| ((collisionWithDown((int) ((x + bounds.x + 2) / Tile.TILEWIDTH), ty)
						|| collisionWithDown((int) ((x + bounds.x + bounds.width + 2) / Tile.TILEWIDTH), ty))
						&& y + bounds.y + bounds.height <= ty * Tile.TILEHEIGHT + 1 && !jumping)) {
			bottoms = true;
		}

		ty = (int) ((y + yMove + bounds.y) / Tile.TILEHEIGHT);
		if (collisionWithTile((int) ((x + bounds.x + 2) / Tile.TILEWIDTH), ty)
				|| collisionWithTile((int) ((x + bounds.x + bounds.width - 2) / Tile.TILEWIDTH), ty)
				|| checkCollision(0f, yMove)) {
			top = true;
		}
		ty = (int) ((y + yMove + bounds.y - 2) / Tile.TILEHEIGHT);
		if (collisionWithTile((int) ((x + bounds.x + 2) / Tile.TILEWIDTH), ty)
				|| collisionWithTile((int) ((x + bounds.x + bounds.width - 2) / Tile.TILEWIDTH), ty)
				|| checkCollision(0f, yMove - 1)) {
			tops = true;
		}

		int tx = (int) ((x + getxMove() + bounds.x + bounds.width) / Tile.TILEWIDTH);
		if (collisionWithTile(tx, (int) (y + bounds.y + 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 4) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height * 0.75) / Tile.TILEHEIGHT)
				|| checkCollision(getxMove() + 1, 0f)) {
			right = true;
		}
		tx = (int) ((x + getxMove() + bounds.x + bounds.width + 2) / Tile.TILEWIDTH);
		if (collisionWithTile(tx, (int) (y + bounds.y + 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 4) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height * 0.75) / Tile.TILEHEIGHT)
				|| checkCollision(getxMove() + 1, 0f)) {
			rights = true;
		}

		tx = (int) ((x + getxMove() + bounds.x) / Tile.TILEWIDTH);
		if (collisionWithTile(tx, (int) (y + bounds.y + 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 4) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height * 0.75) / Tile.TILEHEIGHT)
				|| checkCollision(getxMove(), 0f)) {
			left = true;
		}
		tx = (int) ((x + getxMove() + bounds.x - 2) / Tile.TILEWIDTH);
		if (collisionWithTile(tx, (int) (y + bounds.y + 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 4) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height * 0.75) / Tile.TILEHEIGHT)
				|| checkCollision(getxMove() - 1, 0f)) {
			lefts = true;
		}
	}

	@Override
	public void render(Graphics g) {
		
		if (currentTool != null) {
			if (getFrame() != stab.getFrame()) {
				g.drawImage(Tran.flip(currentTool.texture(), -widthFlip, 1),
						(int) (x - game.getGameCamera().getxOffset()) + (getToolxoffset()),
						(int) (y - game.getGameCamera().getyOffset()) + getToolyoffset(), null);
				currentTool.custom1((int) Public.xO(x + getToolxoffset() + currentTool.texture().getWidth()),
						(int) Public.yO(y));
			} else {
				currentTool.custom1((int) Public.xO(x + getToolxoffset() + currentTool.getFrame().getWidth()),
						(int) Public.yO(y));
			}
		}

		g.drawImage(Tran.flip(getFrame(), -widthFlip, 1), (int) Public.xO(x), (int) Public.yO(y), null);

		if (currentTool != null)
			if (getFrame() == stab.getFrame())
				g.drawImage(Tran.flip(currentTool.getFrame(), -widthFlip, 1), (int) Public.xO(x) + (getToolxoffset()),
						(int) Public.yO(y) + getToolyoffset(), null);
		
		if (health < MAX_HEALTH) {
			showHealthBar(g);
		}
		
	}
	
	public int getToolxoffset() {
		if (getFrame() == airKick || getFrame() == jump.getFrame())
			if (-widthFlip == 1)
				return 33 - currentTool.texture().getWidth() / 2;
			else
				return 3 - currentTool.texture().getWidth() / 2;
		if (getFrame() == crouch.getFrame())
			if (-widthFlip == 1)
				return 28 - currentTool.texture().getWidth() / 2;
			else
				return 8 - currentTool.texture().getWidth() / 2;
		if (getFrame() == walk.getFrame())
			if (walk.getFrame() == walk.getArray()[0])
				if (-widthFlip == 1)
					return 33 - currentTool.texture().getWidth() / 2;
				else
					return 3 - currentTool.texture().getWidth() / 2;
			else if (walk.getFrame() == walk.getArray()[1])
				if (-widthFlip == 1)
					return 26 - currentTool.texture().getWidth() / 2;
				else
					return 10;
			else if (-widthFlip == 1)
				return 25 - currentTool.texture().getWidth() / 2;
			else
				return 14 - currentTool.texture().getWidth() / 2;
		if (getFrame() == hold.getFrame())
			if (-widthFlip == 1)
				return 33 - currentTool.texture().getWidth() / 2;
			else
				return 3 - currentTool.texture().getWidth() / 2;
		if (getFrame() == stab.getFrame())
			if (stab.getFrame() == stab.getArray()[0])
				if (-widthFlip == 1)
					return 16;
				else
					return 20 - currentTool.getFrame().getWidth();
			else if (stab.getFrame() == stab.getArray()[1])
				if (-widthFlip == 1)
					return 13;
				else
					return 23 - currentTool.getFrame().getWidth();
			else if (-widthFlip == 1)
				return 33;
			else
				return 3 - currentTool.getFrame().getWidth();
		return 0;
	}

	public int getToolyoffset() {
		if (getFrame() == airKick || getFrame() == jump.getFrame())
			return 40 - currentTool.texture().getHeight();

		if (getFrame() == crouch.getFrame())
			return 50 - currentTool.texture().getHeight();

		if (getFrame() == walk.getFrame())
			if (walk.getFrame() == walk.getArray()[0])
				return 40 - currentTool.texture().getHeight();

			else if (walk.getFrame() == walk.getArray()[1])
				return 42 - currentTool.texture().getHeight();

			else
				return 25 - currentTool.texture().getHeight();

		if (getFrame() == hold.getFrame())
			if (hold.getFrame() == hold.getArray()[0])
				return 40 - currentTool.texture().getHeight();
			else
				return 41 - currentTool.texture().getHeight();

		if (getFrame() == stab.getFrame())
			if (stab.getFrame() == stab.getArray()[0])
				return 40 - currentTool.getYOffset();

			else if (stab.getFrame() == stab.getArray()[1])
				return 42 - currentTool.getYOffset();
			else
				return 33 - currentTool.getYOffset();

		return 33 - currentTool.texture().getHeight() / 2;
	}

	private BufferedImage getFrame() {

		checkCol();

		if (!bottoms || jumping) {
			if (attacking && punch.getFrame() == punch.getArray()[2] && attackReady) {
				return airKick;
			}
			return jump.getFrame();
		} else if (down) {
			return crouch.getFrame();
		} else if (Math.round(Math.abs(getxMove()) - speed + 0.49) > 0) {
			return walk.getFrame();
		} else if (attacking || primed) {
			if (currentTool != null)
				return stab.getFrame();
			return punch.getFrame();
		} else {
			if (currentTool != null)
				return hold.getFrame();
			return still.getFrame();
		}

	}
	
	

	@Override
	public void tick() {
		if(currentTool != null && currentTool.hasLight()) {
			currentTool.getLight().turnOff();
			currentTool.getLight().setX(-200);
			currentTool.getLight().setY(-200);
		}
		
		
		if(alpha) {name = "Alpha Cannibal";}
		if (!game.getGame().paused) {
			
			if(currentTool != null)
				currentTool.tick();
			
			still.tick();
			walk.tick();
			crouch.tick();
			punch.tick();
			hold.tick();
			stab.tick();

			if (bottoms) {
				if (y - lastHeight > 180) {
					damage((int) Math.floor((y - lastHeight) / 180));
				}

				lastHeight = y;
			} else {
				if (y < lastHeight)
					lastHeight = y;
			}

			attack.x = (int) x - 18;
			attack.y = (int) y;

			if (timer >= 1000000)
				timer = 0;

			if (attackTimer == attackDelay) {
				attackReady = true;
			} else if (attackTimer < attackDelay || attackTimer > attackDelay + delayRange) {
				attackReady = false;
				if (attackTimer > attackDelay + delayRange)
					attackTimer = 0;
			}
			
			if ((punch.getFrame() == punch.getArray()[2]) && getFrame() == punch.getFrame() && bottoms) {
				attacking = false;
			}

			if (stab.getFrame() == stab.getArray()[0] && !game.getMouse().fullLeft && attacking && getFrame() == stab.getFrame())
				attacking = false;
			
			if (bottoms && jumping) {
				jumping = false;
			}
			
			Cannibal closest = this;
			if(!alpha) {
				closest = getAlphaCannibal(x,y, 2);
				
				if(closest == null) {
					alpha = true;
					closest = this;
					
					game.log("Im alpha now! MWAHAHAH!!");
					currentTool = torch;
					delayRange = 15;
					attackDelay = 15;
					damage = 1;
				}
			}
			
			if (Public.dist(x, y, game.getWorld().getEntityManager().getPlayer().getX(),
					game.getWorld().getEntityManager().getPlayer().getY()) < 180 || (chasing && Public.dist(x, y, game.getWorld().getEntityManager().getPlayer().getX(),
							game.getWorld().getEntityManager().getPlayer().getY()) < 400)) {
				if (Public.dist(x, y, game.getWorld().getEntityManager().getPlayer().getX(),
						game.getWorld().getEntityManager().getPlayer().getY()) < 20 && attackReady) {
					attacking = true;
					attackTimer = 0;
					attackReady = false;
					
					chasing = true;
					
					speed = walkSpeed + 1;
					
					game.getWorld().getEntityManager().getPlayer().damage(damage);
				}
				if (x - 10 > game.getWorld().getEntityManager().getPlayer().getX()) {
					l = true;
					r = false;
					if (left)
						jumping = true;
				} else if (x + 10 < game.getWorld().getEntityManager().getPlayer().getX()) {
					l = false;
					r = true;
					if (right)
						jumping = true;
				} else {
					l = false;
					r = false;
				}
			} else if(closest != this && Public.dist(x, y, closest.x, closest.y)>20) {
				chasing = false;
				if(closest.x<x) {
					l = true;
					r = false;
					if (left)
						jumping = true;
				} else if(closest.x>x) {
					l = false;
					r = true;
					if (right)
						jumping = true;
				} else {
					l = false;
					r = false;
				}
			} else if (timer >= 100) {
				chasing = false;
				if (d) {
					timer++;
				}
				if (timer >= 10) {
					d = false;
					timer = 0;
				}

				if (Math.random() < 0.5) {
					if (rChange) {
						l = true;
						r = false;
					} else {
						r = true;
						l = false;
					}
					if (right) {
						jumping = true;
						rChange = true;
					} else {
						jumping = false;
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
					if (left) {
						jumping = true;
						lChange = true;
					} else {
						jumping = false;
						lChange = false;
					}
				} else {
					jumping = false;
				}

				if (Math.random() < 0.05) {
					d = true;
				}

				timer = 0;
			} else {
				timer++;
				chasing = false;
			}
			
			if(!chasing) {
				speed = walkSpeed;
			}

			aiMove();

			move();

			attackTimer++;
		}
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
			}

		}

		if (bottoms && jumping) {
			yVol = (float) -DEFAULT_JUMP_FORCE;
			jumping = false;
		} else if (yVol < 0) {
			yVol -= DEFAULT_JUMP_CARRY;
		}
	}
	
	public void kill() {
		if(currentTool != null && currentTool.hasLight()) {
			currentTool.getLight().turnOff();
			currentTool.getLight().setX(-200);
			currentTool.getLight().setY(-200);
		}
		
		currentTool = null;
		game.getWorld().getEntityManager().getEntities().remove(this);
	}

}
