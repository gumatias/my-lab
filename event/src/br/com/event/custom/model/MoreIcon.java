package br.com.event.custom.model;


public class MoreIcon {

	private Integer title;
	
	private Integer drawable;
	
	private Class<?> clazz;

	public MoreIcon(Integer title, Integer drawable, Class<?> clazz) {
		super();
		this.title = title;
		this.drawable = drawable;
		this.clazz = clazz;
	}
	
	public Integer getDrawable() {
		return drawable;
	}

	public void setDrawable(Integer drawable) {
		this.drawable = drawable;
	}

	public Integer getTitle() {
		return title;
	}

	public void setTitle(Integer title) {
		this.title = title;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public String toString() {
		return "MoreIcon [title=" + title + ", cls=" + clazz + "]";
	}
	
}
