package br.com.event.custom.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Place extends EventComponent {

	@DatabaseField(id = true, index = true)
	private String name;

	public Place() {}
	
	public Place(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Place [name=" + name + "]";
	}

}