package br.com.event.custom.io;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.event.custom.R;
import br.com.event.custom.model.Category;
import br.com.event.custom.model.Establishment;
import br.com.event.custom.model.EstablishmentType;
import br.com.event.custom.model.Event;
import br.com.event.custom.model.Lecture;
import br.com.event.custom.model.LectureSpeaker;
import br.com.event.custom.model.Picture;
import br.com.event.custom.model.Place;
import br.com.event.custom.model.Speaker;
import br.com.event.custom.model.Sponsor;
import br.com.event.custom.model.TwitterAccount;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class EventDatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "event.db";

	private static final int DATABASE_VERSION = 1;

	private Dao<Event, Integer> eventDao 							= null;
	private Dao<Lecture, Integer> lectureDao 						= null;
	private Dao<Establishment, Integer> establishmentDao 			= null;
	private Dao<Category, Integer> categoryDao 						= null;
	private Dao<Place, Integer> placeDao 							= null;
	private Dao<Speaker, Integer> speakerDao 						= null;
	private Dao<Sponsor, Integer> sponsorDao 						= null;
	private Dao<Picture, Integer> pictureDao 						= null;
	private Dao<EstablishmentType, Integer> establishmentTypeDao	= null;
	private Dao<LectureSpeaker, Integer> lsDao 						= null;
	private Dao<TwitterAccount, Integer> twitterAccountDao 			= null;

	public EventDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config);
	}

	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Event.class);
			TableUtils.createTable(connectionSource, Category.class);
			TableUtils.createTable(connectionSource, Place.class);
			TableUtils.createTable(connectionSource, Lecture.class);
			TableUtils.createTable(connectionSource, Speaker.class);
			TableUtils.createTable(connectionSource, Sponsor.class);
			TableUtils.createTable(connectionSource, Establishment.class);
			TableUtils.createTable(connectionSource, Picture.class);
			TableUtils.createTable(connectionSource, LectureSpeaker.class);
			TableUtils.createTable(connectionSource, EstablishmentType.class);
			TableUtils.createTable(connectionSource, TwitterAccount.class);
		} catch (SQLException e) {
			Log.e(EventDatabaseHelper.class.getName(), "Can't create database",
					e);
			throw new RuntimeException(e);
		}
	}

	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, Event.class, 			true);
			TableUtils.dropTable(connectionSource, Lecture.class, 			true);
			TableUtils.dropTable(connectionSource, Establishment.class, 	true);
			TableUtils.dropTable(connectionSource, Category.class, 			true);
			TableUtils.dropTable(connectionSource, Place.class, 			true);
			TableUtils.dropTable(connectionSource, Speaker.class, 			true);
			TableUtils.dropTable(connectionSource, Sponsor.class, 			true);
			TableUtils.dropTable(connectionSource, Picture.class, 			true);
			TableUtils.dropTable(connectionSource, LectureSpeaker.class,	true);
			TableUtils.dropTable(connectionSource, EstablishmentType.class,	true);
			TableUtils.dropTable(connectionSource, TwitterAccount.class,	true);
			
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(EventDatabaseHelper.class.getName(), "Can't drop databases",
					e);
			throw new RuntimeException(e);
		}
	}
	
	public Dao<Event, Integer> getEventDao() throws SQLException {
		if (eventDao == null) eventDao = getDao(Event.class);
		return eventDao;
	}
	
	public Dao<Lecture, Integer> getLectureDao() throws SQLException {
		if (lectureDao == null) lectureDao = getDao(Lecture.class);
		return lectureDao;
	}
	
	public Dao<Establishment, Integer> getEstablishmentDao() throws SQLException {
		if (establishmentDao == null) establishmentDao = getDao(Establishment.class);
		return establishmentDao;
	}
	
	public Dao<Category, Integer> getCategoryDao() throws SQLException {
		if (categoryDao == null) categoryDao = getDao(Category.class);
		return categoryDao;
	}
	
	public Dao<Place, Integer> getPlaceDao() throws SQLException {
		if (placeDao == null) placeDao = getDao(Place.class);
		return placeDao;
	}
	
	public Dao<Speaker, Integer> getSpeakerDao() throws SQLException {
		if (speakerDao == null) speakerDao = getDao(Speaker.class);
		return speakerDao;
	}
	
	public Dao<Sponsor, Integer> getSponsorDao() throws SQLException {
		if (sponsorDao == null) sponsorDao = getDao(Sponsor.class);
		return sponsorDao;
	}
	
	public Dao<Picture, Integer> getPictureDao() throws SQLException {
		if (pictureDao == null) pictureDao = getDao(Picture.class);
		return pictureDao;
	}
	
	public Dao<LectureSpeaker, Integer> getLectureSpeakerDao() throws SQLException {
		if (lsDao == null) lsDao = getDao(LectureSpeaker.class);
		return lsDao;
	}
	
	public Dao<EstablishmentType, Integer> getEstablishmentTypeDao() throws SQLException {
		if (establishmentTypeDao == null) establishmentTypeDao = getDao(EstablishmentType.class);
		return establishmentTypeDao;
	}
	
	public Dao<TwitterAccount, Integer> getTwitterAccountDao() throws SQLException {
		if (twitterAccountDao == null) twitterAccountDao = getDao(TwitterAccount.class);
		return twitterAccountDao;
	}
	
	@Override
	public void close() {
		super.close();
		eventDao 			= null;
		lectureDao			= null;
		establishmentDao 	= null;
		categoryDao 		= null;
		placeDao 			= null;
		speakerDao 			= null;
		sponsorDao 			= null;
		pictureDao			= null;
		lsDao				= null;
		establishmentTypeDao= null;
		twitterAccountDao   = null;
	}

}
