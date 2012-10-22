package br.com.event.custom.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.model.Lecture;
import br.com.event.custom.model.LectureSpeaker;
import br.com.event.custom.model.Speaker;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class SpeakerDAO extends GenericDAO<Speaker> {

	public SpeakerDAO(EventDatabaseHelper helper) {
		super(helper);
	}
	
	@Override
	public List<Speaker> save(List<Speaker> speakers) {
		try {
			Dao<Speaker, Integer> dao = getDao();

			for (Speaker speaker : speakers) {
				dao.createIfNotExists(speaker);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return speakers;
	}
	
	@Override
	public List<Speaker> getAll() {
		List<Speaker> speakers = new ArrayList<Speaker>();
		try {
			speakers = getDao().queryForAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return speakers;
	}
	
	public List<Speaker> findByLecture(Lecture lecture) {
		List<Speaker> speakers = new ArrayList<Speaker>();
		
		QueryBuilder<LectureSpeaker, Integer> lectureSpeakerQB = getLectureSpeakerDao().queryBuilder(); 
		lectureSpeakerQB.selectColumns(LectureSpeaker.SPEAKER_ID_FIELD_NAME);
		try {
			lectureSpeakerQB.where().eq(LectureSpeaker.LECTURE_ID_FIELD_NAME, lecture);
			QueryBuilder<Speaker,Integer> speakerQB = getDao().queryBuilder();
			speakerQB.where().in(Speaker.SPEAKER_ID_FIELD_NAME, lectureSpeakerQB);
			PreparedQuery<Speaker> speakerPrepare = speakerQB.prepare();
			speakers = getDao().query(speakerPrepare);
		} catch (SQLException e) {
			e.printStackTrace();
			return speakers;
		}
		return speakers;
	}
	
	@Override
	public Dao<Speaker, Integer> getDao() {
		try {
			return (Dao<Speaker, Integer>) helper.getSpeakerDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Dao<LectureSpeaker,Integer> getLectureSpeakerDao() {
		try {
			return (Dao<LectureSpeaker, Integer>) helper.getLectureSpeakerDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Speaker save(Speaker speaker) {
		try {
			Dao<Speaker, Integer> dao = getDao();
			dao.create(speaker);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return speaker;
	}

	@Override
	public void update(Speaker speaker) {
		try {
			Dao<Speaker, Integer> dao = getDao();
			dao.update(speaker);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}