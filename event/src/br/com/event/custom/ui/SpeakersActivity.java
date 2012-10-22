package br.com.event.custom.ui;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.adapters.SpeakerAdapter;
import br.com.event.custom.dao.SpeakerDAO;
import br.com.event.custom.io.EventManager;
import br.com.event.custom.model.Speaker;

public class SpeakersActivity extends EventActivity {
	
	private List<Speaker> speakers;
	
	private ProgressBar pb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speakers_main);
		((TextView)findViewById(R.id.header1_title)).setText(R.string.menu_speakers);
		((Button)findViewById(R.id.header1_back_btn)).setOnClickListener(new BackBtnListener());
		loadSpeakers();
	}
	
	private void loadSpeakers() {
		pb = (ProgressBar)findViewById(R.id.refresh_progress);
		pb.setVisibility(View.VISIBLE);
		new SpeakersTask().execute();
	}
	
	class SpeakersTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			speakers = fetchSpeakers();
			if (speakers == null) return null;
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Context ctxt = SpeakersActivity.this;
			
			if (result == null) {
				showDialog(INTERNET_FAILURE_DIALOG);
				return;
			}
			
			ListView listView = (ListView)findViewById(R.id.speakers_listview);
			listView.setAdapter(new SpeakerAdapter(ctxt, speakers));
			pb.setVisibility(View.INVISIBLE);
		}
		
	}
	
	/**
	 * return if already exists in the database, otherwise get it from the sever.
	 * @param speakers
	 * @return
	 */
	private List<Speaker> fetchSpeakers() {
		SpeakerDAO dao = new SpeakerDAO(getHelper());
		List<Speaker> speakers = dao.getAll();
		
		if (speakers.isEmpty() || isFirstTime()) speakers = dao.save(new EventManager().getSpeakers());
		return speakers;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_speakers:
				return false;
		}
		return super.onMenuItemSelected(featureId, item);
	}

}