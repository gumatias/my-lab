package br.com.event.custom.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.model.MoreIcon;
import br.com.event.custom.ui.GoogleMapActivity;
import br.com.event.custom.ui.MapActivity;
import br.com.event.custom.ui.GalleryActivity;
import br.com.event.custom.ui.SettingsActivity;
import br.com.event.custom.ui.ShareActivity;
import br.com.event.custom.ui.SponsorsActivity;

public class MoreIconAdapter extends BaseAdapter {

	private Context context;

	protected LayoutInflater inflater;

	// Gets the context so it can be used later
	public MoreIconAdapter(Context c) {
		context = c;
		inflater = LayoutInflater.from(context);
		createIcons();
	}

	// Total number of things contained within the adapter
	public int getCount() {
		return icons.size();
	}

	// Require for structure, not really used in my code.
	public Object getItem(int position) {
		return null;
	}

	// Require for structure, not really used in my code. Can
	// be used to get the id of an item in the adapter for
	// manual control.
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.more_grid_icon, null);
			holder = new ViewHolder();
			holder.btnView = (Button) convertView.findViewById(R.id.more_icon);
			holder.titleTxt = (TextView) convertView
					.findViewById(R.id.more_text);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.titleTxt.setText(icons.get(position).getTitle());
		holder.btnView.setBackgroundResource(icons.get(position).getDrawable());
		holder.btnView.setOnClickListener(new IconOnClickListener(position));

		return convertView;
	}

	private static class ViewHolder {
		public Button btnView;
		public TextView titleTxt;
	}

	private List<MoreIcon> icons = new ArrayList<MoreIcon>();

	private void createIcons() {
		icons.add(new MoreIcon(R.string.config, R.drawable.configuration_btn_selector,
				SettingsActivity.class));
		icons.add(new MoreIcon(R.string.share, R.drawable.share_btn_selector,
				ShareActivity.class));
		icons.add(new MoreIcon(R.string.media, R.drawable.media_btn_selector,
				GalleryActivity.class));
		icons.add(new MoreIcon(R.string.sponsors, R.drawable.sponsors_btn_selector,
				SponsorsActivity.class));
		icons.add(new MoreIcon(R.string.map, R.drawable.map_btn_selector,
				MapActivity.class));
		icons.add(new MoreIcon(R.string.directions, R.drawable.directions_btn_selector,
				 GoogleMapActivity.class));
	}

	class IconOnClickListener implements OnClickListener {

		private final int position;

		public IconOnClickListener(int position) {
			this.position = position;
		}

		public void onClick(View v) {
			context.startActivity(new Intent(context, icons.get(position)
					.getClazz()));
		}
	}

}
