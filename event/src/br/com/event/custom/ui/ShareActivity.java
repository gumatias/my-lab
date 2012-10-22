package br.com.event.custom.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SectionIndexer;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.model.Contact;

public class ShareActivity extends EventActivity {

	private ListView myListView;

	private List<Contact> contacts;

	private MyIndexerAdapter<String> adapter = null;

	private ProgressBar pb;
	
	private Button allBtn;
	
	private Button noneBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
		((TextView)findViewById(R.id.header1_title)).setText(R.string.share);
		((Button)findViewById(R.id.header1_back_btn)).setOnClickListener(new BackBtnListener());
		
		allBtn = (Button) findViewById(R.id.recommend_all_btn);
		allBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				selectAll();
			}
		});

		noneBtn = (Button) findViewById(R.id.recommend_none_btn);
		noneBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				deselectAll();
			}
		});

		Button emailBtn = (Button) findViewById(R.id.recommend_email_btn);
		emailBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendEmail(getCheckedContacts());
			}
		});

		Button shareBtn = (Button) findViewById(R.id.share_btn);
		shareBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TITLE, getText(R.string.share_event_subject));
				intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.share_event_body));
				startActivity(Intent.createChooser(intent, "Share using"));
			}
		});

		enableFooterButtons(false);
		// setting its values and views
		myListView = (ListView) findViewById(R.id.recommend_listview);
		myListView.setFastScrollEnabled(true);
		loadContacts();
	}

	private void loadContacts() {
		pb = (ProgressBar)findViewById(R.id.refresh_progress);
		pb.setVisibility(View.VISIBLE);
		new ShareTask().execute();
	}

	private void selectAll() {
		for (Contact c : contacts) {
			c.setChecked(true);
		}
		/* notifiyDataSetChanged triggers the re-draw */
		adapter.notifyDataSetChanged();
	}

	private void deselectAll() {
		for (Contact c : contacts) {
			c.setChecked(false);
		}
		/* notifiyDataSetChanged triggers the re-draw */
		adapter.notifyDataSetChanged();
	}

	private String[] getCheckedContacts() {
		String recipients = new String();
		for (Contact c : contacts) {
			/* concatenating the emails that were checked */
			if (c.isChecked())
				recipients += "," + c.getEmail();
		}
		return recipients.split(",");
	}

	/**
	 * Retrieve the contact from the DB and adds to the List
	 */
	protected void fillContactsInList() {
		contacts = new ArrayList<Contact>();
		// create a uri to access contacts
		Uri mContacts = ContactsContract.Data.CONTENT_URI;

		String[] projection = new String[] {
				ContactsContract.Data.RAW_CONTACT_ID,
				ContactsContract.Data.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Email.DATA };

		Cursor c = this.managedQuery(mContacts, projection, null, null,
				ContactsContract.Data.DISPLAY_NAME);

		Map<Integer, String> contactMap = new HashMap<Integer, String>();

		// move to the first row of the results table
		c.moveToFirst();
		int previousId = -1;
		int contactCount = 0;
		do {
			int personId = 0;
			try {
				personId = c.getInt(c
						.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));

			} catch (Exception e) {
				// System.out.println("no connnntacts");
				// TODO do something here when user has no contacts
				// TextView empty = (TextView)findViewById(android.R.id.empty);
				// empty.setText("No Contacts");
				break;
			}

			// if this person is already added in list, do not add again but
			// just add it's information
			if (personId != previousId) {
				String personName = c.getString(c
						.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));

				// setting the first letter on uppercase
				personName = personName.substring(0, 1).toUpperCase()
						+ personName.substring(1, personName.length());
				// the contactCount will only be summed up if it has an email
				contactMap.put(contactCount, personName);
			}
			String personEmail = c
					.getString(c
							.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

			if (personEmail != null && contactMap.get(contactCount) != null
					&& personEmail.contains("@")) {
				// adding only contacts that have email filled up
				contacts.add(new Contact(contactMap.get(contactCount),
						personEmail, false));

				contactCount++;
			}

			previousId = personId;

		} while (c.moveToNext());
		// closing the connection that's been opened
		c.close();

	}

	// the list adapter, where the magic happens
	class MyIndexerAdapter<T> extends ArrayAdapter<Contact> implements
			SectionIndexer {

		ArrayList<String> myElements;
		HashMap<String, Integer> alphaIndexer;

		String[] sections;
		int textViewResourceId;

		public MyIndexerAdapter(Context context, int textViewResourceId,
				List<Contact> objects) {
			super(context, textViewResourceId, objects);
			this.textViewResourceId = textViewResourceId;
			// here is the tricky stuff
			alphaIndexer = new HashMap<String, Integer>();
			// in this hashmap we will store here the positions for
			// the sections

			int size = contacts.size();
			for (int i = size - 1; i >= 0; i--) {
				Contact contact = contacts.get(i);
				String contryName = contact.getName();

				alphaIndexer.put(contryName.substring(0, 1), i);

				// We store the first letter of the word, and its index.
				// The Hashmap will replace the value for identical keys are
				// putted in
			}

			// now we have an hashmap containing for each first-letter
			// sections(key), the index(value) in where this sections begins

			// we have now to build the sections(letters to be displayed)
			// array .it must contains the keys, and must (I do so...) be
			// ordered alphabetically

			Set<String> keys = alphaIndexer.keySet();

			Iterator<String> it = keys.iterator();
			ArrayList<String> keyList = new ArrayList<String>();

			while (it.hasNext()) {
				String key = it.next();
				keyList.add(key);
			}

			Collections.sort(keyList);

			sections = new String[keyList.size()]; // simple conversion to an
			// array of object
			keyList.toArray(sections);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LinearLayout rowView;
			Contact contact = getItem(position);

			if (convertView == null) {
				rowView = new LinearLayout(getContext());
				LayoutInflater vi = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				vi.inflate(textViewResourceId, rowView, true);
			} else {
				rowView = (LinearLayout) convertView;
			}

			if ((position % 2) == 0) rowView.setBackgroundResource(R.drawable.lista_linha1);
			
			TextView contactName = (TextView) rowView
					.findViewById(R.id.recommend_contactname_txt);
			TextView contactEmail = (TextView) rowView
					.findViewById(R.id.recommend_contactemail_txt);
			CheckBox checkbox = (CheckBox) rowView
					.findViewById(R.id.recommend_row_check_button);

			contactName.setText(contact.getName());
			contactEmail.setText(contact.getEmail());
			checkbox.setChecked(contact.isChecked());

			return rowView;
		}

		public int getPositionForSection(int section) {
			String letter = sections[section];
			return alphaIndexer.get(letter);
		}

		public int getSectionForPosition(int position) {
			// you will notice it will be never called (right?)
			return 0;
		}

		public Object[] getSections() {
			return sections; // to string will be called each object, to display
			// the letter
		}

	}

	private void sendEmail(String[] recipients) {

		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		// Add attributes to the intent
		sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT,
				getText(R.string.share_event_subject));
		sendIntent.putExtra(Intent.EXTRA_TEXT, getText(R.string.share_event_body));
		sendIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
		sendIntent.setType("plain/text");

		startActivity(Intent.createChooser(sendIntent, "Event email"));
	}
	
	class ShareTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			fillContactsInList();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Collections.sort(contacts); // Must be sorted!

			// setting its values and views
			adapter = new MyIndexerAdapter<String>(getApplicationContext(),
					R.layout.share_row, contacts);

			myListView.setAdapter(adapter);
			// scrolling the list to the selected item

			// happens when the row is touched
			myListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long l) {
					Contact contact = contacts.get((int) l);
					contact.setChecked(!contact.isChecked());
					adapter.notifyDataSetChanged();
				};
			});
			pb.setVisibility(View.INVISIBLE);
			enableFooterButtons(true);
		}
		
	}
	
	private void enableFooterButtons(boolean enable) {
		noneBtn.setEnabled(enable);
		allBtn.setEnabled(enable);
	}
}
