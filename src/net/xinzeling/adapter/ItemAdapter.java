package net.xinzeling.adapter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.xinzeling2.R;
import net.xinzeling.model.ItemModel;
import net.xinzeling.model.ItemModel.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter{
	ArrayList<Item> dataSet;
	LayoutInflater inflater;
	Context context;
	
	public ItemAdapter(Context ctx,ArrayList<Item> itemList){
		inflater = LayoutInflater.from(ctx);
		dataSet=itemList;
		context = ctx;
	}

	@Override
	public int getCount() {
		return dataSet.size();
	}

	public void notifyDataSetChanged(ArrayList<Item> itemList){
		dataSet = itemList;
		super.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView ==null){
			convertView = inflater.inflate(R.layout.item_item, parent,false);
		}
		Item item = this.dataSet.get(position);
		TextView tv = (TextView)convertView.findViewById(R.id.note_item_content);
		TextView daytime = (TextView)convertView.findViewById(R.id.note_item_daytime);

		tv.setText(item.title);
		daytime.setText(item.showDaytime);

		if(item.referType==ItemModel.REFER_NOTE){
			Pattern pattern = Pattern.compile("_x_icon_y_\\d{1,2}_");
			String title = item.title;
			Matcher matcher = pattern.matcher(title);
			int noteResId = -1;
			while(matcher.find()){
				//重新设置小图标及标题
				String find_str = matcher.group();
				if(noteResId==-1){
					noteResId = Integer.valueOf(find_str.replace("_x_icon_y_", "").replace("_", ""));
				}
				title = title.replaceAll(find_str, "");
			}
			if(noteResId!=-1){
				tv.setText(title);
				ImageView noteIcon = (ImageView)convertView.findViewById(R.id.note_item_icon);
				noteIcon.setImageResource(context.getResources().getIdentifier("small_note_topic_"+noteResId, "drawable", "net.xinzeling"));
			}
		}
		if(item.referType==ItemModel.REFER_GUA){
			convertView.findViewById(R.id.note_item_alarm).setVisibility(View.GONE);
			ImageView noteIcon = (ImageView)convertView.findViewById(R.id.note_item_icon);
			//noteIcon.setImageResource(resId);
		}else{
			convertView.findViewById(R.id.note_item_alarm).setVisibility(View.VISIBLE);
		}
		return convertView;
	}
}