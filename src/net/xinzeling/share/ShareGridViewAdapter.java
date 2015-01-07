/**
 * 
 */
package net.xinzeling.share;

import java.util.ArrayList;

import net.xinzeling2.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ShareGridViewAdapter extends BaseAdapter{

	private LayoutInflater mInflater = null;
	private Context mContext;
	private ArrayList<ShareAppInfo> mShareAppInfoList;
	
	public ShareGridViewAdapter(Context c) {
		mContext = c;
		mInflater = LayoutInflater.from(c);
	}
	
	/** 设置数据源 */
	public void setData(ArrayList<ShareAppInfo> dataList){
		mShareAppInfoList = dataList; 
	}
	
	@Override
	public int getCount() {
		if(mShareAppInfoList != null){
			return mShareAppInfoList.size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mShareAppInfoList.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;	
		if (convertView != null) {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (convertView == null || viewHolder == null) {
			convertView = mInflater.inflate(R.layout.share_sns_item,
					parent, false);

			viewHolder = new ViewHolder();
			viewHolder.ll_share_sns_item_content = (LinearLayout) convertView.findViewById(R.id.ll_share_sns_item_content);
			viewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
			viewHolder.tv_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
			
			convertView.setTag(viewHolder);
		}
		
		ShareAppInfo appInfo = mShareAppInfoList.get(position);
		viewHolder.tv_app_name.setText(appInfo.name);
		viewHolder.iv_app_icon.setBackgroundDrawable(mContext.getResources().getDrawable(appInfo.icon));
		return convertView;
	}

	class ViewHolder{
		LinearLayout ll_share_sns_item_content;
		ImageView iv_app_icon;
		TextView tv_app_name;
	}
}
