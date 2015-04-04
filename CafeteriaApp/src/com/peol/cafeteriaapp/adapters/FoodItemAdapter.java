package com.peol.cafeteriaapp.adapters;

import com.peol.cafeteriaapp.helper.Cart;
import com.peol.cafeteriaapp.items.FoodItem;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.peol.cafeteriaapp.R;

import android.app.Activity;
import android.content.Context;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodItemAdapter extends ArrayAdapter<FoodItem>{

    Context context; 
    int layoutResourceId;    
    FoodItem data[] = null;
    
    public FoodItemAdapter(Context context, int layoutResourceId, FoodItem[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FoodItemHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new FoodItemHolder();
            holder.foodIcon = (ImageView)row.findViewById(R.id.foodicon);
            holder.foodName = (TextView)row.findViewById(R.id.foodname);
            holder.foodPrice = (TextView)row.findViewById(R.id.foodprice);
            holder.selection=(ImageView) row.findViewById(R.id.foodselection);
            row.setTag(holder);
        }
        else
        {
            holder = (FoodItemHolder)row.getTag();
        }
        
        FoodItem item = data[position];
        UrlImageViewHelper.setUrlDrawable(holder.foodIcon, item.img_link);
        holder.foodName.setText(item.name);
        holder.foodPrice.setText("Rs. "+item.cost);
        if(Cart.checkSelection(item.id))
        	holder.selection.setImageDrawable(row.getResources().getDrawable(R.drawable.select));
        else
        	holder.selection.setImageDrawable(row.getResources().getDrawable(R.drawable.unselect));
        return row;
    }
    
    
    static class FoodItemHolder
    {
        ImageView foodIcon;
        TextView foodName;
        TextView foodPrice;
        ImageView selection;
    }
}