package com.peol.cafeteriaapp.items;

public class CartItem {
	public int id, cost, category,count=1;
	public String name, img_link;

	public CartItem(int id, int cost, int category, String name, String img_link) {
		this.id = id;
		this.category = category;
		this.cost = cost;
		this.name = name;
		this.img_link = img_link;
	}
}
