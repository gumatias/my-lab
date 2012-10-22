package br.com.event.custom.dao;

import java.util.List;

import br.com.event.custom.io.EventDatabaseHelper;

import com.j256.ormlite.dao.Dao;

public abstract class GenericDAO<T> {
	
	protected EventDatabaseHelper helper;
	
	public GenericDAO(EventDatabaseHelper helper) {
		this.helper = helper;
	}

	public abstract T save(T object);
	
	public abstract List<T> save(List<T> list);
	
	public abstract void update(T object);
	
	public abstract List<T> getAll();
	
	public abstract Dao<T, Integer> getDao();
	
}
