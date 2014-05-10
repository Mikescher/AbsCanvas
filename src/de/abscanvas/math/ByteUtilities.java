package de.abscanvas.math;

import java.nio.ByteBuffer;
import java.util.Arrays;

import de.abscanvas.network.NetworkConstants;

public class ByteUtilities {
	public static final byte ZERO = 0;
	public static final byte ONE = 1;
	public static final byte NEG_ONE = -1;

	public static double arr2double(byte[] arr, int start) {
		byte[] tmp = removeFirst(arr, start);
		ByteBuffer buf = ByteBuffer.wrap(tmp);
		return buf.getDouble();
	}

	public static int arr2Int(byte[] arr, int start) {
		byte[] tmp = removeFirst(arr, start);
		ByteBuffer buf = ByteBuffer.wrap(tmp);
		return buf.getInt();
	}

	public static long arr2Long(byte[] arr, int start) {
		byte[] tmp = removeFirst(arr, start);
		ByteBuffer buf = ByteBuffer.wrap(tmp);
		return buf.getLong();
	}

	public static short arr2Short(byte[] arr, int start) {
		byte[] tmp = removeFirst(arr, start);
		ByteBuffer buf = ByteBuffer.wrap(tmp);
		return buf.getShort();
	}

	public static boolean arr2Boolean(byte[] arr, int start) {
		byte[] tmp = removeFirst(arr, start);
		return tmp[0] == 1;
	}

	public static byte arr2Byte(byte[] arr, int start) {
		byte[] tmp = removeFirst(arr, start);
		return tmp[0];
	}

	/**
	 * @param var
	 *            the input parameter
	 * @return a 4-Byte long Array
	 */
	public static byte[] int2Arr(int var) {
		byte b[] = new byte[4];

		ByteBuffer buf = ByteBuffer.wrap(b);
		buf.putInt(var);
		return b;
	}

	/**
	 * @param var
	 *            the input parameter
	 * @return a 8-Byte long Array
	 */
	public static byte[] long2Arr(long var) {
		byte b[] = new byte[8];

		ByteBuffer buf = ByteBuffer.wrap(b);
		buf.putLong(var);
		return b;
	}

	/**
	 * @param var
	 *            the input parameter
	 * @return a 2-Byte long Array
	 */
	public static byte[] short2Arr(short var) {
		byte b[] = new byte[2];

		ByteBuffer buf = ByteBuffer.wrap(b);
		buf.putShort(var);
		return b;
	}

	/**
	 * @param var
	 *            the input parameter
	 * @return a 8-Byte long Array
	 */
	public static byte[] double2Arr(double var) {
		byte b[] = new byte[8];

		ByteBuffer buf = ByteBuffer.wrap(b);
		buf.putDouble(var);
		return b;
	}

	/**
	 * @param var
	 *            the input parameter
	 * @return a 1-Byte long Array
	 */
	public static byte[] boolean2Arr(boolean var) {
		byte b[] = new byte[1];

		b[0] = (byte) (var ? 1 : 0);
		return b;
	}

	/**
	 * @param var
	 *            the input parameter
	 * @return a 1-Byte long Array
	 */
	public static byte[] byte2Arr(byte var) {
		byte b[] = new byte[1];
		b[0] = var;
		return b;
	}

	public static byte[] insert(byte[] parent, byte[] insert, int start) {
		int len = insert.length;

		for (int i = start; i < (start + len); i++) {
			if (i < parent.length) {
				parent[i] = insert[i - start];
			}
		}

		return parent;
	}

	public static byte[] removeFirst(byte[] input, int count) {
		if (count <= 0) {
			return input;
		}

		byte[] result = new byte[input.length - count];

		for (int i = count; i < input.length; i++) {
			result[i - count] = input[i];
		}

		return result;
	}

	public static byte[] addLast(byte[] data, byte[] add) {
		byte[] result = new byte[data.length + add.length];

		insert(result, data, 0);
		insert(result, add, data.length);

		return result;
	}

	public static byte[] removeLast(byte[] data, int remCount) {
		byte[] result = new byte[data.length - remCount];

		for (int i = 0; i < result.length; i++) {
			result[i] = data[i];
		}

		return result;
	}

	public static String byteArrayToString(byte[] data) {
		String t = "";
		boolean write = false;
		for (int i = (data.length - 1); i >= 0; i--) {
			write |= (data[i] != 0);

			if (write) {
				t = data[i] + "" + t;
				if (i > 0) {
					t = "," + t;
				}
			}

		}
		return t;
	}

	public static byte[] fill(byte[] b, byte j) {
		for (int i = 0; i < b.length; i++) {
			b[i] = j;
		}
		return b;
	}

	public static byte[] setLength(byte[] data, int length) {
		if (data.length == length) {
			return data;
		} else if (data.length < length) {
			return addLast(data, getZeroArray(length - data.length)); // TODO mit 0 füllen !!!!!!!!
		} else { // (data.length > length)
			return removeLast(data, data.length - length);
		}
	}
	
	public static byte[] string2arr(String s) {
		return setLength(s.getBytes(), NetworkConstants.STANDARD_STRING_SIZE);
	}
	
	public static byte[] shortString2arr(String s) {
		return setLength(s.getBytes(), NetworkConstants.STANDARD_STRING_SIZE);
	}

	public static byte[] getZeroArray(int length) {
		byte[] r = new byte[length];
		Arrays.fill(r, ZERO);
		return r;
	}

	public static int getByteDifference(byte lessByte, byte greaterByte) {
		while (lessByte != 0) {
			lessByte++;
			greaterByte++;
		}
		return greaterByte;
	}

	public static byte[] copy(byte[] var, int start, int count) {
		byte[] result = new byte[count];
		for (int i = start; i < start + count; i++) {
			result[i - start] = var[i];
		}
		return result;
	}
	
	public static String extractString(byte[] data, int start, int length) {
		byte[] stringData = copy(data, start, length);
		String rst = new String(stringData);
		rst = rst.trim();
		return rst;
	}
	
	/**
	 * @param b1 erster Byte
	 * @param b2 zweiter Byte
	 * @return -1: b1>b2  // 0: b1=b2  //  1: b1<b2
	 */
	public static int compareByte(byte b1, byte b2) {
		b1 += 128;
		b2 += 128;
		if (b1 == b2) {
			return 0;
		}
		
		if (( b1 > b2 ) && ( b1 - b2 <= 128 ) || ( b2 > b1 ) && ( b2 - b1 > 128  )) {
			return -1;
		}
		
		return 1;
	}
}