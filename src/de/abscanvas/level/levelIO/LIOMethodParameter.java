package de.abscanvas.level.levelIO;

public class LIOMethodParameter {	
	private final static String STANDARD_DEF_STRING = "WRONG_TYPE";
	private final static int STANDARD_DEF_INT = -1;
	private final static double STANDARD_DEF_DOUBLE = -1;
	private final static boolean STANDARD_DEF_BOOL = false;
	
	private final int type;
	
	private final boolean useDefinition;
	
	private String val_s;
	private final int val_i;
	private final double val_d;
	private final boolean val_b;
	
	public LIOMethodParameter(String s) {
		type = LIODefinitions.TYPE_STRING;
		
		val_s = s;
		val_i = STANDARD_DEF_INT;
		val_d = STANDARD_DEF_DOUBLE;
		val_b = STANDARD_DEF_BOOL;
		
		useDefinition = true;
	}

	public LIOMethodParameter(int i) {
		type = LIODefinitions.TYPE_INT;
		
		val_s = STANDARD_DEF_STRING;
		val_i = i;
		val_d = STANDARD_DEF_DOUBLE;
		val_b = STANDARD_DEF_BOOL;
		
		useDefinition = true;
	}
	
	public LIOMethodParameter(double d) {
		type = LIODefinitions.TYPE_DOUBLE;
		
		val_s = STANDARD_DEF_STRING;
		val_i = STANDARD_DEF_INT;
		val_d = d;
		val_b = STANDARD_DEF_BOOL;
		
		useDefinition = true;
	}
	
	public LIOMethodParameter(boolean b) {
		type = LIODefinitions.TYPE_BOOLEAN;
		
		val_s = STANDARD_DEF_STRING;
		val_i = STANDARD_DEF_INT;
		val_d = STANDARD_DEF_DOUBLE;
		val_b = b;
		
		useDefinition = true;
	}
	
	public LIOMethodParameter(String s, boolean useDef) {
		type = LIODefinitions.TYPE_STRING;
		
		val_s = s;
		val_i = STANDARD_DEF_INT;
		val_d = STANDARD_DEF_DOUBLE;
		val_b = STANDARD_DEF_BOOL;
		
		useDefinition = useDef;
	}

	public LIOMethodParameter(int i, boolean useDef) {
		type = LIODefinitions.TYPE_INT;
		
		val_s = STANDARD_DEF_STRING;
		val_i = i;
		val_d = STANDARD_DEF_DOUBLE;
		val_b = STANDARD_DEF_BOOL;
		
		useDefinition = useDef;
	}
	
	public LIOMethodParameter(double d, boolean useDef) {
		type = LIODefinitions.TYPE_DOUBLE;
		
		val_s = STANDARD_DEF_STRING;
		val_i = STANDARD_DEF_INT;
		val_d = d;
		val_b = STANDARD_DEF_BOOL;
		
		useDefinition = useDef;
	}
	
	public LIOMethodParameter(boolean b, boolean useDef) {
		type = LIODefinitions.TYPE_BOOLEAN;
		
		val_s = STANDARD_DEF_STRING;
		val_i = STANDARD_DEF_INT;
		val_d = STANDARD_DEF_DOUBLE;
		val_b = b;
		
		useDefinition = useDef;
	}
	
	public int getType() {
		return type;
	}
	
	public String getValue_s() {
		return val_s;
	}
	
	public int getValue_i() {
		return val_i;
	}
	
	public double getValue_d() {
		return val_d;
	}
	
	public boolean getValue_b() {
		return val_b;
	}

	public String getValue(LevelWriter w) {
		if (isUseDefinition()) {
			String cDef = null;
			switch (getType()) {
			case LIODefinitions.TYPE_STRING:
				cDef = w.findConstantDefinition_s(getValue_s());
				break;
			case LIODefinitions.TYPE_INT:
				cDef = w.findConstantDefinition_i(getValue_i());
				break;
			case LIODefinitions.TYPE_DOUBLE:
				cDef = w.findConstantDefinition_d(getValue_d());
				break;
			case LIODefinitions.TYPE_BOOLEAN:
				cDef = w.findConstantDefinition_b(getValue_b());
				break;
			}
			if (cDef != null) {
				return cDef;
			}
		}
		
		switch (getType()) {
		case LIODefinitions.TYPE_STRING:
			return LIODefinitions.STRING_START + getValue_s() + LIODefinitions.STRING_END;
		case LIODefinitions.TYPE_INT:
			return "" + getValue_i();
		case LIODefinitions.TYPE_DOUBLE:
			return "" + getValue_d();
		case LIODefinitions.TYPE_BOOLEAN:
			return LIODefinitions.boolToStr(getValue_b());
		default:
			return "<?>";
		}
	}

	public char getValueTypeIdentifier() {
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
	
	public boolean isUseDefinition() {
		return useDefinition;
	}
}
