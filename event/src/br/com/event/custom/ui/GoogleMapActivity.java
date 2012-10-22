package br.com.event.custom.ui;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.dao.EstablishmentDAO;
import br.com.event.custom.dao.EventDAO;
import br.com.event.custom.io.EventDatabaseHelper;
import br.com.event.custom.io.EventManager;
import br.com.event.custom.model.Establishment;
import br.com.event.custom.model.Event;
import br.com.event.custom.model.SpotType;
import br.com.event.custom.view.CustomDialog;
import br.com.event.custom.view.MyBalloonItemizedOverlay;
import br.com.event.custom.view.RouteSegmentOverlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class GoogleMapActivity extends MapActivity {
	
	private MapController mapControl;
	private MapView mapView;
	private ProgressBar pb;
//	private Button routeButton;	
	private List<Overlay> mapOverlays;
	private List<Establishment> establishments;
	private Event event;
	
	String TAG = "GPStest";
	// Set up the array of GeoPoints defining the route
	int numberRoutePoints;
	GeoPoint routePoints[]; // Dimension will be set in class RouteLoader below
	int routeGrade[]; // Index for slope of route from point i to point i+1
	RouteSegmentOverlay route; // This will hold the route segments
	boolean routeIsDisplayed = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // Suppress title bar for
														// more space
		setContentView(R.layout.directions_main);
		((TextView)findViewById(R.id.header1_title)).setText(R.string.directions);
		((Button)findViewById(R.id.header1_back_btn)).setOnClickListener(new BackBtnListener());
		
		loadDirections();
		// Button to control route overlay
//		routeButton = (Button) findViewById(R.id.doRoute);
//		routeButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				if (!routeIsDisplayed) {
//					routeIsDisplayed = true;
////					loadRouteData();
//				} else {
//					if (route != null)
//						route.setRouteView(false);
//					route = null; // To prevent multiple route instances if key
//									// toggled rapidly (see line 235)
//					routeIsDisplayed = false;
//					mapView.postInvalidate();
//				}
//			}
//		});

	}
	
	class BackBtnListener implements OnClickListener {

		public void onClick(View v) {
			finish();
		}
		
	}
	
	/*
	 * Methods to set map overlays. In this case we will place a small overlay
	 * image at a specified location. Place the marker image as a png file in
	 * res > drawable-* . For example, the reference to
	 * R.drawable.knifefork_small below is to an image file called
	 * knifefork_small.png in the project folder res > drawable-hdpi. Can only
	 * use lower case letters a-z, numbers 0-9, ., and _ in these image file
	 * names. In this example the single overlay item is specified by drawable
	 * and the location of the overlay item is specified by overlayitem.
	 */

	// Display accessibility overlay. If not already displayed, successive
	// button clicks display each of
	// the three icons successively, then the next removes them all. This
	// illustrates the ability to
	// change individual overlay items dynamically at runtime.

	private void loadDirections() {
		pb = (ProgressBar)findViewById(R.id.refresh_progress);
		pb.setVisibility(View.VISIBLE);
		new DirectionTask().execute();
	}

	private void setMainOverlay() {
		MyBalloonItemizedOverlay itemizedOverlay = null;
		if (itemizedOverlay == null) {
			mapOverlays = mapView.getOverlays();
			Drawable drawable = this.getResources().getDrawable(
					R.drawable.map_pin_a);
			itemizedOverlay = new MyBalloonItemizedOverlay(this, drawable, mapView);
		}
		
		itemizedOverlay.addOverlay(new OverlayItem(new GeoPoint((int)(event.getLatitude() * 1E6), 
				(int)(event.getLongitude() * 1E6)), event.getName(), event.getDescription()), event);
				mapOverlays.add(itemizedOverlay);
		
		// Added symbols will be displayed when map is redrawn so force redraw
		// now
		mapView.postInvalidate();
	}

	public void setPlacesOverlay() {
		MyBalloonItemizedOverlay itemizedOverlay = null;
		if (itemizedOverlay == null) {
			mapOverlays = mapView.getOverlays();
			Drawable drawable = this.getResources().getDrawable(
					R.drawable.parking_icon);
			itemizedOverlay = new MyBalloonItemizedOverlay(this, drawable, mapView);
		}
		
		for (Establishment establishment : establishments) {
			itemizedOverlay.addOverlay(new OverlayItem(new GeoPoint((int)(establishment.getLatitude() * 1e6), 
																	(int)(establishment.getLongitude() * 1e6)), 
																	establishment.getName(), establishment.
																	getDescription()), establishment);
			mapOverlays.add(itemizedOverlay);
		}
		
		// Added symbols will be displayed when map is redrawn so force redraw
		// now
		mapView.postInvalidate();
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	class DirectionTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			EventDatabaseHelper helper = (EventDatabaseHelper)
					OpenHelperManager.getHelper(GoogleMapActivity.this);
			establishments 	= fetchEstablishments(helper);
			event 			= fetchEvent(helper);
			
			if (establishments == null) return null;
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if (result == null) {
				showDialog(EventActivity.INTERNET_FAILURE_DIALOG);
				return;
			}
			
			// Add map controller with zoom controls
			mapView = (MapView) findViewById(R.id.mv);
			mapView.setSatellite(false);
			mapView.setTraffic(false);
			mapView.setBuiltInZoomControls(true); // Set android:clickable=true in
													// main.xml
			int maxZoom = mapView.getMaxZoomLevel();
			int initZoom = maxZoom - 3;
			mapControl = mapView.getController();
			mapControl.setZoom(initZoom);
			// Convert lat/long in degrees into integers in microdegrees
			mapControl.animateTo(new GeoPoint((int)(event.getLatitude() * 1E6), 
											  (int)(event.getLongitude() * 1E6)));

			setMainOverlay();
			setPlacesOverlay();
			pb.setVisibility(View.INVISIBLE);
		}
		
	}
	
	private List<Establishment> fetchEstablishments(EventDatabaseHelper helper) {
		EstablishmentDAO dao = new EstablishmentDAO(helper);
		List<Establishment> establishments = dao.getAll();
		
		if (establishments.isEmpty()) {
			establishments = new EventManager().getEstablishments();
			if (establishments == null) return null;
			
			setSpotType(establishments);
			dao.save(establishments);
		}
		return establishments;
	}

	private void setSpotType(List<Establishment> establishments) {
		for (Establishment e : establishments) {
			if (e.getEstablishmentType().getId() == SpotType.CAR_PARK.id()) {
				e.setSpotType(SpotType.CAR_PARK);
			} else if (e.getEstablishmentType().getId() == SpotType.RESTAURANT.id()) {
				e.setSpotType(SpotType.RESTAURANT);
			}
		}
	}
	
	private Event fetchEvent(EventDatabaseHelper helper) {
		return new EventDAO(helper).getAll().get(0);
	}
	
	
	@Override
	protected Dialog onCreateDialog(int dialogId) {
		Resources res = getResources();
		
		Dialog dialog = null;
		switch (dialogId) {
		case EventActivity.INTERNET_FAILURE_DIALOG:
			CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
			customBuilder
				.setTitle(res.getString(R.string.failure))
				.setMessage(res.getString(R.string.no_internet))
				.setPositiveButton(res.getString(R.string.ok),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									finish();
								}
							});
			dialog = customBuilder.create();
			break;
		case EventActivity.DEFAULT_DIALOG:
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder
					.setTitle("Default title")
					.setMessage("Default body")
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
			dialog = alertBuilder.create();
			break;
		}
		return dialog;
	}

	// This sets the s key on the phone to toggle between satellite and map view
	// and the t key to toggle between traffic and no traffic view (traffic view
	// relevant only in urban areas where it is reported).

//	public boolean onKeyDown(int keyCode, KeyEvent e) {
//		if (keyCode == KeyEvent.KEYCODE_S) {
//			mapView.setSatellite(!mapView.isSatellite());
//			return true;
//		} else if (keyCode == KeyEvent.KEYCODE_T) {
//			mapView.setTraffic(!mapView.isTraffic());
//			mapControl.animateTo(gp); // To ensure change displays immediately
//		}
//		return (super.onKeyDown(keyCode, e));
//	}

	// Required method since class extends MapActivity
//	@Override
//	protected boolean isRouteDisplayed() {
//		return false; // Don't display a route
//	}

	// Method to read route data from server as XML
//	public void loadRouteData() {
//		try {
//			System.out.println("lat:" + lat + " | lon:" + lon);
//			String url = "http://eagle.phys.utk.edu/reubendb/UTRoute.php";
//			String data = "?lat1=" + lat + "&lon1=" + lon + "&lat2=-23.617366&lon2=-46.661607";
//			System.out.println("url:" + url + data);
//			new RouteLoader().execute(new URL(url + data));
//		} catch (MalformedURLException e) {
//			Log.i("NETWORK", "Failed to generate valid URL");
//		}
//	}

	// Overlay a route. This method is only executed after loadRouteData()
	// completes
	// on background thread.
//
//	public void overlayRoute() {
//		if (route != null)
//			return; // To prevent multiple route instances if key toggled
//					// rapidly (see also line 116)
//		// Set up the overlay controller
//		route = new RouteSegmentOverlay(routePoints, routeGrade); // My class
//																	// defining
//																	// route
//																	// overlay
//		mapOverlays = mapView.getOverlays();
//		mapOverlays.add(route);
//
//		// Added symbols will be displayed when map is redrawn so force redraw
//		// now
//		mapView.postInvalidate();
//	}

	/*
	 * Class to implement single task on background thread without having to
	 * manage the threads directly. Launch with
	 * "new RouteLoader().execute(new URL(urlString)". Must be launched from the
	 * UI thread and may only be invoked once. Adapted from example in Ch. 10 of
	 * Android Wireless Application Development. Use this to do data load from
	 * network on separate thread from main user interface to prevent locking
	 * main UI if there is network delay.
	 */
//
//	private class RouteLoader extends AsyncTask<URL, String, String> {
//
//		/*
//		 * TODO
//		 * to draw the route you're gonna have to do this instead:
//		 * http://blog.synyx.de/2010/06/routing-driving-directions-on-android-%E2%80%93-part-2-draw-the-route/
//		 * http://blog.synyx.de/2010/06/routing-driving-directions-on-android-part-1-get-the-route/
//		 */
//		
//		@Override
//		protected String doInBackground(URL... params) {
//			// This pattern takes more than one param but we'll just use the
//			// first
//			try {
//				URL text = params[0];
//
//				XmlPullParserFactory parserCreator;
//
//				parserCreator = XmlPullParserFactory.newInstance();
//
//				XmlPullParser parser = parserCreator.newPullParser();
//
//				parser.setInput(text.openStream(), null);
//
//				publishProgress("Parsing XML...");
//
//				int parserEvent = parser.getEventType();
//				int pointCounter = -1;
//				int wptCounter = -1;
//				int totalWaypoints = -1;
//				int lat = -1;
//				int lon = -1;
//				String wptDescription = "";
//				int grade = -1;
//
//				// Parse the XML returned on the network
//				while (parserEvent != XmlPullParser.END_DOCUMENT) {
//					switch (parserEvent) {
//					case XmlPullParser.START_TAG:
//						String tag = parser.getName();
//						if (tag.compareTo("number") == 0) {
//							numberRoutePoints = Integer.parseInt(parser
//									.getAttributeValue(null, "numpoints"));
//							totalWaypoints = Integer.parseInt(parser
//									.getAttributeValue(null, "numwpts"));
//							routePoints = new GeoPoint[numberRoutePoints];
//							routeGrade = new int[numberRoutePoints];
//							Log.i(TAG, "   Total points = " + numberRoutePoints
//									+ " Total waypoints = " + totalWaypoints);
//						}
//						if (tag.compareTo("trkpt") == 0) {
//							pointCounter++;
//							lat = Integer.parseInt(parser.getAttributeValue(
//									null, "lat"));
//							lon = Integer.parseInt(parser.getAttributeValue(
//									null, "lon"));
//							System.out.println("lattttttt:" + lat + " | lonnnn:" + lon);
//							grade = Integer.parseInt(parser.getAttributeValue(
//									null, "grade"));
//							routePoints[pointCounter] = new GeoPoint(lat, lon);
//							routeGrade[pointCounter] = grade;
//							Log.i(TAG, "   trackpoint=" + pointCounter
//									+ " latitude=" + lat + " longitude=" + lon
//									+ " grade=" + grade);
//						} else if (tag.compareTo("wpt") == 0) {
//							wptCounter++;
//							lat = Integer.parseInt(parser.getAttributeValue(
//									null, "lat"));
//							lon = Integer.parseInt(parser.getAttributeValue(
//									null, "lon"));
//							System.out.println("lattttttt:" + lat + " | lonnnn:" + lon);
//							wptDescription = parser.getAttributeValue(null,
//									"description");
//							Log.i(TAG, "   waypoint=" + wptCounter
//									+ " latitude=" + lat + " longitude=" + lon
//									+ " " + wptDescription);
//						}
//						break;
//					}
//
//					parserEvent = parser.next();
//				}
//
//			} catch (Exception e) {
//				Log.i("RouteLoader", "Failed in parsing XML", e);
//				return "Finished with failure.";
//			}
//
//			return "Done...";
//		}
//
//		protected void onCancelled() {
//			Log.i("RouteLoader", "GetRoute task Cancelled");
//		}
//
//		// Now that route data are loaded, execute the method to overlay the
//		// route on the map
//		protected void onPostExecute(String result) {
//			Log.i(TAG, "Route data transfer complete");
//			overlayRoute();
//		}
//
//		protected void onPreExecute() {
//			Log.i(TAG, "Ready to load URL");
//		}
//
//		protected void onProgressUpdate(String... values) {
//			super.onProgressUpdate(values);
//		}
//
//	}
	
}