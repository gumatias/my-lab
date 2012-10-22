package com.google.imagesearch.util;

import java.io.InputStream;
import java.io.OutputStream;

public class StorageUtils {
	/**
	 * Copy the image to the SD
	 * @param is
	 * @param os
	 */
	public static void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			while(true) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
}