package br.com.event.custom.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.infra.DrawableManager;
import br.com.event.custom.model.Sponsor;

public class SponsorAdapter extends GenericListAdapter<Sponsor> {

	private DrawableManager drawableManager;
	
	public SponsorAdapter(Context context, List<Sponsor> sponsors) {
		super(context, sponsors);
		drawableManager = new DrawableManager(context, true);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
        if (convertView == null) {
        	convertView = getLayoutInflater().inflate(R.layout.sponsor_list_row, null);
            holder = new ViewHolder();
            holder.nameTxt 		= (TextView)convertView.findViewById(R.id.sponsor_name);
            holder.descTxt		= (TextView)convertView.findViewById(R.id.sponsor_description);
			holder.sponsorPic	= (ImageView)convertView.findViewById(R.id.sponsor_picture);
			holder.arrowBtn = (Button)convertView.findViewById(R.id.sponsor_next_btn);
			
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        
        if ((position % 2) == 0) convertView.setBackgroundResource(R.drawable.lista_linha1);
        
        holder.nameTxt.setText(getList().get(position).getName());
        holder.descTxt.setText(getList().get(position).getDescription());
        holder.arrowBtn.setOnClickListener(new ArrowClickListener(position));
        drawableManager.fetchDrawableOnThread(getList().get(position).getImgUrl(), holder.sponsorPic);
        
        convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				openBrowser(position);
			}
		});

        return convertView;
	}
	
	private void openBrowser(int position) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(getList().get(position).getWebSite()));
		getLayoutInflater().getContext().startActivity(i);
	}

	private static class ViewHolder {
		public TextView nameTxt, descTxt;
		public ImageView sponsorPic;
		public Button arrowBtn;
	}
	
	class ArrowClickListener implements OnClickListener {

		private int position;
		
		public ArrowClickListener(int position) {
			this.position = position;
		}
		
		public void onClick(View v) {
			openBrowser(position);
		}
		
	}

}