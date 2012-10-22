package br.com.event.custom.io;

import java.util.LinkedList;
import java.util.List;

import oauth.signpost.OAuth;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import br.com.event.custom.R;
import br.com.event.custom.adapters.TimelineEventAdapter;
import br.com.event.custom.adapters.TimelineHashTagAdapter;
import br.com.event.custom.adapters.TwitterHashTagEndlessAdapter;
import br.com.event.custom.adapters.TwitterStatusEndlessAdapter;
import br.com.event.custom.util.Constants;
import br.com.event.custom.view.PullToRefreshListView;

public class TwitterManager {

	private ProgressDialog pd;
	
	private ProgressBar pb;

	private Twitter twitter;

	private Context context;

	private ListView timelineListView;

	private Resources res;

	public TwitterManager(Context context, SharedPreferences preferences) {
		this.context = context;
		res = context.getResources();
		twitter = new TwitterFactory().getInstance();
	}

	public void updateEventTimeline(ListView timelineListView) {
		this.timelineListView = timelineListView;
		showLoadingDialog();
		new UserTimelineTask().execute(true, Constants.EVENT_SCREEN_NAME);
	}

	public void updateHashTagTimeline(ListView timelineListView) {
		this.timelineListView = timelineListView;
		showLoadingDialog();
		new HashTagTimelineTask().execute(true);
	}
	
	public void updateUserTimeline(ListView tweetsListView, ProgressBar pb, String screenName, boolean isFirstTime) {
		this.timelineListView = tweetsListView;
		showProgressBar(pb);
		new UserTimelineTask().execute(isFirstTime, screenName);
	}

	private void showLoadingDialog() {
		pd = ProgressDialog.show(context, res.getString(R.string.working),
				res.getString(R.string.updating_tweets), true, false);
	}
	
	private void showProgressBar(ProgressBar pb) {
		this.pb = pb;
		pb.setVisibility(View.VISIBLE);
	}
	
	public class UserTimelineTask extends AsyncTask<Object, Void, List<twitter4j.Status>> {

		private Boolean isFirstTime;
		
		private String screenName;
		
		@Override
		protected List<twitter4j.Status> doInBackground(Object... object) {
			screenName  = (String)object[1];
			isFirstTime = (Boolean)object[0];
			// if (!isOnline()) return tweets; FIXME
			return fetchUserTimeline(screenName);
		}

		protected void onPostExecute(List<twitter4j.Status> statuses) {
			if (isFirstTime) {
				timelineListView.setAdapter(new TwitterStatusEndlessAdapter(
						new TimelineEventAdapter(context, statuses), twitter, screenName));
			} else {
				HeaderViewListAdapter a1 = (HeaderViewListAdapter)timelineListView.getAdapter();
				TwitterStatusEndlessAdapter a2 = (TwitterStatusEndlessAdapter)a1.getWrappedAdapter();
				TimelineEventAdapter wrapperAdapter = a2.getWrappedAdapter();
				LinkedList<twitter4j.Status> s = (LinkedList<twitter4j.Status>)wrapperAdapter.getList();
				
				for (twitter4j.Status status : statuses) {
					if (!s.contains(status)) s.addFirst(status);
				}
				// Call onRefreshComplete when the list has been refreshed.
	            ((PullToRefreshListView) timelineListView).onRefreshComplete();
			}
			if (pd != null) pd.dismiss();
			if (pb != null) pb.setVisibility(View.INVISIBLE);
		};

	}

	public class HashTagTimelineTask extends AsyncTask<Boolean, Void, List<Tweet>> {

		private Boolean isFirstTime;
		
		@Override
		protected List<Tweet> doInBackground(Boolean... object) {
			isFirstTime = object[0];
			// if (!isOnline()) return tweets; FIXME
			return fetchHashTaggedTweets();
		}

		protected void onPostExecute(List<Tweet> tweets) {
			if (isFirstTime) {
				timelineListView.setAdapter(new TwitterHashTagEndlessAdapter(
						new TimelineHashTagAdapter(context, tweets), twitter));
			} else {
				HeaderViewListAdapter a1 = (HeaderViewListAdapter)timelineListView.getAdapter();
				TwitterHashTagEndlessAdapter a2 = (TwitterHashTagEndlessAdapter)a1.getWrappedAdapter();
				TimelineHashTagAdapter wrapperAdapter = a2.getWrappedAdapter();
				LinkedList<Tweet> t = (LinkedList<Tweet>)wrapperAdapter.getList();
				
				for (Tweet tweet : tweets) {
					if (!t.contains(tweet)) t.addFirst(tweet);
				}
				// Call onRefreshComplete when the list has been refreshed.
	            ((PullToRefreshListView) timelineListView).onRefreshComplete();
			}
			pd.dismiss();
		};

	}
	
	/**
	 * fetches user specific tweets 
	 * @return
	 */
	public List<twitter4j.Status> fetchUserTimeline(String screenName) {
		List<twitter4j.Status> statuses = new LinkedList<twitter4j.Status>();
		
		try {
			statuses.addAll(twitter.getUserTimeline(screenName, new Paging(1)));
		} catch (TwitterException e) {
			e.printStackTrace();
			System.out
					.println("exception occured when trying to search for event statuses:" + e.getMessage());
		}
		return statuses;
	}
	
	/**
	 * fetches all tweets with the specified hashtag
	 * @return
	 */
	public List<Tweet> fetchHashTaggedTweets() {
		List<Tweet> tweets = new LinkedList<Tweet>();
		
		try {
			Query query = new Query(Constants.EVENT_HASH_TAG);
			query.setPage(1);
			tweets.addAll(twitter.search(query).getTweets());
		} catch (TwitterException e) {
			e.printStackTrace();
			System.out
					.println("exception occured when trying to search for event tweets");
		}
		return tweets;
	}
	
	public void sendTweet(SharedPreferences prefs, String msg) throws Exception {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		AccessToken a = new AccessToken(token, secret);
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
        twitter.updateStatus(msg);
	}
	
	public void clearCredentials() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		final Editor edit = prefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
	}

}