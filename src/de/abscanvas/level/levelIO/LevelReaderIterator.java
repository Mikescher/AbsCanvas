package de.abscanvas.level.levelIO;

import java.util.ArrayList;
import java.util.Enumeration;

public class LevelReaderIterator implements Enumeration<LIOReadOnlyMethod> {
	ArrayList<LIOReadOnlyMethod> methods;
	
	private int pos;
	private int lastElement;
	
	public LevelReaderIterator(ArrayList<LIOReadOnlyMethod> mtds, int size) {
		pos = -1;
		lastElement = size - 1;
		methods = mtds;
	}

	@Override
	public boolean hasMoreElements() {
		return pos < lastElement;
	}

	@Override
	public LIOReadOnlyMethod nextElement() {
		if (hasMoreElements()) {
			pos++;
			return methods.get(pos);
		}
		return null;
	}
}
