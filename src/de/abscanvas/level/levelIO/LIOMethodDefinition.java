package de.abscanvas.level.levelIO;

public class LIOMethodDefinition {
	private final String representation;
	private final int definition;
	
	public LIOMethodDefinition(String rep, int def) {
		definition = def;
		representation = rep;
	}

	public String getStringRepresentation() {
		return LIODefinitions.KEYWORD_DEFINE + " " + LIODefinitions.KEYWORD_METHOD + " " + representation + LIODefinitions.ASSIGN_OPERATOR + definition;
	}
	
	public int getDefinition() {
		return definition;
	}
	
	public String getRepresentation() {
		return representation;
	}
}
