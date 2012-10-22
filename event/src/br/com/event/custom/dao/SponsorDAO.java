package br.com.event.custom.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.model.Sponsor;

import com.j256.ormlite.dao.Dao;

public class SponsorDAO extends GenericDAO<Sponsor> {

	public SponsorDAO(EventDatabaseHelper helper) {
		super(helper);
	}
	
	@Override
	public List<Sponsor> save(List<Sponsor> sponsors) {
		try {
			Dao<Sponsor, Integer> dao = getDao();
			for (Sponsor sponsor : sponsors) {
				dao.create(sponsor);	
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return sponsors;
	}
	
	@Override
	public List<Sponsor> getAll() {
		List<Sponsor> list = new ArrayList<Sponsor>();
		try {
			list = getDao().queryForAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return list;
	}
	
	@Override
	public Dao<Sponsor, Integer> getDao() {
		try {
			return (Dao<Sponsor, Integer>) helper.getSponsorDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Sponsor save(Sponsor sponsor) {
		try {
			Dao<Sponsor, Integer> dao = getDao();
			dao.create(sponsor);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return sponsor;
	}

	@Override
	public void update(Sponsor sponsor) {
		try {
			Dao<Sponsor, Integer> dao = getDao();
			dao.update(sponsor);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}