package de.abscanvas.level.levelIO;

public class LIOConstantDefinition {
	private final static String STANDARD_DEF_STRING = "WRONG_TYPE";
	private final static int STANDARD_DEF_INT = -1;
	private final static double STANDARD_DEF_DOUBLE = -1;
	private final static boolean STANDARD_DEF_BOOL = false;
	
	private final String representation;
	
	private final int type;
	
	private final String definition_s;
	private final int definition_i;
	private final double definition_d;
	private final boolean definition_b;
	
	public LIOConstantDefinition(String rep, String def) {
		definition_s = def;
		definition_i = STANDARD_DEF_INT;
		definition_d = STANDARD_DEF_DOUBLE;
		definition_b = STANDARD_DEF_BOOL;
		
		representation = rep;
		type = LIODefinitions.TYPE_STRING;
	}
	
	public LIOConstantDefinition(String rep, int def) {
		definition_s = STANDARD_DEF_STRING;
		definition_i = def;
		definition_d = STANDARD_DEF_DOUBLE;
		definition_b = STANDARD_DEF_BOOL;
		
		representation = rep;
		type = LIODefinitions.TYPE_INT;
	}
	
	public LIOConstantDefinition(String rep, double def) {
		definition_s = STANDARD_DEF_STRING;
		definition_i = STANDARD_DEF_INT;
		definition_d = def;
		definition_b = STANDARD_DEF_BOOL;
		
		representation = rep;
		type = LIODefinitions.TYPE_DOUBLE;
	}
	
	public LIOConstantDefinition(String rep, boolean def) {
		definition_s = STANDARD_DEF_STRING;
		definition_i = STANDARD_DEF_INT;
		definition_d = STANDARD_DEF_DOUBLE;
		definition_b = def;
		
		representation = rep;
		type = LIODefinitions.TYPE_BOOLEAN;
	}

	public String getStringRepresentation() {
		String data = "";
		
		data += LIODefinitions.KEYWORD_DEFINE + " " + LIODefinitions.KEYWORD_CONSTANT + " " + LIODefinitions.TYPEDEF_START;
		data += getDefinitionTypeIdentifier();
		data += LIODefinitions.TYPEDEF_END + " " + representation + LIODefinitions.ASSIGN_OPERATOR + getDefinition();
		
		return data;
	}
	
	public int getType() {
		return type;
	}
	
	public String getDefinition_s() {
		return definition_s;
	}
	
	public int getDefinition_i() {
		return definition_i;
	}
	
	public double getDefinition_d() {
		return definition_d;
	}
	
	public boolean getDefinition_b() {
		return definition_b;
	}
	
	public String getDefinition() {
		switch(getType()) {
		case LIODefinitions.TYPE_STRING:
			return LIODefinitions.STRING_START + getDefinition_s() + LIODefinitions.STRING_END;
		case LIODefinitions.TYPE_INT:
			return "" + getDefinition_i();
		case LIODefinitions.TYPE_DOUBLE:
			return "" + getDefinition_d();
		case LIODefinitions.TYPE_BOOLEAN:
			return LIODefinitions.boolToStr(getDefinition_b());
		default:
			return "<?>";
		}
	}
	
	public char getDefinitionTypeIdentifier() {
		switch(getType()) {
		case LIODefinitions.TYPE_STRING:
			return LIODefinitions.TYPE_ID_STRING;
		case LIODefinitions.TYPE_INT:
			return LIODefinitions.TYPE_ID_INT;
		case LIODefinitions.TYPE_DOUBLE:
			return LIODefinitions.TYPE_ID_DOUBLE;
		case LIODefinitions.TYPE_BOOLEAN:
			return LIODefinitions.TYPE_ID_BOOLEAN;
		default:
			return '#';
		}
	}

	public String getRepresentation() {
		return representation;
	}
}
