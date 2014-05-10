package de.abscanvas.level;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import de.abscanvas.Screen;
import de.abscanvas.additional.chat.ChatReciever;
import de.abscanvas.entity.LevelEntity;
import de.abscanvas.entity.network.ClientEntity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.internal.EntityRegister;
import de.abscanvas.internal.TileRegister;
import de.abscanvas.level.tile.EmptyTile;
import de.abscanvas.level.tile.Tile;
import de.abscanvas.math.BoxBounds;
import de.abscanvas.math.ByteUtilities;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.network.ClientAdapter;
import de.abscanvas.network.NetworkConstants;
import de.abscanvas.network.NetworkListener;
import de.abscanvas.network.NetworkUser;
import de.abscanvas.network.ServerUser;

public abstract class ClientLevel extends Level implements NetworkListener {
	private ClientAdapter clientAdapter;

	private ArrayList<EntityRegister> entityRegister = new ArrayList<EntityRegister>();
	private ArrayList<TileRegister> tileRegister = new ArrayList<TileRegister>();

	private String username = "";

	private int ping = 1; // ping in ms
	private long ping_last_request = System.currentTimeMillis(); // The time ping_request was recieved
	private boolean isPinging = false; // Ping answer send ... but not confirmed

	private int packagesLost = 0;

	private boolean isConnected = false;
	private int ticksSinceLastConnectionTry = 0;
	private boolean isTryingToConnect = false;

	private ChatReciever chatListener = null;

	private ArrayList<ServerUser> serverUser = new ArrayList<ServerUser>();
	private long ownUID = -1;

	public ClientLevel(Screen o, int tile_w, int tile_h, String username, InetAddress server_ip, int server_port, byte[] programm_identifier) {
		super(o, tile_w, tile_h, 1, 1);
		this.username = username;
		clientAdapter = new ClientAdapter(server_ip, server_port, this, programm_identifier);
		clientAdapter.start();
	}

	public void connect() {
		getNetworkAdapter().send(NetworkConstants.CMD_USER_CONNECT, ByteUtilities.string2arr(username));
		ticksSinceLastConnectionTry = 0;
		isTryingToConnect = true;
		// NOT reliable - Serverside-NetworkUser does not exist in that point
	}

	@Override
	public void tick() {
		if (tickTiles) {
			for (Tile[] td : tiles) {
				for (Tile t : td) {
					if (t == null) {
						continue;
					}
					t.tick();
				}
			}
		}

		MouseButtons mb = owner.getMouseButtons();

		if (tickEntities) {
			for (int i = 0; i < entities.size(); i++) {
				ClientEntity e = (ClientEntity) entities.get(i);

				if (e == null) {
					continue;
				}

				if (!e.isRemoved()) {
					MPoint mpi = mb.getPosition().clone();
					MDPoint mp = screenToCanvas(mpi);

					BoxBounds bbs = e.getBoxBounds();

					if (mp.isInRect(bbs)) {
						if (mb.isMouseMoving()) {
							e.onMouseMove(mb);
						}

						if (mb.isPressed()) {
							e.onMousePress(mb);
						} else if (mb.isReleased()) {
							e.onMouseRelease(mb);
						} else {
							if (!e.isHovered()) {
								e.setHovered(true);
								e.onMouseEnter(mb);
							} else {
								e.onMouseHover(mb);
							}
						}
					} else {
						if (e.isHovered()) {
							e.setHovered(false);
							e.onMouseLeave(mb);
						}
					}

					e.tick();

					int xtn = (int) (e.getPosX() - e.getRadius().getX()) / getTileWidth();
					int ytn = (int) (e.getPosY() - e.getRadius().getY()) / getTileHeight();
					if (xtn != e.getXto() || ytn != e.getYto()) {
						removeFromEntityMap(e);
						insertToEntityMap(e);
					}

					if (e.isUserControllable()) {
						if (e.hasMovedSinceLastServerUpdate(true)) {
							getNetworkAdapter().send(NetworkConstants.CMD_ENTITY_UPDATE_POSITION, e.getPositionSendData());
						}
					}
				}
				if (e.isRemoved()) {
					entities.remove(i--);
					removeFromEntityMap(e);
				}
			}
		}

		if (tickHUD) {
			if (hud != null) {
				hud.tick();
			}
		}

		if (mb.isMouseMoving()) {
			onMouseMove(mb);
		}

		if (mb.isPressed()) {
			onPressed(mb);
			if (mb.getHoveredEntities(this).isEmpty() && mb.getHoveredHUDElements(hud).isEmpty()) {
				if (mb.isHover(hud)) {
					onHUDPressed(mb);
				} else {
					onTilePressed(mb);
				}
			}
		}

		// ----------------------------------------------------------------------------------------------------------------------------
		// ------------------------------------------- Networking -------------------------------------------------------------------
		// ----------------------------------------------------------------------------------------------------------------------------

		if (!isConnected() && isTryingToConnect) {
			ticksSinceLastConnectionTry++;
			if (ticksSinceLastConnectionTry > 60) {
				System.out.println("tryToConnectAgain");
				connect();
			}
		}

		if (getNetworkAdapter().getBuffer().isForceSendByTime()) {
			getNetworkAdapter().sendBuffer();
		}

		getNetworkAdapter().getReliabilityInterface().tick();

		if (getPing() > NetworkConstants.PING_TIME_OUT) {
			if (isConnected()) {
				sendLocalChatMessage("Connection to the Server lost");
			}
			isConnected = false;
			onConnectionLost();
		}
	}

	public void registerEntity(Class<? extends ClientEntity> c, int cid) {
		entityRegister.add(new EntityRegister(c, (short) cid));
	}

	public void registerTile(Class<? extends Tile> c, int cid) {
		tileRegister.add(new TileRegister(c, (short) cid));
	}

	public short findClassID(ClientEntity e) {
		for (EntityRegister reg : entityRegister) {
			if (reg.getC().isAssignableFrom(e.getClass())) {
				return reg.getCID();
			}
		}
		return -1;
	}

	public short findClassID(Tile t) {
		for (TileRegister reg : tileRegister) {
			if (reg.getC().isAssignableFrom(t.getClass())) {
				return reg.getCID();
			}
		}
		return -1;
	}

	public TileRegister findTileRegister(short cid) {
		for (TileRegister reg : tileRegister) {
			if (reg.getCID() == cid) {
				return reg;
			}
		}
		return null;
	}

	public EntityRegister findEntityRegister(short cid) {
		for (EntityRegister reg : entityRegister) {
			if (reg.getCID() == cid) {
				return reg;
			}
		}
		return null;
	}

	@Override
	public int recieveCommand(byte packageID, int commandID, final byte[] data, InetAddress ip, int port) {
		switch (commandID) {
		case NetworkConstants.CMD_CONNECTION_ACCEPTED:
			return cmd_ConnectionAccepted(data);
		case NetworkConstants.CMD_ENTITY_POSITION_CHANGED:
			return cmd_EntityChanged(commandID, data, packageID);
		case NetworkConstants.CMD_ENTITY_FORCE_UPDATE_POSITION:
			return cmd_EntityChanged(commandID, data, packageID);
		case NetworkConstants.CMD_ENTITY_ADDED:
			return cmd_entityAdded(data);
		case NetworkConstants.CMD_ENTITY_ANIMATION_LAYERCHANGE:
			return cmd_EntityChanged(commandID, data, packageID);
		case NetworkConstants.CMD_ENTITY_ANIMATION_STARTSTOP:
			return cmd_EntityChanged(commandID, data, packageID);
		case NetworkConstants.CMD_ENTITY_ANIMATION_CHANGED:
			return cmd_EntityChanged(commandID, data, packageID);
		case NetworkConstants.CMD_ENTITY_REMOVED:
			return cmd_EntityChanged(commandID, data, packageID);
		case NetworkConstants.CMD_SET_MAPSIZE:
			return cmd_setMapSize(data);
		case NetworkConstants.CMD_SET_TILE:
			return cmd_setTiles(data);
		case NetworkConstants.CMD_ENTITY_CLEARALL:
			return cmd_removeAllEntitys(data);
		case NetworkConstants.CMD_PING_REQUEST:
			return cmd_Ping(1);
		case NetworkConstants.CMD_PING_CONFIRM:
			return cmd_Ping(2);
		case NetworkConstants.CMD_USER_KICKED:
			isConnected = false;
			sendLocalChatMessage("You've been kicked from the Server");
			onClientKicked();
			return 0;
		case NetworkConstants.CMD_RELIEABLE_PACKAGE_CONFIRMED:
			getNetworkAdapter().getReliabilityInterface().recieveReliablePackageConfirmation(ByteUtilities.arr2Long(data, 0));
			return 8;
		case NetworkConstants.CMD_MAP_INTIALIZATION_START:
			return 0;
		case NetworkConstants.CMD_MAP_INTIALIZATION_END:
			getNetworkAdapter().getReliabilityInterface().send(NetworkConstants.CMD_INTIALIZATION_SUCCESFULL);
			return 0;
		case NetworkConstants.CMD_ADD_USER:
			return cmd_addUsername(data);
		case NetworkConstants.CMD_UPDATE_USERNAME:
			return cmd_updateUsername(data);
		case NetworkConstants.CMD_CHAT_RECIEVE:
			return cmd_recieveChatMessage(data, false);
		case NetworkConstants.CMD_CHAT_RECIEVE_PRIVATE:
			return cmd_recieveChatMessage(data, true);
		default:
			return onCustomClientCommand(packageID, commandID, data);
		}
	}

	private int cmd_ConnectionAccepted(final byte[] data) {
		getNetworkAdapter().getReliabilityInterface().send(NetworkConstants.CMD_MAP_REQUEST);
		ownUID = ByteUtilities.arr2Long(data, 0);
		ping_last_request = System.currentTimeMillis();
		isConnected = true;
		isTryingToConnect = false;
		ticksSinceLastConnectionTry = 0;
		sendLocalChatMessage("Connected to the Server");
		return 8;
	}

	private int cmd_recieveChatMessage(byte[] data, boolean isPivate) {
		if (chatListener != null) {
			long broadcaster = ByteUtilities.arr2Long(data, 0);
			String txt = ByteUtilities.extractString(data, 8, NetworkConstants.STANDARD_STRING_SIZE);

			chatListener.recieveMessage(broadcaster, txt, isPivate);
		}
		
		return 8 + NetworkConstants.STANDARD_STRING_SIZE;
	}

	private int cmd_Ping(int ping_identifier) {
		if (ping_identifier == 1) { // PING_REQUEST
			ping_last_request = System.currentTimeMillis();
			isPinging = true;
			getNetworkAdapter().send(NetworkConstants.CMD_PING_ANSWER);
		} else if (ping_identifier == 2) { // PING_CONFIRM
			ping = (int) (System.currentTimeMillis() - ping_last_request);
			isPinging = false;
		} else {
			System.out.println("WHOOPS - SOMETHING WENT TERRIBLE WRONG");
		}
		
		return 0;
	}

	private int cmd_addUsername(byte[] data) {
		serverUser.add(new ServerUser(ByteUtilities.extractString(data, 8, NetworkConstants.SMALL_STRING_SIZE), ByteUtilities.arr2Long(data, 0)));
		
		return 8 + NetworkConstants.SMALL_STRING_SIZE;
	}

	private int cmd_updateUsername(byte[] data) {
		ServerUser su = getServersideUser(ByteUtilities.arr2Long(data, 0));
		if (su != null) {
			su.updateName(ByteUtilities.extractString(data, 8, NetworkConstants.SMALL_STRING_SIZE));
		}
		
		return 8 + NetworkConstants.SMALL_STRING_SIZE;
	}

	public int getPing() {
		if (isPinging) {
			return (int) Math.max((System.currentTimeMillis() - ping_last_request), ping);
		} else {
			if (((System.currentTimeMillis() - ping_last_request) + ping + 100) > NetworkConstants.TICKS_BETWEEN_PING * 17) {
				return (int) ((System.currentTimeMillis() - ping_last_request) - NetworkConstants.TICKS_BETWEEN_PING * 17);
			} else {
				return ping;
			}
		}
	}

	private int cmd_setTiles(byte[] data) {
		int x = ByteUtilities.arr2Int(data, 0);
		int y = ByteUtilities.arr2Int(data, 4);
		short tid = ByteUtilities.arr2Short(data, 8);
		Tile k = findTileRegister(tid).create();
		setTile(x, y, k);
		
		return 10;
	}

	/**
	 * @param data  
	 */
	private int cmd_removeAllEntitys(byte[] data) {
		entities.clear();
		
		return 0;
	}

	public void disconnect() {
		isConnected = false;
		isTryingToConnect = false;
	}

	@SuppressWarnings("unchecked")
	private int cmd_setMapSize(byte[] data) {
		int w = ByteUtilities.arr2Int(data, 0);
		int h = ByteUtilities.arr2Int(data, 4);

		this.tiles = new Tile[w][h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				Tile tile = new EmptyTile();

				setTile(x, y, tile);
			}
		}

		this.entityMap = new List[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				this.entityMap[x][y] = new ArrayList<LevelEntity>();
			}
		}

		this.width = w;
		this.height = h;
		
		return 8;
	}

	private int cmd_entityAdded(byte[] data) {
		long uid = ByteUtilities.arr2Long(data, 0);
		double x = ByteUtilities.arr2double(data, 8);
		double y = ByteUtilities.arr2double(data, 16);
		short classid = ByteUtilities.arr2Short(data, 24);

		System.out.println("Entity Added " + uid + "[" + classid + "] [" + x + "|" + y + "]");

		EntityRegister tr = findEntityRegister(classid);
		ClientEntity te = (ClientEntity) tr.create();
		if (te != null) {
			te.setPos(x, y);
			addEntity(te, uid);

			te.serverCommand_added(x, y, data);
		} else {
			System.out.println("ERROR: Entity not registered");
		}
		
		return 8 + 8 + 8 + 2;
	}

	private int cmd_EntityChanged(int cmd, byte[] data, byte packageID) {
		long uid = ByteUtilities.arr2Long(data, 0);

		ClientEntity e = (ClientEntity) getEntity(uid);
		if (e != null) {
			switch (cmd) {
			case NetworkConstants.CMD_ENTITY_ANIMATION_CHANGED:
				// NOTHING
				return 8;
			case NetworkConstants.CMD_ENTITY_ANIMATION_LAYERCHANGE:
				e.serverCommand_animationLayerChanged(data);
				return 8 + 4;
			case NetworkConstants.CMD_ENTITY_ANIMATION_STARTSTOP:
				e.serverCommand_animationStateChanged(data);
				return 8 + 1;
			case NetworkConstants.CMD_ENTITY_POSITION_CHANGED:
				e.serverCommand_positionChanged(data, false, packageID);
				return 8 + 8 * 2;
			case NetworkConstants.CMD_ENTITY_FORCE_UPDATE_POSITION:
				e.serverCommand_positionChanged(data, true, packageID);
				return 8 + 8 * 2;
			case NetworkConstants.CMD_ENTITY_REMOVED:
				e.serverCommand_removed(data);
				return 8;
			default:
				System.out.println("MYSTERIOUS ENTITY CMD ... WtF");
				return 8;
			}
		} else {
			System.out.println("ERROR: ENTITY NOT FOUND ON COMMAND RECIEVE ((" + NetworkConstants.cmd2String(cmd) + "))");

			switch (cmd) {
			case NetworkConstants.CMD_ENTITY_ANIMATION_CHANGED:
				return 8;
			case NetworkConstants.CMD_ENTITY_ANIMATION_LAYERCHANGE:
				return 8 + 4;
			case NetworkConstants.CMD_ENTITY_ANIMATION_STARTSTOP:
				return 8 + 1;
			case NetworkConstants.CMD_ENTITY_POSITION_CHANGED:
				return 8 + 8 * 2;
			case NetworkConstants.CMD_ENTITY_REMOVED:
				return 8;
			default:
				System.out.println("MYSTERIOUS ENTITY CMD ... WtF");
				return 8;
			}
		}
	}

	public String getServersideUsername(long userUID) {
		if (userUID == ownUID) {
			return username;
		} else if (userUID == NetworkConstants.CHAT_BID_LOCALDEBUGMESSAGE) {
			return "LOCAL_DEBUG_MSG";
		} else if (userUID == NetworkConstants.CHAT_BID_LOCALMESSAGE) {
			return "LOCAL";
		} else if (userUID == NetworkConstants.CHAT_BID_SERVERDEBUGMESSAGE) {
			return "SERVER_DEBUG_MSG";
		} else if (userUID == NetworkConstants.CHAT_BID_SERVERMESSAGE) {
			return "SERVER";
		} else if (userUID == NetworkConstants.CHAT_BID_UNKNOWN) {
			return "UNKNOWN";
		}

		for (int i = 0; i < serverUser.size(); i++) {
			if (serverUser.get(i).getUID() == userUID) {
				return serverUser.get(i).getName();
			}
		}

		return "{" + userUID + "}";
	}

	public ServerUser getServersideUser(long userUID) {
		for (int i = 0; i < serverUser.size(); i++) {
			if (serverUser.get(i).getUID() == userUID) {
				return serverUser.get(i);
			}
		}

		return null;
	}

	@Override
	public void recievePackage(byte[] data, byte packageID, InetAddress ip, int port) {
		// DO NOTHING
	}

	@Override
	public void onPackageLoss(int lossCount, NetworkUser u) {
		packagesLost += lossCount;
	}

	public int getLostPackages() {
		return packagesLost;
	}

	public void addEntity(ClientEntity e) {
		super.addEntity(e);
	}

	@Override
	public void failRecieveCommand(byte[] data, InetAddress ip, int port) {
		System.out.println("FAIL RECIEVE");
	}

	public ClientAdapter getNetworkAdapter() {
		return clientAdapter;
	}

	@Override
	public NetworkUser getServerUser(InetAddress ip, int port) {
		return null;
	}

	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * @return the NetworkUser-UID on the Server-side
	 */
	public long getServerSideUID() {
		return ownUID;
	}

	public void setChatListener(ChatReciever rec) {
		chatListener = rec;
	}

	public ChatReciever getChatListener() {
		return chatListener;
	}

	public String getUsername() {
		return username;
	}

	public void sendLocalChatMessage(String txt) {
		if (chatListener != null) {
			chatListener.recieveMessage(NetworkConstants.CHAT_BID_LOCALMESSAGE, txt, false);
		}
	}

	public void sendLocalChatDebugMessage(String txt) {
		if (chatListener != null) {
			chatListener.recieveMessage(NetworkConstants.CHAT_BID_LOCALDEBUGMESSAGE, txt, false);
		}
	}

	// --------------------------------------------------------------------------------------------------------------
	// ------------------------- EVENTS ---------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------

	public abstract void onConnectionLost();

	public abstract void onClientKicked();

	public abstract int onCustomClientCommand(byte packageID, int commandID, byte[] data);
}
