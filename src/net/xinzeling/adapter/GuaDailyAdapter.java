package net.xinzeling.adapter;

import java.util.ArrayList;

import net.xinzeling2.R;
import net.xinzeling.model.GuaModel.Gua;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
//home 日视图下的卦adapter
public class GuaDailyAdapter extends BaseAdapter{
	private ArrayList<Gua> dataSet;
	private LayoutInflater inflater;
	public GuaDailyAdapter(Context ctx,ArrayList guaList){
		inflater = LayoutInflater.from(ctx);
		dataSet = new ArrayList<Gua>();
		this.notifyDataSetChanged();
	}
	
	public void notifyDataSetChanged(ArrayList<Gua> guaList){
		dataSet = guaList;
		super.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return dataSet.size();
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
	public View getView(int position, View convertView, ViewGroup parentView) {
		if(convertView ==null){
			convertView = inflater.inflate(R.layout.item_gua_child, parentView,false);
		}
		//@todo 
		return convertView;
	}
}
