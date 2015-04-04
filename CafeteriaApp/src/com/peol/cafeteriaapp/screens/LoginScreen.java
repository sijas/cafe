package com.peol.cafeteriaapp.screens;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.peol.cafeteriaapp.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginScreen extends Activity {
	String username = null;
	String password = null;
	AQuery aq = null;
	EditText etUsername = null;
	EditText etPassword = null;
	ProgressDialog pd = null;
	SharedPreferences sharedPref=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		etUsername = (EditText) findViewById(R.id.username);
		etPassword = (EditText) findViewById(R.id.password);
		Button signinButton = (Button) findViewById(R.id.signin);
		aq = new AQuery(this);
		pd = new ProgressDialog(this);
		pd.setCancelable(false);
		sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		pd.setMessage(getString(R.string.progress_dialog_message));
		signinButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				attemptSignin();
			}
		});
		etPassword
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.etsignin || id == EditorInfo.IME_NULL) {
							attemptSignin();
							return true;
						}
						return false;
					}
				});

	}

	private void attemptSignin() {
		String url = "http://184.107.229.154/~cafe/login.php";
		Map<String, Object> params = new HashMap<String, Object>();
		username = etUsername.getText().toString();
		password = etPassword.getText().toString();
		if(username.equals("")|| password.equals("")){
			showError();
			return;
		}
		pd.show();
		params.put("user", username);
		params.put("pass", password);
		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				pd.cancel();
				try {
					if (json.getBoolean("flag")) {
						String user_id=json.getString("user_id");
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putString("logindata",user_id);
						editor.commit();
						Intent intent = new Intent(LoginScreen.this,
								MainScreen.class);
						intent.putExtra("type", "Full");
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);
						overridePendingTransition(R.animator.enter_animation,
								R.animator.exit_animation);
					} else {
						showError();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void showError() {
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		etUsername.setText("");
		etPassword.setText("");
		etUsername.requestFocus();
		etUsername.startAnimation(shake);
		etPassword.startAnimation(shake);
		Toast.makeText(LoginScreen.this, getString(R.string.login_error), Toast.LENGTH_LONG).show();
	}
}
