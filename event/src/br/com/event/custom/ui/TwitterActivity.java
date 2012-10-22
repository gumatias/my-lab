package br.com.event.custom.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.io.TwitterManager;
import br.com.event.custom.util.Constants;
import br.com.event.custom.util.TwitterUtils;
import br.com.event.custom.view.PullToRefreshListView;
import br.com.event.custom.view.PullToRefreshListView.OnRefreshListener;

public class TwitterActivity extends EventActivity {

	private TwitterManager twitterManager;

	private PullToRefreshListView tweetsListView;

	private Button eventTwitterBtn;

	private Button eventHashBtn;

	private Button tweetBtn;

	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		((TextView) findViewById(R.id.header_title))
				.setText(R.string.menu_social);
		((Button) findViewById(R.id.header_back_btn))
				.setOnClickListener(new BackBtnListener());

		if (!isOnline()) {
			showDialog(INTERNET_FAILURE_DIALOG);
			return;
		}

		new AuthenticationCheckTask().execute();

		twitterManager = new TwitterManager(this, getPreferences());
		tweetsListView = (PullToRefreshListView) findViewById(R.id.tweets);

		eventTwitterBtn = (Button) findViewById(R.id.tweets_eventside_btn);
		eventTwitterBtn.setOnClickListener(new EventTwitterListener());
		eventTwitterBtn.setText("@" + Constants.EVENT_SCREEN_NAME);
		eventHashBtn = (Button) findViewById(R.id.tweets_hashside_btn);
		eventHashBtn.setOnClickListener(new EventHashListener());
		eventHashBtn.setText(Constants.EVENT_HASH_TAG);

		tweetBtn = (Button) findViewById(R.id.tweet_btn);
		tweetBtn.setOnClickListener(new TweetListener());

		/* first page is listing event tweets */
		eventTwitterBtn.setTag(true);
		twitterManager.updateEventTimeline(tweetsListView);

		((PullToRefreshListView) tweetsListView)
				.setOnRefreshListener(new OnRefreshListener() {

					public void onRefresh() {
						if ((Boolean) eventTwitterBtn.getTag())
							twitterManager.new UserTimelineTask().execute(
									false, Constants.EVENT_SCREEN_NAME);
						else
							twitterManager.new HashTagTimelineTask()
									.execute(false);
					}
				});

	}

	class AuthenticationCheckTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (!TwitterUtils.isUserAuthenticated(prefs))
				TwitterUtils.authenticateReader();
			return null;
		}

	}

	class TweetListener implements OnClickListener {

		public void onClick(View v) {
			Intent i = new Intent(getApplicationContext(), TwitterStatusTextBoxActivity.class);
			startActivity(i);
		}

	}

	class EventTwitterListener implements OnClickListener {

		public void onClick(View v) {
			eventTwitterBtn.setTag(true);
			twitterManager.updateEventTimeline(tweetsListView);
			toggle();
		}

	}

	class EventHashListener implements OnClickListener {

		public void onClick(View v) {
			eventTwitterBtn.setTag(false);
			twitterManager.updateHashTagTimeline(tweetsListView);
			toggle();
		}

	}

	private void toggle() {
		Resources res = getResources();
		if ((Boolean) eventTwitterBtn.getTag()) {
			eventTwitterBtn.setTag(false);
			eventTwitterBtn.setTextColor(res.getColor(R.color.red));
			eventHashBtn.setTextColor(res.getColor(R.color.gray));
		} else {
			eventTwitterBtn.setTextColor(res.getColor(R.color.gray));
			eventHashBtn.setTextColor(res.getColor(R.color.red));
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_social:
			return false;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onPause() {
		super.onPause();
		setFirstTime(false);
	}

}