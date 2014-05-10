package de.abscanvas.internal;

import java.lang.reflect.Constructor;

public class ClassHelper {
	@SuppressWarnings("rawtypes")
	public static Object getObject(Class classref) {
		// Check for valid class reference
		if (classref == null)
			return null;

		// Since this method is called when you want to
		// create an object of a class using its default constructor
		// you must declare a default constructor in your class and
		// declare it public.If you don't declare it public then
		// yuo have to tweak the security settings.
		try {
			Object o = classref.newInstance();
			return o;
		} catch (Exception e) {
			System.out.println("ERROR in creating Object from Class Reference (Exception in Constructor ?)");
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getObject(Class classRef, Object[] args) {
		// Check for valid class reference
		if (classRef == null || args == null)
			return null;

		Object o = null;

		/*
		 * You can create an object from a class reference passing arguments to the constructor of that class using an object of class Constructor
		 */

		try {
			/*
			 * Get the class type of all the arguments passed and store them in an array which will be used to find out the matching constructor
			 */
			Class[] argsClass = new Class[args.length];
			for (int i = 0; i < args.length; i++)
				argsClass[i] = args[i].getClass();

			// Get the constructor of the class matching the arguments passed
			Constructor con = classRef.getConstructor(argsClass);

			// Create an object using the constructor
			if (con != null)
				o = con.newInstance(args);
			else
				System.out.println(" Constructor is returning null");
			return o;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
