package br.com.event.custom.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import br.com.event.custom.R;
import br.com.event.custom.adapters.MoreIconAdapter;

public class MoreActivity extends EventActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_main);
		((TextView)findViewById(R.id.header1_title)).setText(R.string.menu_more);
		((Button)findViewById(R.id.header1_back_btn)).setOnClickListener(new BackBtnListener());
		
		GridView gridview = (GridView) findViewById(R.id.more_grid);  
		gridview.setAdapter(new MoreIconAdapter(this));
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_more:
				return false;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
}