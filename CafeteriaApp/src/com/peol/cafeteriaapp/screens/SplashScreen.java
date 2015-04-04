package com.peol.cafeteriaapp.screens;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.peol.cafeteriaapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SplashScreen extends Activity {
	private AQuery aq;
	private ProgressBar pb;
	Button refresh = null;
	Handler h = null;
	SharedPreferences myPreferences = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		aq = new AQuery(this);
		h = new Handler();
		myPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		pb = (ProgressBar) findViewById(R.id.networkProgress);
		refresh = (Button) findViewById(R.id.refresh);
		refresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pb.setVisibility(ProgressBar.VISIBLE);
				refresh.setVisibility(Button.INVISIBLE);
				testNetwork();
			}
		});
		testNetwork();
	}

	private void testNetwork(){
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				aq.ajax("http://google.com", String.class, new AjaxCallback<String>(){
					@Override
					public void callback(String url, String object,
							AjaxStatus status) {
						if(object!=null){
							Intent intent=null;
							String user_id=myPreferences.getString("logindata","");
							if(user_id.equals(""))
								intent = new Intent(SplashScreen.this, LoginScreen.class);
							else
								intent = new Intent(SplashScreen.this, MainScreen.class);
							intent.putExtra("type", "Full");
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intent);
							overridePendingTransition(R.animator.enter_animation,
									R.animator.exit_animation);	
						}
						else{
							pb.setVisibility(ProgressBar.INVISIBLE);
							refresh.setVisibility(Button.VISIBLE);
							Toast.makeText(SplashScreen.this,getString(R.string.network_error), Toast.LENGTH_SHORT).show();
						}
					}
				});
				
			}
		}, 2000);
	}
	@Override
	public void onBackPressed() {
		return;
	}
}
