package br.com.event.custom.infra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import br.com.event.custom.R;

public class DrawableManager {
	
	private Context context;
	
	private boolean storeInSD;
	
	public DrawableManager(Context context, boolean storeInSD) {
		this.context   = context;
		this.storeInSD = storeInSD;
	}
	
    private static Map<String, Drawable> drawableMap = new HashMap<String, Drawable>();

    public Drawable fetchDrawable(String urlString) {
    	Drawable drawable = null;
    	File file = new File(Environment.getExternalStorageDirectory() + "/custom-event-cache-2/", clearFileName(urlString));
    	
    	System.out.println("file.exists():" + file.exists());
    	if (file.exists()) {
			System.out.println("file.Path():" + file.getPath());
			Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			System.out.println("myBitmap:" + myBitmap);
    		return Drawable.createFromPath(file.getPath());
    	} else {
    		System.out.println("making dirs");
    		file.getParentFile().mkdirs();
    	} 
    	
    	if (drawableMap.containsKey(urlString)) {
            return drawableMap.get(urlString);
        }

        Log.d(this.getClass().getSimpleName(), "image url:" + urlString);
        try {
            InputStream is = fetch(urlString);
            drawable = Drawable.createFromStream(is, "src");

            if (drawable != null && storeInSD) {
            	if (!file.exists()) saveFile(is, file);
            } else if (drawable != null) {
                drawableMap.put(urlString, drawable);
                Log.d(this.getClass().getSimpleName(), "got a thumbnail drawable: " + drawable.getBounds() + ", "
                        + drawable.getIntrinsicHeight() + "," + drawable.getIntrinsicWidth() + ", "
                        + drawable.getMinimumHeight() + "," + drawable.getMinimumWidth());
            } else {
              Log.w(this.getClass().getSimpleName(), "could not get thumbnail from " + urlString);
            }

            return drawable;
        } catch (MalformedURLException e) {
            Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
            return context.getResources().getDrawable(R.drawable.anonimo);
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
            return context.getResources().getDrawable(R.drawable.anonimo);
        }
    }

    public void fetchDrawableOnThread(final String urlString, final ImageView imageView) {
        if (drawableMap.containsKey(urlString)) {
            imageView.setImageDrawable(drawableMap.get(urlString));
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                imageView.setImageDrawable((Drawable) message.obj);
                System.out.println("imageview:" + imageView.getVisibility());
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                //TODO : set imageView to a "pending" image
                Drawable drawable = fetchDrawable(urlString);
                System.out.println("drawable:" + drawable);
                Message message = handler.obtainMessage(1, drawable);
                handler.sendMessage(message);
            }
        };
        thread.start();
    }

    private InputStream fetch(String urlString) throws MalformedURLException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlString);
        HttpResponse response = httpClient.execute(request);
        return response.getEntity().getContent();
    }
    
    private String clearFileName(String fileUrlString) {
    	return fileUrlString.replaceAll("\\.", "_").replaceAll("/", "_").replaceAll(":", "_").replaceAll("\\?", "_");
    }
    
	private void saveFile(InputStream is, File file) {
		System.out.println("saving file..." + is);
		System.out.println("file.getAbsolutePath():" + file.getAbsolutePath());
		
		try {
            FileOutputStream fos = new FileOutputStream(file.getPath());
            try {
                byte[] buffer = new byte[4096];
                int l;
                while ((l = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, l);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                is.close();
                fos.flush();
                fos.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    public static void clearCache() {
    	drawableMap = null;
    	drawableMap = new HashMap<String, Drawable>();
    }
    
}