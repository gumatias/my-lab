package br.com.event.custom.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Sponsor extends EventComponent implements Parcelable {

	@DatabaseField(generatedId = true)
	private transient int id;
	
	@DatabaseField(index = true)
	private String name;
	
	@DatabaseField(canBeNull = false, columnName = "web_site")
	@SerializedName("site")
	private String webSite;
	
	@DatabaseField
	private String description;
	
	@DatabaseField(columnName = "image_url")
	@SerializedName("image_url")
	private String imgUrl;
	
	@DatabaseField(dataType = DataType.ENUM_STRING)
	private SponsorType type;

	public Sponsor() {}
	
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

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public SponsorType getType() {
		return type;
	}

	public void setType(SponsorType type) {
		this.type = type;
	}
	
	public Sponsor(Parcel parcel) {
		readFromParcel(parcel);
	}
	
	public static final Parcelable.Creator<Sponsor> CREATOR = new Parcelable.Creator<Sponsor>() {

		public Sponsor createFromParcel(Parcel source) {
			return new Sponsor(source);
		}

		public Sponsor[] newArray(int size) {
			return new Sponsor[size];
		}

	};
	
	private void readFromParcel(Parcel parcel) {
		name 		= parcel.readString();
		webSite 	= parcel.readString();
		description = parcel.readString();
		imgUrl 		= parcel.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(webSite);
		dest.writeString(description);
		dest.writeString(imgUrl);
	}

	@Override
	public String toString() {
		return "Sponsor [name=" + name + ", webSite=" + webSite
				+ ", description=" + description + ", imgUrl=" + imgUrl
				+ ", type=" + type + "]";
	}

}