package com.google.imagesearch;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.imagesearch.model.Image;
import com.gus.test.R;

public class ImageAdapter extends BaseAdapter {

	private static Activity activity;
	private List<Image> images;
	private static LayoutInflater inflater = null;
	private static ImageLoader imageLoader;

	public static ImageLoader getImageLoader() {
		if(imageLoader == null) imageLoader = new ImageLoader(activity.getApplicationContext());
		return imageLoader;
	}

	public static void setImageLoader(ImageLoader imageLoader) {
		ImageAdapter.imageLoader = imageLoader;
	}

	public ImageAdapter(Activity activity, List<Image> images) {
		ImageAdapter.activity = activity;
		this.images = images;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return images.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public TextView text;
		public ImageView image;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.item, null);
			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.text);
			holder.image = (ImageView) vi.findViewById(R.id.image);
			vi.setTag(holder);
		} else{
			holder = (ViewHolder) vi.getTag();	
		}

		holder.text.setText(images.get(position).getTitle());
		holder.image.setTag(images.get(position).getUrl());
		System.out.println("img:" + images.get(position) + " holder:" + holder.image);
		getImageLoader().displayImage(images.get(position), activity, holder.image);
		return vi;
	}
}