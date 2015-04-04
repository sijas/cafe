package com.peol.cafeteriaapp.screens;

import com.peol.cafeteriaapp.adapters.CuisineListAdapter;
import com.peol.cafeteriaapp.helper.CartHelper;
import com.peol.cafeteriaapp.items.CuisineItem;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CuisineList extends Activity {
	ListView cuisinelistview = null;
	AQuery aq = null;
	ProgressDialog pd = null;
	SharedPreferences sharedPref = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cuisine_list);
		cuisinelistview = (ListView) findViewById(R.id.cuisinelist);
		sharedPref = getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		String user_id = sharedPref.getString("logindata", "");
		aq = new AQuery(this);
		pd = new ProgressDialog(this);
		pd.setCancelable(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		pd.setMessage(getString(R.string.progress_dialog_message));
		String url = "http://184.107.229.154/~cafe/mobileApi.php";
		Map<String, Object> params = new HashMap<String, Object>();
		pd.show();
		params.put("user_id", user_id);
		params.put("requestType", "getCategory");
		aq.ajax(url, params, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String json, AjaxStatus status) {
				pd.cancel();
				try {
					if (json != null) {
						JSONArray jArray = new JSONArray(json);
						CuisineItem[] cuisinelist = new CuisineItem[jArray
								.length()];
						int TotalLength = jArray.length();
						for (int i = 0; i < TotalLength; i++) {
							JSONObject jsonObject = jArray.getJSONObject(i);
							String title = jsonObject.getString("cat_name");
							int id = jsonObject.getInt("cat_id");
							cuisinelist[i] = new CuisineItem(id, title);
						}
						CuisineListAdapter adapter = new CuisineListAdapter(
								CuisineList.this, R.layout.cuisine_list_item,
								cuisinelist);
						cuisinelistview.setAdapter(adapter);
						cuisinelistview.setClickable(true);
						cuisinelistview
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										CuisineItem item = (CuisineItem) cuisinelistview
												.getItemAtPosition(position);
										Intent intent=new Intent(CuisineList.this,FoodList.class);
										intent.putExtra("catId",item.id);
										startActivity(intent);
										overridePendingTransition(R.animator.enter_animation,
												R.animator.exit_animation);

									}
								});
					}
					else{
						Toast.makeText(CuisineList.this,getString(R.string.network_error), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.otherscreens, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.cart:
			return CartHelper.checkCart(this);
		case android.R.id.home:
			CuisineList.this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			return true;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		CuisineList.this.finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}
}
