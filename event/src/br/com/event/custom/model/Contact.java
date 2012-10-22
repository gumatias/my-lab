package br.com.event.custom.model;

public class Contact implements Comparable<Contact> {
	
	private String name, email;
	
	private boolean checked;
	
	public Contact(String name, String email, boolean checked){
		this.name = name;
		this.email = email;
		this.checked = checked;
	}
	
	public int compareTo(Contact contact) {
		return name.compareTo(contact.name);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	@Override
	public String toString() {
		return "Contact [name=" + name + ", email=" + email + ", checked="
				+ checked + "]";
	}
}
