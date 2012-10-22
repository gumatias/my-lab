package br.com.event.custom.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.io.TwitterManager;
import br.com.event.custom.view.PullToRefreshListView;
import br.com.event.custom.view.PullToRefreshListView.OnRefreshListener;

public class UserTimelineActivity extends EventActivity {

	private PullToRefreshListView timelineListView;

	private TwitterManager twitterManager;

	private ProgressBar pb;
	
	private String screenName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_timeline);
		screenName = (String) getIntent().getExtras().get("user_screen_name");
		
		((TextView) findViewById(R.id.header1_title)).setText(R.string.speaker);
		((TextView) findViewById(R.id.user_screen_name)).setText("@" + screenName);
		((Button) findViewById(R.id.header1_back_btn)).setOnClickListener(new BackBtnListener());
		
		pb = (ProgressBar) findViewById(R.id.refresh_progress);
		
		if (!isOnline()) {
			showDialog(INTERNET_FAILURE_DIALOG);
			return;
		}
		
		twitterManager   = new TwitterManager(this, getPreferences());
		timelineListView = (PullToRefreshListView) findViewById(R.id.timeline);
		
		fetchTimeline(true);
		((PullToRefreshListView) timelineListView).setOnRefreshListener(new OnRefreshListener() {

			public void onRefresh() {
				fetchTimeline(false);
		    }
			
		});
	}
	
	private void fetchTimeline(boolean isFirstTime) {
		twitterManager.updateUserTimeline(timelineListView, pb, screenName, isFirstTime);
	}

}