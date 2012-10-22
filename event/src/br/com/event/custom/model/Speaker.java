package br.com.event.custom.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Speaker extends EventComponent implements Parcelable {
	
	public final static String SPEAKER_ID_FIELD_NAME = "name";
	
	@DatabaseField(id = true, index = true)
	private String name;
	
	@DatabaseField(canBeNull = false)
	private String description;
	
	@DatabaseField
	private String twitter;
	
	@DatabaseField
	private String email;
	
	@DatabaseField(columnName = "image_url")
	@SerializedName("image_url")
	private String imgUrl;
	
	public Speaker() {}
	
    public Speaker(Parcel in) {  
        readFromParcel(in);  
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

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(email);
		dest.writeString(twitter);
		dest.writeString(description);
		dest.writeString(imgUrl);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		public Speaker createFromParcel(Parcel source) {
			return new Speaker(source);
		}

		public Speaker[] newArray(int size) {
			return new Speaker[size];
		}

	};

	private void readFromParcel(Parcel in) {
		name = in.readString();
		email = in.readString();
		twitter = in.readString();
		description = in.readString();
		imgUrl = in.readString();
	}

	@Override
	public String toString() {
		return "Speaker [name=" + name + ", description="
				+ description + ", twitter=" + twitter + ", email=" + email
				+ ", imgUrl=" + imgUrl + "]";
	}

}