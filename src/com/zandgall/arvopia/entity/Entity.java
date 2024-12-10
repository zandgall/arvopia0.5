package com.zandgall.arvopia.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;

import com.zandgall.arvopia.Game;
import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.entity.creatures.Cannibal;
import com.zandgall.arvopia.entity.creatures.Creature;
import com.zandgall.arvopia.utils.Public;

public abstract class Entity {
	
	public static boolean variety = true;
	
	protected Handler game;
	protected double x, y;
	protected int width, height;
	protected Rectangle bounds;

	public boolean isSolid;

	public double layer;

	public boolean creature, particle, staticEntity;
	public boolean dead;

	public Entity(Handler handler, double x, double y, int width, int height, boolean solid, boolean creature,
			boolean particle, boolean staticEntity) {
		this.game = handler;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		dead = false;

		this.creature = creature;
		this.particle = particle;
		this.staticEntity = staticEntity;

		this.isSolid = solid;

		bounds = new Rectangle(0, 0, width, height);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void tick() {
	};

	public abstract void render(Graphics g);

	public boolean checkCollision(float xOffset, float yOffset) {
		for (Entity e : game.getWorld().getEntityManager().getEntities()) {
			if (e.equals(this))
				continue;
			if (e.isSolid && e.getCollision(0f, 0f).intersects(getCollision(xOffset, yOffset))) {
				return true;
			}
		}
		return false;
	}

	public boolean checkTouching(float xOffset, float yOffset) {
		for (Entity e : game.getWorld().getEntityManager().getEntities()) {
			if (e.equals(this))
				continue;
			if (e.getCollision(0f, 0f).intersects(getCollision(xOffset, yOffset))) {
				return true;
			}
		}
		return false;
	}

	public Entity getClosest(double x, double y) {
		ArrayList<Entity> e = game.getWorld().getEntityManager().getEntities();
		if (e.isEmpty())
			return null;
		Entity b = e.get(0);

		for (Entity i : e) {
			if (distance(Public.zoomX(i.centerX()), Public.zoomY(i.centerY()), x, y) < distance(Public.zoomX(b.centerX()), Public.zoomY(b.centerY()), x, y)) {
				b = i;
			}
		}

		return b;
	}
	
	public ArrayList<Creature> getClosest(double x, double y, int amount) {
		ArrayList<Entity> e = game.getWorld().getEntityManager().getEntities();
		Creature b;
		if (e.size() > 0 && e.get(0).creature) {
			b = (Creature) e.get(0);
		}
		ArrayList<Creature> list = new ArrayList<Creature>();

		Comparator<Creature> c = new Comparator<Creature>() {

			@Override
			public int compare(Creature a, Creature b) {
				if (distance(x, y, a.centerX() * Game.scale, a.centerY() * Game.scale) < distance(x, y,
						b.centerX() * Game.scale, b.centerY() * Game.scale)) {
					return -1;
				} else {
					return 1;
				}
			}

		};

		for (Entity i : e) {
			if (i.creature && i != this) {
				if (list.size() < amount) {
					list.add((Creature) i);
				} else {
					list.sort(c);
					b = list.get(list.size() - 1);
					if (i.creature && distance(i.centerX() * Game.scale, i.centerY() * Game.scale, x,
							y) < distance(b.centerX() * Game.scale, b.centerY() * Game.scale, x, y)) {

						list.add(list.indexOf(b), (Creature) i);
					}
				}
			}
		}

		if (list.size() > amount)
			list.subList(amount, list.size()).clear();

		return list;
	}

	public double centerX() {
		return x + bounds.x + bounds.width / 2;
	}

	public double centerY() {
		return y + bounds.y + bounds.height / 2;
	}

	public ArrayList<Creature> getInRadius(double x, double y, int radius) {
		ArrayList<Entity> e = game.getWorld().getEntityManager().getEntities();

		ArrayList<Creature> list = new ArrayList<Creature>();

		for (Entity i : e) {
			if (i.creature && i != this) {
				if (Public.dist(i.x, i.y, x, y) < radius) {
					list.add((Creature) i);
				}
			}
		}

		return list;

	}

	public ArrayList<Cannibal> getClosestCannibal(double x, double y, int amount) {
		ArrayList<Entity> e = game.getWorld().getEntityManager().getEntities();
		Cannibal b;
		if (e.size() > 0 && e.get(0).getClass() == Cannibal.class) {
			b = (Cannibal) e.get(0);
		} else if (e.size() < 0) {
			return null;
		}
		ArrayList<Cannibal> list = new ArrayList<Cannibal>();

		Comparator<Cannibal> c = new Comparator<Cannibal>() {

			@Override
			public int compare(Cannibal a, Cannibal b) {
				if (distance(x, y, a.x, a.y) < distance(x, y, b.x, b.y)) {
					return -1;
				} else {
					return 1;
				}
			}

		};

		for (Entity i : e) {
			if (i.getClass() == Cannibal.class) {
				if (list.size() < amount) {
					list.add((Cannibal) i);
				} else {
					list.sort(c);
					b = list.get(list.size() - 1);
					if (i.creature && distance(i.x + i.getbounds().x + i.getbounds().getWidth() / 2,
							i.y + i.getbounds().y + i.getbounds().getHeight() / 2, x,
							y) < distance(b.x + b.getbounds().x + b.getbounds().getWidth() / 2,
									b.y + b.getbounds().y + b.getbounds().getHeight() / 2, x, y)) {

						list.add((Cannibal) i);
					}
				}
			}
		}

		if (list.size() < amount) {
			for (int i = 0; i < amount - list.size(); i++) {
				list.add(null);
			}
		}

		return list;
	}

	public Cannibal getAlphaCannibal(double x, double y, int amount) {
		Entity output = null;

		for (Entity e : game.getWorld().getEntityManager().getEntities()) {
			if (e.getClass() == Cannibal.class) {
				Cannibal c = (Cannibal) e;
				if (c.alpha)
					output = c;
			}
		}

		return (Cannibal) output;
	}

	public Entity getEntity(float xOffset, float yOffset) {
		for (Entity e : game.getWorld().getEntityManager().getEntities()) {
			if (e.equals(this))
				continue;
			if (e.isSolid && e.getCollision(0f, 0f).intersects(getCollision(xOffset, yOffset))) {
				return e;
			}
		}
		return this;
	}

	public Rectangle getCollision(float xOffset, float yOffset) {
		return new Rectangle((int) (x + bounds.x + xOffset), (int) (y + bounds.y + yOffset), bounds.width,
				bounds.height);
	}

	public Rectangle getbounds() {
		return bounds;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void reset() {

	}

	public void showBox(Graphics g) {
		if (!this.creature) {
			if (isSolid) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.green);
			}

			g.drawRect((int) (x + bounds.x - game.getGameCamera().getxOffset()),
					(int) (y + bounds.y - game.getGameCamera().getyOffset()), bounds.width, bounds.height);
		}

		if (this.creature) {
			Creature c = (Creature) this;

			int left = (int) (x + bounds.x - game.getGameCamera().getxOffset());
			int right = (int) (x + bounds.x + bounds.width - game.getGameCamera().getxOffset());
			int top = (int) (y + bounds.y - game.getGameCamera().getyOffset());
			int bottom = (int) (y + bounds.y + bounds.height - game.getGameCamera().getyOffset());

			// g.drawRect((int) x + bounds.x, (int) y+bounds.y, bounds.width,
			// bounds.height);

			if (c.lefts) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.green);
			}
			g.fillRect(left, top, 2, bottom - top);
			if (c.rights) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.green);
			}
			g.fillRect(right, top, 2, bottom - top);
			if (c.tops) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.green);
			}
			g.fillRect(left, top, right - left, 2);
			if (c.bottoms) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.green);
			}
			g.fillRect(left, bottom, right - left + 2, 2);
		}
	}

	protected double distance(double x1, double y1, double x2, double y2) {
		double d = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
		return d;
	}

	public void kill() {
		game.getWorld().getEntityManager().getEntities().remove(this);
	}

}
