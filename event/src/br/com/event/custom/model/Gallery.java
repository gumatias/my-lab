package br.com.event.custom.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class Gallery extends EventComponent {

	private volatile int id;
	
	private String name;

	@SerializedName("gallery_images")
	private List<Picture> pictures;

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

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}
	
}
