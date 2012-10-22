package br.com.event.custom.model;

public enum EventContext {

	PUBLIC_EVENT	(".json"),
	LECTURES 		("/public.json"), 
	SPEAKERS		("/public.json"),
	PLACES			("/public.json"),
	SPONSORS		("/public.json"),
	GALLERIES		("/public.json"),
	CATEGORIES		("/public.json"),
	ESTABLISHMENTS	("/public.json");

	private String url;

	private EventContext(String filePath) {
		this.url = "http://eventweb.webbyapp.com/events/1/" + name().toLowerCase() + filePath;
	}

	public String url() {
		return url;
	}
	
}