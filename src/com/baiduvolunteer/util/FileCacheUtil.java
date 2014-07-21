package com.baiduvolunteer.util;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Context;

public class FileCacheUtil {
	/**
	 * 
	 * edit by coolgo
	 * 
	 * @param activity
	 * @param text
	 * @param name
	 */
	public static void write(Activity activity, String text, String name) {
		OutputStream outputStream = null;
		try {
			String content = text;
			outputStream = activity.openFileOutput(name, Context.MODE_PRIVATE);
			if (content != null) {
				outputStream.write(content.getBytes(), 0,
						content.getBytes().length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void writeObject(Context activity, Serializable object,
			String name) {
		OutputStream outputStream = null;
		try {
			outputStream = activity.openFileOutput(name,
					Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(outputStream);
			if (object != null) {
				os.writeObject(object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Object readObject(Context context, String name) {
		InputStream inputStream = null;
		try {
			inputStream = context.openFileInput(name);
			ObjectInputStream is = new ObjectInputStream(inputStream);
			return is.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * edit by coolgo
	 * 
	 * @param activity
	 * @param name
	 * @return
	 */
	public static String read(Activity activity, String name) {
		InputStream inputStream = null;
		BufferedInputStream bis = null;
		String myString = null;
		try {
			inputStream = activity.openFileInput(name);
			bis = new BufferedInputStream(inputStream);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			myString = EncodingUtils.getString(baf.toByteArray(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}

				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return myString;
	}

	/**
	 * 
	 edit by coolgo
	 * 
	 * @param activity
	 * @param name
	 * @return
	 */
	public static boolean isExistFile(Activity activity, String name) {
		InputStream is = null;
		boolean result = false;
		try {
			is = activity.openFileInput(name);
			if (is != null) {
				result = true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}