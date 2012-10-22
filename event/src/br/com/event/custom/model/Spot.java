package br.com.event.custom.model;

import android.os.Parcelable;

public interface Spot extends Parcelable {

	public String getName();
	
	public SpotType getSpotType();
	
	public String getAddress();
	
	public String getDescription();
	
	public String getPhone();
	
}
