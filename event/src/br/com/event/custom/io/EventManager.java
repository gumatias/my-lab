package br.com.event.custom.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import br.com.event.custom.infra.EventComponentParser;
import br.com.event.custom.model.Category;
import br.com.event.custom.model.Establishment;
import br.com.event.custom.model.EvenType;
import br.com.event.custom.model.Event;
import br.com.event.custom.model.EventContext;
import br.com.event.custom.model.Gallery;
import br.com.event.custom.model.Lecture;
import br.com.event.custom.model.Place;
import br.com.event.custom.model.Speaker;
import br.com.event.custom.model.Sponsor;
import br.com.event.custom.model.SpotType;

import com.google.gson.reflect.TypeToken;

public class EventManager {

	/* attempting to do some abstract stuff */
//	public T fetchData(EventContext eventCxt) {
//		try {
//			System.out.println("eventCxt.url():" + eventCxt.url());
//			String result = new GetEventDataTask().execute(eventCxt.url()).get();
//			return new EventComponentParser<T>().parseObject(result, new TypeToken<T>(){});
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	public Event getEvent() {
		Event event = null;
		try {
			String result = new GetEventDataTask().execute(EventContext.PUBLIC_EVENT.url()).get();
			if (result == null) return null;
			
			event = new EventComponentParser<Event>().parseObject(result, new TypeToken<Event>(){});
			event.setSpotType(SpotType.EVENT);
			event.setEventType(EvenType.LECTURE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return event;
	}

	public List<Lecture> getLectures() {
		try {
			String result = new GetEventDataTask().execute(EventContext.LECTURES.url()).get();
			if (result == null) return null;
			
			return new EventComponentParser<Lecture>()
					.parseArray(result, new TypeToken<Lecture>(){});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Speaker> getSpeakers() {
		try {
			String result = new GetEventDataTask().execute(EventContext.SPEAKERS.url()).get();
			if (result == null) return null;
			
			return new EventComponentParser<Speaker>()
					.parseArray(result, new TypeToken<Speaker>(){});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Place getPlaces() {
//		try {
//			String result = new GetEventDataTask().execute(
//					EventContext.PLACES.url()).get();
//			new EventComponentParser().parse(result);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	public List<Sponsor> getSponsors() {
		try {
			String result = new GetEventDataTask().execute(EventContext.SPONSORS.url()).get();
			if (result == null) return null;
			
			return new EventComponentParser<Sponsor>()
					.parseArray(result, new TypeToken<Sponsor>(){});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Category> getCategories() {
		try {
			String result = new GetEventDataTask().execute(EventContext.CATEGORIES.url()).get();
			if (result == null) return null;
			
			return new EventComponentParser<Category>()
					.parseArray(result, new TypeToken<Category>(){});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Establishment> getEstablishments() {
		try {
			String result = new GetEventDataTask().execute(EventContext.ESTABLISHMENTS.url()).get();
			if (result == null) return null;
			
			return new EventComponentParser<Establishment>()
					.parseArray(result, new TypeToken<Establishment>(){});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Gallery> getGalleries() {
		try {
			String result = new GetEventDataTask().execute(EventContext.GALLERIES.url()).get();
			if (result == null) return null;
			
			return new EventComponentParser<Gallery>()
					.parseArray(result, new TypeToken<Gallery>(){});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	private class GetEventDataTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... urls) {
			return executeHttpGet(urls[0]);
		}

	}

	private String executeHttpGet(String uriStr) {
		BufferedReader in = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(uriStr));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			return sb.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
