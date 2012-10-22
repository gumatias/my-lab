package br.com.event.custom.model;

import br.com.event.custom.R;

public enum SpotType {
	
	EVENT 		(0, R.string.event, 	 R.drawable.map_pin_a), 
	RESTAURANT	(1, R.string.restaurant, R.drawable.restaurant_icon),
	CAR_PARK	(2, R.string.parkinglot, R.drawable.parking_icon);
	
	private int id, titleId, imageId;
	
	private SpotType(int id, int titleId, int imageId) {
		this.id = id;
		this.titleId = titleId;
		this.imageId = imageId;
	}

	public int id() {
		return id;
	}

	public int title() {
		return titleId;
	}
	
	public int image() {
		return imageId;
	}
	
}