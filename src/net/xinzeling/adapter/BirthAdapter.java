package net.xinzeling.adapter;

import net.xinzeling2.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BirthAdapter extends BaseAdapter{
	public static String[] dataSet = new String[]{"23:00-1:00(子时)","01:00-03:00(丑时)","03:00-05:00(寅时)","05:00-07:00(卯时)",
			"07:00-09:00(辰时)","09:00-11:00(巳时)","11:00-13:00(午时)","13:00-15:00(未时)","15:00-17:00(申时)",
			"17:00-19:00(酉时)","19:00-21:00(戌时)","21:00-23:00(亥时)"};
	public static String[] timeSet = new String[]{"子时","丑时","寅时","卯时","辰时","巳时","午时","未时","申时","酉时","戌时","亥时"};
	
	private LayoutInflater inflater;
	
	public BirthAdapter(Context ctx){
		inflater = LayoutInflater.from(ctx);
	}
	@Override
	public int getCount() {
		return dataSet.length;
	}

	@Override
	public Object getItem(int position) {
		return dataSet[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView ==null){
			convertView = inflater.inflate(R.layout.item_birth, parent,false);
		}
		TextView tv = (TextView)convertView;
		tv.setText(dataSet[position]);
		return tv;
	}

}
