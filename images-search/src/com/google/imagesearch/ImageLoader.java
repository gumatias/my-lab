package com.google.imagesearch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.google.imagesearch.dao.ImageDAO;
import com.google.imagesearch.model.Image;
import com.google.imagesearch.util.StorageUtils;
import com.gus.test.R;

/**
 * simple in-memory cache implementation
 */
public class ImageLoader {
	
	private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();
	private Context context;
	private PhotosLoader photoLoaderThread = new PhotosLoader();
	final int stub_id = R.drawable.stub;

	public ImageLoader(Context context) {
		/* Make the background thread low priority. This way it will not affect
		 	the UI performance */
		photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
		this.context = context;

		File cacheDir = getImagesCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}
	
	private File getImagesCacheDir() {
		File cacheDir;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(android.os.Environment
					.getExternalStorageDirectory(), "ImagesList");
		else
			cacheDir = context.getCacheDir();
		return cacheDir;
	}

	public void displayImage(Image image, Activity activity, ImageView imageView) {
		if (cache.containsKey(image.getUrl())) {
			imageView.setImageBitmap(cache.get(image.getUrl()));
		} else {
			queuePhoto(image.getUrl(), activity, imageView);
			imageView.setImageResource(stub_id);
		}
	}

	public void queuePhoto(String url, Activity activity, ImageView imageView) {
		/* This ImageView may be used for other images before. So there may be
			some old tasks in the queue. We need to discard them */
		photosQueue.Clean(imageView);
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		synchronized (photosQueue.photosToLoad) {
			photosQueue.photosToLoad.push(p);
			photosQueue.photosToLoad.notifyAll();
		}
		
		/* start thread if it's not started yet */
		if (photoLoaderThread != null && photoLoaderThread.getState() == Thread.State.NEW)
			photoLoaderThread.start();
	}

	private Bitmap getBitmap(String url) {
		/* I identify images by hashcode. Not a perfect solution */
		String filename = String.valueOf(url.hashCode());
		File f = new File(getImagesCacheDir(), filename);
		
		/* from SD cache */
		Bitmap b = decodeFile(f);
		if (b != null) return b;	

		/* from web */
		try {
			Bitmap bitmap = null;
			InputStream is = new URL(url).openStream();
			OutputStream os = new FileOutputStream(f);
			StorageUtils.copyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/* decodes image and scales it to reduce memory consumption */
	private Bitmap decodeFile(File f) {
		try {
			/* decode image size */
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			/* find the correct scale value */
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			/* decode with inSampleSize */
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	/* Task for the queue */
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	PhotosQueue photosQueue = new PhotosQueue();

	public void stopThread() {
		if (photoLoaderThread != null) photoLoaderThread.interrupt();
		photoLoaderThread = null;
	}

	/* stores list of photos to download */
	class PhotosQueue {
		private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

		/* removes all instances of this ImageView */
		public void Clean(ImageView image) {
			for (int j = 0; j < photosToLoad.size();) {
				if (photosToLoad.get(j).imageView == image)
					photosToLoad.remove(j);
				else
					++j;
			}
		}
	}

	class PhotosLoader extends Thread {
		public void run() {
			try {
				while (true) {
					/* thread waits until there are any images to load in the queue */
					if (photosQueue.photosToLoad.size() == 0)
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						} else {
						PhotoToLoad photoToLoad;
						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.pop();
						}
						Bitmap bmp = getBitmap(photoToLoad.url);
						
						cache.put(photoToLoad.url, bmp);
						Object tag = photoToLoad.imageView.getTag();
						if (tag != null
								&& ((String) tag).equals(photoToLoad.url)) {
							BitmapDisplayer bd = new BitmapDisplayer(bmp,
									photoToLoad.imageView);
							Activity a = (Activity) photoToLoad.imageView
									.getContext();
							a.runOnUiThread(bd);
						}
					}
					if (Thread.interrupted())
						break;
				}
			} catch (InterruptedException e) {
				/* allow thread to exit */
			}
		}
	}

	/* Used to display bitmap in the UI thread */
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;

		public BitmapDisplayer(Bitmap b, ImageView i) {
			bitmap = b;
			imageView = i;
		}

		public void run() {
			if (bitmap != null)
				imageView.setImageBitmap(bitmap);
			else
				imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		/* clear memory cache */
		cache.clear();

		ArrayList<String> hashs = ImageDAO.getInstance(context)
				.fetchImagesHash();
		/* clear SD cache */
		File[] files = getImagesCacheDir().listFiles();
		for (File f : files) {
			if (!hashs.contains(f.getName())) f.delete();
		}

	}
}
