package de.abscanvas.level.levelIO;

public class LIOReadOnlyMethod {
	private LIOMethod wrapped;
	
	public LIOReadOnlyMethod(LIOMethod w) {
		wrapped = w;
	}

	public int getMethodname() {
		return wrapped.getMethodname();
	}
	
	public int getParameterCount() {
		return wrapped.getParameterCount();
	}
	
	public int getParameterType(int index) {
		return wrapped.getParameter(index).getType();
	}
	
	public String getParameter_s(int index) {
		return wrapped.getParameter(index).getValue_s();
	}
	
	public int getParameter_i(int index) {
		return wrapped.getParameter(index).getValue_i();
	}
	
	public double getParameter_d(int index) {
		return wrapped.getParameter(index).getValue_d();
	}
	
	public boolean getParameter_b(int index) {
		return wrapped.getParameter(index).getValue_b();
	}
} 
