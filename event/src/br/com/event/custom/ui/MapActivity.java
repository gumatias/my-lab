package br.com.event.custom.ui;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.dao.EventDAO;
import br.com.event.custom.infra.DrawableManager;
import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.model.Event;
import br.com.event.custom.view.PinchImageView;

public class MapActivity extends EventActivity {
	
	private Event event;
	
	private Drawable eventMap;
	
	private ProgressBar pb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventmap_main);
		((TextView)findViewById(R.id.header1_title)).setText(R.string.map);
		((Button)findViewById(R.id.header1_back_btn)).setOnClickListener(new BackBtnListener());
		loadMapImage();
	}
	
	private void loadMapImage() {
		pb = (ProgressBar)findViewById(R.id.refresh_progress);
		pb.setVisibility(View.VISIBLE);
		new DirectionTask().execute();
	}
	
	class DirectionTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			event 			= fetchEvent(getHelper());
			eventMap		= new DrawableManager(MapActivity.this, false)
										.fetchDrawable(event.getMapUrl());
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if (!isOnline()) {
				showDialog(INTERNET_FAILURE_DIALOG);
				return;
			}
			
			PinchImageView mapView = (PinchImageView) findViewById(R.id.map_event);
			mapView.setImageDrawable(eventMap);
			pb.setVisibility(View.INVISIBLE);
		}
		
	}
	
	private Event fetchEvent(EventDatabaseHelper helper) {
		return new EventDAO(helper).getAll().get(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event_menu, menu);
		return true;
	}
	
}
