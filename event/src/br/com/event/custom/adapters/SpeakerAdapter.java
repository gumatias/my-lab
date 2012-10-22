package br.com.event.custom.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.adapters.SponsorAdapter.ArrowClickListener;
import br.com.event.custom.infra.DrawableManager;
import br.com.event.custom.model.Speaker;
import br.com.event.custom.ui.SpeakerDetailActivity;

public class SpeakerAdapter extends GenericListAdapter<Speaker> {

	private DrawableManager drawableManager;
	
	public SpeakerAdapter(Context context, List<Speaker> list) {
		super(context, list);
		drawableManager = new DrawableManager(context, true);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		
		ViewHolder holder = null;
        if (convertView == null) {
        	convertView = getLayoutInflater().inflate(R.layout.speakers_list_row, null);
            holder = new ViewHolder();
            holder.nameTxt = (TextView)convertView.findViewById(R.id.speaker_name);
            holder.descTxt = (TextView)convertView.findViewById(R.id.speaker_description);
			holder.spkrPic = (ImageView)convertView.findViewById(R.id.speaker_picture);
			holder.arrowBtn = (Button)convertView.findViewById(R.id.speaker_arrow_btn);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        
        holder.nameTxt.setText(getList().get(position).getName());
        holder.descTxt.setText(Html.fromHtml(getList().get(position).getDescription()));
        holder.arrowBtn.setOnClickListener(new ArrowClickListener(position));
        drawableManager.fetchDrawableOnThread(getList().get(position).getImgUrl(), holder.spkrPic);
        
        convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startSpeakerDescription(pos);
			}
		});
        
        return convertView;
	}
	
	private void startSpeakerDescription(int position) {
		Context context = getLayoutInflater().getContext();
		Intent it = new Intent(context, SpeakerDetailActivity.class);
		it.putExtra("speaker", getList().get(position));
		context.startActivity(it);
	}

	private static class ViewHolder {
		public TextView nameTxt, descTxt;
		public ImageView spkrPic;
		public Button arrowBtn;
	}
	
	class ArrowClickListener implements OnClickListener {

		private int position;
		
		public ArrowClickListener(int position) {
			this.position = position;
		}
		
		public void onClick(View v) {
			startSpeakerDescription(position);
		}
		
	}
	
}
