package com.peol.cafeteriaapp.helper;

import com.peol.cafeteriaapp.screens.FoodCart;

import com.peol.cafeteriaapp.R;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CartHelper {
	public static Boolean checkCart(Context c) {
		if(Cart.cartitems.size()>0){
			Intent intent=new Intent(c,FoodCart.class);
			c.startActivity(intent);
		}
		else
			Toast.makeText(c,"Empty cart", Toast.LENGTH_LONG).show();
		return true;
	}
}
