package br.com.event.custom.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import br.com.event.custom.R;

public class SplashScreenActivity extends EventActivity implements Runnable {
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        new Handler().postDelayed(this, 3000);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	public void run() {
		startActivity(new Intent(this, ScheduleActivity.class));
		finish();
	}

}