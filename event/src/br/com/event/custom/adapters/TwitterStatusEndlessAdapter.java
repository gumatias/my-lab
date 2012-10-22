package br.com.event.custom.adapters;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import br.com.event.custom.R;

public class TwitterStatusEndlessAdapter extends EndlessAdapter {

	private Twitter twitter;
	
	private String screenName;
	
	private List<Status> statuses;
	
	private static Integer page = 2;
	
	public TwitterStatusEndlessAdapter(TimelineEventAdapter adapter, Twitter twitter, String screenName) {
		super(adapter.getContext(), adapter, R.layout.endlesslist_loading);
		this.twitter 	= twitter;
		this.screenName = screenName;
	}

	@Override
	protected boolean cacheInBackground() throws Exception {
		try {
			statuses = new ArrayList<Status>();
			statuses = twitter.getUserTimeline(screenName, new Paging(page++));
		} catch (TwitterException e) {
			System.out.println("exception occured when trying to search for event statuses");
		}

		if (getWrappedAdapter().getCount() < 75) {
			return (true);
		}

		throw new Exception("Gadzooks!");
	}

	@Override
	protected void appendCachedData() {
		if (getWrappedAdapter().getCount() < 75) {
			TimelineEventAdapter adapter = getWrappedAdapter();
			adapter.getList().addAll(statuses);
		}
	}

	public TimelineEventAdapter getWrappedAdapter() {
		return (TimelineEventAdapter) super.getWrappedAdapter();
	}

}