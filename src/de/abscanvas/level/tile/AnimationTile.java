package de.abscanvas.level.tile;

import de.abscanvas.surface.Animation;
import de.abscanvas.surface.Surface;

public class AnimationTile extends Tile{
	private Animation animation = new Animation();
	
	public AnimationTile() {
		super();
		animation.setOriginToUpperLeft();
	}
	
	@Override
    public void render(Surface screen) {
        animation.render(screen, getX() * getLevel().getTileWidth(), getY() * getLevel().getTileHeight());
    }
    
    public Animation getAnimation() {
    	return animation;
    }
    
    @Override
    public void tick() {
    	animation.tick();
    }
    
    @Override
    public void onInit() {
    	animation.setScreen(getOwner().getOwner());
    }
}
