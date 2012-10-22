package br.com.event.custom.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.model.Spot;

public class SpotDescriptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window w = getWindow();
		w.requestFeature(Window.FEATURE_LEFT_ICON);
		
		setContentView(R.layout.map_place_description);

		w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				android.R.drawable.ic_dialog_info);
		
		Button okBtn = (Button)findViewById(R.id.map_place_desc_ok_btn);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		ImageView iconImg 	= (ImageView)findViewById(R.id.map_place_desc_icon);
		TextView descTxt 	= (TextView) findViewById(R.id.map_place_desc_body);
		TextView addrTxt 	= (TextView) findViewById(R.id.map_place_desc_address);
		TextView nameTxt 	= (TextView) findViewById(R.id.map_place_desc_name);
		TextView phoneTxt 	= (TextView) findViewById(R.id.map_place_desc_phone);
		
		Spot spot = (Spot)getIntent().getExtras().get("spot");
		setTitle(spot.getSpotType().title());
		iconImg	.setImageResource(spot.getSpotType().image());
		descTxt	.setText(Html.fromHtml(spot.getDescription()));
		addrTxt	.setText(spot.getAddress());
		nameTxt	.setText(spot.getName());
		phoneTxt.setText(spot.getPhone());
	}
	
}