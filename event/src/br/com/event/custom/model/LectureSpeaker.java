package br.com.event.custom.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class LectureSpeaker {

	public final static String LECTURE_ID_FIELD_NAME = "lecture_id";
	public final static String SPEAKER_ID_FIELD_NAME = "speaker_name";
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(foreign = true, columnName = LECTURE_ID_FIELD_NAME)
	private Lecture lecture;
	
	@DatabaseField(foreign = true, columnName = SPEAKER_ID_FIELD_NAME)
	private Speaker speaker;

	public LectureSpeaker() { }
	
	public LectureSpeaker(Lecture lecture, Speaker speaker) {
		this.lecture = lecture;
		this.speaker = speaker;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Lecture getLecture() {
		return lecture;
	}

	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}

	public Speaker getSpeaker() {
		return speaker;
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}
	
}
