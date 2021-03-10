package com.zandgall.arvopia.tiles;

import com.zandgall.arvopia.gfx.Assets;
import com.zandgall.arvopia.gfx.ImageLoader;

public class GrassTile extends Tile {

	private static Assets grassTileset = new Assets(ImageLoader.loadImage("/textures/Tiles/DirtTileset.png"), 18, 18,
			"GrassTile");

	public GrassTile(int id, int x, int y) {
		super(grassTileset.get(x, y), id);
	}

	public boolean isSolid() {
		if(id == 19 || id == 20 || id == 23)
			return false;
		return true;
	}

	public void init() {
		
	}

	@Override
	public void tick() {
		
	}
	
	

	@Override
	public void reset() {
		grassTileset.reset(ImageLoader.loadImage("/textures/DirtTileset.png"), 18, 18,
				"GrassTile");
	}
}
