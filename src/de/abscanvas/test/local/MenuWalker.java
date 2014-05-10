package de.abscanvas.test.local;

import java.util.Random;

import de.abscanvas.entity.Entity;
import de.abscanvas.entity.Facing;
import de.abscanvas.entity.MenuEntity;
import de.abscanvas.math.MDPoint;

public class MenuWalker extends MenuEntity {
	
	MDPoint aMov = new MDPoint(0, 0);

	int steps = 0;
	
	int sst = 0;
	
	public Facing facing = new Facing(8);
	
	public MenuWalker(double d, double e) {
		getAnimation().setAnimation(GameArt.herrSpeck, (700));
		setPos(d, e);
		turn();
	}

	@Override
	public void tick() {
		super.tick();
		
		walk(aMov);
		
		if (Math.random()*900 < sst) {
			turn();
		}
		
		if ((getPosY() < 20) || (getPosY() > (getOwner().getOwner().getScreenHeight() - 20))) {
			aMov.setY(1);
			facing.setVector(aMov);
			getAnimation().setLayer(facing);
			
			sst = 0;
		} 
		if  (getPosY() > (getOwner().getOwner().getScreenHeight() - 20)) {
			aMov.setY(-1);
			facing.setVector(aMov);
			getAnimation().setLayer(facing);
			
			sst = 0;
		}
		if (getPosX() < 20) {
			aMov.setX(1);
			facing.setVector(aMov);
			getAnimation().setLayer(facing);
			
			sst = 0;
		}
		if (getPosX() > (getOwner().getOwner().getScreenWidth() - 20)) {
			aMov.setX(-1);
			facing.setVector(aMov);
			getAnimation().setLayer(facing);
			
			sst = 0;
		}
			
	}
	
	private void walk(MDPoint aMov) {
		move(aMov.getX(), aMov.getY());
		getAnimation().animate(true);
		
		aMov.mult(1 / aMov.getLength());

		Random r = new Random();
		if (steps % 10 == 0) {
			if (r.nextInt(4) == 2) {
				getOwner().addEffect(GameArt.dust, 750, (int) getPosX(), (int) getPosY());
			}

		}

		steps++;
		sst++;
	}
	
	private void turn() {
		MDPoint t = aMov.clone();
		
		while (t.compare(aMov) || t.isZero()) {
			t.set(Math.floor(Math.random()*3)-1, Math.floor(Math.random()*3)-1);
		}
		
		aMov = t;
		
		facing.setVector(aMov);
		getAnimation().setLayer(facing);
		
		sst = 0;
	}
	
	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		//nocode
	}

}
