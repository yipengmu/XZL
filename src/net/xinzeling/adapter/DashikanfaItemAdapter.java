package net.xinzeling.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.xinzeling2.R;
import net.xinzeling.MyApplication;
import net.xinzeling.lib.LunarCalendar;
import net.xinzeling.news.GuaNewsDetailActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DashikanfaItemAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<String[]> msg_list_data;
//	private Typeface tf;
	public Context context;

	public DashikanfaItemAdapter(Context ctx,Typeface tf) {
//		this.tf = tf;
		context = ctx;
		inflater =LayoutInflater.from(ctx);
		//		String[] item1 = {"1","1","0","2014-10-11","click_id"};//内部编号 是否是日期标题 是否读过 标题值 新闻id
		//		String[] item2 = {"2","0","0","xxxx","23"};
		msg_list_data = new ArrayList<String[]>();
		//		msg_list_data.add(item1);
		//		msg_list_data.add(item2);
	}
	
	public DashikanfaItemAdapter(Context ctx) {
		context = ctx;
		inflater =LayoutInflater.from(ctx);
		//		String[] item1 = {"1","1","0","2014-10-11","click_id"};//内部编号 是否是日期标题 是否读过 标题值 新闻id
		//		String[] item2 = {"2","0","0","xxxx","23"};
		msg_list_data = new ArrayList<String[]>();
		//		msg_list_data.add(item1);
		//		msg_list_data.add(item2);
	}


	public void setData(JSONArray arr) throws JSONException {
		if(arr.length()<=0)return;
		Map<String,ArrayList<JSONObject>> date_data_map = new HashMap<String,ArrayList<JSONObject>>();
		for(int i=0;i<arr.length();i++){
			JSONObject item = arr.getJSONObject(i);
			if(item!=null){
				String date = (item.getString("vc_RealDate")).substring(0,10);
				if(!date_data_map.containsKey(date)){
					ArrayList<JSONObject> l = new ArrayList<JSONObject>();
					date_data_map.put(date, l);
				}
				date_data_map.get(date).add(item);
			}
		}
		List<Map.Entry<String, ArrayList<JSONObject>>> infoIds =
				new ArrayList<Map.Entry<String, ArrayList<JSONObject>>>(date_data_map.entrySet());

		Collections.sort(infoIds, new Comparator<Map.Entry<String, ArrayList<JSONObject>>>() {   

			@Override
			public int compare(Entry<String, ArrayList<JSONObject>> arg0,
					Entry<String, ArrayList<JSONObject>> arg1) {
				return arg1.getKey().toString().compareTo(arg0.getKey().toString());
			}
		}); 
		int index=0;
		msg_list_data.clear();
		for(int i=0;i<infoIds.size();i++){
			Entry<String,ArrayList<JSONObject>> m = infoIds.get(i);
			String[] title = new String[]{index+"","1","0",m.getKey(),"0"};
			msg_list_data.add(title);
			index++;
			ArrayList<JSONObject> date_data_list = m.getValue();
			for(int date_data_list_size=0;date_data_list_size<date_data_list.size();date_data_list_size++){
				JSONObject one_data = date_data_list.get(date_data_list_size);
				String[] item = new String[]{"","","","",""};
				item[0] = index+"";
				item[1] = "0";
				item[2] = "0";
				item[3] = one_data.getString("vc_Title");
				item[4] = one_data.getString("i_PushID");
				msg_list_data.add(item);
				index++;
			}
		}
	}

	@Override
	public int getCount() {
		int size = 0;
		size = msg_list_data.size();
		return size;
	}

	@Override
	public Object getItem(int arg0) {
		Object ob;
		ob = msg_list_data.get(arg0);
		return ob;
	}

	@Override
	public long getItemId(int arg0) {
		long ret;
		ret = Integer.valueOf(msg_list_data.get(arg0)[0]);
		return ret;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		arg1 = inflater.inflate(R.layout.msglist_content, arg2,false);
		LinearLayout only_title_line = (LinearLayout)arg1.findViewById(R.id.only_title_line);
		LinearLayout only_msg_title_line = (LinearLayout)arg1.findViewById(R.id.only_msg_title_line);

		String[] item = (String[])getItem(arg0);
		//System.out.println(arg0+"=>"+item[1]+":"+item[3]);
		boolean isOnlyDateTitle = "1".equals(item[1]) == true;

		if(isOnlyDateTitle){
			TextView title_date = (TextView)arg1.findViewById(R.id.date_title);
//			title_date.setTypeface(tf);
			title_date.setText(item[3].replace("-", "."));

			only_title_line.setTag(isOnlyDateTitle);
			only_title_line.setVisibility(View.VISIBLE);
			only_msg_title_line.setVisibility(View.GONE);
			//设置时间标题
		}else{
			ImageView read_status = (ImageView)arg1.findViewById(R.id.msg_read_status);

			//判断是否读过了
			if(MyApplication.isNewsReaded(Integer.valueOf(item[4]))){
				read_status.setImageResource(R.drawable.back_huangli_content);
			}
			TextView msg_title = (TextView)arg1.findViewById(R.id.msg_title);
//			msg_title.setTypeface(tf);
			msg_title.setTag(item[4]);
			msg_title.setText(item[3]);
			msg_title.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Intent news = new Intent(context,GuaNewsDetailActivity.class);
					//news.putExtra("news_id", Integer.valueOf((String)arg0.getTag()));
					Bundle bundle = new Bundle();
					bundle.putInt("news_id", Integer.valueOf((String)arg0.getTag()));
					news.putExtras(bundle);
					news.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(news);
					//设置为读
					ImageView read_status = (ImageView)((LinearLayout)(arg0.getParent())).findViewById(R.id.msg_read_status);
					read_status.setImageResource(R.drawable.back_huangli_content);
				}

			});

			only_msg_title_line.setTag(!isOnlyDateTitle);
			only_msg_title_line.setVisibility(View.VISIBLE);
			only_title_line.setVisibility(View.GONE);

		}

		return arg1;
	}

}
