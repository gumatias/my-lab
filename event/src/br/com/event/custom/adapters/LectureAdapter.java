package br.com.event.custom.adapters;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.dao.LectureDAO;
import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.model.Lecture;
import br.com.event.custom.ui.LectureDetailActivity;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class LectureAdapter extends GenericListAdapter<Lecture> {

	private EventDatabaseHelper helper;
	
	private LectureDAO lectureDAO;
	
	private boolean isAllTabVisible;
	
	public LectureAdapter(Context context, List<Lecture> lectures, boolean isAllTabVisible) {
		super(context, lectures);
		this.isAllTabVisible = isAllTabVisible;
		helper = (EventDatabaseHelper)OpenHelperManager.getHelper(context);
		lectureDAO = new LectureDAO(helper);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
        if (convertView == null) {
        	convertView = getLayoutInflater().inflate(R.layout.schedule_list_row, null);
            holder = new ViewHolder();
            holder.timeTxt 	 = (TextView)convertView.findViewById(R.id.schedule_list_row_time);
            holder.titleTxt  = (TextView)convertView.findViewById(R.id.schedule_list_row_title);
            holder.placeTxt  = (TextView)convertView.findViewById(R.id.schedule_list_row_place);
            holder.statusBtn = (Button)convertView.findViewById(R.id.schedule_list_row_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        
        final Lecture lecture = getList().get(position);
        holder.titleTxt.setText(lecture.getName().trim());
        holder.placeTxt.setText(lecture.getPlace().getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        holder.timeTxt.setText(dateFormat.format(lecture.getInitialDate()) 
        						+ " - "
        						+ dateFormat.format(lecture.getEndDate()));
        
        setEquivalentDrawable(lecture, holder.statusBtn);
        holder.statusBtn.setOnClickListener(new StatusClickListener(holder.statusBtn, lecture));
        
        convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), LectureDetailActivity.class);
				intent.putExtra("lecture", lecture);
				getContext().startActivity(intent);
			}
		});
        return convertView;
	}
	
	/**
	 * returns a drawable based on whether it's user favorite lecture or not.
	 * @param statusBtn 
	 * @return
	 */
	private void setEquivalentDrawable(Lecture lecture, Button statusBtn) {
		Resources res = getContext().getResources();
		Drawable d 	  = null;
		
		if (lecture.isUserFavorite()) d = res.getDrawable(R.drawable.remove_btn_selector);
		else d = res.getDrawable(R.drawable.add_btn_selector);

		statusBtn.setBackgroundDrawable(d);
	}

	public static class ViewHolder {
        public TextView titleTxt, timeTxt, placeTxt;
        public Button statusBtn;
    }
	
	class StatusClickListener implements OnClickListener {

		private Button statusBtn;
		
		private Lecture lecture;
		
		public StatusClickListener(Button statusBtn, Lecture lecture) {
			super();
			this.statusBtn = statusBtn;
			this.lecture = lecture;
		}

		public void onClick(View v) {
			if (lecture.isUserFavorite()) lecture.setUserFavorite(false);
			else lecture.setUserFavorite(true);
			
			setEquivalentDrawable(lecture, statusBtn);
			lectureDAO.update(lecture);
			
			if (!isAllTabVisible) {
				getList().remove(lecture);
				notifyDataSetChanged();
			}
		}

	}
}