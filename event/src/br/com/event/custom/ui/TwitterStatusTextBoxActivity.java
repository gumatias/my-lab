package br.com.event.custom.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.event.custom.R;
import br.com.event.custom.io.TwitterManager;
import br.com.event.custom.util.Constants;
import br.com.event.custom.util.TwitterUtils;

public class TwitterStatusTextBoxActivity extends Activity {

	private TwitterManager twitterManager;
	
	private SharedPreferences prefs;
	
	private EditText messageExt;
	
	private Button tweetBtn;
	
	private final Handler mTwitterHandler = new Handler();

	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {
			Toast.makeText(getBaseContext(), getResources().getString(R.string.tweet_sent),
					Toast.LENGTH_LONG).show();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.twitter_status_box);
		
		((TextView)findViewById(R.id.dialog_title))
			.setText(getResources().getString(R.string.twitter));
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		twitterManager = new TwitterManager(this, prefs);

		tweetBtn   = (Button)findViewById(R.id.twitter_tweet_btn);
		messageExt = (EditText)findViewById(R.id.twitter_message_ext);
		
		messageExt.addTextChangedListener(new UpdateTextWatcher());
		tweetBtn.setOnClickListener(new TweetMessageListener());
		
		messageExt.setText(Constants.EVENT_HASH_TAG);
	}

	class TweetMessageListener implements OnClickListener {

		public void onClick(View v) {
			tweet();
			finish();
		}
		
	}
	
	class UpdateTextWatcher implements TextWatcher {

		public void afterTextChanged(Editable s) {

		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (s.length() >= Constants.TWEET_LIMIT) {
				Resources res 	= getResources();
				String errorMsg = res.getString(R.string.tweet_limit);
				messageExt.setError(errorMsg);
				String s1 = messageExt.getText().toString().substring(0, Constants.TWEET_LIMIT - 1);
				messageExt.setText(s1);
			}
		}

	}
	
	private void tweet() {
		String message = messageExt.getText().toString();
		
		if (TwitterUtils.isUserAuthenticated(prefs)) {
			sendTweet(message);
		} else {
			Intent i = new Intent(getApplicationContext(), PrepareRequestTokenActivity.class);
			i.putExtra("tweet_msg", message);
			startActivity(i);
		}
		
	}
	
	private void sendTweet(final String message) {
		new Thread() {
			public void run() {

				try {
					twitterManager.sendTweet(prefs, message);
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}.start();
	}

}
