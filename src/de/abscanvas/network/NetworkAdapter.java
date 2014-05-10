package de.abscanvas.network;

public class NetworkAdapter {
	private byte[] programmIdentifier;
	private byte[] full_identifier;
	
	// [2 BYTE AbsCanv-ID] [2 BYTE Programm-ID] [1 BYTE Package-ID]    [2 BYTE Command-ID] [... PARAMETER ...] [2 BYTE Command-ID] [... PARAMETER ...]
	public NetworkAdapter(byte[] p_identifier) {
		this.programmIdentifier = p_identifier;
		
		full_identifier = new byte[4];
		full_identifier[0] = NetworkConstants.ABSCANVAS_IDENTIFIER[0];
		full_identifier[1] = NetworkConstants.ABSCANVAS_IDENTIFIER[1];
		full_identifier[2] = programmIdentifier[0];
		full_identifier[3] = programmIdentifier[1];
	}

	public byte[] getIdentifier() {
		return full_identifier;
	}
}
