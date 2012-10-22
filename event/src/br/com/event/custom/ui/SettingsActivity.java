package br.com.event.custom.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import br.com.event.custom.R;
import br.com.event.custom.adapters.SeparatedListAdapter;

public class SettingsActivity extends EventActivity {

	public final static String ITEM_TITLE = "title";  
    public final static String ITEM_CAPTION = "caption";  
  
    @Override  
    public void onCreate(Bundle icicle) {  
        super.onCreate(icicle);  
  
        List<Map<String,?>> event = new LinkedList<Map<String,?>>();  
        event.add(createItem("Feedback", "Send a feedback about the event and its application"));  
        
        List<Map<String,?>> general = new LinkedList<Map<String,?>>();
        general.add(createItem("Resync Data", "Resync all data from the server"));
        general.add(createItem("Sync automatically", "Every 15 mins"));
        general.add(createItem("Clear cache", "Clear both images and texts cache"));
        general.add(createItem("Passwords", "Clear all stored password"));
        general.add(createItem("Accounts", "Manage all synced accounts"));
        
        List<Map<String,?>> other = new LinkedList<Map<String,?>>();  
        other.add(createItem("About / Legal", "Event version and details"));  
        other.add(createItem("Get Support", "Get support via email"));
        other.add(createItem("Send Error", "Send log error to the developer")); 
        other.add(createItem("Send Statistics To Event", "Allow us to know what you have been using most of our application"));
  
        // create our list and custom adapter  
        SeparatedListAdapter adapter = new SeparatedListAdapter(this);  
//        adapter.addSection("Event", new ArrayAdapter<String>(this,  
//            R.layout.settings_item, new String[] { "First item", "Item two" }));  

        adapter.addSection("Event", new SimpleAdapter(this, event, R.layout.settings_complex,  
                new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.settings_complex_title, R.id.settings_complex_caption }));
        adapter.addSection("General", new SimpleAdapter(this, general, R.layout.settings_complex,  
                new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.settings_complex_title, R.id.settings_complex_caption }));
        adapter.addSection("Other", new SimpleAdapter(this, other, R.layout.settings_complex,  
                new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.settings_complex_title, R.id.settings_complex_caption }));
  
        ListView list = new ListView(this);  
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnSettingsClickListener());
        this.setContentView(list);  
  
    }
    
    private class OnSettingsClickListener implements OnItemClickListener {
    	
    	public void onItemClick(AdapterView<?> arg0, View arg1, int pos1,
				long pos2) {
    		switch (pos1) {
				case 1:
					System.out.println("settings 1");
					break;
				case 2:
					System.out.println("settings 2");
					break;
				case 3:
					System.out.println("settings 3");
					break;
				case 4:
					System.out.println("settings 4");
					break;
				case 5:
					System.out.println("settings 5");
					break;
				case 6:
					System.out.println("settings 6");
					break;
				case 7:
					System.out.println("settings 7");
					break;
				case 8:
					System.out.println("settings 8");
					break;
				case 9:
					System.out.println("settings 9");
					break;
				case 10:
					System.out.println("settings 10");
					break;
				case 11:
					System.out.println("settings 11");
					break;
				case 12:
					System.out.println("settings 12");
					break;
				default:
					throw new IllegalArgumentException("Settings option not found");
			}
    	}
    	
    }
    
    public Map<String,?> createItem(String title, String caption) {  
        Map<String, String> item = new HashMap<String, String>();  
        item.put(ITEM_TITLE, title);  
        item.put(ITEM_CAPTION, caption);
        return item;  
    }
    
}