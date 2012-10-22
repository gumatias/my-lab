package com.google.imagesearch.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.imagesearch.model.Image;

public class ImageDAO extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "image_search.db",
			IMAGES_TABLE = "images", TAG = "tag", HASH = "hash", URL = "url", TITLE = "title";
	private static final String IMAGES_TABLE_CREATE = "create table "
			+ IMAGES_TABLE + " (_id integer primary key autoincrement, " + TAG
			+ " text not null, " + TITLE + " text not null, " + URL + " text not null, " + HASH + " text not null);";
	private static ImageDAO imageDAO;

	public static ImageDAO getInstance(Context context) {
		if (imageDAO == null)
			imageDAO = new ImageDAO(context);
		return imageDAO;
	}

	private ImageDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(IMAGES_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS images");
		onCreate(db);
	}

	public long insertImage(Image image, String tag) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TAG, tag);
		values.put(TITLE, image.getTitle());
		values.put(URL, image.getUrl());
		values.put(HASH, image.getHash());
		return db.insert(IMAGES_TABLE, null, values);
	}
	
	/**
	 * fetch images from the given tag
	 * @return array of images
	 */
	public ArrayList<Image> fetchImagesFromTag(String tag) {
		ArrayList<Image> images = new ArrayList<Image>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + TITLE + "," + URL + " FROM " + IMAGES_TABLE
				+ " WHERE " + TAG + "='" + tag + "';", null);

		while (cursor.moveToNext()) {
			String title = cursor.getString(cursor.getColumnIndex(TITLE));
			String url = cursor.getString(cursor.getColumnIndex(URL));
			images.add(new Image().setTitle(title).setUrl(url));
		}
		return images;
	}

	/**
	 * fetch images from the given tag
	 * @return array of hashs
	 */
	public ArrayList<String> fetchImagesHash(String tag) {
		ArrayList<String> hashs = new ArrayList<String>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + TAG + ", " + HASH + " FROM "
				+ IMAGES_TABLE + " WHERE " + TAG + "='" + tag + "';", null);

		while (cursor.moveToNext()) {
			String dbHash = cursor.getString(cursor.getColumnIndex(HASH));
			hashs.add(dbHash);
		}
		return hashs;
	}

	/**
	 * fetch all images regardless of the tag
	 * @return array of hashs
	 */
	public ArrayList<String> fetchImagesHash() {
		ArrayList<String> hashs = new ArrayList<String>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + HASH + " FROM " + IMAGES_TABLE
				+ ";", null);

		while (cursor.moveToNext()) {
			String dbHash = cursor.getString(cursor.getColumnIndex(HASH));
			hashs.add(dbHash);
		}
		return hashs;
	}
}
