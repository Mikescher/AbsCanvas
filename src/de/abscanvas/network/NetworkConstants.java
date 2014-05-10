package de.abscanvas.network;

public class NetworkConstants {
	
	// ------------------------------------------------------------------------
	// ------------------------------ GENERAL ---------------------------------
	// ------------------------------------------------------------------------
	
	public final static int MIN_PORT_NUMBER = 1;
	public final static int MAX_PORT_NUMBER = 49151;
	
	public final static int MIN_PORTSCAN_NUMBER = 8700;
	public final static int MAX_PORTSCAN_NUMBER = 8800;

	public final static byte[] ABSCANVAS_IDENTIFIER = { 87, 43 };
	public final static int PACKAGE_SIZE = 512;
	
	public final static int STANDARD_STRING_SIZE = 128; // = 128 Chars / 128 Bytes
	public final static int SMALL_STRING_SIZE = 32; // = 32 Chars / 32 Bytes

	public final static int SEND_SLEEP = 10;  // in ms
	public final static long MAX_BUFFER_WAITTIME = 32; // in ms
	
	public final static int TICKS_BETWEEN_PING = 15; // 250 ms
	
	public final static int PING_TIME_OUT = 10000; // in ms = 10s
	public final static int NETWORKSCANNER_TIME_OUT = 60000; // in ms = 60s
	
	public final static int PING_LEVEL_VERYGOOD = 80; 				// 0ms - 80ms
	public final static int PING_LEVEL_GOOD = 160; 					// 80ms - 160ms
	public final static int PING_LEVEL_MEDIUM = 260; 				// 160ms - 260ms
	public final static int PING_LEVEL_BAD = 360; 					// 260ms - 360ms
	public final static int PING_LEVEL_VERYBAD = 512;				// 512ms - 999 ms
	public final static int PING_LEVEL_DEAD = 999;					// 999+ ms

	// ------------------------------------------------------------------------
	// -------------------------- SERVER <> CLIENT ----------------------------
	// ------------------------------------------------------------------------
	
	public final static short CMD_BUFFER_END = 7;
	public final static short CMD_NO_COMMAND = 1;
	
	public final static short CMD_RELIEABLE_PACKAGE_SEND = 51;
	public final static short CMD_RELIEABLE_PACKAGE_CONFIRMED = 52;
	
	// ------------------------------------------------------------------------
	// -------------------------- SERVER -> CLIENT ----------------------------
	// ------------------------------------------------------------------------

	public final static short CMD_CONNECTION_ACCEPTED = 13;
	public final static short CMD_CONNECTION_REFUSED = 14;
	public final static short CMD_USER_KICKED = 15;

	public final static short CMD_ENTITY_POSITION_CHANGED = 21;
	public final static short CMD_ENTITY_ANIMATION_CHANGED = 22;
	public final static short CMD_ENTITY_ADDED = 23;
	public final static short CMD_ENTITY_REMOVED = 24;
	public final static short CMD_ENTITY_CLEARALL = 25;
	public final static short CMD_ENTITY_ANIMATION_STARTSTOP = 26;
	public final static short CMD_ENTITY_ANIMATION_LAYERCHANGE = 27;
	public final static short CMD_ENTITY_FORCE_UPDATE_POSITION = 29;

	public final static short CMD_MAP_INTIALIZATION_START = 31;
	public final static short CMD_TILE_UPDATED = 32;
	public final static short CMD_SET_MAPSIZE = 33;
	public final static short CMD_SET_TILE = 34;
	public final static short CMD_MAP_INTIALIZATION_END = 35;
	
	public final static short CMD_PING_REQUEST = 40; // First ping part
	public final static short CMD_PING_CONFIRM = 42; // Third ping part
	
	public final static short CMD_SERVERINFORMATION_ANSWER = 62;
	public final static short CMD_UNCONNECTED_PING_ANSWER = 64;
	
	public final static short CMD_UPDATE_USERNAME = 80;
	public final static short CMD_ADD_USER = 81;
	
	public final static short CMD_CHAT_RECIEVE = 92;
	public final static short CMD_CHAT_RECIEVE_PRIVATE = 93;

	// ------------------------------------------------------------------------
	// -------------------------- SERVER <- CLIENT ----------------------------
	// ------------------------------------------------------------------------

	public final static short CMD_USER_CONNECT = 11;
	public final static short CMD_USER_DISCONNECT = 12;

	public final static short CMD_MAP_REQUEST = 30;
	public final static short CMD_INTIALIZATION_SUCCESFULL = 36;
	
	public final static short CMD_PING_ANSWER = 41; // Second ping part
	
	public final static short CMD_ENTITY_UPDATE_POSITION = 28; // Clientcontrollable Entity
	
	public final static short CMD_SERVERINFORMATION_REQUEST = 61;
	public final static short CMD_UNCONNECTED_PING_REQUEST = 63;
	
	public final static short CMD_ENTITY_ISALIVE_REQUEST = 71;
	
	public final static short CMD_CHAT_SEND = 90;
	public final static short CMD_CHAT_SEND_PRIVATE = 91;
	
	// ------------------------------------------------------------------------
	// -------------------------- CHAT ----------------------------------------
	// ------------------------------------------------------------------------
	
	// 0 - MAX-LONG -> NetworkUser
	public final static long CHAT_BID_UNKNOWN = -1;
	public final static long CHAT_BID_SERVERMESSAGE = -100;
	public final static long CHAT_BID_LOCALMESSAGE = -101;
	public final static long CHAT_BID_SERVERDEBUGMESSAGE = -102;
	public final static long CHAT_BID_LOCALDEBUGMESSAGE = -103;
	
	public final static int CHAT_HISTORY_SIZE = 64;

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------

	public static String cmd2String(int i) {
		switch (i) {
		case CMD_USER_CONNECT:
			return "USER_CONNECT";
		case CMD_NO_COMMAND:
			return "NO_COMMAND";
		case CMD_MAP_INTIALIZATION_START:
			return "MAP_INTIALIZATION_START";
		case CMD_MAP_INTIALIZATION_END:
			return "MAP_INTIALIZATION_END";
		case CMD_INTIALIZATION_SUCCESFULL:
			return "INTIALIZATION_SUCCESFULL";
		case CMD_BUFFER_END:
			return "BUFFER_END";
		case CMD_USER_DISCONNECT:
			return "USER_DISCONNECT";
		case CMD_CONNECTION_ACCEPTED:
			return "CONNECTION_ACCEPTED";
		case CMD_CONNECTION_REFUSED:
			return "CONNECTION_REFUSED";
		case CMD_ENTITY_POSITION_CHANGED:
			return "ENTITY_POS_CHANGED";
		case CMD_ENTITY_ANIMATION_CHANGED:
			return "ENTITY_ANIMATION_CHANGED";
		case CMD_ENTITY_ADDED:
			return "ENTITY_ADDED";
		case CMD_ENTITY_REMOVED:
			return "ENTITY_REMOVED";
		case CMD_ENTITY_CLEARALL:
			return "ENTITYS_CLEAR";
		case CMD_ENTITY_ANIMATION_STARTSTOP:
			return "ENTITY_ANIMATION_START/STOP";
		case CMD_ENTITY_ANIMATION_LAYERCHANGE:
			return "ENTITY_ANIMATION_LAYERCHANGE";
		case CMD_MAP_REQUEST:
			return "REQUEST_MAP";
		case CMD_TILE_UPDATED:
			return "UPDATE_TILES";
		case CMD_SET_MAPSIZE:
			return "MAPSIZE_SET";
		case CMD_SET_TILE:
			return "SET_TILE";
		case CMD_USER_KICKED:
			return "USER_KICKED";
		case CMD_PING_ANSWER:
			return "PING_ANSWER";
		case CMD_PING_REQUEST:
			return "PING_REQUEST";
		case CMD_PING_CONFIRM:
			return "PING_CONFIRM";
		case CMD_RELIEABLE_PACKAGE_SEND:
			return "RELIEABLE_PACKAGE_SEND";
		case CMD_RELIEABLE_PACKAGE_CONFIRMED:
			return "RELIEABLE_PACKAGE_CONFIRMED";
		case CMD_ENTITY_UPDATE_POSITION:
			return "ENTITY_UPDATE_POSITION";
		case CMD_ENTITY_FORCE_UPDATE_POSITION:
			return "ENTITY_FORCE_UPDATE_POSITION";
		case CMD_SERVERINFORMATION_REQUEST:
			return "SERVERINFORMATION_REQUEST";
		case CMD_SERVERINFORMATION_ANSWER:
			return "SERVERINFORMATION_ANSWER";
		case CMD_UNCONNECTED_PING_REQUEST:
			return "UNCONNECTED_PING_REQUEST";
		case CMD_UNCONNECTED_PING_ANSWER:
			return "UNCONNECTED_PING_ANSWER";
		case CMD_ENTITY_ISALIVE_REQUEST:
			return "ENTITY_ISALIVE_REQUEST";
		case CMD_UPDATE_USERNAME:
			return "UPDATE_USERNAME";
		case CMD_ADD_USER:
			return "ADD_USER";
		case CMD_CHAT_RECIEVE:
			return "CHAT_RECIEVE";
		case CMD_CHAT_RECIEVE_PRIVATE:
			return "RECIEVE_PRIVATE";
		case CMD_CHAT_SEND:
			return "CHAT_SEND";
		case CMD_CHAT_SEND_PRIVATE:
			return "CHAT_SEND_PRIVATE";
		default:
			return "CMD NOT FOUND ("+i+")";
		}
	}
}
