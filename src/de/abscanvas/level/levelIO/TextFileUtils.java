package de.abscanvas.level.levelIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class TextFileUtils {
	public static String readTextFile(String filename) throws IOException {
		File file = new File(filename);
		StringBuffer content = new StringBuffer();
		BufferedReader reader = null;
		boolean first = true;

		try {
			reader = new BufferedReader(new FileReader(file));
			String s = null;

			while ((s = reader.readLine()) != null) {
				if (!first) {
					content.append(System.getProperty("line.separator"));
				}
				content.append(s);
				first = false;
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return content.toString();
	}

	public static String readTextResource(String resourcename, Class<?> c) throws IOException {
		BufferedReader reader = null;
		StringBuffer content = new StringBuffer();
		
		try {
			InputStream is = c.getResourceAsStream(resourcename);
			if (is == null) throw new IOException();
			reader = new BufferedReader(new InputStreamReader(is));
			String s = null;
			boolean first = true;

			while ((s = reader.readLine()) != null) {
				if (!first) {
					content.append(System.getProperty("line.separator"));
				}
				content.append(s);
				first = false;
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return content.toString();
	}
	
	public static void writeTextFile(String filename, String text) throws IOException {
		FileWriter outFile = null;
		PrintWriter out = null;
		try {
			outFile = new FileWriter(filename);
			out = new PrintWriter(outFile);
			out.print(text);
		} finally {
			if (out != null) out.close();
		}
	}
}