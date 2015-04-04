package com.peol.cafeteriaapp.screens;

import com.peol.cafeteriaapp.helper.Cart;
import com.peol.cafeteriaapp.helper.CartHelper;

import com.peol.cafeteriaapp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainScreen extends Activity implements OnClickListener {

	SharedPreferences sharedPref = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		sharedPref = getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		Button bookOrder = (Button) findViewById(R.id.order_new);
		Button viewOrders = (Button) findViewById(R.id.view_order);
		Button manageFriends = (Button) findViewById(R.id.manage_friends);
		Button support = (Button) findViewById(R.id.support);
		bookOrder.setOnClickListener(this);
		viewOrders.setOnClickListener(this);
		manageFriends.setOnClickListener(this);
		support.setOnClickListener(this);
		Cart.clearSelections();
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				new ContextThemeWrapper(this,
						android.R.style.Theme_Holo_Light_Dialog));
		dialog.setTitle("Warning");
		dialog.setMessage("Do you want to exit");
		dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainScreen.this.finish();
			}
		});
		dialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainscreen, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.cart:
			return CartHelper.checkCart(this);
		case R.id.logout:
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("logindata", "");
			editor.commit();
			Intent intent = new Intent(MainScreen.this, LoginScreen.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			overridePendingTransition(R.animator.enter_animation,
					R.animator.exit_animation);
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.order_new:
			intent = new Intent(MainScreen.this, CuisineList.class);
			startActivity(intent);
			intent.putExtra("type", 0);
			overridePendingTransition(R.animator.enter_animation,
					R.animator.exit_animation);
			break;
		case R.id.view_order:
			intent = new Intent(MainScreen.this, ViewOrders.class);
			startActivity(intent);
			overridePendingTransition(R.animator.enter_animation,
					R.animator.exit_animation);
			break;
		case R.id.manage_friends:
			intent = new Intent(MainScreen.this, FriendListScreen.class);
			startActivity(intent);
			overridePendingTransition(R.animator.enter_animation,
					R.animator.exit_animation);
			break;
		}
	}
}
