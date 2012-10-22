package br.com.event.custom.ui;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import br.com.event.custom.R;
import br.com.event.custom.infra.DrawableManager;
import br.com.event.custom.view.PinchImageView;

public class PicturePreviewActivity extends EventActivity {

	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_pic_preview);
		
		String imgUrl = getIntent().getExtras().getString("image_url");
		loadImage(imgUrl);
	}

	private void loadImage(String imgUrl) {
		pd = ProgressDialog.show(this, getString(R.string.working), 
				   getString(R.string.working),
				   true, false);
		new PictureTask().execute(imgUrl);
	}
	
	class PictureTask extends AsyncTask<String, String, Drawable> {

		@Override
		protected Drawable doInBackground(String... params) {
			return new DrawableManager(PicturePreviewActivity.this, true).fetchDrawable(params[0]);		
		}
		
		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			PinchImageView pictureView = (PinchImageView) findViewById(R.id.gallery_picture);
			pictureView.setImageDrawable(result);
			pd.dismiss();
		}
		
	}
	
}