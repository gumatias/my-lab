package br.com.event.custom.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.adapters.SpeakerLecturesAdapter;
import br.com.event.custom.dao.LectureDAO;
import br.com.event.custom.infra.DrawableManager;
import br.com.event.custom.model.Lecture;
import br.com.event.custom.model.Speaker;

public class SpeakerDetailActivity extends EventActivity {
	
	private Speaker speaker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speakers_description);
		speaker = (Speaker)getIntent().getExtras().get("speaker");
		
		((TextView)findViewById(R.id.header_title)).setText(R.string.speaker);
		((Button)findViewById(R.id.header_back_btn)).setOnClickListener(new BackBtnListener());
		Button userTwitterBtn = (Button)findViewById(R.id.tweet_btn);
		if (speaker.getTwitter() == null || speaker.getTwitter().trim().equals("")) userTwitterBtn.setVisibility(View.INVISIBLE);
		else userTwitterBtn.setOnClickListener(new SpeakerTwitterBtnListener());
		
		TextView name 			= (TextView) findViewById(R.id.speaker_name);
		TextView description 	= (TextView) findViewById(R.id.speaker_description);
		ImageView speakerPic 	= (ImageView) findViewById(R.id.speaker_picture);
		
		name.setText(speaker.getName());
		description.setText(Html.fromHtml(speaker.getDescription()));
		new DrawableManager(this, true).
			fetchDrawableOnThread(speaker.getImgUrl(), speakerPic);
		
		List<Lecture> lectures = new LectureDAO(getHelper()).findBySpeaker(speaker);
		ListView lecturesList = (ListView)findViewById(R.id.speakers_lectures_listview);
		lecturesList.setAdapter(new SpeakerLecturesAdapter(this, lectures));
	}

	class SpeakerTwitterBtnListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent(SpeakerDetailActivity.this, 
									 	UserTimelineActivity.class);
			intent.putExtra("user_screen_name", speaker.getTwitter());
			startActivity(intent);
		}
		
	}
	
}
