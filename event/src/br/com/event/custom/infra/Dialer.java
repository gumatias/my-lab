package br.com.event.custom.infra;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Dialer {

	private Context context;
	
	public Dialer(Context context) {
		this.context = context;
	}
	
	public void call(String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ phoneNumber));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}