package com.google.imagesearch;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.imagesearch.dao.ImageDAO;
import com.google.imagesearch.model.Image;
import com.google.imagesearch.util.CustomHttpClient;
import com.google.imagesearch.util.JSONParser;
import com.gus.test.R;

public class ImageSearchActivity extends Activity {

	private static int imagesOffset;
	private TextView inputTxt;
	private List<Image> images;
	private ProgressDialog dialog;
	private ListView list;
	private ImageAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		images = new ArrayList<Image>();
		list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(itemClickListener);
		inputTxt = (TextView) findViewById(R.id.header_txt);
		Button searchBtn = (Button) findViewById(R.id.header_search_btn);
		searchBtn.setOnClickListener(searchListener);
	}

	@Override
	public void onDestroy() {
		ImageAdapter.getImageLoader().stopThread();
		ImageAdapter.getImageLoader().clearCache();
		list.setAdapter(null);
		super.onDestroy();
	}

	private void showProgressDialog() {
		dialog = ProgressDialog.show(ImageSearchActivity.this,
				getText(R.string.search), getText(R.string.wait), true, true);
	}

	private OnClickListener searchListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if (!isOnline()) {
				Toast.makeText(ImageSearchActivity.this, "No internet connection found", 3000).show();
				ArrayList<Image> images = ImageDAO.getInstance(
						ImageSearchActivity.this).fetchImagesFromTag(
						inputTxt.getText().toString());
				ImageSearchActivity.this.images = images;
				adapter = new ImageAdapter(ImageSearchActivity.this, images);
				list.setAdapter(adapter);
			} else {
				showProgressDialog();
				new DownloadImageTask().execute(inputTxt.getText().toString());
			}
		}
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Image image = images.get(position);
			Intent intent = new Intent(ImageSearchActivity.this,
					ImagePopupActivity.class);
			intent.putExtra("image", image);
			intent.putExtra("tag", inputTxt.getText().toString());
			startActivity(intent);
		}
	};

	private class DownloadImageTask extends
			AsyncTask<String, List<Image>, List<Image>> {

		protected void onPostExecute(final List<Image> images) {
			for (Image image : images) {
				ImageSearchActivity.this.images.add(image);
				if (adapter != null)
					adapter.notifyDataSetChanged();
			}

			if (adapter == null) {
				adapter = new ImageAdapter(ImageSearchActivity.this,
						ImageSearchActivity.this.images);
				Button loadBtn = new Button(ImageSearchActivity.this);
				loadBtn.setText(R.string.load_more);
				loadBtn.setOnClickListener(searchListener);
				list.addFooterView(loadBtn);
				list.setAdapter(adapter);
			}
			dialog.dismiss();
		}

		@Override
		protected List<Image> doInBackground(String... params) {
			int searchSize = 6;

			if (images != null) {
				imagesOffset += images.size();
			}

			String response = CustomHttpClient
					.executeHttpGet("http://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz="
							+ searchSize
							+ "&start="
							+ imagesOffset
							+ "&q="
							+ params[0]);

			return new JSONParser().parse(response);
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) return true;
		return false;
	}
}