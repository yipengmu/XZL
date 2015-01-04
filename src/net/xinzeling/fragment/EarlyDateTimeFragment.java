package net.xinzeling.fragment;

import java.util.Arrays;

import net.xinzeling2.R;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.WheelView;
import net.xinzeling.note.NoteActivity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class EarlyDateTimeFragment extends Fragment implements OnClickListener{
	private NoteActivity activity;

	private WheelView wv_month;
	private static final String[] MONTHS = new String[]{" 0月", " 1月"," 2月"," 3月"," 4月"," 5月"," 6月"," 7月"," 8月"," 9月","10月","11月","12月"};

	private WheelView wv_day;
	private static final String[] DAYS = new String[]{" 0天"," 1天"," 2天"," 3天"," 4天"," 5天"," 6天"," 7天"," 8天"," 9天","10天","11天","12天","13天","14天","15天","16天","17天","18天","19天","20天","21天","22天","23天","24天","25天","26天","27天","28天","29天","30天"};
	
	private WheelView wv_hour;
	private static final String[] HOURS = new String[]{" 0时"," 1时"," 2时"," 3时"," 4时"," 5时"," 6时"," 7时"," 8时"," 9时","10月时","11时","12时","13时","14时","15时","16时","17时","18时","19时","20时","21时","22时","23时","24时"};

	private WheelView wv_min;
	private static final String[] MINS = new String[]{" 0分", " 1分"," 2分"," 3分"," 4分"," 5分"," 6分"," 7分"," 8分"," 9分"
				,"10分","11分","12分","13分","14分","15分","16分","17分","18分","19分","20分","21分","22分","23分","24分","25分","26分","27分","28分","29分"
				,"30分,31分","32分","33分","34分","35分","36分","37分","38分","39分"
				,"40分","41分","42分","43分","44分","45分","46分","47分","48分","49分","50分","51分","52分","53分","54分","55分","56分","57分","58分","59分","60"};

	private int month,day,hour,min;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity=(NoteActivity) this.getActivity();
		month = day = hour = min;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_early_date_time, container,false);
		wv_month = (WheelView)view.findViewById(R.id.wheelview_month);

		wv_month.setOffset(2);
		wv_month.setItems(Arrays.asList(MONTHS));
		wv_month.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				System.out.println("selectedIndex: " + selectedIndex + ", item: " + item);
				month = Integer.valueOf(item.replace("月", "").replace(" ", ""));
			}
		});

		wv_day = (WheelView)view.findViewById(R.id.wheelview_day);

		wv_day.setOffset(2);
		wv_day.setItems(Arrays.asList(DAYS));
		wv_day.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				System.out.println("selectedIndex: " + selectedIndex + ", item: " + item);
				day = Integer.valueOf(item.replace("天", "").replace(" ", ""));
			}
		});

		wv_hour = (WheelView)view.findViewById(R.id.wheelview_hour);

		wv_hour.setOffset(2);
		wv_hour.setItems(Arrays.asList(HOURS));
		wv_hour.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				System.out.println("selectedIndex: " + selectedIndex + ", item: " + item);
				hour = Integer.valueOf(item.replace("时", "").replace(" ", ""));
			}
		});

		wv_min = (WheelView)view.findViewById(R.id.wheelview_min);

		wv_min.setOffset(2);
		wv_min.setItems(Arrays.asList(MINS));
		wv_min.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				System.out.println("selectedIndex: " + selectedIndex + ", item: " + item);
				min = Integer.valueOf(item.replace("分", "").replace(" ", ""));
			}
		});
		view.findViewById(R.id.btn_ok).setOnClickListener(this);
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
		return view;
	}
		
	public void onClick(View view) {
		int viewId =view.getId();
		switch(viewId){
		case R.id.btn_cancel:
			activity.hideEarlyDateTimeFragment();
			break;
		case R.id.btn_ok:
			activity.EarlyDateTimeValue[0] = month;
			activity.EarlyDateTimeValue[1] = day;
			activity.EarlyDateTimeValue[2] = hour;
			activity.EarlyDateTimeValue[3] = min;
			activity.hideEarlyDateTimeFragment();
			break;
		}
	}

}
