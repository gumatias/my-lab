package br.com.event.custom.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.adapters.CategoryAdapter;
import br.com.event.custom.dao.CategoryDAO;
import br.com.event.custom.io.EventManager;
import br.com.event.custom.model.Category;

public class CategoryActivity extends EventActivity {

	public static final String CATEGORY_FIRST_TIME = "category_first_time_execution";
	
	private ListView categoriesListView;
	
	private ArrayList<Category> categories;
	
	private ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_main);
		((TextView)findViewById(R.id.header1_title)).setText(R.string.category);
		((Button)findViewById(R.id.header1_back_btn)).setOnClickListener(new BackClickListener());

		loadCategories();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			updateCategories();
			resumeSchedule();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void updateCategories() {
		new CategoryDAO(getHelper()).update(categories);
	}
	
	class BackClickListener implements OnClickListener {

		public void onClick(View v) {
			updateCategories();
			resumeSchedule();
			finish();
		}
		
	}
	
	private void resumeSchedule() {
		Intent intent = new Intent(CategoryActivity.this, ScheduleActivity.class);
		intent.putExtra("categories", categories);
		startActivity(intent);
	}
	
	private void loadCategories() {
		pb = (ProgressBar) findViewById(R.id.refresh_progress);
		pb.setVisibility(View.VISIBLE);
		new CategoryTask().execute();
	}
	
	private class CategoryTask extends AsyncTask<String, Object, ArrayList<Category>> {

		@Override
		protected ArrayList<Category> doInBackground(String... params) {
			return (ArrayList<Category>)fetchCategories();
		}

		@Override
		protected void onPostExecute(ArrayList<Category> result) {
			super.onPostExecute(result);
			
			if (result == null) {
				showDialog(INTERNET_FAILURE_DIALOG);
				return;
			}

			categories = result;
			categoriesListView = (ListView) findViewById(R.id.categories_list);
			categoriesListView.setAdapter(new CategoryAdapter(CategoryActivity.this, categories));
			pb.setVisibility(View.INVISIBLE);
		}
		
	}
	
	/**
	 * return if already exists in the database, otherwise get it from the sever.
	 * @return categories
	 */
	private List<Category> fetchCategories() {
		CategoryDAO dao = new CategoryDAO(getHelper());
		List<Category> categories = dao.getAll();
		
		if (isFirstTime()) {
			List<Category> c = new EventManager().getCategories();
			if (c == null) return null;
			
			categories = dao.save(c);
			checkAll(categories);
			setFirstTime(false);
		}
		return categories;
	}
	
	private void checkAll(List<Category> categories) {
		for (Category c : categories) {
			c.setChecked(true);
		}
	}
	
	@Override
	protected boolean isFirstTime() {
		return getPreferences().getBoolean(CATEGORY_FIRST_TIME, true);
	}
	
	@Override
	protected void setFirstTime(boolean isFirstTime) {
		Editor editor = getPreferences().edit();
		editor.putBoolean(CATEGORY_FIRST_TIME, isFirstTime);
		editor.commit();
	}
	
}