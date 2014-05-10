package de.abscanvas.level.levelIO;

import java.util.ArrayList;

public class LIOMethod {
	private int methodname;
	private ArrayList<LIOMethodParameter> parameter = new ArrayList<LIOMethodParameter>();
	private String comment = "";
	
	public LIOMethod(int name) {
		setMethodname(name);
	}

	public int getMethodname() {
		return methodname;
	}

	public void setMethodname(int methodname) {
		this.methodname = methodname;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getParameterCount() {
		return parameter.size();
	}
	
	public LIOMethod addStringParameter(String s) {
		parameter.add(new LIOMethodParameter(s));
		return this;
	}
	
	public LIOMethod addIntegerParameter(int i) {
		parameter.add(new LIOMethodParameter(i));
		return this;
	}
	
	public LIOMethod addDoubleParameter(double d) {
		parameter.add(new LIOMethodParameter(d));
		return this;
	}
	
	public LIOMethod addBooleanParameter(boolean b) {
		parameter.add(new LIOMethodParameter(b));
		return this;
	}
	
	public LIOMethod addStringParameter(String s, boolean useDef) {
		parameter.add(new LIOMethodParameter(s, useDef));
		return this;
	}
	
	public LIOMethod addIntegerParameter(int i, boolean useDef) {
		parameter.add(new LIOMethodParameter(i, useDef));
		return this;
	}
	
	public LIOMethod addDoubleParameter(double d, boolean useDef) {
		parameter.add(new LIOMethodParameter(d, useDef));
		return this;
	}
	
	public LIOMethod addBooleanParameter(boolean b, boolean useDef) {
		parameter.add(new LIOMethodParameter(b, useDef));
		return this;
	}

	public String getStringRepresentation(LevelWriter w) {
		String rep = "";
		rep += w.findMethodDefinition(methodname);
		
		rep += LIODefinitions.TYPEDEF_START;
		for(LIOMethodParameter lmp : parameter) {
			rep += lmp.getValueTypeIdentifier();
		}
		rep += LIODefinitions.TYPEDEF_END;
		
		rep += LIODefinitions.PARAMETER_START;
		for(int i = 0; i < parameter.size(); i++) {
			rep += parameter.get(i).getValue(w);
			if ((i+1) < parameter.size()) {
				rep += LIODefinitions.DELIMITER + " ";
			}
		}
		rep += LIODefinitions.PARAMETER_END;
		
		return rep;
	}
	
	public LIOMethodParameter getParameter(int index) {
		return parameter.get(index);
	}
}
