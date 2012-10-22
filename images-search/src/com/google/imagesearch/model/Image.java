package com.google.imagesearch.model;

import java.io.Serializable;

public class Image implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String title, width, height, url, hash;

	public String getHash() {
		return hash;
	}

	public Image setHash(String hash) {
		this.hash = hash;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Image setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getWidth() {
		return width;
	}

	public Image setWidth(String width) {
		this.width = width;
		return this;
	}

	public String getHeight() {
		return height;
	}

	public Image setHeight(String height) {
		this.height = height;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Image setUrl(String url) {
		this.url = url;
		/* automatically setting the hash for the given url */
		setHash(stringToHash(url));
		return this;
	}

	private String stringToHash(String str) {
		return String.valueOf(str.hashCode());
	}
}
