package br.com.event.custom.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.model.Event;

import com.j256.ormlite.dao.Dao;

public class EventDAO extends GenericDAO<Event> {

	public EventDAO(EventDatabaseHelper helper) {
		super(helper);
	}
	
	@Override
	public List<Event> save(List<Event> list) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Event save(Event event) {
		try {
			Dao<Event, Integer> dao = getDao();
			dao.create(event);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return event;
	}
	
	@Override
	public List<Event> getAll() {
		List<Event> list = new ArrayList<Event>();
		try {
			list = getDao().queryForAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return list;
	}
	
	@Override
	public Dao<Event, Integer> getDao() {
		try {
			return (Dao<Event, Integer>) helper.getEventDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Event event) {
		try {
			Dao<Event, Integer> dao = getDao();
			dao.update(event);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}