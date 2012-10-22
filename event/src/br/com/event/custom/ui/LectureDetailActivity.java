package br.com.event.custom.ui;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.adapters.LectureSpeakersAdapter;
import br.com.event.custom.dao.SpeakerDAO;
import br.com.event.custom.model.Lecture;
import br.com.event.custom.model.Speaker;

public class LectureDetailActivity extends EventActivity {

	private Lecture lecture;

	private TextView timeTxt;

	private TextView dateTxt;

	private TextView placeTxt;

	private TextView descriptionTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lecture_detailed);
		((TextView) findViewById(R.id.header2_title)).setText(R.string.lecture);
		((Button) findViewById(R.id.lecture_back_btn))
				.setOnClickListener(new BackBtnListener());

		lecture = (Lecture) getIntent().getExtras().get("lecture");
		List<Speaker> speakers = new SpeakerDAO(getHelper())
				.findByLecture(lecture);

		((TextView) findViewById(R.id.lecture_title)).setText(lecture.getName()
				.trim());

		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM/dd");
		dateTxt = (TextView) findViewById(R.id.lecture_date);
		dateTxt.setText(dateFormat.format(lecture.getInitialDate()));

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeTxt = (TextView) findViewById(R.id.lecture_time);
		timeTxt.setText(timeFormat.format(lecture.getInitialDate()) + " - "
				+ timeFormat.format(lecture.getEndDate()));

		placeTxt = (TextView) findViewById(R.id.lecture_place);
		placeTxt.setText(lecture.getPlace().getName());

		descriptionTxt = (TextView) findViewById(R.id.lecture_description);
		descriptionTxt.setText(Html.fromHtml(lecture.getDescription()));

		ListView speakersList = (ListView) findViewById(R.id.lecture_speakers_listview);
		speakersList.setAdapter(new LectureSpeakersAdapter(this, speakers));

		Button notePadBtn = (Button) findViewById(R.id.notepad_btn);
		notePadBtn.setOnClickListener(new NotePadClickListener());

		Button shareBtn = (Button) findViewById(R.id.share_btn);
		shareBtn.setOnClickListener(new ShareClickListener());
	}

	class NotePadClickListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent(LectureDetailActivity.this,
					NotePadActivity.class);
			intent.putExtra("lecture", lecture);
			startActivity(intent);
		}

	}

	class ShareClickListener implements OnClickListener {

		public void onClick(View v) {
			sendEmail();
		}

	}

	private void sendEmail() {
		Resources res = getResources();
		String message = getText(R.string.share_lecture_body) + "\n\n"
				+ res.getString(R.string.date) + ": " + timeTxt.getText()
				+ dateTxt.getText() + "\n\n" + res.getString(R.string.place)
				+ ": " + placeTxt.getText() + "\n\n"
				+ res.getString(R.string.lecture_desc) + "\n\n"
				+ descriptionTxt.getText();

		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT,
				getText(R.string.share_lecture_subject));
		sendIntent.putExtra(Intent.EXTRA_TEXT, message);
		sendIntent.setType("plain/text");

		startActivity(Intent.createChooser(sendIntent, "Event email"));
	}

}