package br.com.event.custom.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Category extends EventComponent implements Parcelable {
	
	public final static String ID_FIELD_NAME = "category_name";
	
	@DatabaseField(id = true, index = true, columnName = ID_FIELD_NAME)
	private String name;
	
	@DatabaseField
	private boolean checked;

	public Category() {}
	
	public Category(String name) {
		this.name = name;
	}
	
    public Category(Parcel in) {  
        readFromParcel(in);  
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isChecked() {
		return checked;
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		public Category createFromParcel(Parcel source) {
			return new Category(source);
		}

		public Category[] newArray(int size) {
			return new Category[size];
		}

	};
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(String.valueOf(checked));
	}
	
	private void readFromParcel(Parcel in) {
		name 	= in.readString();
		checked = Boolean.parseBoolean(in.readString());
	}

	@Override
	public String toString() {
		return "Category [name=" + name + "]";
	}

}