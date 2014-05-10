package de.abscanvas.test.client;

import java.net.InetAddress;

import de.abscanvas.Screen;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.level.ClientLevel;
import de.abscanvas.level.tile.EmptyTile;
import de.abscanvas.math.ByteUtilities;
import de.abscanvas.math.MDPoint;
import de.abscanvas.network.NetworkConstants;
import de.abscanvas.network.NetworkUser;

public class GameLevel extends ClientLevel {
	public final static byte[] IDENTIFIER = { 100, 23 };

	private long myPlayer = -1;

	public GameLevel(Screen o, InetAddress ip, int port, String name) {
		super(o, 32, 32, name, ip, port, IDENTIFIER);

		registerEntity(Player.class, 1);
		registerEntity(Mummy.class, 2);

		registerTile(EmptyTile.class, 0);
		registerTile(FloorTile.class, 1);
		registerTile(HillTile.class, 2);
		registerTile(RockTile.class, 3);
		registerTile(SpawnerTile.class, 4);
		
		setHUD(new GameHUD(this));

		connect();
	}

	@Override
	public void tick() {
		super.tick();

		GameKeys keys = (GameKeys) getOwner().getKeys();

		MDPoint aMov = new MDPoint(0, 0);
		
		boolean speed = false;
		
		if (keys.shift.isDown) {
			speed = true;
		}

		if (keys.up.isDown) {
			aMov.subY(2);
		}
		if (keys.down.isDown) {
			aMov.addY(2);
		}
		if (keys.left.isDown) {
			aMov.subX(2);
		}
		if (keys.right.isDown) {
			aMov.addX(2);
		}

		Player myP = (Player) getEntity(myPlayer);
		if (myP != null) {
			myP.setUserControllable(true);
			myP.nextStep.set(aMov);
			myP.plusspeed = speed;
			
			MDPoint o = new MDPoint(myP.getClonedPos());
			o.sub(getOwner().getScreenWidth() / 2, getOwner().getScreenHeight() / 2);

			getOwner().setOffset(o.trunkToMPoint());
		}
	}

	@Override
	public void onPackageLoss(int lossCount, NetworkUser u) {
		super.onPackageLoss(lossCount, u);
		System.out.println("PACKAGE LOST: " + lossCount + " ABSOLUT: " + getLostPackages());
	}

	@Override
	public void onTilePressed(MouseButtons mouse) {
		System.out.println("TilePressed");
	}

	@Override
	public void onMouseMove(MouseButtons mouse) {
		// Event - override this as needed
	}

	@Override
	public void onHUDPressed(MouseButtons mouse) {
		System.out.println("HUDPressed");
	}

	@Override
	public void onPressed(MouseButtons mouse) {
		// Event - override this as needed
	}

	@Override
	public void onConnectionLost() {
		// DO NOTHING
	}

	@Override
	public void onClientKicked() {
		// DO NOTHING
	}

	@Override
	public int onCustomClientCommand(byte packageID, int commandID, byte[] data) {
		if (commandID == (255 + 20)) {
			myPlayer = ByteUtilities.arr2Long(data, 0);
			return 8;
		} else {
			System.out.println("UNKNOWN CUSTOM USER COMMAND " + commandID);
			return 0;
		}
	}

	public void stop() {
		getNetworkAdapter().sendNow(NetworkConstants.CMD_USER_DISCONNECT);
	}
}
