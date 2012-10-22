package br.com.event.custom.adapters;

import java.util.List;

import twitter4j.Status;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.infra.DrawableManager;

public class TimelineEventAdapter extends GenericListAdapter<Status> {

	private DrawableManager drawableManager;
	
	public TimelineEventAdapter(Context context, List<Status> statuses) {
		super(context, statuses);
		drawableManager = new DrawableManager(context, true);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
        if (convertView == null) {
        	convertView = getLayoutInflater().inflate(R.layout.twitter_timeline_row, null);
            holder = new ViewHolder();
            holder.userNameTxt = (TextView)convertView.findViewById(R.id.tweet_username);
            holder.tweetTxt = (TextView)convertView.findViewById(R.id.tweet_text);
			holder.userPic = (ImageView)convertView.findViewById(R.id.tweet_userpic);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        
        Status status = list.get(position);
        holder.userNameTxt.setText("@" + status.getUser().getName());
        holder.tweetTxt.setText(status.getText());
        drawableManager.fetchDrawableOnThread(status.getUser().getProfileImageURL().toString(), holder.userPic);
        
        convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
//				Intent it = new Intent(context, SpeakersDescriptionActivity.class);
//				it.putExtra("speakers", tweets.get(pos));
//				context.startActivity(it);
			}
		});
        
        return convertView;
	}

	private static class ViewHolder {
		public TextView userNameTxt, tweetTxt;
		public ImageView userPic;
	}
}