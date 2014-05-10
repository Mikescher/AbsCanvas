package de.abscanvas.test.server;

import java.net.InetAddress;

import de.abscanvas.ConsoleScreen;
import de.abscanvas.additional.swinginterface.StandardServerWindow;
import de.abscanvas.level.ServerLevel;
import de.abscanvas.level.tile.EmptyTile;
import de.abscanvas.math.ByteUtilities;
import de.abscanvas.network.NetworkUser;

public class TestLevel extends ServerLevel {
	public TestLevel(ConsoleScreen o, int port, byte[] identifier) {
		super(o, 32, 32, 65, 65, port, identifier);

		registerEntity(Player.class, 1);
		registerEntity(Mummy.class, 2);

		registerTile(EmptyTile.class, 0);
		registerTile(FloorTile.class, 1);
		registerTile(HillTile.class, 2);
		registerTile(RockTile.class, 3);
		registerTile(SpawnerTile.class, 4);

		loadMapFromRessources("/map.acm");
		
//		create();
		
//		LevelIO.saveLevel(this, new TranslationTable() {
//			
//			@Override
//			public int getID(Tile t) {
//				if (t instanceof EmptyTile) {
//					return 0;
//				} else if (t instanceof FloorTile) {
//					return 1;
//				} else if (t instanceof HillTile) {
//					return 2;
//				} else if (t instanceof RockTile) {
//					return 3;
//				} else if (t instanceof SpawnerTile) {
//					return 4;
//				} else {
//					System.out.println("ERROR");
//					return 0;
//				}
//			}
//		}, new File("/babbla.acm"));
	}

//	private void create() {
//		for (int x = 0; x < getWidth(); x++) {
//			for (int y = 0; y < getHeight(); y++) {
//				if (! (getTile(x, y) instanceof EmptyTile)) {
//					continue;
//				}
//				
//				if ((x > 9 && y > 9 && x < (getWidth() - 10) && y < (getHeight() - 10)) && (x == 10 || y == 10 || x == (getWidth() - 11) || y == (getHeight() - 11))) {
//					// BORDER ;)
//					setTile(x, y, new HillTile());
//					continue;
//				}
//
//				if (Math.random() > 0.1) {
//					setTile(x, y, new FloorTile());
//				} else {
//					if (Math.random() > 0.05) {
//						setTile(x, y, new HillTile());
//					} else {
//						if (x > 11 && y > 11 && x < (getWidth() - 12) && y < (getHeight() - 12)) {
//							setTile(x, y, new SpawnerTile());
//							setTile(x-1, y-1, new FloorTile());
//							setTile(x-1, y, new FloorTile());
//							setTile(x-1, y+1, new FloorTile());
//							setTile(x, y-1, new FloorTile());
//							setTile(x+1, y-1, new FloorTile());
//							setTile(x+1, y, new FloorTile());
//							setTile(x+1, y+1, new FloorTile());
//							setTile(x, y+1, new FloorTile());
//						} else {
//							setTile(x, y, new HillTile());
//						}
//					}
//				}
//			}
//		}
//
//		setPaintOrder(paintOrder);
//
//		MPoint tp = new MDPoint(width * Tile.WIDTH / 2d, height * Tile.HEIGHT / 2d).roundToMPoint();
//		tp.div(Tile.WIDTH);
//
//		Tile tile = new RockTile();
//		setTile(tp.getX() + 0, tp.getY() + 0, tile);
//		tile = new RockTile();
//		setTile(tp.getX() - 1, tp.getY() - 1, tile);
//		tile = new RockTile();
//		setTile(tp.getX() - 1, tp.getY() + 0, tile);
//		tile = new RockTile();
//		setTile(tp.getX() - 1, tp.getY() + 1, tile);
//		tile = new RockTile();
//		setTile(tp.getX() + 0, tp.getY() - 1, tile);
//		tile = new RockTile();
//		setTile(tp.getX() + 1, tp.getY() - 1, tile);
//		tile = new RockTile();
//		setTile(tp.getX() + 1, tp.getY() + 0, tile);
//		tile = new RockTile();
//		setTile(tp.getX() + 1, tp.getY() + 1, tile);
//		tile = new RockTile();
//		setTile(tp.getX() + 0, tp.getY() + 1, tile);
//	}

	@Override
	public int onCustomClientCommand(byte packageID, int commandID, byte[] data, NetworkUser u) {
		System.out.println("UNKNOWN CUSTOM USER COMMAND " + commandID);
		return 0;
	}

	@Override
	public NetworkUser onUserConnect(String name, InetAddress ip, int port) {
		Player player = new Player();
		player.setPos(width * getTileWidth() / 2d, height * getTileHeight() / 2d);
		addEntity(player);

		return new MyUser(name, ip, port, getNetworkAdapter(), player);
	}

	@Override
	public void onAfterUserConnect(NetworkUser u) {
		if (!((TestScreen) getOwner()).isConsole) {
			((StandardServerWindow) (((TestScreen) getOwner()).owner)).addUser(u, getUsers().size());
		}

		u.getReliabilityInterface().send((short) (255 + 20), ByteUtilities.long2Arr(((MyUser) u).player.getUID()));
	}

	@Override
	public void onPackageLoss(int lossCount, NetworkUser u) {
		super.onPackageLoss(lossCount, u);
	}

	@Override
	public void onUserTimedOut(NetworkUser u) {
		redoUserList();
		removeEntity(((MyUser) u).player.getUID());
	}

	@Override
	public void onUserKicked(NetworkUser u) {
		redoUserList();
		removeEntity(((MyUser) u).player.getUID());
	}

	@Override
	public void onUserDisconnected(NetworkUser u) {
		redoUserList();
		removeEntity(((MyUser) u).player.getUID());
	}

	private void redoUserList() {
		if (!((TestScreen) getOwner()).isConsole) {
			((StandardServerWindow) (((TestScreen) getOwner()).owner)).resetUserList();

			for (NetworkUser kk : getUsers()) {
				((StandardServerWindow) (((TestScreen) getOwner()).owner)).addUser(kk, getUsers().size());
			}
		}
	}

	@Override
	public String getServerName() {
		return "Semtex Gaming Server";
	}

	@Override
	public String getGameName() {
		return "Mafia 2: Super Mario";
	}

	@Override
	public String getVersionString() {
		return MainWindow.VERSION;
	}

	@Override
	public int getMaxClientCount() {
		return 6;
	}

	@Override
	public boolean canConnectNewUser() {
		return getUsers().size() < getMaxClientCount();
	}
}
