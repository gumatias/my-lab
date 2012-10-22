package br.com.event.custom.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import br.com.event.custom.R;
import br.com.event.custom.infra.DrawableManager;
import br.com.event.custom.model.Picture;
import br.com.event.custom.ui.PicturePreviewActivity;

public class PictureAdapter extends BaseAdapter {
	
	private Context context;
	private List<Picture> pictures;
	protected LayoutInflater inflater;
	private DrawableManager drawableManager;
	
	public PictureAdapter(Context context, List<Picture> pictures) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.pictures = pictures;
		this.drawableManager = new DrawableManager(context, true);
	}

	public int getCount() {
		return pictures.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		final String imgUrl = pictures.get(position).getImgUrl();
		
		ViewHolder holder = null;
		if (convertView == null) {
        	convertView = inflater.inflate(R.layout.gallery_picture_item, null);
            holder = new ViewHolder();
            holder.imgView 	= (ImageView)convertView.findViewById(R.id.gallery_picture_img);
            
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}

		System.out.println("fecthing...");
		drawableManager.fetchDrawableOnThread(imgUrl, holder.imgView);
		holder.imgView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, PicturePreviewActivity.class);
				intent.putExtra("image_url", imgUrl);
				context.startActivity(intent);
				}
		});
		return convertView;
	}
	
	private class ViewHolder {
		ImageView imgView;
	}
}