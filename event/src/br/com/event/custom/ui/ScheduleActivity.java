package br.com.event.custom.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;
import br.com.event.custom.R;
import br.com.event.custom.adapters.LectureAdapter;
import br.com.event.custom.dao.EventDAO;
import br.com.event.custom.dao.LectureDAO;
import br.com.event.custom.io.EventManager;
import br.com.event.custom.model.Category;
import br.com.event.custom.model.Event;
import br.com.event.custom.model.Lecture;

public class ScheduleActivity extends EventActivity {

	private Map<Calendar, List<Lecture>> schedule = null;
	private Calendar dayOfWeek = null;
	private TextView dayOfWeekTxt;
	private ListView lecturesListView;
	private Button categoryBtn;
	private Button allTabBtn;
	private Button mineTabBtn;
	private Button scheduleBtn;
	private Button previousBtn;
	private Button nextBtn;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;
	private List<Lecture> lectures;
	private Event event;
	private ProgressBar pb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_main);
		((TextView)findViewById(R.id.schedule_title)).setText(R.string.menu_schedule);

		dayOfWeekTxt = (TextView)findViewById(R.id.schedule_day_of_week_txt);
		
		categoryBtn = (Button) findViewById(R.id.schedule_filter_btn);
		categoryBtn.setOnClickListener(new CategoryButtonListener());

//		scheduleBtn = (Button) findViewById(R.id.schedule_schedule_btn);
//		scheduleBtn.setOnClickListener(new ScheduleButtonListner());

		previousBtn = (Button) findViewById(R.id.schedule_previous_btn);
		previousBtn.setOnClickListener(new PreviousButtonListener());

		nextBtn = (Button) findViewById(R.id.schedule_next_btn);
		nextBtn.setOnClickListener(new NextButtonListener());
		
		allTabBtn = (Button) findViewById(R.id.schedule_todas_btn);
		allTabBtn.setOnClickListener(new AllTabListener());
		
		mineTabBtn = (Button) findViewById(R.id.schedule_minhas_btn);
		mineTabBtn.setOnClickListener(new MineTabListener());
		
		allTabBtn.setTag(true);
		loadLectures();
	}
	
	class AllTabListener implements OnClickListener {

		public void onClick(View v) {
			toggle(true);
			loadLectures();
		}
		
	}
	
	class MineTabListener implements OnClickListener {

		public void onClick(View v) {
			toggle(false);
			loadLectures();
		}
		
	}
	
	private void toggle(boolean isAllVisible) {
		allTabBtn.setTag(isAllVisible);
		
		Resources res = getResources();
		if (isAllVisible) {
			allTabBtn.setTextColor(res.getColor(R.color.red));
			mineTabBtn.setTextColor(res.getColor(R.color.gray));			
		} else {
			allTabBtn.setTag(false);
			allTabBtn.setTextColor(res.getColor(R.color.gray));
			mineTabBtn.setTextColor(res.getColor(R.color.red));
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		setFirstTime(false);
	}

	private LectureAdapter buildLecturesAdapter(Map<Calendar, List<Lecture>> schedule, boolean isAllTabVisible) {
		List<Lecture> lectures 		= null;
		Set<Calendar> calendarSet 	= schedule.keySet();
		
		int i = 0;
		for (Calendar cal : calendarSet) {
			dayOfWeekTxt.setText(new SimpleDateFormat("MMM/dd").format(cal.getTime()));
		
			/* first result stored in case there's no such date as now in the map */
			if (i == 0) lectures = schedule.get(cal);
			
			if (cal.getTime().getDate() == dayOfWeek.getTime().getDate()) {
				return new LectureAdapter(this, schedule.get(cal), isAllTabVisible);
			}
			i++;
		}
		return new LectureAdapter(this, lectures, isAllTabVisible);
	}

	class CategoryButtonListener implements OnClickListener {

		public void onClick(View v) {
			startActivity(new Intent(ScheduleActivity.this,
					CategoryActivity.class));
		}

	}

	class NowButtonListner implements OnClickListener {

		public void onClick(View v) {
			startActivity(new Intent(ScheduleActivity.this,
					CategoryActivity.class));
		}

	}

	class ScheduleButtonListner implements OnClickListener {

		public void onClick(View v) {
			// startActivity(new Intent(ScheduleActivity.this,
			// FilterActivity.class));
			scheduleBtn.setSelected(true);
		}

	}

	class FavoriteButtonListner implements OnClickListener {

		public void onClick(View v) {
			startActivity(new Intent(ScheduleActivity.this,
					CategoryActivity.class));
		}

	}

	class PreviousButtonListener implements OnClickListener {

		public void onClick(View view) {
			goToPreviousDate(view);
		}

	}

	class NextButtonListener implements OnClickListener {

		public void onClick(View view) {
			goToNextDate(view);
		}

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_schedule:
			return false;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	private void goToNextDate(View view) {
		boolean increment = true;
		dayOfWeek.roll(Calendar.DATE, increment);
		cleanCalendarFields(dayOfWeek);
		checkDatePickerVisibility();
		lecturesListView.setAdapter(buildLecturesAdapter(schedule, (Boolean)allTabBtn.getTag()));
		pushViewToLeft(view);
	}
	
	private void goToPreviousDate(View view) {
		boolean increment = false;
		dayOfWeek.roll(Calendar.DATE, increment);
		cleanCalendarFields(dayOfWeek);
		checkDatePickerVisibility();
		lecturesListView.setAdapter(buildLecturesAdapter(schedule, (Boolean)allTabBtn.getTag()));
		pushViewToRight(view);
	}
	
	/**
	 * Checks if there's the next/previous date, if not, 
	 * then the button must be invisible.
	 * @param dayOfWeek
	 * @param increment
	 */
	private void checkDatePickerVisibility() {
		/* checking day before */
		if (isSupposedDateAvailable(false)) previousBtn.setVisibility(View.VISIBLE);
		else previousBtn.setVisibility(View.INVISIBLE);
		
		/* checking a day later */
		if (isSupposedDateAvailable(true)) nextBtn.setVisibility(View.VISIBLE);
		else nextBtn.setVisibility(View.INVISIBLE);
	}
	
	private boolean isSupposedDateAvailable(boolean increment) {
		Calendar supposedDate = (Calendar)dayOfWeek.clone();
		supposedDate.roll(Calendar.DATE, increment);
		if (schedule.get(supposedDate) == null) return false;
		return true;
	}

	private void pushViewToLeft(View view) {
		ViewFlipper vf = (ViewFlipper) findViewById(R.id.schedule_flipper);
		vf.setInAnimation(view.getContext(), R.anim.push_left_in);
		vf.setOutAnimation(view.getContext(), R.anim.push_left_out);
		vf.showNext();
	}

	private void pushViewToRight(View view) {
		ViewFlipper vf = (ViewFlipper) findViewById(R.id.schedule_flipper);
		vf.setInAnimation(view.getContext(), R.anim.push_right_in);
		vf.setOutAnimation(view.getContext(), R.anim.push_right_out);
		vf.showNext();
	}

	private class MyGestureDetector extends SimpleOnGestureListener {
		
		private ListView listView;
		
		public MyGestureDetector(ListView listView) {
			this.listView = listView;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//					pushViewToLeft(listView);
					System.out.println("isSupposedDateAvailable(true):" + isSupposedDateAvailable(true));
					goToNextDate(listView); 
				// left to right swipe
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//					pushViewToRight(listView);
					System.out.println("isSupposedDateAvailable(false):" + isSupposedDateAvailable(false));
					goToPreviousDate(listView);
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}

	}
	
	/**
	 * This method creates map key with initialDate, 
	 * builds a list for each date, retrieves the map 
	 * list from that date, and adds the lecture to 
	 * the specific date.
	 * @return schedule
	 */
	private Map<Calendar, List<Lecture>> buildSchedule() {
		Map<Calendar, List<Lecture>> schedule = new Hashtable<Calendar, List<Lecture>>();
		
		List<Lecture> ls = null;
		int i = 0;
		for (Lecture lecture : lectures) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(lecture.getInitialDate());
			cleanCalendarFields(calendar);
			
			/* saves first day of the event */
			if (i == 0) dayOfWeek = (Calendar)calendar.clone();
			
			ls = schedule.get(calendar);
			if (ls == null) ls = new ArrayList<Lecture>();
			ls.add(lecture);

			schedule.put(calendar, ls);
			i++;
		}
		return schedule;
	}
	
	private void loadLectures() {
		pb = (ProgressBar)findViewById(R.id.refresh_progress);
		pb.setVisibility(View.VISIBLE);
		new ScheduleTask().execute();
	}

	/**
	 * return if already exists in the database, otherwise get it from the sever.
	 * @return event
	 */
	private Event fetchEvent() {
		EventDAO dao = new EventDAO(getHelper());
		List<Event> events = dao.getAll();
		
		if (events.isEmpty()) {
			Event e = new EventManager().getEvent();
			if (e == null) return null; 
			events.add(dao.save(e));
		}
		return events.get(0);
	}
	
	/**
	 * fetch all user selected lectures
	 * @return
	 */
	private List<Lecture> fetchUserLectures() {
		LectureDAO lDao = new LectureDAO(getHelper());
		return lDao.getAllFromUser();
	}
	
	/**
	 * return all lectures if already exists in the database, otherwise get it from the sever.
	 * @return lectures
	 */
	@SuppressWarnings("unchecked")
	private List<Lecture> fetchAllLectures() {
		LectureDAO lDao = new LectureDAO(getHelper());
		
		List<Lecture> lectures 	  = null;
		List<Category> categories = null;
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) 	categories = (ArrayList<Category>) extras.get("categories");			
		if (categories != null) lectures = buildFilteredLectures(categories, lDao);
		else lectures = lDao.getAll();
		
		if (lectures.isEmpty()) {
			List<Lecture> l = new EventManager().getLectures();
			if (l != null) lectures = lDao.save(l);
		}

		return lectures;
	}

	private List<Lecture> buildFilteredLectures(List<Category> categories, LectureDAO dao) {
		List<Lecture> lectures = new ArrayList<Lecture>();
		for (Category category : categories) {
			if (category.isChecked()) {
				List<Lecture> list = dao.findByCategory(category);
				lectures.addAll(list);
			}
		}
		return lectures;
	}
	
	private class ScheduleTask extends AsyncTask<Object, String, String> {

		private Boolean isAllTabVisible;
		
		@Override
		protected String doInBackground(Object... params) {
			isAllTabVisible = (Boolean)allTabBtn.getTag();
			event = fetchEvent();
			
			if (isAllTabVisible) lectures = fetchAllLectures();				
			else lectures = fetchUserLectures();
			
			if (event == null || lectures == null) return null;
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if (result == null) {
				showDialog(INTERNET_FAILURE_DIALOG);
				return;
			}
			
			if (!isAllTabVisible && lectures.isEmpty()) {
				showDialog(EMPTY_SCHEDULE_DIALOG);
				toggle(true);
				pb.setVisibility(View.INVISIBLE);
				return;
			}
			
			schedule = buildSchedule();
			
			lecturesListView = (ListView) findViewById(R.id.schedule_listview);
			lecturesListView.setAdapter(buildLecturesAdapter(schedule, (Boolean)allTabBtn.getTag()));
			checkDatePickerVisibility();
			
			/* suppose to change the date based on the user gesture,
			 * disabled for awhile since it's not very accurate. */
//			gestureDetector = new GestureDetector(new MyGestureDetector(lecturesListView));
//			gestureListener = new View.OnTouchListener() {
//				public boolean onTouch(View v, MotionEvent event) {
//					if (gestureDetector.onTouchEvent(event)) return true;
//					return false;
//				}
//			};
//			lecturesListView.setOnTouchListener(gestureListener);
			pb.setVisibility(View.INVISIBLE);
		}
		
	}
	
	private void cleanCalendarFields(Calendar calendar) {
		calendar.clear(Calendar.HOUR_OF_DAY);
		calendar.clear(Calendar.HOUR);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
	}
	
}