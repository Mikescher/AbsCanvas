package de.abscanvas.entity.network;

import de.abscanvas.entity.FacingListener;
import de.abscanvas.input.Art;
import de.abscanvas.level.ServerLevel;
import de.abscanvas.math.ByteUtilities;
import de.abscanvas.math.MDPoint;
import de.abscanvas.network.NetworkConstants;
import de.abscanvas.network.NetworkUser;
import de.abscanvas.surface.Surface;

public abstract class ServerEntity extends NetworkEntity implements FacingListener{
	private boolean animationLayerChanged = false;
	private boolean animationChanged = false;
	private boolean animationStateChanged = false;
	
	private boolean minimapColorCalculated = false;
	private int minimapColor = -1;
	
	public ServerEntity() {
		super();
		getAnimation().setFacingListener(this);
	}
	
	@Override
	public void render(Surface surface) {
		// doop nothing
	}
	
	public byte[] getSendData_PositionChanged() {
		byte[] result = new byte[24];

		result = ByteUtilities.insert(result, ByteUtilities.long2Arr(getUID()), 0);
		result = ByteUtilities.insert(result, ByteUtilities.double2Arr(getPosX()), 8);
		result = ByteUtilities.insert(result, ByteUtilities.double2Arr(getPosY()), 16);

		return result;
	}

	public byte[] getSendData_EntityAdded(ServerLevel owner) {
		byte[] result = new byte[26];

		result = ByteUtilities.insert(result, ByteUtilities.long2Arr(getUID()), 0);
		result = ByteUtilities.insert(result, ByteUtilities.double2Arr(getPosX()), 8);
		result = ByteUtilities.insert(result, ByteUtilities.double2Arr(getPosY()), 16);
		result = ByteUtilities.insert(result, ByteUtilities.short2Arr(owner.findClassID(this)), 24);

		return result;
	}
	
	public byte[] getSendData_AnimationStateChanged() {
		byte[] result = new byte[9];

		result = ByteUtilities.insert(result, ByteUtilities.long2Arr(getUID()), 0);
		result = ByteUtilities.insert(result, ByteUtilities.boolean2Arr(getAnimation().isAnimated()), 8);

		return result;
	}

	public byte[] getSendData_AnimationLayerChanged() {
		byte[] result = new byte[12];

		result = ByteUtilities.insert(result, ByteUtilities.long2Arr(getUID()), 0);
		result = ByteUtilities.insert(result, ByteUtilities.int2Arr(getAnimation().getLayer()), 8);

		return result;
	}
	
	@Override
	public void onLayerChanged(int layer) {
		animationLayerChanged = true;
	}

	@Override
	public void onAnimatedChanged(boolean animated) {
		animationStateChanged = true;
	}

	@Override
	public void onAnimationChanged() {
		animationChanged = true;
	}

	public void setAnimationChanged(boolean animationChanged) {
		this.animationChanged = animationChanged;
	}

	public boolean isAnimationChanged(boolean reset) {
		if (animationChanged) {
			animationChanged = !reset;
			return true;
		} else {
			return false;
		}
	}

	public void setAnimationStateChanged(boolean animationStateChanged) {
		this.animationStateChanged = animationStateChanged;
	}

	public boolean isAnimationStateChanged(boolean reset) {
		if (animationStateChanged) {
			animationStateChanged = !reset;
			return true;
		} else {
			return false;
		}
	}

	public void setAnimationLayerChanged(boolean animationLayerChanged) {
		this.animationLayerChanged = animationLayerChanged;
	}
	
	public void forcePos(MDPoint fp) {
		forcePos(fp.getX(), fp.getY());
	}
	
	public void forcePos(double fx, double fy) {
		setPos(fx, fy);
		((ServerLevel)getOwner()).sendAll(NetworkConstants.CMD_ENTITY_FORCE_UPDATE_POSITION, getSendData_PositionChanged());
	}

	public boolean isAnimationLayerChanged(boolean reset) {
		if (animationLayerChanged) {
			animationLayerChanged = !reset;
			return true;
		} else {
			return false;
		}
	}
	
	public abstract boolean isControllableByUser(NetworkUser controllRequest);
	public abstract void onAfterClientControlMove(MDPoint move);

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
