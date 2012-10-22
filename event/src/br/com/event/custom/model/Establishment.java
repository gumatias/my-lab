package br.com.event.custom.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Establishment extends EventComponent implements Spot {

	@DatabaseField(generatedId = true)
	private transient int id;
	
	@DatabaseField(index = true)
	private String name;
	
	@DatabaseField
	private String description;
	
	@DatabaseField(canBeNull = false)
	@SerializedName ("telephone")
	private String phone;
	
	@DatabaseField(canBeNull = false)
	private String address;
	
	@DatabaseField(canBeNull = false, dataType = DataType.DOUBLE)
	private double latitude;
	
	@DatabaseField(canBeNull = false, dataType = DataType.DOUBLE)
	private double longitude;
	
	@DatabaseField(canBeNull = false, dataType = DataType.ENUM_STRING, 
			columnName = "spot_type")
	private SpotType spotType;

	@DatabaseField(foreign = true, foreignAutoCreate = true, canBeNull = false)
	@SerializedName("establishment_type")
	private EstablishmentType establishmentType;
	
	public Establishment() {}
	
	public Establishment(Parcel source) {
		readFromParcel(source);
	}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public EstablishmentType getEstablishmentType() {
		return establishmentType;
	}

	public void setEstablishmentType(EstablishmentType establishmentType) {
		this.establishmentType = establishmentType;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(phone);
		dest.writeString(address);
		dest.writeString(description);
		dest.writeString(spotType.name());
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
	}

	public static final Parcelable.Creator<Establishment> CREATOR = new Parcelable.Creator<Establishment>() {

		public Establishment createFromParcel(Parcel source) {
			return new Establishment(source);
		}

		public Establishment[] newArray(int size) {
			return new Establishment[size];
		}

	};

	private void readFromParcel(Parcel in) {
		name 		= in.readString();
		phone 		= in.readString();
		address 	= in.readString();
		description = in.readString();
		spotType	= SpotType.valueOf(in.readString());
		latitude	= in.readDouble();
		longitude	= in.readDouble();
	}

	@Override
	public String toString() {
		return "Establishment [name=" + name + ", description=" + description
				+ ", phone=" + phone + ", address=" + address + ", latitude=" 
				+ latitude + ", longitude=" + longitude + ", spotType=" + spotType 
				+ ", establishmentType=" + establishmentType + "]";
	}

}