package br.com.event.custom.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.dao.LectureDAO;
import br.com.event.custom.model.Lecture;

public class NotePadActivity extends EventActivity {

	private Lecture lecture;
	
	private EditText userNote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notepad);
		((Button)findViewById(R.id.header1_back_btn)).setOnClickListener(new BackBtnListener());
		((TextView)findViewById(R.id.header1_title)).setText(R.string.notepad);
		
		lecture = (Lecture)getIntent().getExtras().get("lecture");
		lecture = new LectureDAO(getHelper()).getReference(lecture.getId());
		
		userNote = (EditText)findViewById(R.id.user_note);
		userNote.setText(lecture.getUserNote());
		
		((Button)findViewById(R.id.save_note_btn)).setOnClickListener(new SaveNoteListener());
	}
	
	private class SaveNoteListener implements OnClickListener {

		public void onClick(View v) {
			lecture.setUserNote(userNote.getText().toString().trim());
			new LectureDAO(getHelper()).update(lecture);
		}
		
	}
	
}