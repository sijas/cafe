package com.peol.cafeteriaapp.adapters;

import com.peol.cafeteriaapp.items.CuisineItem;

import com.peol.cafeteriaapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;

public class CuisineListAdapter extends ArrayAdapter<CuisineItem> {

	Context context;
	int layoutResourceId;
	CuisineItem data[] = null;

	public CuisineListAdapter(Context context, int layoutResourceId,
			CuisineItem[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		CuisineHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new CuisineHolder();
			holder.title = (TextView) row.findViewById(R.id.cuisineitem);
			row.setTag(holder);
		} else {
			holder = (CuisineHolder) row.getTag();
		}
		CuisineItem item = getItem(position);
		holder.title.setText(item.name);
		return row;
	}

	static class CuisineHolder {
		TextView title;
	}
}