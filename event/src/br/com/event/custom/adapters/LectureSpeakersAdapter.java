package br.com.event.custom.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.infra.DrawableManager;
import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.model.Speaker;
import br.com.event.custom.ui.SpeakerDetailActivity;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class LectureSpeakersAdapter extends GenericListAdapter<Speaker> {

	private DrawableManager drawableManager;
	
	private EventDatabaseHelper helper;
	
	public LectureSpeakersAdapter(Context context, List<Speaker> speakers) {
		super(context, speakers);
		helper = (EventDatabaseHelper)OpenHelperManager.getHelper(context);
		drawableManager = new DrawableManager(context, true);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
        if (convertView == null) {
        	convertView = getLayoutInflater().inflate(R.layout.lecture_speakers_list_row, null);
            holder = new ViewHolder();
            holder.nameTxt  = (TextView)convertView.findViewById(R.id.speaker_name);
            holder.spkrPic  = (ImageView)convertView.findViewById(R.id.speaker_picture);
            holder.arrowBtn = (Button)convertView.findViewById(R.id.speaker_arrow_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        
        holder.nameTxt.setText(getList().get(position).getName());
        drawableManager.fetchDrawableOnThread(getList().get(position).getImgUrl(), holder.spkrPic);
        holder.arrowBtn.setOnClickListener(new ArrowClickListener(position));
        
        convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startSpeakerActivity(position);
			}
		});
        return convertView;
	}
	
	private void startSpeakerActivity(int position) {
		Speaker speaker = getList().get(position);
		Intent intent = new Intent(getContext(), SpeakerDetailActivity.class);
		intent.putExtra("speaker", speaker);
		getContext().startActivity(intent);
	}
	
	public static class ViewHolder {
        public TextView nameTxt;
        public ImageView spkrPic;
        public Button arrowBtn;
    }
	
	class ArrowClickListener implements OnClickListener {

		private int position;
		
		public ArrowClickListener(int position) {
			this.position = position;
		}
		
		public void onClick(View v) {
			startSpeakerActivity(position);
		}
		
	}
	
}