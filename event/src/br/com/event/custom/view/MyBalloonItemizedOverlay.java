package br.com.event.custom.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import br.com.event.custom.R;
import br.com.event.custom.infra.Dialer;
import br.com.event.custom.model.Spot;
import br.com.event.custom.ui.SpotDescriptionActivity;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyBalloonItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> myOverlays ;
	
	private List<Spot> spots;
	
	private Context context;

	public MyBalloonItemizedOverlay(Context context, Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		this.context = context;
		myOverlays = new ArrayList<OverlayItem>();
		spots = new ArrayList<Spot>();
		populate();
		setBalloonBottomOffset(15);
	}

	public void addOverlay(OverlayItem overlay, Spot spot) {
		myOverlays.add(overlay);
		spots.add(spot);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return myOverlays.get(i);
	}

	@Override
	public int size() {
		return myOverlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index) {
		GeoPoint  gpoint = myOverlays.get(index).getPoint();
		double lat = gpoint.getLatitudeE6()  / 1e6;
		double lon = gpoint.getLongitudeE6() / 1e6;
		
		Intent intent = new Intent(context, SpotDescriptionActivity.class);
		intent.putExtra("spot", spots.get(index));
		context.startActivity(intent);
		return true;
	}
	
	@Override
	protected boolean onTap(final int index) {
		super.onTap(index);
		
		ImageView callBtn = (ImageView) getBalloonView().findViewById(R.id.balloon_call_btn);
		callBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new Dialer(context).call(spots.get(index).getPhone());
			}
		});
		return true;
	}
	
}
