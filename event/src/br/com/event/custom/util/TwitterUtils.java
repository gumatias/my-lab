package br.com.event.custom.util;

import oauth.signpost.OAuth;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.content.SharedPreferences;

public class TwitterUtils {

	public static boolean isUserAuthenticated(SharedPreferences prefs) {
		String token  = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		return authenticate(token, secret);
	}
	
	public static boolean authenticateReader() {
		return authenticate(Constants.READER_ACCESS_TOKEN, Constants.READER_ACCESS_SECRET);
	}
	
	public static boolean authenticate(String token, String secret) {
		try {
			AccessToken a 	= new AccessToken(token, secret);
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
			twitter.setOAuthAccessToken(a);
			twitter.getAccountSettings();
			return true;
		} catch (TwitterException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
}