package de.abscanvas.level;

import java.net.InetAddress;
import java.util.ArrayList;

import de.abscanvas.ConsoleScreen;
import de.abscanvas.entity.LevelEntity;
import de.abscanvas.entity.network.ServerEntity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.internal.EntityRegister;
import de.abscanvas.internal.TileRegister;
import de.abscanvas.level.tile.Tile;
import de.abscanvas.math.ByteUtilities;
import de.abscanvas.math.MDPoint;
import de.abscanvas.network.NetworkConstants;
import de.abscanvas.network.NetworkListener;
import de.abscanvas.network.NetworkUser;
import de.abscanvas.network.ServerAdapter;
import de.abscanvas.output.LevelIO;
import de.abscanvas.output.ServerCreationTable;
import de.abscanvas.surface.Surface;

public abstract class ServerLevel extends Level implements NetworkListener {
	private ServerAdapter serverAdapter;

	private ArrayList<NetworkUser> users = new ArrayList<NetworkUser>();

	private ArrayList<EntityRegister> entityRegister = new ArrayList<EntityRegister>();
	private ArrayList<TileRegister> tileRegister = new ArrayList<TileRegister>();

	private int packagesLost = 0;

	private int ticks_since_ping = NetworkConstants.TICKS_BETWEEN_PING;

	public ServerLevel(ConsoleScreen o, int tile_w, int tile_h, int width, int height, int port, byte[] identifier) {
		super(o, tile_w, tile_h, width, height);
		serverAdapter = new ServerAdapter(port, this, identifier);
		if (serverAdapter.start()) {
			consoleOutput("Server started on port " + port);
			consoleOutput();
		} else {
			consoleOutput("Server could not start (Port " + port + " already in use ?)");
		}
	}
	
	public void loadMapFromRessources(String map) {
		LevelIO.load(this, map, new ServerCreationTable(this));
	}

	@Override
	public int recieveCommand(byte packageID, int commandID, final byte[] data, InetAddress ip, int port) {
		if (commandID == NetworkConstants.CMD_USER_CONNECT) {
			return cmd_ConnectNewUser(data, ip, port);
		} else if (commandID == NetworkConstants.CMD_SERVERINFORMATION_REQUEST) {
			return cmd_serverinformationRequest(ip, port);
		} else if (commandID == NetworkConstants.CMD_UNCONNECTED_PING_REQUEST) {
			byte[] d = new byte[6];
			ByteUtilities.insert(d, getNetworkAdapter().getIdentifier(), 0); // 4
			ByteUtilities.insert(d, ByteUtilities.short2Arr(NetworkConstants.CMD_UNCONNECTED_PING_ANSWER), 4);

			getNetworkAdapter().sendUnconnected(d, ip, port);

			return 0;
		} else {
			NetworkUser u = getUser(ip, port);
			if (u != null) {
				switch (commandID) {
				case NetworkConstants.CMD_USER_DISCONNECT:
					disconnectUser(u);
					consoleOutput("USER '" + u.getName() + "' DISCONNECTED (" + u.getIP().getHostAddress() + ")");
					return 0;
				case NetworkConstants.CMD_MAP_REQUEST:
					resendMapTo(u);
					return 0;
				case NetworkConstants.CMD_PING_ANSWER:
					u.recievePingAnswer();
					return 0;
				case NetworkConstants.CMD_RELIEABLE_PACKAGE_CONFIRMED:
					u.getReliabilityInterface().recieveReliablePackageConfirmation(ByteUtilities.arr2Long(data, 0));
					return 8;
				case NetworkConstants.CMD_INTIALIZATION_SUCCESFULL:
					consoleOutput("USER '" + u.getName() + "' FULLY INTIALIZED");
					return 0;
				case NetworkConstants.CMD_ENTITY_UPDATE_POSITION:
					cmd_UpdateEntityPos(data, u);
					return 24;
				case NetworkConstants.CMD_ENTITY_ISALIVE_REQUEST:
					cmd_isAliveRequest(data);
					return 8;
				case NetworkConstants.CMD_CHAT_SEND:
					cmd_chatMsgRecieve(u, data);
					return NetworkConstants.STANDARD_STRING_SIZE;
				case NetworkConstants.CMD_CHAT_SEND_PRIVATE:
					cmd_privateChatMsgRecieve(u, data);
					return NetworkConstants.STANDARD_STRING_SIZE;
				default:
					return onCustomClientCommand(packageID, commandID, data, u);
				}
			} else {
				System.out.println("UNCONNECTED USERS TRIES TO SEND DATA (" + NetworkConstants.cmd2String(commandID) + ")");
				return 0;
			}
		}
	}
	
	private void cmd_privateChatMsgRecieve(NetworkUser u, byte[] data) {
		NetworkUser su = getUser(ByteUtilities.arr2Long(data, 0));
		if (su != null) {
			sendPrivateChatMessage(u.getUID(), ByteUtilities.extractString(data, 8, NetworkConstants.STANDARD_STRING_SIZE), su);
		}
	}
	
	private void cmd_chatMsgRecieve(NetworkUser u, byte[] data) {
		sendChatMessage(u.getUID(), ByteUtilities.extractString(data, 0, NetworkConstants.STANDARD_STRING_SIZE));
	}

	private void cmd_isAliveRequest(final byte[] data) {
		long uid = ByteUtilities.arr2Long(data, 0);

		ServerEntity e = (ServerEntity) getEntity(uid);
		if (e == null) {
			byte[] rt = new byte[8];
			rt = ByteUtilities.insert(rt, ByteUtilities.long2Arr(uid), 0);

			sendAll(NetworkConstants.CMD_ENTITY_REMOVED, rt);
		} // no else !!
	}

	private int cmd_ConnectNewUser(final byte[] data, InetAddress ip, int port) {
		if (getUser(ip, port) == null) {
			NetworkUser connUser = onUserConnect(ByteUtilities.extractString(data, 0, NetworkConstants.STANDARD_STRING_SIZE), ip, port);
			if (connUser != null) {
				addUser(connUser);
				consoleOutput("USER '" + connUser.getName() + "' CONNECTED (" + connUser.getIP().getHostAddress() + ")");
				onAfterUserConnect(connUser);
			}
		} else {
			System.out.println("double user connect prohibited");
		}
		return NetworkConstants.STANDARD_STRING_SIZE;

	}

	private int cmd_serverinformationRequest(InetAddress ip, int port) {
		byte[] d = new byte[111];
		ByteUtilities.insert(d, getNetworkAdapter().getIdentifier(), 0); // 4
		ByteUtilities.insert(d, ByteUtilities.short2Arr(NetworkConstants.CMD_SERVERINFORMATION_ANSWER), 4);

		ByteUtilities.insert(d, ByteUtilities.boolean2Arr(canConnectNewUser()), 6);
		ByteUtilities.insert(d, ByteUtilities.int2Arr(getUsers().size()), 7);
		ByteUtilities.insert(d, ByteUtilities.int2Arr(getMaxClientCount()), 11);

		ByteUtilities.insert(d, ByteUtilities.shortString2arr(getServerName()), 15);
		ByteUtilities.insert(d, ByteUtilities.shortString2arr(getGameName()), 47);
		ByteUtilities.insert(d, ByteUtilities.shortString2arr(getVersionString()), 79);

		getNetworkAdapter().sendUnconnected(d, ip, port);

		consoleOutput("Information request from  " + ip + ":" + port);

		return 0;
	}

	@Override
	public synchronized void tick() {
		if (tickTiles) {
			for (Tile[] td : tiles) {
				for (Tile t : td) {
					t.tick();
				}
			}
		}

		if (tickEntities) {
			for (int i = 0; i < entities.size(); i++) {
				ServerEntity e = (ServerEntity) entities.get(i);

				if (!e.isRemoved()) {
					e.tick();

					int xtn = (int) (e.getPosX() - e.getRadius().getX()) / getTileWidth();
					int ytn = (int) (e.getPosY() - e.getRadius().getY()) / getTileHeight();
					if (xtn != e.getXto() || ytn != e.getYto()) {
						removeFromEntityMap(e);
						insertToEntityMap(e);
					}

					if (e.isPositionChanged(true)) {
						sendAll(NetworkConstants.CMD_ENTITY_POSITION_CHANGED, e.getSendData_PositionChanged());
					}
					if (e.isAnimationStateChanged(true)) {
						sendAll(NetworkConstants.CMD_ENTITY_ANIMATION_STARTSTOP, e.getSendData_AnimationStateChanged());
					}
					if (e.isAnimationLayerChanged(true)) {
						sendAll(NetworkConstants.CMD_ENTITY_ANIMATION_LAYERCHANGE, e.getSendData_AnimationLayerChanged());
					}
					if (e.isAnimationChanged(true)) {
						// MAL NOCH NIX
					}
				}
				if (e.isRemoved()) {
					entities.remove(i--);
					removeFromEntityMap(e);
					sendAll(NetworkConstants.CMD_ENTITY_REMOVED, getSendData_EntityRemoved(e));
				}
			}
		}

		ticks_since_ping++;

		if (ticks_since_ping >= NetworkConstants.TICKS_BETWEEN_PING) {
			ticks_since_ping = 0;
			sendPings();
		}

		for (int i = 0; i < users.size(); i++) {
			users.get(i).getReliabilityInterface().tick();
		}

		checkTimeOuts();

		sendAllBuffer(); // LAST COMMAND !!!
	}

	private void cmd_UpdateEntityPos(byte[] data, NetworkUser u) {
		long uid = ByteUtilities.arr2Long(data, 0);
		double x = ByteUtilities.arr2double(data, 8);
		double y = ByteUtilities.arr2double(data, 16);

		ServerEntity e = (ServerEntity) getEntity(uid);

		if (e != null) {
			if (e.isControllableByUser(u)) {
				MDPoint eP = new MDPoint(x, y);
				eP.sub(e.getPosition());
				if (e.move(eP.getX(), eP.getY())) {
					// Alles ist gut
				} else {
					getNetworkAdapter().send(NetworkConstants.CMD_ENTITY_FORCE_UPDATE_POSITION, e.getSendData_PositionChanged(), u);
				}
				e.onAfterClientControlMove(eP);
			} else {
				consoleOutput("ERROR: ENTITY NOT CONTROLLABLE ON COMMAND RECIEVE (cmd_UpdateEntity)");
			}
		} else {
			consoleOutput("ERROR: ENTITY NOT FOUND ON COMMAND RECIEVE (cmd_UpdateEntity)");
		}
	}

	private void checkTimeOuts() {
		for (int i = 0; i < users.size(); i++) {
			NetworkUser u = users.get(i);
			if (u.getPing() > NetworkConstants.PING_TIME_OUT) {
				timeOutUser(u);
				consoleOutput("USER '" + u.getName() + "' timed out (" + u.getIP().getHostAddress() + ")");
			}
		}
	}

	private void sendPings() {
		for (NetworkUser u : users) {
			u.sendPing();
		}
	}

	public void registerEntity(Class<? extends ServerEntity> c, int cid) {
		entityRegister.add(new EntityRegister(c, (short) cid));
	}

	public void registerTile(Class<? extends Tile> c, int cid) {
		tileRegister.add(new TileRegister(c, (short) cid));
	}

	public short findClassID(ServerEntity e) {
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

	public void sendAll(short cmd, byte[] data) {
		for (int i = 0; i < users.size(); i++) {
			NetworkUser u = users.get(i);

			serverAdapter.send(cmd, data, u);
		}
	}

	public void sendAllReliable(short cmd, byte[] data) {
		for (int i = 0; i < users.size(); i++) {
			users.get(i).getReliabilityInterface().send(cmd, data);
		}
	}

	@Override
	public void recievePackage(byte[] data, byte packageID, InetAddress ip, int port) {
		NetworkUser u = getUser(ip, port);
		if (u != null) {
			u.recievePackage(packageID, this);
		}
	}

	public synchronized void sendAllBuffer() {
		for (int i = 0; i < users.size(); i++) {
			NetworkUser u = users.get(i);

			if (u.getBuffer().isForceSendByTime() && u.getBuffer().isFilled()) {
				serverAdapter.sendBuffer(u);
			}
		}
	}

	private byte[] getSendData_EntityRemoved(ServerEntity e) {
		byte[] result = new byte[8];

		result = ByteUtilities.insert(result, ByteUtilities.long2Arr(e.getUID()), 0);

		return result;
	}

	public void addEntity(ServerEntity e) {
		super.addEntity(e);
		sendAllReliable(NetworkConstants.CMD_ENTITY_ADDED, e.getSendData_EntityAdded(this));
	}

	@Override
	public ConsoleScreen getOwner() {
		return (ConsoleScreen) owner;
	}

	public void consoleOutput() {
		consoleOutput("");
	}

	public void consoleOutput(int i) {
		consoleOutput(i + "");
	}

	public void consoleOutput(String s) {
		getOwner().consoleOutput(s);
	}

	private void resendMapTo(NetworkUser u) {
		byte[] mapsizedata = new byte[8];
		byte[] singledata = new byte[10];

		u.getReliabilityInterface().send(NetworkConstants.CMD_MAP_INTIALIZATION_START);

		// -------------------- SEND MAP-SIZE (CLEARS THE CLIENT-MAP) --------------------

		mapsizedata = ByteUtilities.insert(mapsizedata, ByteUtilities.int2Arr(getWidth()), 0);
		mapsizedata = ByteUtilities.insert(mapsizedata, ByteUtilities.int2Arr(getHeight()), 4);
		u.getReliabilityInterface().send(NetworkConstants.CMD_SET_MAPSIZE, mapsizedata);

		// -------------------- CLEAR THE CLIENT ENTITYS --------------------

		u.getReliabilityInterface().send(NetworkConstants.CMD_ENTITY_CLEARALL);

		// -------------------- RESENDS ALL ENTITYS --------------------

		for (LevelEntity te : getEntities()) {
			ServerEntity e = (ServerEntity) te;
			u.getReliabilityInterface().send(NetworkConstants.CMD_ENTITY_ADDED, e.getSendData_EntityAdded(this));
		}

		// -------------------- SEND MAP-TILES --------------------

		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				singledata = ByteUtilities.insert(singledata, ByteUtilities.int2Arr(x), 0);
				singledata = ByteUtilities.insert(singledata, ByteUtilities.int2Arr(y), 4);
				singledata = ByteUtilities.insert(singledata, ByteUtilities.short2Arr(findClassID(getTile(x, y))), 8);
				u.getReliabilityInterface().send(NetworkConstants.CMD_SET_TILE, singledata);
			}
		}

		u.getReliabilityInterface().send(NetworkConstants.CMD_MAP_INTIALIZATION_END);

		// -------------------- SEND USERS --------------------

		for (int i = 0; i < users.size(); i++) {
			NetworkUser uv = users.get(i);

			byte[] dta = new byte[8 + NetworkConstants.SMALL_STRING_SIZE];
			dta = ByteUtilities.insert(dta, ByteUtilities.long2Arr(uv.getUID()), 0);
			dta = ByteUtilities.insert(dta, ByteUtilities.shortString2arr(uv.getName()), 8);

			u.getReliabilityInterface().send(NetworkConstants.CMD_ADD_USER, dta);
		}
	}

	private void removeUser(NetworkUser u) {
		users.remove(u);
		
		sendServerChatMessage("User "+u.getName()+" has disconnected");
	}

	private void disconnectUser(NetworkUser u) {
		removeUser(u);
		onUserDisconnected(u);
	}

	private void timeOutUser(NetworkUser u) {
		removeUser(u);
		onUserTimedOut(u);
	}

	public void kickUser(NetworkUser u) {
		getNetworkAdapter().send(NetworkConstants.CMD_USER_KICKED, u);
		getNetworkAdapter().sendBuffer(u);

		removeUser(u);

		consoleOutput("USER '" + u.getName() + "' kicked (" + u.getIP().getHostAddress() + ")");

		onUserKicked(u);
	}

	private NetworkUser getUser(InetAddress ip, int port) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getIP().equals(ip) && users.get(i).getPort() == port) {
				return users.get(i);
			}
		}
		return null;
	}

	public NetworkUser getUser(long uid) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUID() == uid) {
				return users.get(i);
			}
		}
		return null;
	}

	public String getUsername(long uid) {
		if (uid == NetworkConstants.CHAT_BID_SERVERDEBUGMESSAGE) {
			return "SERVER_DEBUG";
		} else if (uid == NetworkConstants.CHAT_BID_SERVERMESSAGE) {
			return "SERVER";
		}
		
		NetworkUser vt = getUser(uid);
		if (vt == null) {
			return "{" + uid + "}";
		} else {
			return vt.getName();
		}
	}

	private synchronized NetworkUser addUser(NetworkUser u) {
		serverAdapter.send(NetworkConstants.CMD_CONNECTION_ACCEPTED, ByteUtilities.long2Arr(u.getUID()), u);

		byte[] data = new byte[8 + NetworkConstants.SMALL_STRING_SIZE];
		data = ByteUtilities.insert(data, ByteUtilities.long2Arr(u.getUID()), 0);
		data = ByteUtilities.insert(data, ByteUtilities.shortString2arr(u.getName()), 8);

		sendAll(NetworkConstants.CMD_ADD_USER, data);
		
		sendServerChatMessage("User "+u.getName()+" has joined");

		users.add(u);

		return u;
	}

	@Override
	public void failRecieveCommand(byte[] data, InetAddress ip, int port) {
		consoleOutput("MESSAGE WITH WRONG IDENTIFIER RECIEVED -> " + ip.getHostAddress());
	}

	@Override
	public void onPackageLoss(int lossCount, NetworkUser u) {
		packagesLost += lossCount;
	}

	public int getLostPackages() {
		return packagesLost;
	}

	public double getPackageSendPerSec() {
		return getNetworkAdapter().getPackageSendPerSec();
	}

	public double getPackageRecievedPerSec() {
		return getNetworkAdapter().getPackageRecievedPerSec();
	}

	@Override
	public void onTilePressed(MouseButtons mouse) {
		//nocode
	}

	@Override
	public void onHUDPressed(MouseButtons mouse) {
		//nocode
	}

	@Override
	public void onPressed(MouseButtons mouse) {
		//nocode
	}

	@Override
	public void onMouseMove(MouseButtons mouse) {
		//nocode
	}

	@Override
	public void render(Surface surface, int xScroll, int yScroll) {
		//nocode
	}

	public ServerAdapter getNetworkAdapter() {
		return serverAdapter;
	}

	public ArrayList<NetworkUser> getUsers() {
		return users;
	}

	@Override
	public NetworkUser getServerUser(InetAddress ip, int port) {
		return getUser(ip, port);
	}

	public void sendChatMessage(long broadcaster, String text) {
		consoleOutput("[" + getUsername(broadcaster) + "]: " + text);
		
		byte[] dt = new byte[8 + NetworkConstants.STANDARD_STRING_SIZE];
		
		dt = ByteUtilities.insert(dt, ByteUtilities.long2Arr(broadcaster), 0);
		dt = ByteUtilities.insert(dt, ByteUtilities.string2arr(text), 8);
		
		sendAll(NetworkConstants.CMD_CHAT_RECIEVE, dt);
	}

	public void sendPrivateChatMessage(long broadcaster, String text, NetworkUser reciever) {
		consoleOutput("[" + getUsername(broadcaster) + "]->[" + reciever.getName() + "]: " + text);
		
		byte[] dt = new byte[8 + NetworkConstants.STANDARD_STRING_SIZE];
		
		dt = ByteUtilities.insert(dt, ByteUtilities.long2Arr(broadcaster), 0);
		dt = ByteUtilities.insert(dt, ByteUtilities.string2arr(text), 8);
		
		getNetworkAdapter().send(NetworkConstants.CMD_CHAT_RECIEVE_PRIVATE, dt, reciever);
	}

	public void sendServerChatMessage(String text) {
		sendChatMessage(NetworkConstants.CHAT_BID_SERVERMESSAGE, text);
	}

	public void sendServerChatDebugMessage(String text) {
		sendChatMessage(NetworkConstants.CHAT_BID_SERVERDEBUGMESSAGE, text);
	}

	// --------------------------------------------------------------------------------------------------------------
	// ------------------------- EVENTS ---------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------

	/**
	 * @return the byte-size of the command
	 */
	public abstract int onCustomClientCommand(byte packageID, int commandID, byte[] data, NetworkUser u);

	/**
	 * @return true if the user is allowed to connect
	 */
	public abstract NetworkUser onUserConnect(String name, InetAddress ip, int port); // return null if not allowed to connect

	public abstract void onAfterUserConnect(NetworkUser u);

	public abstract void onUserTimedOut(NetworkUser u);

	public abstract void onUserKicked(NetworkUser u);

	public abstract void onUserDisconnected(NetworkUser u);

	public abstract String getServerName();

	public abstract String getGameName();

	public abstract String getVersionString();

	public abstract int getMaxClientCount();

	public abstract boolean canConnectNewUser();
}
