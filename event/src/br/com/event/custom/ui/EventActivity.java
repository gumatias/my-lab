package br.com.event.custom.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import br.com.event.custom.R;
import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.view.CustomDialog;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class EventActivity extends OrmLiteBaseActivity<EventDatabaseHelper> {

	public static final String EVENT_PREF = "EVENT_PREFERENCES";

	public static final String FIRT_TIME = "app_first_time_execution";

	public static final int DEFAULT_DIALOG = 1;
	
	public static final int INTERNET_FAILURE_DIALOG = 2;
	
	public static final int TWITTER_BOX_DIALOG = 3;

	public static final int EMPTY_SCHEDULE_DIALOG = 4;
	
	protected Resources res;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		res = getResources();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_more:
			startActivity(new Intent(this, MoreActivity.class));
			break;
		case R.id.menu_social:
			startActivity(new Intent(this, TwitterActivity.class));
			break;
		case R.id.menu_speakers:
			startActivity(new Intent(this, SpeakersActivity.class));
			break;
		case R.id.menu_schedule:
			startActivity(new Intent(this, ScheduleActivity.class));
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	protected SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(this);
	}

	public boolean isOnline() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager.getActiveNetworkInfo() != null)
			return connectivityManager.getActiveNetworkInfo()
					.isConnectedOrConnecting();
		else
			return false;
	}
	
	@Override
	protected Dialog onCreateDialog(int dialogId) {
		Dialog dialog = null;
		CustomDialog.Builder customBuilder = null;
		Resources res = getResources();
		
		switch (dialogId) {
		case INTERNET_FAILURE_DIALOG:
			customBuilder = new CustomDialog.Builder(this);
			customBuilder
					.setTitle(res.getString(R.string.failure))
					.setMessage(res.getString(R.string.no_internet))
					.setPositiveButton(res.getString(R.string.ok),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									finish();
								}
							});
			dialog = customBuilder.create();
			break;
		case EMPTY_SCHEDULE_DIALOG:
			customBuilder = new CustomDialog.Builder(this);
			customBuilder
					.setTitle(res.getString(R.string.warning)) 
					.setMessage(res.getString(R.string.empty_schedule))
					.setPositiveButton(res.getString(R.string.ok),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
			dialog = customBuilder.create();
			break;
		case DEFAULT_DIALOG:
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder
					.setTitle("Default title")
					.setMessage("Default body")
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
			dialog = alertBuilder.create();
			break;
		}
		return dialog;
	}

	protected boolean isFirstTime() {
		return getPreferences().getBoolean(FIRT_TIME, true);
	}

	protected void setFirstTime(boolean isFirstTime) {
		Editor editor = getPreferences().edit();
		editor.putBoolean(FIRT_TIME, isFirstTime);
		editor.commit();
	}

	protected class BackBtnListener implements OnClickListener {

		public void onClick(View v) {
			finish();
		}

	}

}