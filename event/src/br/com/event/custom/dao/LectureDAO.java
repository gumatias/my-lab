package br.com.event.custom.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.model.Category;
import br.com.event.custom.model.Lecture;
import br.com.event.custom.model.LectureSpeaker;
import br.com.event.custom.model.Place;
import br.com.event.custom.model.Speaker;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class LectureDAO extends GenericDAO<Lecture> {

	public LectureDAO(EventDatabaseHelper helper) {
		super(helper);
	}
	
	@Override
	public List<Lecture> save(List<Lecture> lectures) {
		try {
			Dao<Lecture, Integer> 			lDao 	= getDao();
			Dao<Speaker, Integer> 			sDao 	= getSpeakerDao();
			Dao<LectureSpeaker, Integer> 	lsDao 	= getLectureSpeakerDao();
			Dao<Category, Integer> 			cDao 	= getCategoryDao();
			Dao<Place, Integer> 			pDao 	= getPlaceDao();

			for (Lecture lecture : lectures) {
				pDao.createIfNotExists(lecture.getPlace());
				cDao.createIfNotExists(lecture.getCategory());
				
				lecture.setUserFavorite(false);
				lDao.createIfNotExists(lecture);
				for (Speaker s1 : lecture.getSpeakers()) {
					Speaker s2 = sDao.createIfNotExists(s1);
					lsDao.create(new LectureSpeaker(lecture, s2));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return lectures;
	}
	
	public List<Lecture> findBySpeaker(Speaker speaker) {
		List<Lecture> lectures = new ArrayList<Lecture>();
		
		QueryBuilder<LectureSpeaker, Integer> lectureSpeakerQB = getLectureSpeakerDao().queryBuilder(); 
		lectureSpeakerQB.selectColumns(LectureSpeaker.LECTURE_ID_FIELD_NAME);
		try {
			lectureSpeakerQB.where().eq(LectureSpeaker.SPEAKER_ID_FIELD_NAME, speaker);
			QueryBuilder<Lecture, Integer> lectureQB = getDao().queryBuilder();
			lectureQB.where().in(Lecture.ID_FIELD_NAME, lectureSpeakerQB);
			PreparedQuery<Lecture> lecturePrepare = lectureQB.prepare();
			
			return getDao().query(lecturePrepare);
		} catch (SQLException e) {
			e.printStackTrace();
			return lectures;
		}
	}
	
	public List<Lecture> findByCategory(Category category) {
		List<Lecture> lectures = new ArrayList<Lecture>();

		try {
			return getDao().queryBuilder().where().eq(Category.ID_FIELD_NAME, category).query();
		} catch (SQLException e) {
			e.printStackTrace();
			return lectures;
		}
	}
	
	@Override
	public List<Lecture> getAll() {
		List<Lecture> list = new ArrayList<Lecture>();
		try {
			list = getDao().queryForAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return list;
	}
	
	public List<Lecture> getAllFromUser() {
		List<Lecture> list = new ArrayList<Lecture>();
		try {
			list = getDao().queryForEq(Lecture.USER_FAVORITE_FIELD_NAME, true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return list;
	}
	
	@Override
	public Dao<Lecture, Integer> getDao() {
		try {
			return (Dao<Lecture, Integer>) helper.getLectureDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Dao<Speaker, Integer> getSpeakerDao() {
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
	
	private Dao<Place, Integer> getPlaceDao() {
		try {
			return (Dao<Place, Integer>) helper.getPlaceDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private Dao<Category, Integer> getCategoryDao() {
		try {
			return (Dao<Category, Integer>) helper.getCategoryDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Lecture save(Lecture lecture) {
		try {
			Dao<Lecture, Integer> dao = getDao();
			dao.create(lecture);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return lecture;
	}
	
	public void update(Lecture lecture) {
		try {
			Dao<Lecture, Integer> dao = getDao();
			dao.update(lecture);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Lecture getReference(int id) {
		try {
			Dao<Lecture, Integer> dao = getDao();
			return dao.queryForId(id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}