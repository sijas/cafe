package com.peol.cafeteriaapp.items;

public class FoodItem {
	public int id, cost, category;
	public String name, img_link;

	public FoodItem(int id, int cost, int category, String name, String img_link) {
		this.id = id;
		this.category = category;
		this.cost = cost;
		this.name = name;
		this.img_link = img_link;
	}
}
