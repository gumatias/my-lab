package br.com.event.custom.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Event extends EventComponent implements Spot {
	
	@DatabaseField(generatedId = true)
	private transient int id;

	@DatabaseField(index = true)
	private String name;
	
	@DatabaseField(canBeNull = false)
	private String description;
	
	@DatabaseField(canBeNull = false)
	private String address;
	
	@DatabaseField
	private String state;
	
	@DatabaseField(canBeNull = false)
	private String city;
	
	@DatabaseField(canBeNull = false, dataType = DataType.ENUM_STRING, 
			columnName = "spot_type")
	private SpotType spotType;
	
	@DatabaseField(canBeNull = false)
	@SerializedName("telephone")
	private String phone;
	
	@DatabaseField(columnName = "twitter_hashtag")
	@SerializedName("twitter_hashtag")
	private String twitterHashTag;
	
	@DatabaseField(canBeNull = false, columnName = "initial_date", dataType = DataType.DATE)
	@SerializedName("start_date")
	private Date initialDate;
	
	@DatabaseField(canBeNull = false, columnName = "end_date", dataType = DataType.DATE)
	@SerializedName("end_date")
	private Date endDate;
	
	@DatabaseField(columnName = "map_url")
	@SerializedName("map_url")
	private String mapUrl;
	
	@DatabaseField(canBeNull = false, dataType = DataType.ENUM_STRING, 
			columnName = "event_type")
	private EvenType eventType;
	
	@DatabaseField(canBeNull = false, dataType = DataType.DOUBLE)
	private double latitude;
	
	@DatabaseField(canBeNull = false, dataType = DataType.DOUBLE)
	private double longitude;

	public Event() {}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTwitterHashTag() {
		return twitterHashTag;
	}

	public void setTwitterHashTag(String twitterHashTag) {
		this.twitterHashTag = twitterHashTag;
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getMapUrl() {
		return mapUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}

	public EvenType getEventType() {
		return eventType;
	}

	public void setEventType(EvenType eventType) {
		this.eventType = eventType;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public SpotType getSpotType() {
		return spotType;
	}

	public void setSpotType(SpotType spotType) {
		this.spotType = spotType;
	}
	
	public int describeContents() {
		return 0;
	}
	
	public Event(Parcel source) {
		readFromParcel(source);
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(phone);
		dest.writeString(address);
		dest.writeString(description);
		dest.writeString(eventType.name());
		dest.writeString(spotType.name());
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
	}

	public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {

		public Event createFromParcel(Parcel source) {
			return new Event(source);
		}

		public Event[] newArray(int size) {
			return new Event[size];
		}

	};

	private void readFromParcel(Parcel in) {
		name 		= in.readString();
		phone 		= in.readString();
		address 	= in.readString();
		description = in.readString();
		eventType 	= EvenType.valueOf(in.readString());
		spotType 	= SpotType.valueOf(in.readString());
		latitude	= in.readDouble();
		longitude	= in.readDouble();
	}

	@Override
	public String toString() {
		return "Event [name=" + name + ", description=" + description
				+ ", address=" + address + ", state=" + state + ", city="
				+ city + ", spotType=" + spotType + ", phone=" + phone
				+ ", twitterHashTag=" + twitterHashTag + ", initialDate="
				+ initialDate + ", endDate=" + endDate + ", mapUrl=" + mapUrl
				+ ", eventType=" + eventType + ", latitude=" + latitude
				+ ", longitude=" + longitude + "]";
	}
	
}
