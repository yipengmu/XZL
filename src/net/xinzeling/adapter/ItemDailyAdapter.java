package net.xinzeling.adapter;

import java.util.ArrayList;

import net.xinzeling2.R;
import net.xinzeling.model.ItemModel.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemDailyAdapter extends BaseAdapter {

	private String[] hours = new String[] {"00:00-02:00", "03:00-05:00",
			"06:00-08:00", "09:00-11:00", "12:00-14:00", "15:00-17:00",
			"18:00-20:00", "21:00-23:00" };
	private LayoutInflater inflater;

	public ItemDailyAdapter(Context ctx) {
		inflater =LayoutInflater.from(ctx);
	}

	public void notifyDataSetChanged(ArrayList<Item> itemList){
		//dataSet = itemList;
		super.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return hours.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = inflater.inflate(R.layout.item_item_daily, parent,false);
		}
		TextView hour = (TextView)convertView.findViewById(R.id.item_hour);
		hour.setText(hours[position]);
		return convertView;
	}

}
