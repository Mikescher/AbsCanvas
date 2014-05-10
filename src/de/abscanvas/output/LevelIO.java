package de.abscanvas.output;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import de.abscanvas.level.Level;
import de.abscanvas.level.ServerLevel;
import de.abscanvas.level.tile.Tile;
import de.abscanvas.math.MPoint;

public class LevelIO {
	public static final String DELIMITER_XY = ",";
	public static final String DELIMITER_VAL = ";";
	public static final String DELIMITER_KAT = "\n";
	public static final String DELIMITER_END = ".";

	private static Level loadResult = null;

	public static boolean saveLevel(Level l, TranslationTable t, File f) {
		String cod = getLevelCode(l, t);
		if (cod == null) {
			System.out.println("SAVE-ERROR [004]");
			return false;
		}

		boolean succ = saveTextFile(f, cod);

		return succ;
	}

	public static String getLevelCode(Level l, TranslationTable t) {
		String result = new String();

		int w = l.getWidth();
		int h = l.getHeight();

		result += w + DELIMITER_XY + h;

		result += DELIMITER_KAT; // ----------------------------------------

		ArrayList<Integer> ids = getIDList(l, t);

		if (ids == null) {
			System.out.println("SAVE-ERROR [003]");
			return null;
		}

		for (int i = 0; i < ids.size(); i++) {
			result += ids.get(i) + DELIMITER_VAL;
		}

		result += DELIMITER_KAT; // ----------------------------------------

		for (int x = 0; x < l.getWidth(); x++) {
			for (int y = 0; y < l.getHeight(); y++) {
				int id = t.getID(l.getTile(x, y));
				if (id < 0) {
					System.out.println("SAVE-ERROR [002]");
					return null;
				}
				result += x + DELIMITER_XY + y + DELIMITER_XY + id + DELIMITER_VAL;
			}
		}

		result += DELIMITER_KAT; // ----------------------------------------

		result += DELIMITER_END;

		return result;
	}

	private static ArrayList<Integer> getIDList(Level l, TranslationTable t) {
		ArrayList<Integer> result = new ArrayList<Integer>();

		for (int x = 0; x < l.getWidth(); x++) {
			for (int y = 0; y < l.getHeight(); y++) {
				int id = t.getID(l.getTile(x, y));
				if (id < 0) {
					System.out.println("SAVE-ERROR [001]");
					return null;
				}
				if (!hasElement(result, id)) {
					result.add(id);
				}
			}
		}

		return result;
	}

	private static boolean hasElement(ArrayList<Integer> a, int i) {
		for (int j = 0; j < a.size(); j++) {
			if (a.get(j).compareTo(i) == 0) {
				return true;
			}
		}
		return false;
	}

	public static MPoint loadWidthAndHeight(File f) {
		String s = loadTextFile(f);
		if (s == null) {
			System.out.println("LOAD_WH-ERROR [001]");
			return null;
		}

		String[] split = s.split(DELIMITER_KAT);

		if (split.length < 4) {
			System.out.println("LOAD_WH-ERROR [010]");
			return null;
		}
		int w = 0;
		int h = 0;
		try {
			w = Integer.parseInt(split[0].split(DELIMITER_XY)[0].trim());
			h = Integer.parseInt(split[0].split(DELIMITER_XY)[1].trim());
		} catch (NumberFormatException e) {
			System.out.println("LOAD_WH-ERROR [004]");
			e.printStackTrace();
			return null;
		}

		return new MPoint(w, h);
	}

	public static boolean load(File f, CreationTable t) {
		String s = loadTextFile(f);
		if (s == null) {
			System.out.println("LOAD-ERROR [002]");
			return false;
		}

		return load(t, s);
	}

	private static boolean load(CreationTable t, String s) {
		String[] split = s.split(DELIMITER_KAT);

		if (split.length < 4) {
			System.out.println("LOAD-ERROR [010]");
			return false;
		}
		int w = 0;
		int h = 0;
		try {
			w = Integer.parseInt(split[0].split(DELIMITER_XY)[0].trim());
			h = Integer.parseInt(split[0].split(DELIMITER_XY)[1].trim());
		} catch (NumberFormatException e) {
			System.out.println("LOAD-ERROR [004]");
			e.printStackTrace();
			return false;
		}

		Level l = t.createLevel(w, h);

		if (l == null) {
			System.out.println("LOAD-ERROR [003]");
			return false;
		}

		String[] ids = split[1].split(DELIMITER_VAL);
		for (int i = 0; i < ids.length - 1; i++) {
			int id = -1;
			try {
				id = Integer.parseInt(ids[i]);
			} catch (NumberFormatException e) {
				System.out.println("LOAD-ERROR [005]");
				e.printStackTrace();
				return false;
			}

			if (t.createTile(id) == null) {
				System.out.println("LOAD-ERROR [006]");
				return false;
			}
		}

		String[] tls = split[2].split(DELIMITER_VAL);

		for (int i = 0; i < tls.length - 1; i++) {
			int xx;
			int yy;
			int ii;
			try {
				xx = Integer.parseInt(tls[i].split(DELIMITER_XY)[0]);
				yy = Integer.parseInt(tls[i].split(DELIMITER_XY)[1]);
				ii = Integer.parseInt(tls[i].split(DELIMITER_XY)[2]);
			} catch (NumberFormatException e) {
				System.out.println("LOAD-ERROR [007]");
				e.printStackTrace();
				return false;
			}

			Tile tt = t.createTile(ii);

			if (t.createTile(ii) == null) {
				System.out.println("LOAD-ERROR [008]");
				return false;
			}

			l.setTile(xx, yy, tt);
		}

		if (split[3].trim().equals(DELIMITER_END)) {
			loadResult = l;
			return true;
		} else {
			System.out.println("LOAD-ERROR [009]");
			return false;
		}
	}

	public static boolean load(ServerLevel l, File f, ServerCreationTable t) {
		String s = loadTextFile(f);
		if (s == null) {
			System.out.println("LOAD-ERROR [002]");
			return false;
		}

		return load2(l, t, s);
	}

	public static boolean load(ServerLevel l, String ressourcePath, ServerCreationTable t) {
		String s = loadTextFileFromRessource(ressourcePath, l.getClass());
		if (s == null) {
			System.out.println("LOAD-ERROR [002]");
			return false;
		}

		return load2(l, t, s);
	}

	private static boolean load2(ServerLevel l, ServerCreationTable t, String s) {
		String[] split = s.split(DELIMITER_KAT);

		if (split.length < 4) {
			System.out.println("LOAD-ERROR [010]");
			return false;
		}
		int w = 0;
		int h = 0;
		try {
			w = Integer.parseInt(split[0].split(DELIMITER_XY)[0].trim());
			h = Integer.parseInt(split[0].split(DELIMITER_XY)[1].trim());
		} catch (NumberFormatException e) {
			System.out.println("LOAD-ERROR [004]");
			e.printStackTrace();
			return false;
		}

		if (l.getWidth() != w || l.getHeight() != h) {
			System.out.println("LOAD-ERROR [011]");
			return false;
		}

		String[] ids = split[1].split(DELIMITER_VAL);
		for (int i = 0; i < ids.length; i++) {
			int id = -1;
			try {
				id = Integer.parseInt(ids[i]);
			} catch (NumberFormatException e) {
				System.out.println("LOAD-ERROR [005]");
				e.printStackTrace();
				return false;
			}

			if (t.createTile(id) == null) {
				System.out.println("LOAD-ERROR [006]");
				return false;
			}
		}

		String[] tls = split[2].split(DELIMITER_VAL);

		for (int i = 0; i < tls.length; i++) {
			int xx;
			int yy;
			int ii;
			try {
				xx = Integer.parseInt(tls[i].split(DELIMITER_XY)[0]);
				yy = Integer.parseInt(tls[i].split(DELIMITER_XY)[1]);
				ii = Integer.parseInt(tls[i].split(DELIMITER_XY)[2]);
			} catch (NumberFormatException e) {
				System.out.println("LOAD-ERROR [007]");
				e.printStackTrace();
				return false;
			}

			Tile tt = t.createTile(ii);

			if (t.createTile(ii) == null) {
				System.out.println("LOAD-ERROR [008]");
				return false;
			}

			l.setTile(xx, yy, tt);
		}

		if (split[3].trim().equals(DELIMITER_END)) {
			loadResult = l;
			return true;
		} else {
			System.out.println("LOAD-ERROR [009]");
			return false;
		}
	}

	public static String loadTextFile(File aFile) {
		StringBuilder contents = new StringBuilder();

		try {
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			System.out.println("LOAD-ERROR [001]");
			ex.printStackTrace();
			return null;
		}

		return contents.toString();
	}

	public static String loadTextFileFromRessource(String aRes, Class<?> c) {
		Writer writer = new StringWriter();
		InputStream is = c.getResourceAsStream(aRes);
		char[] buffer = new char[1024];
		try {
			Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
		}
		
		return writer.toString();

	}

	public static boolean saveTextFile(File f, String cod) {
		try {
			PrintWriter out = new PrintWriter(f);
			out.print(cod);
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("SAVE-ERROR [005]");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Level getLoadResult() {
		return loadResult;
	}
}
