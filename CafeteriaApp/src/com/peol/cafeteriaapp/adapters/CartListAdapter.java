package com.peol.cafeteriaapp.adapters;

import com.peol.cafeteriaapp.items.CartItem;
import com.peol.cafeteriaapp.items.FoodItem;
import com.peol.cafeteriaapp.screens.FoodCart;

import com.peol.cafeteriaapp.R;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings.TextSize;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class CartListAdapter extends ArrayAdapter<CartItem> {
	public Boolean pressed;
	Context context;
	int layoutResourceId;
	CartItem data[] = null;
	FoodCart ref;

	public CartListAdapter(Context context, int layoutResourceId,
			CartItem[] data, FoodCart ref) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.ref = ref;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		CartItemHolder holder = null;
		final CartItem item = data[position];
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new CartItemHolder();
			holder.foodName = (TextView) row.findViewById(R.id.cartitemname);
			holder.foodPrice = (TextView) row.findViewById(R.id.cartitemcost);
			holder.add = (Button) row.findViewById(R.id.cartitemadd);
			holder.subtract = (Button) row.findViewById(R.id.cartitemsubtract);
			holder.count = (EditText) row.findViewById(R.id.cartitemcount);
			final CartItemHolder h = holder;
			holder.add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int count = (h.count.getText().toString().equals("")) ? 0
							: Integer.parseInt(h.count.getText().toString());
					h.count.setText("" + (count + 1), BufferType.EDITABLE);
					item.count = count + 1;
					ref.calculateTotal();
				}
			});
			holder.add.setOnTouchListener(new OnTouchListener() {
				Handler repeater = new Handler();

				@Override
				public boolean onTouch(final View v, MotionEvent event) {
					int action = event.getAction();
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						pressed = true;
						v.performClick();
						repeater.postDelayed(new Runnable() {

							@Override
							public void run() {
								if (pressed) {
									v.performClick();
									repeater.postDelayed(this, 250);
								}
							}
						}, 250);
						break;
					case MotionEvent.ACTION_UP:
						pressed = false;
						break;
					}
					return true;
				}
			});
			holder.subtract.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int count = (h.count.getText().toString().equals("")) ? 0
							: Integer.parseInt(h.count.getText().toString());
					if (count > 0) {
						item.count = count - 1;
						h.count.setText("" + (count - 1), BufferType.EDITABLE);
						ref.calculateTotal();
					}
				}
			});
			holder.subtract.setOnTouchListener(new OnTouchListener() {
				Handler repeater = new Handler();

				@Override
				public boolean onTouch(final View v, MotionEvent event) {
					int action = event.getAction();
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						pressed = true;
						v.performClick();
						repeater.postDelayed(new Runnable() {

							@Override
							public void run() {
								if (pressed) {
									v.performClick();
									repeater.postDelayed(this, 250);
								}
							}
						}, 250);
						break;
					case MotionEvent.ACTION_UP:
						pressed = false;
						break;
					}
					return true;
				}
			});
			row.setTag(holder);
		} else {
			holder = (CartItemHolder) row.getTag();
		}
		holder.foodName.setText(item.name);
		holder.foodPrice.setText("Rs. " + item.cost);
		return row;
	}

	static class CartItemHolder {
		Button add;
		Button subtract;
		EditText count;
		TextView foodName;
		TextView foodPrice;
	}

}