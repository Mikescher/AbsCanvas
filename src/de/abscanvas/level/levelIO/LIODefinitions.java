package de.abscanvas.level.levelIO;

public class LIODefinitions {
	public final static int TYPE_STRING  = 1;
	public final static int TYPE_INT     = 2;
	public final static int TYPE_DOUBLE  = 3;
	public final static int TYPE_BOOLEAN = 4;
	
	public final static char TYPE_ID_STRING  = 'S';
	public final static char TYPE_ID_INT     = 'I';
	public final static char TYPE_ID_DOUBLE  = 'D';
	public final static char TYPE_ID_BOOLEAN = 'B';
	
	public final static String BOOL_TRUE = "true";
	public final static String BOOL_FALSE = "false";
	
	public final static String KEYWORD_START = "programm";
	public final static String KEYWORD_DEFINE = "#define";
	public final static String KEYWORD_METHOD = "[METHOD]";
	public final static String KEYWORD_CONSTANT = "[CONSTANT]";
	public final static String DELIMITER = ",";
	public final static String TYPEDEF_START = "[";
	public final static String TYPEDEF_END = "]";
	public final static String PARAMETER_START = "(";
	public final static String PARAMETER_END = ")";
	public final static String ASSIGN_OPERATOR = "=>";
	public final static String LINEBREAK = "\n";
	public final static String STRING_START = "\"";
	public final static String STRING_END = "\"";
	public static final String COMMENT_START = "//";
	
	public static String boolToStr(boolean b) {
		if (b) {
			return LIODefinitions.BOOL_TRUE;
		} else {
			return LIODefinitions.BOOL_FALSE;
		}
	}
	
	public static boolean strToBool(String s) throws LIOParsingException {
		if (s.equals(BOOL_TRUE)) {
			return true;
		} else if (s.equals(BOOL_FALSE)) {
			return false;
		} else {
			throw new LIOParsingException("Cant parse Boolean: " + s);
		}
	}
}
