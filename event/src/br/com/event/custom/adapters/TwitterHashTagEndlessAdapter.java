package br.com.event.custom.adapters;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import br.com.event.custom.R;
import br.com.event.custom.util.Constants;

public class TwitterHashTagEndlessAdapter extends EndlessAdapter {

	private Twitter twitter;
	
	private List<Tweet> tweets;
	
	private static Integer page = 2;
	
	public TwitterHashTagEndlessAdapter(TimelineHashTagAdapter adapter, Twitter twitter) {
		super(adapter.getContext(), adapter, R.layout.endlesslist_loading);
		this.twitter = twitter;
	}

	@Override
	protected boolean cacheInBackground() throws Exception {
		try {
			tweets = new ArrayList<Tweet>();
			Query query = new Query(Constants.EVENT_HASH_TAG);
			query.setPage(page++);
			tweets = twitter.search(query).getTweets();
		} catch (TwitterException e) {
			System.out.println("exception occured when trying to search for event tweets");
		}

		if (getWrappedAdapter().getCount() < 75) {
			return (true);
		}

		throw new Exception("Gadzooks!");
	}

	@Override
	protected void appendCachedData() {
		if (getWrappedAdapter().getCount() < 75) {
			TimelineHashTagAdapter a = getWrappedAdapter();
			a.getList().addAll(tweets);
		}
	}
	
	public TimelineHashTagAdapter getWrappedAdapter() {
		return (TimelineHashTagAdapter) super.getWrappedAdapter();
	}
}