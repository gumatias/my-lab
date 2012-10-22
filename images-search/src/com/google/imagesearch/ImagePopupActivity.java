package com.google.imagesearch;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.imagesearch.dao.ImageDAO;
import com.google.imagesearch.model.Image;
import com.gus.test.R;

public class ImagePopupActivity extends Activity {

	private CheckBox offlineChk;
	private ImageView imageView;
	private Image image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_popup);
		
		image = (Image) getIntent().getSerializableExtra("image");

		TextView imageTitleTxt = (TextView) findViewById(R.id.img_title_popup);
		imageTitleTxt.setText(image.getTitle());
		imageView = (ImageView) findViewById(R.id.img_popup);
		ImageAdapter.getImageLoader().displayImage(image, this, imageView);
		offlineChk = (CheckBox) findViewById(R.id.offline_img_chk);

		Button okBtn = (Button) findViewById(R.id.img_ok_btn);
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				saveImage();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) saveImage();
		return super.onKeyDown(keyCode, event);
	}

	private void saveImage() {
		if (!offlineChk.isChecked()) return;
		
		String tag = getIntent().getStringExtra("tag");
		ImageDAO.getInstance(this).insertImage(image, tag);
	}
}
