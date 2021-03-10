package com.zandgall.arvopia.input;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.zandgall.arvopia.ArvopiaLauncher;
import com.zandgall.arvopia.Game;
import com.zandgall.arvopia.Handler;

public class MouseManager implements MouseListener, MouseMotionListener, MouseWheelListener {
	
	private Handler game;
	
	private boolean left, right, dragged, in, clicked, held, prevHeld, sliderMalfunc;
	public boolean fullLeft;
	private double mouseX, mouseY, mouseScroll;
	
	private long timer = 0;
	
	public MouseManager(Handler game) {
		this.game = game;
	}
	
	public void changeCursor(String name) {
		if(name=="WAIT")
			game.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		else if(name=="HAND")
			game.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		else if(name=="TYPE")
			game.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		else game.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public boolean isLeft() {
		return left;
	} 
	
	public boolean isRight() {
		return right;
	}
	
	public int getMouseX() {
		try {
			return (int) (mouseX);
		} catch (NullPointerException e) {
			System.out.println("Error: "+e);
			return 0;
		}
	}
	
	public int rMouseX() {
		return (int) mouseX;
	}
	
	public int rMouseY() {
		return (int) mouseY;
	}
	
	public int getMouseY() {
		try {
			return (int) (mouseY);
		} catch (NullPointerException e) {
			System.out.println("Error: "+e);
			return 0;
		}
	}
	
	public boolean isDragged() {
		return dragged;
	}
	
	public boolean isHeld() {
		return held;
	}
	
	public void setDragged(boolean dragged) {
		this.dragged = dragged;
	}
	
	public boolean isClicked() {
		return clicked;
	}
	
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}
	
	public boolean isIn() {
		return in;
	}
	
	
	// Reset
	
	public void tick() {
		
		if(timer%100 == 0) {
			if(prevHeld) {
				if(left) {
					held = true;
				} else {
					prevHeld = false;
				}
			}
		}
		
		if(!clicked && !dragged) {
			left = false;
			right = false;
		}
		
		mouseScroll = 0;
		
		
		timer ++;
		clicked = false;
		
		if(timer >= 1000000) 
			timer = 0;
		
		
	}
	
	public void setSliderMalfunction(boolean tf) {
		sliderMalfunc = tf;
	}
	
	public void setHandler(Handler game) {
		this.game = game;
	}
	
	// Implemented

	@Override
	public void mouseDragged(MouseEvent e) {
		dragged = true;
		mouseX = e.getX();
		mouseY = e.getY();
		
		if(sliderMalfunc) {
			int b1 = MouseEvent.BUTTON1_DOWN_MASK;
			int b2 = MouseEvent.BUTTON2_DOWN_MASK;
			if ((e.getModifiersEx() & (b1 | b2)) == b1) {
			    left = true;
			} else if((e.getModifiersEx() & (b1 | b2)) == b2) {
				right = true;
			}
		} else {
			if(e.getButton() == MouseEvent.BUTTON1) {
				left = true;
			} else if(e.getButton() == MouseEvent.BUTTON3) {
				right = true;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		clicked = true;
		if(e.getButton() == MouseEvent.BUTTON1) {
			left = true;
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			right = true;
		}
		
		game.log("Mouse clicked: ("+getMouseX()+", "+getMouseY()+")");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		in = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		in = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			left = true;
			fullLeft = true;
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			right = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		clicked = false;
		dragged = false;
		if(e.getButton() == MouseEvent.BUTTON1) {
			left = false;
			fullLeft = false;
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			right = false;
		}
	}

	public void reset() {
		clicked = false;
		dragged = false;
		left = false;
		right = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseScroll = e.getWheelRotation();
		game.log("Mouse Scrolled: "+mouseScroll);
	}

	public double getMouseScroll() {
		return mouseScroll;
	}

}
