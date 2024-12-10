package com.zandgall.arvopia.entity.moveableStatics;


import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.entity.Entity;

public abstract class MoveableStatic extends Entity{

	public MoveableStatic(Handler handler, double x, double y, int width, int height, boolean solid) {
		super(handler, x, y, width, height, solid, false, false, false);
		
	}

	public abstract void tick();
	
	public abstract void init();
	
}
