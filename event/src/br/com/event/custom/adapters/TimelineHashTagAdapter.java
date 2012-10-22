package br.com.event.custom.adapters;

import java.util.List;

import twitter4j.Tweet;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.infra.DrawableManager;

public class TimelineHashTagAdapter extends GenericListAdapter<Tweet> {

	private DrawableManager drawableManager;
	
	public TimelineHashTagAdapter(Context context, List<Tweet> tweets) {
		super(context, tweets);
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
        
        Tweet tweet = getList().get(position);
        holder.userNameTxt.setText("@" + tweet.getFromUser());
        holder.tweetTxt.setText(tweet.getText());
        drawableManager.fetchDrawableOnThread(tweet.getProfileImageUrl(), holder.userPic);
        
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
