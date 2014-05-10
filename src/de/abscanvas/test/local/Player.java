package de.abscanvas.test.local;

import java.util.Random;

import de.abscanvas.Screen;
import de.abscanvas.entity.DebugRenderer;
import de.abscanvas.entity.Entity;
import de.abscanvas.input.Keys;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.MDPoint;
import de.abscanvas.surface.Surface;

public class Player extends Mob {
	private Keys keys;

	public int shootDelay = 0;

	Screen screen;

	int steps = 0;

	MDPoint startPos;

	public Player(Keys keys, Screen screen, int x, int y) {
		super(x, y, 8);
		getAnimation().setAnimation(GameArt.herrSpeck, (700));
		startPos = new MDPoint(x, y);
		this.keys = keys;
		this.screen = screen;
		
		setRadius(10);

		updateOffset();
	}

	@Override
	public void tick() {
		super.tick();

		MDPoint aMov = new MDPoint(0, 0);

		if (((GameKeys) keys).up.isDown) {
			aMov.subY(1);
		}
		if (((GameKeys) keys).down.isDown) {
			aMov.addY(1);
		}
		if (((GameKeys) keys).left.isDown) {
			aMov.subX(1);
		}
		if (((GameKeys) keys).right.isDown) {
			aMov.addX(1);
		}

		if (!aMov.isZero()) {
			walk(aMov);
		} else {
			getAnimation().animate(false);
		}
	}

	private void walk(MDPoint aMov) {
		facing.setVector(aMov);

		double dd = aMov.getLength();
		double speed = getSpeed() / dd;

		aMov.mult(speed);

		if (((GameKeys) keys).shift.isDown) {
			setPos(getPosX() + aMov.getX() * 2, getPosY() + aMov.getY() * 2);

			steps += 2;
		} else {
			move(aMov.getX(), aMov.getY());

			steps++;
		}

		updateOffset();

		getAnimation().animate(true);

		Random r = new Random();
		if (steps % 20 == 0) {
//			getSoundPlayer().playSound("/Step " + (r.nextInt(2) + 1) + ".wav");
		}
		if (steps % 10 == 0) {
			if (r.nextInt(4) == 2) {
				getOwner().addEffect(GameArt.dust, 750, (int) getPosX(), (int) getPosY());
			}

		}

	}

	@Override
	public void render(Surface surface) {
		getAnimation().render(surface, getPosX(), getPosY() - 9);
		if (isDebugging()) (new DebugRenderer(this)).render(surface);
	}

	private void updateOffset() {
		MDPoint o = new MDPoint(getPosX(), getPosY());
		o.sub(screen.getScreenWidth() / 2, screen.getScreenHeight() / 2);

		screen.setOffset(o.trunkToMPoint());
	}

	@Override
	public boolean canPass(Entity e) {
		return false;
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		//nocode
	}

	@Override
	public void onInit(int tileW, int tileH) {
		super.onInit(tileH, tileH);
		updateOffset();
	}

	public void resetPosition() {
		setPos(startPos);
		updateOffset();
	}

	@Override
	public void onMousePress(MouseButtons mouse) {
		System.out.println("Stop clicking the Player");
		setDebugging(!isDebugging());
	}
}
