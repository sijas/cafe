package com.peol.cafeteriaapp.adapters;

import com.peol.cafeteriaapp.items.CartItem;
import com.peol.cafeteriaapp.items.FoodItem;
import com.peol.cafeteriaapp.items.SingleOrder;
import com.peol.cafeteriaapp.items.SingleOrderItem;
import com.peol.cafeteriaapp.screens.FoodCart;
import com.peol.cafeteriaapp.screens.MainScreen;
import com.peol.cafeteriaapp.screens.ViewOrders;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

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
import android.content.DialogInterface;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.webkit.WebSettings.TextSize;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

public class OrderedItemListAdpater extends ArrayAdapter<SingleOrder> {
	public Boolean pressed;
	Context context;
	int layoutResourceId;
	SingleOrder data[] = null;
	ViewOrders activity = null;
	ProgressDialog pd = null;
	String user_id;
	OrderedItemListAdpater adapter;
	public OrderedItemListAdpater(Context context, int layoutResourceId,
			SingleOrder[] data, ViewOrders activity, ProgressDialog pd,
			String user_id) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.activity = activity;
		this.pd = pd;
		this.user_id = user_id;
		adapter = this;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SingleOrderItem holder = null;
		final SingleOrder item = data[position];
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		if (row == null) {
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new SingleOrderItem();
			holder.token = (TextView) row.findViewById(R.id.tokenno);
			holder.orderstatus = (TextView) row.findViewById(R.id.orderstatus);
			holder.ordertime = (TextView) row.findViewById(R.id.orderedtime);
			holder.total = (TextView) row.findViewById(R.id.totalordervalue);
			holder.singleItem = (LinearLayout) row
					.findViewById(R.id.singleorderitem);
			holder.cancel = (ImageButton) row.findViewById(R.id.cancelorder);
			row.setTag(holder);
		} else {
			holder = (SingleOrderItem) row.getTag();
			holder.singleItem.removeAllViews();
		}
		for (int i = 0; i < item.items.length; i++) {
			LinearLayout single_item_of_order = (LinearLayout) inflater
					.inflate(R.layout.order_item_row_single, null);
			TextView orderitename = (TextView) single_item_of_order
					.findViewById(R.id.orderitemname);
			TextView orderitemcost = (TextView) single_item_of_order
					.findViewById(R.id.orderitemcost);
			TextView orderitemcount = (TextView) single_item_of_order
					.findViewById(R.id.orderitemcount);
			orderitename.setText(item.items[i].name);
			orderitemcost.setText("Rs " + item.items[i].price);
			orderitemcount.setText(item.items[i].count + " items");
			holder.singleItem.addView(single_item_of_order);
		}
		holder.token.setText(item.token);
		holder.ordertime.setText(item.order_time);
		holder.total.setText("Rs. "+item.total);
		if (item.processed == 0) {
			holder.orderstatus.setText("Status : Not Delivered");
		} else if (item.processed == 1) {
			holder.cancel.setVisibility(ImageButton.GONE);
			holder.orderstatus.setText("Status : Delivered");
		} else if (item.processed == 2) {
			holder.cancel.setVisibility(ImageButton.GONE);
			holder.orderstatus.setText("Status : Cancelled");
		}
		final SingleOrderItem h = holder;
		holder.cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						new ContextThemeWrapper(context,
								android.R.style.Theme_Holo_Light_Dialog));
				dialog.setTitle("Warning");
				dialog.setMessage("Do you want to cancel the order with Token no "
						+ h.token.getText().toString());
				dialog.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								AQuery aq = new AQuery(activity);
								String url = "http://184.107.229.154/~cafe/mobileApi.php";
								Map<String, Object> params = new HashMap<String, Object>();
								pd.show();
								params.put("user_id", user_id);
								params.put("requestType", "cancelOrder");
								params.put("token_no", h.token.getText()
										.toString());
								pd.show();
								aq.ajax(url, params, JSONObject.class,
										new AjaxCallback<JSONObject>() {
											@Override
											public void callback(String url,
													JSONObject object,
													AjaxStatus status) {
												if (object != null) {
													activity.getOrderedList();
													Toast.makeText(
															activity,
															activity.getString(R.string.cancelled),
															Toast.LENGTH_SHORT)
															.show();
												} else {
													Toast.makeText(
															activity,
															activity.getString(R.string.network_error),
															Toast.LENGTH_SHORT)
															.show();
												}
												pd.cancel();
											}
										});
							}
						});
				dialog.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});
				dialog.show();
			}
		});
		return row;
	}

	static class SingleOrderItem {
		LinearLayout singleItem;
		TextView token, ordertime, orderstatus, total;
		ImageButton cancel;
	}
}