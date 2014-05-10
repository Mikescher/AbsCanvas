package de.abscanvas.level.levelIO;

import java.util.ArrayList;

import de.abscanvas.Screen;

public class LevelWriter {
	private ArrayList<LIOMethodDefinition> methodDefinition = new ArrayList<LIOMethodDefinition>();
	private ArrayList<LIOConstantDefinition> constantDefinition = new ArrayList<LIOConstantDefinition>();
	private ArrayList<LIOMethod> methods = new ArrayList<LIOMethod>();
	
	private String programmName;
	
	public LevelWriter() {
		// stub
	}

	public boolean addMethodDefinition(String representation, int definition) {
		if (hasMethodDefinition(definition)) return false;
		methodDefinition.add(new LIOMethodDefinition(representation, definition));
		return true;
	}
	
	public void addConstantDefinition(String representation, int definition) {
		constantDefinition.add(new LIOConstantDefinition(representation, definition));
	}
	
	public void addMethod(LIOMethod method) {
		methods.add(method);
	}
	
	public void setProgrammName(String name) {
		programmName = name;
	}
	
	public String findMethodDefinition(int def) {
		for(LIOMethodDefinition d : methodDefinition) {
			if (d.getDefinition() == def) return d.getRepresentation();
		}
		return "" + def;
	}
	
	private boolean hasMethodDefinition(int def) {
		for(LIOMethodDefinition d : methodDefinition) {
			if (d.getDefinition() == def) return true;
		}
		return false;
	}

	public String findConstantDefinition_s(String value_s) {
		for (LIOConstantDefinition cd : constantDefinition) {
			if (cd.getType() == LIODefinitions.TYPE_STRING) {
				if (cd.getDefinition_s() == value_s) return cd.getRepresentation();
			}
		}
		return null;
	}

	public String findConstantDefinition_i(int value_i) {
		for (LIOConstantDefinition cd : constantDefinition) {
			if (cd.getType() == LIODefinitions.TYPE_INT) {
				if (cd.getDefinition_i() == value_i) return cd.getRepresentation();
			}
		}
		return null;
	}

	public String findConstantDefinition_d(double value_d) {
		for (LIOConstantDefinition cd : constantDefinition) {
			if (cd.getType() == LIODefinitions.TYPE_DOUBLE) {
				if (cd.getDefinition_d() == value_d) return cd.getRepresentation();
			}
		}
		return null;
	}

	public String findConstantDefinition_b(boolean value_b) {
		for (LIOConstantDefinition cd : constantDefinition) {
			if (cd.getType() == LIODefinitions.TYPE_BOOLEAN) {
				if (cd.getDefinition_b() == value_b) return cd.getRepresentation();
			}
		}
		return null;
	}
	
	public String getFileString() {
		String data = "";
		
		data += "// absCanvas v" + Screen.VERSION + " generated LevelFile" + LIODefinitions.LINEBREAK;
		data += "// Programm: " + programmName + LIODefinitions.LINEBREAK;
		data += "// Method-Definitions: " + methodDefinition.size() + LIODefinitions.LINEBREAK;
		data += "// Constant-Definitions: " + constantDefinition.size() + LIODefinitions.LINEBREAK;
		data += "// Methods: " + methods.size() + LIODefinitions.LINEBREAK;
		
		data += LIODefinitions.LINEBREAK;
		data += LIODefinitions.LINEBREAK;
		
		data += LIODefinitions.KEYWORD_START + " " + LIODefinitions.STRING_START + programmName + LIODefinitions.STRING_END + LIODefinitions.LINEBREAK;
		
		data += LIODefinitions.LINEBREAK;
		data += LIODefinitions.LINEBREAK;
		
		data += "//  ------------------------  Constant Definitions:  ------------------------  " + LIODefinitions.LINEBREAK;
		data += LIODefinitions.LINEBREAK;
		for(LIOConstantDefinition cd : constantDefinition) {
			data += cd.getStringRepresentation() + LIODefinitions.LINEBREAK;
		}
		
		data += LIODefinitions.LINEBREAK;
		data += LIODefinitions.LINEBREAK;
		
		data += "//  ------------------------  Method Definitions:  ------------------------  " + LIODefinitions.LINEBREAK;
		data += LIODefinitions.LINEBREAK;
		for(LIOMethodDefinition md : methodDefinition) {
			data += md.getStringRepresentation() + LIODefinitions.LINEBREAK;
		}
		
		data += LIODefinitions.LINEBREAK;
		data += LIODefinitions.LINEBREAK;
		
		data += "//  ------------------------  Methods:  ------------------------  " + LIODefinitions.LINEBREAK;
		data += LIODefinitions.LINEBREAK;
		for (LIOMethod m : methods) {
			data += m.getStringRepresentation(this) + LIODefinitions.LINEBREAK;
		}
		
		return data;
	}
}
