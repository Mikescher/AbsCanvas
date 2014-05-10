package de.abscanvas.entity.network;

import de.abscanvas.entity.Entity;
import de.abscanvas.input.Art;
import de.abscanvas.level.ClientLevel;
import de.abscanvas.math.ByteUtilities;
import de.abscanvas.math.MDPoint;
import de.abscanvas.network.NetworkConstants;

public abstract class ClientEntity extends NetworkEntity {
	private final static int TBU_LIMIT = 45;
	private final static int MAX_NO_UPDATE = 180;

	private boolean isUserControllable = false;
	private MDPoint lastServerUpdatePosition = new MDPoint(-1, -1);

	// --------- Smoothing the movement of Client Enities ---------
	private boolean enableSmooth = true;
	private MDPoint newSmoothPosition = new MDPoint();
	private MDPoint oldSmoothPosition = new MDPoint();
	private byte lastSmoothPackageID = Byte.MIN_VALUE;
	private long lastSmoothUpdate = System.currentTimeMillis();
	private long lastUpdate = 0;
	private int ticksBetweenUpdate = 4;
	private int TBU_limiter = 0;
	private int TBU_counter = 0;
	private int TBU_tick_counter = 0;
	private boolean smooth_moving = false;

	// ------------------------------------------------------------
	
	private boolean minimapColorCalculated = false;
	private int minimapColor = -1;

	public ClientEntity() {
		super();
	}

	@Override
	public void tick() {
		super.tick();

		TBU_tick_counter++;
		lastUpdate++;

		if (!isUserControllable() && smooth_moving && enableSmooth) {
			calcSmoothMovement();
		}

		if (lastUpdate > MAX_NO_UPDATE) {
			lastUpdate = 0;
			((ClientLevel) getOwner()).getNetworkAdapter().send(NetworkConstants.CMD_ENTITY_ISALIVE_REQUEST, ByteUtilities.long2Arr(getUID()));
		}
	}

	@Override
	public void onInit(int tileW, int tileH) {
		super.onInit(tileW, tileH);
		newSmoothPosition.set(getPosition());
		oldSmoothPosition.set(getPosition());
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		// keine Client Collisions
	}

	private void calcSmoothMovement() {
		int x = (int) (oldSmoothPosition.getX() + ((newSmoothPosition.getX() - oldSmoothPosition.getX()) / ticksBetweenUpdate) * TBU_tick_counter);
		int y = (int) (oldSmoothPosition.getY() + ((newSmoothPosition.getY() - oldSmoothPosition.getY()) / ticksBetweenUpdate) * TBU_tick_counter);

		setPos(x, y);

		if (TBU_tick_counter >= ticksBetweenUpdate) {
			smooth_moving = false;
			setPos(newSmoothPosition.getX(), newSmoothPosition.getY());
		}
	}

	public synchronized void serverCommand_positionChanged(byte[] data, boolean force, byte packageID) {
		lastUpdate = 0;

		if (!force && isUserControllable()) {
			return;
		}

		double x = ByteUtilities.arr2double(data, 8);
		double y = ByteUtilities.arr2double(data, 16);

		if (enableSmooth && !force) {
			TBU_counter += TBU_tick_counter;
			TBU_tick_counter = 0;
			TBU_limiter++;

			if (TBU_limiter > TBU_LIMIT) {
				int ttbu = TBU_counter / TBU_limiter;

				if (ttbu < 1) {
					ttbu = 1;
				}

				ticksBetweenUpdate = ttbu;

				TBU_counter = 0;
				TBU_limiter = 0;
			}

			if (ByteUtilities.compareByte(lastSmoothPackageID, packageID) > 0 || (System.currentTimeMillis() - lastSmoothUpdate) > ticksBetweenUpdate * 16) {
				lastSmoothPackageID = packageID;
				lastSmoothUpdate = System.currentTimeMillis();
				oldSmoothPosition.set(getPosition());
				newSmoothPosition.set(x, y);
				smooth_moving = true;
			}
		} else if (force && smooth_moving){
			TBU_counter += TBU_tick_counter;
			TBU_tick_counter = 0;
			TBU_limiter++;

			if (TBU_limiter > TBU_LIMIT) {
				int ttbu = TBU_counter / TBU_limiter;

				if (ttbu < 1) {
					ttbu = 1;
				}

				ticksBetweenUpdate = ttbu;

				TBU_counter = 0;
				TBU_limiter = 0;
			}

			if (ByteUtilities.compareByte(lastSmoothPackageID, packageID) > 0 || (System.currentTimeMillis() - lastSmoothUpdate) > ticksBetweenUpdate * 16) {
				lastSmoothPackageID = packageID;
				lastSmoothUpdate = System.currentTimeMillis();
				oldSmoothPosition.set(x, y);
				newSmoothPosition.set(x, y);
				setPos(x, y);
				smooth_moving = true;
			}
		} else {
			setPos(x, y);
		}
	}

	public void serverCommand_animationLayerChanged(byte[] data) {
		lastUpdate = 0;

		int l = ByteUtilities.arr2Int(data, 8);

		getAnimation().setLayer(l);
	}

	public void serverCommand_animationStateChanged(byte[] data) {
		lastUpdate = 0;

		boolean s = ByteUtilities.arr2Boolean(data, 8);

		getAnimation().animate(s);
	}

	/**
	 * @param x 
	 * @param y 
	 * @param data  
	 */
	public void serverCommand_added(double x, double y, byte[] data) {
		lastUpdate = 0;

		// Congratz, now ... you live
	}

	/**
	 * @param data  
	 */
	public void serverCommand_removed(byte[] data) {
		lastUpdate = 0;

		remove();
	}

	public void setUserControllable(boolean uc) {
		isUserControllable = uc;
	}

	public boolean isUserControllable() {
		return isUserControllable;
	}

	public boolean hasMovedSinceLastServerUpdate(boolean reset) {
		if (getPosition().compare(lastServerUpdatePosition)) {
			return false;
		}

		if (reset) {
			lastServerUpdatePosition.set(getPosition());
		}

		return true;
	}

	public byte[] getPositionSendData() {
		byte[] d = new byte[24];
		d = ByteUtilities.insert(d, ByteUtilities.long2Arr(getUID()), 0);
		d = ByteUtilities.insert(d, ByteUtilities.double2Arr(getPosX()), 8);
		d = ByteUtilities.insert(d, ByteUtilities.double2Arr(getPosY()), 16);

		return d;
	}

	public void setAutoSmoothing(boolean as) {
		enableSmooth = as;
	}

	public boolean isAutoSmooth() {
		return enableSmooth;
	}

	public void setMinimapColor(int c) {
		minimapColor = c;
		
		minimapColorCalculated = true;
	}
		
	
	public int getMinimapColor() {
		if (! minimapColorCalculated) {
			setMinimapColor(Art.getColor(Art.getColors(getAnimation().getAllSprite())));
		}
		
		return minimapColor;
	}
}
