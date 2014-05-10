package de.abscanvas.level.levelIO;

import java.util.ArrayList;

public class LevelReader {

	private ArrayList<LIOMethodDefinition> methodDefinition = new ArrayList<LIOMethodDefinition>();
	private ArrayList<LIOConstantDefinition> constantDefinition = new ArrayList<LIOConstantDefinition>();
	
	private ArrayList<LIOReadOnlyMethod> methods = new ArrayList<LIOReadOnlyMethod>();

	private String programmName;
	
	public LevelReader() {

	}

	/**
	 * @param unparsed unparsed text / file-content
	 * @param programmName programmName for Checking (null means no checking)
	 * @return an iterator over all Methods
	 * @throws LIOParsingException on Parsing failure
	 */
	public LevelReaderIterator parse(String unparsed, String progName) throws LIOParsingException{
		String[] alllines = unparsed.split("\n");

		ArrayList<String> lines = trimLines(alllines);

		lines = parseProgrammName(lines);
		lines = parseConstantDefinitions(lines);
		lines = parseMethodDefinitions(lines);
		
		parseMethods(lines); // Nurnoch Methods übrig ...
		
		if (progName != null) {
			if (! progName.equals(programmName)) {
				throw new LIOParsingException("Programm-Name unequals Name in File");
			}
		}

		return new LevelReaderIterator(methods, methods.size());
	}
	
	public String getProgrammName() {
		return programmName;
	}
	
	private ArrayList<String> parseProgrammName(ArrayList<String> lines) throws LIOParsingException {
		ArrayList<String> result = new ArrayList<String>();

		programmName = null;
		
		for (String ln : lines) {
			boolean consumed = false;
			
			if (ln.startsWith(LIODefinitions.KEYWORD_START)) {
				String n = ln.substring(LIODefinitions.KEYWORD_START.length());
				programmName = getValue_s(n);
				consumed = true;
			}
			
			if (! consumed) {
				result.add(ln);
			}
		}

		return result;
	}

	private ArrayList<String> trimLines(String[] lines) {
		ArrayList<String> result = new ArrayList<String>();

		for (String ln : lines) {
			String lnt = ln.trim();

			if ((!lnt.isEmpty()) && (!lnt.startsWith(LIODefinitions.COMMENT_START))) {
				result.add(lnt);
			}
		}

		return result;
	}

	private ArrayList<String> parseConstantDefinitions(ArrayList<String> lines) throws LIOParsingException {
		ArrayList<String> result = new ArrayList<String>();

		for (String ln : lines) {
			boolean consumed = false;
			
			if (ln.startsWith(LIODefinitions.KEYWORD_DEFINE)) {
				String[] splitted = ln.split(" ");
				if (splitted.length < 2)
					throw new LIOParsingException("Cant parse " + ln);

				if (splitted[1].equals(LIODefinitions.KEYWORD_CONSTANT)) {
					if (splitted.length != 4)
						throw new LIOParsingException("Cant parse " + ln);
					
					int type = parseParameterTypeList(splitted[2])[0];
					String[] resplit = splitted[3].split(LIODefinitions.ASSIGN_OPERATOR);

					if (resplit.length != 2)
						throw new LIOParsingException("Cant parse " + ln);

					String representation = resplit[0];

					switch (type) {
					case LIODefinitions.TYPE_STRING:
						constantDefinition.add(new LIOConstantDefinition(representation, getValue_s(resplit[1])));
						break;
					case LIODefinitions.TYPE_INT:
						constantDefinition.add(new LIOConstantDefinition(representation, getValue_i(resplit[1])));
						break;
					case LIODefinitions.TYPE_DOUBLE:
						constantDefinition.add(new LIOConstantDefinition(representation, getValue_d(resplit[1])));
						break;
					case LIODefinitions.TYPE_BOOLEAN:
						constantDefinition.add(new LIOConstantDefinition(representation, getValue_b(resplit[1])));
						break;
					}
					
					consumed = true;
				}
			}
			
			if (! consumed) {
				result.add(ln);
			}
		}

		return result;
	}

	private ArrayList<String> parseMethodDefinitions(ArrayList<String> lines) throws LIOParsingException {
		ArrayList<String> result = new ArrayList<String>();

		for (String ln : lines) {
			boolean consumed = false;
			
			if (ln.startsWith(LIODefinitions.KEYWORD_DEFINE)) {
				String[] splitted = ln.split(" ");
				if (splitted.length < 2)
					throw new LIOParsingException("Cant parse " + ln);

				if (splitted[1].equals(LIODefinitions.KEYWORD_METHOD)) {
					if (splitted.length != 3)
						throw new LIOParsingException("Cant parse " + ln);
					
					String[] resplit = splitted[2].split(LIODefinitions.ASSIGN_OPERATOR);

					if (resplit.length != 2)
						throw new LIOParsingException("Cant parse " + ln);

					String representation = resplit[0];

					methodDefinition.add(new LIOMethodDefinition(representation, getValue_i(resplit[1])));
					
					consumed = true;
				}
			}
			
			if (! consumed) {
				result.add(ln);
			}
		}

		return result;
	}
	
	private void parseMethods(ArrayList<String> lines) throws LIOParsingException {
		for (String ln : lines) {			
			String[] splitted = splitMethod(ln);
			
			int name = findMethodDefinition(splitted[0]);
			
			int[] types = parseParameterTypeList(splitted[1]);
			
			LIOMethod meth = new LIOMethod(name);
			
			int pos = 0;
			for(int tp : types) {
				switch (tp) {
				case LIODefinitions.TYPE_STRING:
					String cds = findConstantDefinition_s(getParameter_u(pos, splitted[2]));
					if (cds != null) {
						meth.addStringParameter(cds);
					} else {
						meth.addStringParameter(getParameter_s(pos, splitted[2]));
					}
					break;
				case LIODefinitions.TYPE_INT:
					Integer cdi = findConstantDefinition_i(getParameter_u(pos, splitted[2]));
					if (cdi != null) {
						meth.addIntegerParameter(cdi);
					} else {
						meth.addIntegerParameter(getParameter_i(pos, splitted[2]));
					}
					break;
				case LIODefinitions.TYPE_DOUBLE:
					Double cdd = findConstantDefinition_d(getParameter_u(pos, splitted[2]));
					if (cdd != null) {
						meth.addDoubleParameter(cdd);
					} else {
						meth.addDoubleParameter(getParameter_d(pos, splitted[2]));
					}
					break;
				case LIODefinitions.TYPE_BOOLEAN:
					Boolean cdb = findConstantDefinition_b(getParameter_u(pos, splitted[2]));
					if (cdb != null) {
						meth.addBooleanParameter(cdb);
					} else {
						meth.addBooleanParameter(getParameter_b(pos, splitted[2]));
					}
					break;
				}
				pos++;
			}
			
			methods.add(new LIOReadOnlyMethod(meth));
		}
	}
	
	private String getParameter_u(int index, String unparsed) throws LIOParsingException {
		if (!(unparsed.startsWith(LIODefinitions.PARAMETER_START) && unparsed.endsWith(LIODefinitions.PARAMETER_END)))
			throw new LIOParsingException("Cant parse Parameter " + unparsed);
		
		unparsed = unparsed.substring(1, unparsed.length() - 1);
		
		String[] args = unparsed.split(",");
		if (args.length <= index) throw new LIOParsingException("Too less Parameter (cant find nmbr " + index + ") " + unparsed);
		
		return args[index].trim();
	}
	
	private String getParameter_s(int index, String unparsed) throws LIOParsingException {
		String s = getParameter_u(index, unparsed);
		
		return getValue_s(s);
	}
	
	private int getParameter_i(int index, String unparsed) throws LIOParsingException {
		String s = getParameter_u(index, unparsed);
		
		return getValue_i(s);
	}
	
	private double getParameter_d(int index, String unparsed) throws LIOParsingException {
		String s = getParameter_u(index, unparsed);
		
		return getValue_d(s);
	}
	
	private boolean getParameter_b(int index, String unparsed) throws LIOParsingException {
		String s = getParameter_u(index, unparsed);
		
		return getValue_b(s);
	}
	
	private int findMethodDefinition(String s) throws LIOParsingException {
		s = s.trim();
		
		for (LIOMethodDefinition md : methodDefinition) {
			if (md.getRepresentation().equals(s)) {
				return md.getDefinition();
			}
		}
		
		try {
			int result = Integer.parseInt(s);
			return result;
		} catch (NumberFormatException e) {
			throw new LIOParsingException("Cant parse Method-Definition to int: " + s);
		}
	}
	
	private String findConstantDefinition_s(String u) {
		u = u.trim();
		
		for(LIOConstantDefinition cd : constantDefinition) {
			if (cd.getRepresentation().equals(u) && cd.getType() == LIODefinitions.TYPE_STRING) {
				return cd.getDefinition_s();
			}
		}
		return null;
	}
	
	private Integer findConstantDefinition_i(String u) {
		u = u.trim();
		
		for(LIOConstantDefinition cd : constantDefinition) {
			if (cd.getRepresentation().equals(u) && cd.getType() == LIODefinitions.TYPE_INT) {
				return cd.getDefinition_i();
			}
		}
		return null;
	}
	
	private Double findConstantDefinition_d(String u) {
		u = u.trim();
		
		for(LIOConstantDefinition cd : constantDefinition) {
			if (cd.getRepresentation().equals(u) && cd.getType() == LIODefinitions.TYPE_DOUBLE) {
				return cd.getDefinition_d();
			}
		}
		return null;
	}
	
	private Boolean findConstantDefinition_b(String u) {
		u = u.trim();
		
		for(LIOConstantDefinition cd : constantDefinition) {
			if (cd.getRepresentation().equals(u) && cd.getType() == LIODefinitions.TYPE_BOOLEAN) {
				return cd.getDefinition_b();
			}
		}
		return null;
	}
	
	private String[] splitMethod(String m) throws LIOParsingException {
		String[] result = new String[3];
		
		int tds = m.indexOf(LIODefinitions.TYPEDEF_START, 0);
		int pds = m.indexOf(LIODefinitions.PARAMETER_START, tds);
		
		if (tds < 0 || pds < 0) throw new LIOParsingException("Cant parse Method: " + m);
		
		result[0] = m.substring(0, tds);
		result[1] = m.substring(tds, pds);
		result[2] = m.substring(pds);
		
		return result;
	}

	private int[] parseParameterTypeList(String list) throws LIOParsingException {
		list = list.trim();

		if (!(list.startsWith(LIODefinitions.TYPEDEF_START) && list.endsWith(LIODefinitions.TYPEDEF_END)))
			throw new LIOParsingException("Cant parse TypeParamList: " + list);

		int[] result = new int[list.length() - 2];
		for (int i = 1; i < list.length() - 1; i++) {
			result[i - 1] = getTypeIdentifierByChar(list.charAt(i));
		}
		return result;
	}

	private int getTypeIdentifierByChar(char c) throws LIOParsingException {
		switch (c) {
		case LIODefinitions.TYPE_ID_STRING:
			return LIODefinitions.TYPE_STRING;
		case LIODefinitions.TYPE_ID_INT:
			return LIODefinitions.TYPE_INT;
		case LIODefinitions.TYPE_ID_DOUBLE:
			return LIODefinitions.TYPE_DOUBLE;
		case LIODefinitions.TYPE_ID_BOOLEAN:
			return LIODefinitions.TYPE_BOOLEAN;
		default:
			throw new LIOParsingException("Cant find TypeIdentifier: " + c);
		}
	}

	private String getValue_s(String in) throws LIOParsingException {
		in = in.trim();

		if (!(in.startsWith(LIODefinitions.STRING_START) && in.endsWith(LIODefinitions.STRING_END)))
			throw new LIOParsingException("String does not start/end with \" : " + in);

		try {
			String s = in.substring(1, in.length() - 1);
			return s;
		} catch (IndexOutOfBoundsException e) {
			throw new LIOParsingException("String too short: " + in);
		}
	}

	private int getValue_i(String in) throws LIOParsingException {
		in = in.trim();

		try {
			int i = Integer.parseInt(in);
			return i;
		} catch (NumberFormatException e) {
			throw new LIOParsingException("Cant parse Integer: " + in);
		}
	}

	private double getValue_d(String in) throws LIOParsingException {
		in = in.trim();

		try {
			double d = Double.parseDouble(in);
			return d;
		} catch (NumberFormatException e) {
			throw new LIOParsingException("Cant parse Double: " + in);
		}
	}

	private boolean getValue_b(String in) throws LIOParsingException {
		in = in.trim();

		return LIODefinitions.strToBool(in);
	}
}
