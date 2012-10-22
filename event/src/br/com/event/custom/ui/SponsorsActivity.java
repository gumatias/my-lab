package br.com.event.custom.ui;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.adapters.SponsorAdapter;
import br.com.event.custom.dao.SponsorDAO;
import br.com.event.custom.io.EventManager;
import br.com.event.custom.model.Sponsor;

public class SponsorsActivity extends EventActivity {

	private ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sponsors_main);
		((TextView)findViewById(R.id.header1_title)).setText(R.string.sponsors);
		((Button)findViewById(R.id.header1_back_btn)).setOnClickListener(new BackBtnListener());
		loadSponsors();
	}

	private void loadSponsors() {
		pb = (ProgressBar)findViewById(R.id.refresh_progress);
		pb.setVisibility(View.VISIBLE);
		new SponsorTask().execute();
	}

	class SponsorTask extends AsyncTask<String, Object, List<Sponsor>> {

		@Override
		protected List<Sponsor> doInBackground(String... params) {
			return fetchSponsors();
		}

		@Override
		protected void onPostExecute(List<Sponsor> sponsors) {
			super.onPostExecute(sponsors);
			Context ctxt = SponsorsActivity.this;
			
			if (sponsors == null) {
				showDialog(INTERNET_FAILURE_DIALOG);
				return;
			}
			
			ListView listView = (ListView) findViewById(R.id.sponsors_listview);
			listView.setAdapter(new SponsorAdapter(ctxt, sponsors));
			pb.setVisibility(View.INVISIBLE);
		}

	}

	/**
	 * return if already exists in the database, otherwise get it from the
	 * sever.
	 * 
	 * @param sponsors
	 * @return
	 */
	private List<Sponsor> fetchSponsors() {
		SponsorDAO dao = new SponsorDAO(getHelper());
		List<Sponsor> sponsors = dao.getAll();

		if (sponsors.isEmpty()) {
			List<Sponsor> s = new EventManager().getSponsors();
			if (s == null) return null;
			sponsors = dao.save(s);
		}
		return sponsors;
	}
}
