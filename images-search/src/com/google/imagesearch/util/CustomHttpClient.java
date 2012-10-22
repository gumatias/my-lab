package com.google.imagesearch.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CustomHttpClient {

	public static String executeHttpGet(String getURL) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(getURL.replaceAll(" ", "%20"));
			HttpResponse responseGet = client.execute(get);
			HttpEntity resEntityGet = responseGet.getEntity();
			
			String response = null;
			if (resEntityGet != null) {
				response = EntityUtils.toString(resEntityGet);
			}
			
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Bitmap downloadImage(String imgUrl) throws IOException {
		try {
			URL url = new URL(imgUrl);
			InputStream is = (InputStream) url.getContent();
			byte[] buffer = new byte[8192];
			int bytesRead;
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			while ((bytesRead = is.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			
			return BitmapFactory.decodeByteArray(output.toByteArray(), 0, output.toByteArray().length);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
