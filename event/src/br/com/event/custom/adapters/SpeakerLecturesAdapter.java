package br.com.event.custom.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.model.Lecture;
import br.com.event.custom.ui.LectureDetailActivity;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class SpeakerLecturesAdapter extends GenericListAdapter<Lecture> {

	private EventDatabaseHelper helper;
	
	public SpeakerLecturesAdapter(Context context, List<Lecture> lectures) {
		super(context, lectures);
		helper = (EventDatabaseHelper)OpenHelperManager.getHelper(context);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
        if (convertView == null) {
        	convertView = getLayoutInflater().inflate(R.layout.speakers_lectures_list_row, null);
            holder = new ViewHolder();
            holder.titleTxt = (TextView)convertView.findViewById(R.id.lecture_title);
            holder.arrowBtn = (Button)convertView.findViewById(R.id.lectures_arrow_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        
        holder.titleTxt.setText(getList().get(position).getName().trim());
        holder.arrowBtn.setOnClickListener(new ArrowClickListener(position));
        convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startLectureActivity(position);
			}
		});
        return convertView;
	}
	
	private void startLectureActivity(int position) {
		Lecture lecture = getList().get(position);
		Intent intent = new Intent(getContext(), LectureDetailActivity.class);
		intent.putExtra("lecture", lecture);
		getContext().startActivity(intent);
	}
	
	public static class ViewHolder {
        public TextView titleTxt;
        public Button arrowBtn;
    }
	
	class ArrowClickListener implements OnClickListener {

		private int position;
		
		public ArrowClickListener(int position) {
			this.position = position;
		}
		
		public void onClick(View v) {
			startLectureActivity(position);
		}
		
	}
	
}