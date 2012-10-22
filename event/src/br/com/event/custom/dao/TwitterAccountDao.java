package br.com.event.custom.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.model.TwitterAccount;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class TwitterAccountDao extends GenericDAO<TwitterAccount>{

	public TwitterAccountDao(EventDatabaseHelper helper) {
		super(helper);
	}
	
	@Override
	public TwitterAccount save(TwitterAccount account) {
		try {
			Dao<TwitterAccount, Integer> dao = getDao();
			dao.createIfNotExists(account);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return account;
	}

	@Override
	public List<TwitterAccount> save(List<TwitterAccount> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(TwitterAccount account) {
		try {
			Dao<TwitterAccount, Integer> dao = getDao();
			dao.update(account);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void delete(TwitterAccount account) {
		try {
			Dao<TwitterAccount, Integer> dao = getDao();
			dao.delete(account);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public TwitterAccount fetch(int id) {
		try {
			Dao<TwitterAccount, Integer> dao = getDao();
			QueryBuilder<TwitterAccount, Integer> queryBuilder = dao.queryBuilder();
			List<TwitterAccount> list = queryBuilder.where()
										.eq(TwitterAccount.ID_FIELD_NAME, id).query();
			if (!list.isEmpty()) return list.get(0);
			return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public TwitterAccount fetchForActive() {
		try {
			Dao<TwitterAccount, Integer> dao = getDao();
			QueryBuilder<TwitterAccount, Integer> queryBuilder = dao.queryBuilder();
			List<TwitterAccount> list = queryBuilder.where()
										.eq(TwitterAccount.ACTIVE_FIELD_NAME, true).query();
			if (!list.isEmpty()) return list.get(0);
			return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<TwitterAccount> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dao<TwitterAccount, Integer> getDao() {
		try {
			return (Dao<TwitterAccount, Integer>) helper.getTwitterAccountDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
