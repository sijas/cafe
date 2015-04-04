package com.peol.cafeteriaapp.helper;

import com.peol.cafeteriaapp.items.CartItem;
import com.peol.cafeteriaapp.items.FoodItem;

import java.util.ArrayList;

import com.peol.cafeteriaapp.R;

import android.content.Context;
import android.widget.ImageView;

public class Cart {
	static ArrayList<FoodItem> cartitems = new ArrayList<FoodItem>();
	static ArrayList<Integer> ids = new ArrayList<Integer>();

	public static Boolean addToCart(Context c, FoodItem item, ImageView status) {
		if (ids.contains((Integer) item.id)) {
			cartitems.remove(findItemWithid(item.id));
			ids.remove((Integer) item.id);
			status.setImageDrawable(c.getResources().getDrawable(
					R.drawable.unselect));
			return false;
		} else {
			cartitems.add(item);
			status.setImageDrawable(c.getResources().getDrawable(
					R.drawable.select));
			ids.add(item.id);
			return true;
		}
	}

	public static Boolean checkSelection(int id) {
		if (ids.contains(id))
			return true;
		else
			return false;
	}

	public static void clearSelections() {
		cartitems.clear();
		ids.clear();
	}
	private static FoodItem findItemWithid(int id){
		for(int i=0;i<cartitems.size();i++){
			FoodItem singleItem=cartitems.get(i);
			if(singleItem.id==id)
				return singleItem;
		}
		return null;
	}
	public static int getNumberOfItems(){
		return cartitems.size();
	}
	public static CartItem[] getCartItems(){
		CartItem[] allItems=new CartItem[cartitems.size()];
		for(int i=0;i<cartitems.size();i++){
			FoodItem item=cartitems.get(i);
			allItems[i]=new CartItem(item.id, item.cost, item.category, item.name, item.img_link);
		}
		return allItems;
	}
}
