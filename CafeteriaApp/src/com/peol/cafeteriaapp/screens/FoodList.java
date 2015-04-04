package com.peol.cafeteriaapp.screens;

import com.peol.cafeteriaapp.adapters.FoodItemAdapter;
import com.peol.cafeteriaapp.helper.Cart;
import com.peol.cafeteriaapp.helper.CartHelper;
import com.peol.cafeteriaapp.items.FoodItem;

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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class FoodList extends Activity {
	private GridView foodlist = null;
	private AQuery aq = null;
	ProgressDialog pd = null;
	SharedPreferences sharedPref = null;
	int catId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_list);
		foodlist = (GridView) findViewById(R.id.foodgrid);
		catId = getIntent().getExtras().getInt("catId");
		aq = new AQuery(this);
		pd = new ProgressDialog(this);
		pd.setCancelable(false);
		pd.setMessage(getString(R.string.progress_dialog_message));
		sharedPref = getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		String user_id = sharedPref.getString("logindata", "");
		String url = "http://184.107.229.154/~cafe/mobileApi.php";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", user_id);
		params.put("requestType", "getItems");
		params.put("catId", catId);
		pd.show();
		aq.ajax(url, params, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String data, AjaxStatus status) {
				if (data != null) {
					pd.cancel();
					try {
						JSONArray jArray = new JSONArray(data);
						int TotalLength = jArray.length();
						FoodItem[] fooditems = new FoodItem[TotalLength];
						for (int i = 0; i < TotalLength; i++) {
							JSONObject jobj = jArray.getJSONObject(i);
							fooditems[i] = new FoodItem(jobj.getInt("item_id"), jobj
									.getInt("cost"), jobj.getInt("cat_id"), jobj
									.getString("item_name"), jobj
									.getString("img_link"));
						}
						FoodItemAdapter adapter = new FoodItemAdapter(
								FoodList.this, R.layout.foodlist_item_row,
								fooditems);
						foodlist.setAdapter(adapter);
						foodlist.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								ImageView status = (ImageView) view
										.findViewById(R.id.foodselection);
								FoodItem currentItem = (FoodItem) foodlist
										.getItemAtPosition(position);
								Cart.addToCart(FoodList.this, currentItem,
										status);
							}
						});
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				else{
					Toast.makeText(FoodList.this,getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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
			FoodList.this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			return true;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		FoodList.this.finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}
}
