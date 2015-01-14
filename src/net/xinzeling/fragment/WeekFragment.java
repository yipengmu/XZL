package net.xinzeling.fragment;

import java.util.ArrayList;
import java.util.Date;

import net.xinzeling.HomeActivity;
import net.xinzeling.MyApplication;
import net.xinzeling2.R;
import net.xinzeling.adapter.GuaDailyAdapter;
import net.xinzeling.adapter.ItemAdapter;
import net.xinzeling.adapter.ItemDailyAdapter;
import net.xinzeling.gua.JieGuaActivity;
import net.xinzeling.lib.BlurBehind;
import net.xinzeling.lib.CalendarView;
import net.xinzeling.lib.CalendarView.CalendarListener;
import net.xinzeling.lib.DateTime;
import net.xinzeling.model.ItemModel.Item;
import net.xinzeling.model.ItemModel;
import net.xinzeling.model.LunarModel;
import net.xinzeling.model.GuaModel.Gua;
import net.xinzeling.note.NoteActivity;
import net.xinzeling.note.NoteCheckActivity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class WeekFragment extends Fragment implements CalendarListener,OnClickListener,OnItemClickListener,OnCheckedChangeListener{

	private HomeActivity homeActivity;
	public CalendarView calendarView;
	private ListView itemListView;
	private ItemDailyAdapter itemAdapter;
	
	private ItemAdapter itemGuaAdapter;
	private Item itemGua;
	private ArrayList<Item> itemListGua;
	private RadioGroup radioGroup;
	private ImageView todayBtn;
	private int selectDateYYYYMMDD;
	private int selectGuaOrNote;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		homeActivity = (HomeActivity)this.getActivity();
		selectDateYYYYMMDD = 0;
		selectGuaOrNote = 0;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_home_week, container, false);
		calendarView =(CalendarView)view.findViewById(R.id.calendar_week);
		calendarView.setCalendarListener(this);
		todayBtn = (ImageView)view.findViewById(R.id.goback_today);
		todayBtn.setOnClickListener(this);
		
		itemGuaAdapter = null;
		itemAdapter = null;
		itemListView = (ListView)view.findViewById(R.id.list_note);
		itemListView.setOnItemClickListener(this);
		
		radioGroup =(RadioGroup)view.findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(this);
		if(selectDateYYYYMMDD==0){
			this.onDateSelect(new Date(),true);
		}
		return view;
	}

	private void setTodayBtnShow(boolean isCurrent) {
		if(isCurrent){
			todayBtn.setVisibility(View.INVISIBLE);
		}else{
			todayBtn.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
		itemGua = itemListGua.get(position);
		if (itemGua.referType == ItemModel.REFER_GUA) {
			Intent intent = new Intent(homeActivity, JieGuaActivity.class);
			intent.putExtra("guaid", itemGua.referId);
			this.startActivity(intent);
		}// else if (item.referType == ItemModel.REFER_NOTE) {
//			BlurBehind.getInstance().execute(homeActivity, new Runnable() {
//				public void run() {
//					Intent intent = new Intent(homeActivity, NoteCheckActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					intent.putExtra("noteid", item.referId);
//					startActivity(intent);
//				}
//			});
//		} else if (item.referType == ItemModel.REFER_MEM) {
//			BlurBehind.getInstance().execute(homeActivity, new Runnable() {
//				public void run() {
//					Intent intent = new Intent(homeActivity, MemCheckActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					intent.putExtra("memid", item.referId);
//					startActivity(intent);
//				}
//			});
//		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.goback_today:
			todayBtn.setVisibility(View.INVISIBLE);
			calendarView.goBackToday();
			homeActivity.refreshTitleTxt(true);
			selectDateYYYYMMDD = Integer.valueOf(DateTime.getTodayYmd(null));
			//今日信息
			break;
		}
	}
	
	//根据日期选择当天的数据填充到列表
	private class NoteLoadTask extends AsyncTask<Integer,Void,Void>{
		private ArrayList<Item> itemList;
		@Override
		protected Void doInBackground(Integer... params) {
			itemList = new ArrayList<Item>();
			return null;
		}
		@Override
		protected void onPostExecute(Void result){
			if(itemAdapter ==null){
				itemAdapter = new ItemDailyAdapter(homeActivity);		
				itemListView.setAdapter(itemAdapter);
			}else{
				itemAdapter.notifyDataSetChanged(itemList);
			}
		}
	}
	
	private class GuaLoadTask extends AsyncTask<Integer,Void,Void>{
		@Override
		protected Void doInBackground(Integer... args) {
			itemListGua = ItemModel.getItemList(ItemModel.REFER_GUA,args[0]+"");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			itemGuaAdapter = null;
			if (itemGuaAdapter == null) {
				itemGuaAdapter = new ItemAdapter(homeActivity, itemListGua);
				itemListView.setAdapter(itemGuaAdapter);
			} else {
				itemGuaAdapter.notifyDataSetChanged(itemListGua);
			}
		}
	}	

	@Override
	public void onResume() {
		super.onResume();
		if(selectGuaOrNote!=R.id.radio_note){
			new GuaLoadTask().execute(selectDateYYYYMMDD);
		}else{
			new NoteLoadTask().execute(selectDateYYYYMMDD);
		}
	}
	
	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		if(checkedId==0){
			selectGuaOrNote = 0;
			new NoteLoadTask().execute();
		}else{
			selectGuaOrNote = R.id.radio_note;
			new GuaLoadTask().execute(selectDateYYYYMMDD);
		}
	}
	
	@Override
	public void onMonthChange(Date date,boolean isCurrent) {
		this.onDateSelect(date,isCurrent);
	}

	@Override
	public void onDateSelect(Date date,boolean isCurrent) {
		homeActivity.lunar = LunarModel.fetchByDate(date);
		homeActivity.refreshTitleTxt(false);

		selectDateYYYYMMDD = Integer.valueOf(DateTime.getTodayYmd(date));
		onCheckedChanged(null,selectGuaOrNote);
		setTodayBtnShow(isCurrent);
	}

	@Override
	public void onDoubleClick(Date date) {
		// TODO Auto-generated method stub
		this.startActivity(new Intent(homeActivity, NoteActivity.class));
	}
}