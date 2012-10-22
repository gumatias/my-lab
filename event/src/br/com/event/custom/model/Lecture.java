package br.com.event.custom.model;

import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Lecture extends EventComponent implements Parcelable {

	public final static String ID_FIELD_NAME = "id";
	
	public final static String USER_FAVORITE_FIELD_NAME = "is_user_favorite";

	@DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
	private transient int id;

	@DatabaseField(index = true)
	private String name;

	@DatabaseField(canBeNull = false, columnName = "initial_date", dataType = DataType.DATE)
	@SerializedName("initial_date")
	private Date initialDate;

	@DatabaseField(canBeNull = false, columnName = "end_date", dataType = DataType.DATE)
	@SerializedName("end_date")
	private Date endDate;

	@DatabaseField(foreign = true, foreignAutoCreate = true, columnName = Category.ID_FIELD_NAME)
	private Category category;

	@DatabaseField(foreign = true, foreignAutoCreate = true)
	private Place place;

	private List<Speaker> speakers;

	@DatabaseField(canBeNull = false)
	private String description;

	@DatabaseField(columnName = "user_note")
	private String userNote;

	@DatabaseField(dataType = DataType.BOOLEAN, columnName = USER_FAVORITE_FIELD_NAME)
	private Boolean userFavourite;

	public Lecture() {
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

	public void setPlace(Place place) {
		this.place = place;
	}

	public Place getPlace() {
		return place;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setSpeakers(List<Speaker> speakers) {
		this.speakers = speakers;
	}

	public List<Speaker> getSpeakers() {
		return speakers;
	}

	@Override
	public String toString() {
		return "Lecture [id=" + id + ", name=" + name + "]";
	}

	public Lecture(Parcel in) {
		readFromParcel(in);
	}

	private void readFromParcel(Parcel in) {
		id = in.readInt();
		name = in.readString();
		initialDate = new Date(in.readLong());
		endDate = new Date(in.readLong());
		category = new Category(in.readString());
		place = new Place(in.readString());
		description = in.readString();
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeLong(initialDate.getTime());
		dest.writeLong(endDate.getTime());
		dest.writeString(category != null ? category.getName() : null);
		dest.writeString(place != null ? place.getName() : null);
		dest.writeString(description);
	}

	public int describeContents() {
		return 0;
	}

	public String getUserNote() {
		return userNote;
	}

	public void setUserNote(String userNote) {
		this.userNote = userNote;
	}

	public Boolean isUserFavorite() {
		return userFavourite;
	}

	public void setUserFavorite(Boolean userFavorite) {
		this.userFavourite = userFavorite;
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		public Lecture createFromParcel(Parcel source) {
			return new Lecture(source);
		}

		public Lecture[] newArray(int size) {
			return new Lecture[size];
		}

	};

}