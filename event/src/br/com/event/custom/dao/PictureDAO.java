package br.com.event.custom.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.model.Picture;

import com.j256.ormlite.dao.Dao;

public class PictureDAO extends GenericDAO<Picture> {

	public PictureDAO(EventDatabaseHelper helper) {
		super(helper);
	}
	
	@Override
	public List<Picture> save(List<Picture> pictures) {
		try {
			Dao<Picture, Integer> dao = getDao();

			for (Picture picture : pictures) {
				dao.create(picture);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return pictures;
	}
	
	@Override
	public List<Picture> getAll() {
		List<Picture> list = new ArrayList<Picture>();
		try {
			list = getDao().queryForAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return list;
	}
	
	@Override
	public Dao<Picture, Integer> getDao() {
		try {
			return (Dao<Picture, Integer>) helper.getPictureDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Picture save(Picture picture) {
		try {
			Dao<Picture, Integer> dao = getDao();
			dao.create(picture);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return picture;
	}

	@Override
	public void update(Picture picture) {
		try {
			Dao<Picture, Integer> dao = getDao();
			dao.update(picture);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}