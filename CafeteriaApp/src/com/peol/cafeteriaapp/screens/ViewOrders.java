package com.peol.cafeteriaapp.screens;

import com.peol.cafeteriaapp.adapters.OrderedItemListAdpater;
import com.peol.cafeteriaapp.helper.CartHelper;
import com.peol.cafeteriaapp.items.CartItem;
import com.peol.cafeteriaapp.items.SingleOrder;
import com.peol.cafeteriaapp.items.SingleOrderItem;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.peol.cafeteriaapp.R;
import com.peol.cafeteriaapp.R.id;
import com.peol.cafeteriaapp.R.layout;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ViewOrders extends Activity{
	ListView orderedlist = null;
	ProgressDialog pd = null;
	AQuery aq = null;
	SharedPreferences sharedPref = null;
	String user_id;
	int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_orders);
		type = getIntent().getIntExtra("type", 0);
		orderedlist = (ListView) findViewById(R.id.orderedlist);
		pd = new ProgressDialog(this);
		pd.setCancelable(false);
		pd.setMessage(getString(R.string.progress_dialog_message));
		aq = new AQuery(this);
		sharedPref = getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		user_id = sharedPref.getString("logindata", "");
		getOrderedList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (type == 0) 
			inflater.inflate(R.menu.notdeliveredscreen, menu);
		else
			inflater.inflate(R.menu.otherscreens, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(ViewOrders.this, ViewOrders.class);
		switch (item.getItemId()) {
		case R.id.viewcancelled:
			intent.putExtra("type", 2);
			startActivity(intent);
			break;
		case R.id.viewdelivered:
			intent.putExtra("type", 1);
			startActivity(intent);
			break;
		case R.id.cart:
			return CartHelper.checkCart(this);
		case android.R.id.home:
			ViewOrders.this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			return true;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		ViewOrders.this.finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	public void getOrderedList() {
		String url = "http://184.107.229.154/~cafe/mobileApi.php";
		Map<String, Object> params = new HashMap<String, Object>();
		pd.show();
		params.put("user_id", user_id);
		params.put("requestType", "getOrders");
		params.put("type", type);
		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object,
					AjaxStatus status) {
				try {
					if (object != null) {
						Boolean flag = object.getBoolean("isData");
						if (flag) {
							JSONObject dataObject = object
									.getJSONObject("data");
							JSONArray tokens = dataObject.names();
							SingleOrder[] orders = new SingleOrder[tokens
									.length()];
							for (int i = 0; i < tokens.length(); i++) {
								String token = tokens.getString(i);
								JSONObject itemObject = dataObject
										.getJSONObject(token);
								String delivery_time = itemObject
										.getString("delivery_time");
								String order_time = itemObject
										.getString("order_time");
								int sortOrder = itemObject.getInt("sortOrder");
								int total = itemObject.getInt("price");
								int processed = itemObject.getInt("processed");
								JSONArray items = itemObject
										.getJSONArray("items");
								SingleOrderItem[] cartitems = new SingleOrderItem[items
										.length()];
								for (int j = 0; j < items.length(); j++) {
									JSONObject singleItem = items
											.getJSONObject(j);
									cartitems[j] = new SingleOrderItem(
											singleItem.getString("name"),
											singleItem.getInt("price"),
											singleItem.getInt("qty"));
								}
								orders[i] = new SingleOrder(token,
										delivery_time, order_time, cartitems,
										total, sortOrder, processed);

							}
							for (int i = 0; i < orders.length; i++) {
								for (int j = 0; j < orders.length - 1; j++) {
									if (orders[j].sortOrder > orders[j + 1].sortOrder) {
										SingleOrder temp = orders[j + 1];
										orders[j + 1] = orders[j];
										orders[j] = temp;
									}
								}
							}
							OrderedItemListAdpater adapter = new OrderedItemListAdpater(
									ViewOrders.this, R.layout.ordered_item_row,
									orders, ViewOrders.this, pd, user_id);
							orderedlist.setAdapter(adapter);
						} else {
							Toast.makeText(ViewOrders.this,
									getString(R.string.nolist),
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(ViewOrders.this,
								getString(R.string.network_error),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pd.cancel();
			}
		});
	}
}