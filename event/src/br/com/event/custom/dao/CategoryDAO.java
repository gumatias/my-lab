package br.com.event.custom.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.model.Category;

import com.j256.ormlite.dao.Dao;

public class CategoryDAO extends GenericDAO<Category>{

	public CategoryDAO(EventDatabaseHelper helper) {
		super(helper);
	}

	@Override
	public List<Category> save(List<Category> categories) {
		try {
			Dao<Category, Integer> dao = getDao();

			for (Category category : categories) {
				dao.createIfNotExists(category);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return categories;
	}

	@Override
	public List<Category> getAll() {
		List<Category> categories = new ArrayList<Category>();
		try {
			categories = getDao().queryForAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return categories;
	}

	@Override
	public Dao<Category, Integer> getDao() {
		try {
			return (Dao<Category, Integer>) helper.getCategoryDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Category save(Category category) {
		try {
			Dao<Category, Integer> dao = getDao();
			dao.create(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return category;
	}

	@Override
	public void update(Category category) {
		try {
			Dao<Category, Integer> dao = getDao();
			dao.update(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void update(List<Category> categories) {
		try {
			Dao<Category, Integer> dao = getDao();
			for (Category category : categories) {
				dao.update(category);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}