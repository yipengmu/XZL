package net.xinzeling.adapter;

import java.util.ArrayList;

import net.xinzeling2.R;
import net.xinzeling.model.GuaModel.Gua;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class GuaListAdapter extends BaseExpandableListAdapter{
	private LayoutInflater inflater;
	private ArrayList<String> groupSet;
	private ArrayList<ArrayList<Gua>> childSet;
	private onGuaContentClickListener mGuaContentClickListener;
	public GuaListAdapter(Context ctx, ArrayList<String> groupList,ArrayList<ArrayList<Gua>> childList){
		inflater = LayoutInflater.from(ctx);
		groupSet = groupList;
		childSet = childList;
	}
	
	@Override
	public int getGroupCount() {
		return groupSet.size();
	}
	
	public void setGuaContentClickListener(onGuaContentClickListener m) {
		this.mGuaContentClickListener = m;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childSet.get(groupPosition).size();
	}
	
	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}
	
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		TextView groupView;
		if (convertView == null) {
			groupView = (TextView)inflater.inflate(R.layout.item_gua_group, parent, false);
		} else {
			groupView = (TextView)convertView;
		}
		groupView.setText(groupSet.get(groupPosition));
		return groupView;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_gua_child, parent, false);
		} 
		Gua gua = childSet.get(groupPosition).get(childPosition);
		if(gua !=null){
			TextView title = (TextView) convertView.findViewById(R.id.gua_item_title);
			title.setText(gua.title);
			TextView time =(TextView) convertView.findViewById(R.id.gua_item_time);
			time.setText(gua.time);
			TextView content =(TextView)convertView.findViewById(R.id.gua_item_content);
			content.setText(gua.inference);
			content.setTag(gua.guaid);
			content.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					if(mGuaContentClickListener!=null){
						mGuaContentClickListener.onClick((int)arg0.getTag());
					}
				}
				
			});
		}
		return convertView;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public interface onGuaContentClickListener{
		public void onClick(int guaid);
	}
}