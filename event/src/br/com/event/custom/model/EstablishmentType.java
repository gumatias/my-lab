package br.com.event.custom.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class EstablishmentType extends EventComponent {

	@DatabaseField(id = true, index = true)
	private int id;
	
	@DatabaseField(canBeNull = false)
	private String name;

	public EstablishmentType() {}
	
	public EstablishmentType(int id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "EstablishmentType [id=" + id + ", name=" + name + "]";
	}
	
}