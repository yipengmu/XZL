package net.xinzeling.ui.myxzl;

import java.util.ArrayList;

import net.xinzeling2.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyHistoryGuaAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private ArrayList<HistoryGuaBean> mData;
	public Context context;

	public MyHistoryGuaAdapter(Context c) {
		context = c;
	}
	
	public void setData(ArrayList<HistoryGuaBean> data){
		mData = data;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mData == null ?0:mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return mData == null ? null:mData.get(arg0);
	}

	@Override
	public View getView(int index, View v, ViewGroup arg2) {
		v = LayoutInflater.from(context).inflate(R.layout.lv_zhuafa_item_layout, arg2, false);
		
		drawCell(v,index);
		return v;
	}

	private void drawCell(View v, int index) {
		TextView msg_title = (TextView) v.findViewById(R.id.tv_zf_msg_title);
		TextView date_title = (TextView) v.findViewById(R.id.tv_zf_date_title);
		
//		msg_title.setText(mData.get(index).title);
//		date_title.setText(mData.get(index).time);
//		msg_title.setText(mData.get(index).msgTitle);
	}
	

	@Override
	public long getItemId(int position) {
		return position;
	}
}
