package com.peol.cafeteriaapp.screens;

import com.peol.cafeteriaapp.adapters.CartListAdapter;
import com.peol.cafeteriaapp.helper.Cart;
import com.peol.cafeteriaapp.items.CartItem;

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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FoodCart extends Activity {
	ListView cartlist = null;
	TextView totalAmount = null;
	CartItem[] cartitems = null;
	AQuery aq = null;
	String user_id = null;
	ProgressDialog pd = null;
	SharedPreferences sharedPref = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_cart);
		cartlist = (ListView) findViewById(R.id.cartlist);
		totalAmount = (TextView) findViewById(R.id.totalValue);
		aq = new AQuery(this);
		pd = new ProgressDialog(this);
		pd.setCancelable(false);
		pd.setMessage(getString(R.string.progress_dialog_message));
		sharedPref = getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		user_id = sharedPref.getString("logindata", "");
		cartitems = Cart.getCartItems();
		CartListAdapter adapter = new CartListAdapter(this,
				R.layout.cart_item_row, cartitems, this);
		cartlist.setAdapter(adapter);
		Button ordernow = (Button) findViewById(R.id.ordernow);
		ordernow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				makeAnOrder();
			}
		});
		calculateTotal();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			FoodCart.this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			return true;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		FoodCart.this.finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	public void calculateTotal() {
		int total = 0;
		for (int i = 0; i < cartitems.length; i++) {
			CartItem item = cartitems[i];
			int sum = item.cost * item.count;
			total += sum;
		}
		totalAmount.setText("Rs. " + total);
	}

	private int getTotal() {
		int total = 0;
		for (int i = 0; i < cartitems.length; i++) {
			CartItem item = cartitems[i];
			int sum = item.cost * item.count;
			total += sum;
		}
		return total;
	}

	public void makeAnOrder() {
		if (getTotal() > 0) {
			pd.show();
			String url = "http://184.107.229.154/~cafe/order_items2.php";
			Map<String, Object> order = new HashMap<String, Object>();
			String data = "[";
			Boolean flag = true;
			for (int i = 0; i < cartitems.length; i++) {
				JSONObject dataitem = new JSONObject();
				if (cartitems[i].count > 0) {
					try {
						dataitem.put("item_id", cartitems[i].id);
						dataitem.put("item_name", cartitems[i].name);
						dataitem.put("cost", cartitems[i].cost);
						dataitem.put("cat_id", cartitems[i].category);
						dataitem.put("qty", cartitems[i].count);
						if (flag)
							data += dataitem.toString();
						else
							data += "," + dataitem.toString();
						flag = false;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			data += "]";
			order.put("orders", data);
			order.put("user_id", user_id);
			order.put("time_lim", 15);
			order.put("msg", "Message");
			aq.ajax(url, order, JSONObject.class,
					new AjaxCallback<JSONObject>() {

						@Override
						public void callback(String url, JSONObject object,
								AjaxStatus status) {
							pd.cancel();
							if (object == null)
								Toast.makeText(FoodCart.this,
										getString(R.string.network_error),
										Toast.LENGTH_SHORT).show();
							else {
								try {
									Toast.makeText(
											FoodCart.this,
											getString(R.string.ordersuccess)
													+ object.getString("token_no"),
											Toast.LENGTH_SHORT).show();
									Cart.clearSelections();
									Intent intent = new Intent(FoodCart.this,
											MainScreen.class);
									intent.putExtra("type", "Full");
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
											| Intent.FLAG_ACTIVITY_CLEAR_TASK);
									startActivity(intent);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
		} else
			Toast.makeText(FoodCart.this, getString(R.string.order_error),
					Toast.LENGTH_SHORT).show();
	}
}
