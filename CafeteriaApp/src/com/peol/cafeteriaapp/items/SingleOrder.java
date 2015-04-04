package com.peol.cafeteriaapp.items;

public class SingleOrder {
	public String token,delivery_time,order_time;
	public SingleOrderItem[] items;
	public int total,sortOrder,processed;
	public SingleOrder(String token,String delivery_time,String order_time,SingleOrderItem[] items,int total,int sortOrder,int processed) {
		this.token=token;
		this.delivery_time=delivery_time;
		this.order_time=order_time;
		this.total=total;
		this.items=items;
		this.sortOrder=sortOrder;
		this.processed=processed;
	}
}
