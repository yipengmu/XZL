package net.xinzeling.wheel;

import net.xinzeling.wheel.WheelView.WheelViewAdapter;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WheelTextAdapter implements WheelViewAdapter {
    private String[] dataSet;

    protected Context context;

    public WheelTextAdapter(Context ctx, String[] items){
    	this.context=ctx;
    	this.dataSet = items;
    }
    
	@Override
	public int getItemsCount() {
		return dataSet.length;
	}

	@Override
	public View getItemView(int index, View convertView, ViewGroup parent) {
		TextView itemView;
		if(convertView ==null){
			itemView = new TextView(this.context);
			itemView.setTextColor(Color.WHITE);
		}else{
			itemView = (TextView)convertView;
		}
		itemView.setText(this.dataSet[index]);
		return itemView;
	}

	@Override
	public View getEmptyItemView(View convertView, ViewGroup parent) {
		return new TextView(this.context);
	}
}