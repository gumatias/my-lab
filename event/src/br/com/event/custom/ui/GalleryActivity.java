package br.com.event.custom.ui;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.adapters.PictureAdapter;
import br.com.event.custom.dao.PictureDAO;
import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.io.EventManager;
import br.com.event.custom.model.Gallery;
import br.com.event.custom.model.Picture;

public class GalleryActivity extends EventActivity {
	
	private List<Picture> pictures;
	
	private ProgressBar pb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_main);

		((TextView)findViewById(R.id.header1_title)).setText(R.string.media);
		((Button)findViewById(R.id.header1_back_btn)).setOnClickListener(new BackBtnListener());
		loadGallery();
	}
	
	private void loadGallery() {
		pb = (ProgressBar)findViewById(R.id.refresh_progress);
		pb.setVisibility(View.VISIBLE);
		new GalleryTask().execute();
	}
	
	class GalleryTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			pictures = fetchPictures(getHelper());
			if (pictures == null) return null;
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Context ctxt = GalleryActivity.this;
			
			if (result == null) {
				showDialog(INTERNET_FAILURE_DIALOG);
				return;
			}
			
			GridView media = (GridView) findViewById(R.id.gallery_view);
			media.setAdapter(new PictureAdapter(ctxt, pictures));
			pb.setVisibility(View.INVISIBLE);
		}
		
	}
	
	private List<Picture> fetchPictures(EventDatabaseHelper helper) {
		PictureDAO dao = new PictureDAO(getHelper());
		List<Picture> pictures = dao.getAll();
		
		if (pictures.isEmpty()) {
			List<Gallery> galleries = new EventManager().getGalleries();
			if (galleries == null) return null;
			
			pictures = galleries.get(0).getPictures();
			pictures = dao.save(pictures);
		}
		return pictures;
	}
	
}