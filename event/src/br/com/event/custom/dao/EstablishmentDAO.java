package br.com.event.custom.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.model.Establishment;

import com.j256.ormlite.dao.Dao;

public class EstablishmentDAO extends GenericDAO<Establishment> {

	public EstablishmentDAO(EventDatabaseHelper helper) {
		super(helper);
	}
	
	@Override
	public List<Establishment> save(List<Establishment> establishments) {
		try {
			Dao<Establishment, Integer> dao = getDao();
			for (Establishment establishment : establishments) {
				dao.create(establishment);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return establishments;
	}
	
	@Override
	public List<Establishment> getAll() {
		List<Establishment> list = new ArrayList<Establishment>();
		try {
			list = getDao().queryForAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return list;
	}
	
	@Override
	public Dao<Establishment, Integer> getDao() {
		try {
			return (Dao<Establishment, Integer>) helper.getEstablishmentDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Establishment save(Establishment establishment) {
		try {
			Dao<Establishment, Integer> dao = getDao();
			dao.create(establishment);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return establishment;
	}

	@Override
	public void update(Establishment establishment) {
		try {
			Dao<Establishment, Integer> dao = getDao();
			dao.update(establishment);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
