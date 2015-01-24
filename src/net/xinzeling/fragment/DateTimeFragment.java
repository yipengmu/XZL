package net.xinzeling.fragment;

import java.util.Arrays;

import net.xinzeling.base.BaseFragment;
import net.xinzeling.lib.WheelView;
import net.xinzeling.note.NoteActivity;
import net.xinzeling2.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class DateTimeFragment extends BaseFragment implements OnClickListener {
	private NoteActivity activity;

	private WheelView wv_year;
	private static final String[] YEARS = new String[] { "2010年", "2011年", "2012年", "2013年", "2014年", "2015年", "2016年", "2017年", "2018年", "2019年", "2020年", "2021年", "2022年", "2023年", "2024年",
			"2025年", "2026年", "2027年", "2028年", "2029年", "2030年", "2031年", "2032年", "2033年", "2034年", "2035年", "2036年", "2037年", "2038年", "2039年", "2040年", "2041年", "2042年", "2043年", "2044年",
			"2045年", "2046年", "2047年", "2048年", "2049年", "2050年", "2051年", "2052年", "2053年", "2054年", "2055年", "2056年", "2057年", "2058年", "2059年", "2060年", "2061年", "2062年", "2063年", "2064年",
			"2065年", "2066年", "2067年", "2068年", "2069年", "2070年", "2071年", "2072年", "2073年", "2074年", "2075年", "2076年", "2077年", "2078年", "2079年", "2080年", "2081年", "2082年", "2083年", "2084年",
			"2085年", "2086年", "2087年", "2088年", "2089年", "2090年", "2091年", "2092年", "2093年", "2094年", "2095年", "2096年", "2097年", "2098年", "2099年" };

	private WheelView wv_month;
	private static final String[] MONTHS = new String[] { " 1月", " 2月", " 3月", " 4月", " 5月", " 6月", " 7月", " 8月", " 9月", "10月", "11月", "12月" };

	private WheelView wv_day;
	private static final String[] DAYS = new String[] { " 1日", " 2日", " 3日", " 4日", " 5日", " 6日", " 7日", " 8日", " 9日", "10日", "11日", "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日",
			"21日", "22日", "23日", "24日", "25日", "26日", "27日", "28日", "29日", "30日" };

	private WheelView wv_hour;
	private static final String[] HOURS = new String[] { " 0点", " 1点", " 2点", " 3点", " 4点", " 5点", " 6点", " 7点", " 8点", " 9点", "10点", "11点", "12点", "13点", "14点", "15点", "16点", "17点", "18点", "19点",
			"20点", "21点", "22点", "23点" };

	private WheelView wv_min;
	private static final String[] MINS = new String[] { " 0分", " 1分", " 2分", " 3分", " 4分", " 5分", " 6分", " 7分", " 8分", " 9分", "10分", "11分", "12分", "13分", "14分", "15分", "16分", "17分", "18分", "19分",
			"20分", "21分", "22分", "23分", "24分", "25分", "26分", "27分", "28分", "29分", "30分,31分", "32分", "33分", "34分", "35分", "36分", "37分", "38分", "39分", "40分", "41分", "42分", "43分", "44分", "45分", "46分",
			"47分", "48分", "49分", "50分", "51分", "52分", "53分", "54分", "55分", "56分", "57分", "58分", "59分", "60分" };

	private int year, month, day, hour, min;

	/* 1 代表起始日期 2 代表结束日期* */
	private int mType = 1;

	public DateTimeFragment(Activity act) {
		activity = (NoteActivity) act;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateDateInfo();
	}

	private void updateDateInfo() {
		if (mType == 1) {
			year = activity.DateTimeStartInfo[0];
			month = activity.DateTimeStartInfo[1];
			day = activity.DateTimeStartInfo[2];
			hour = activity.DateTimeStartInfo[3];
			min = activity.DateTimeStartInfo[4];
		} else if (mType == 2) {
			year = activity.DateTimeEndInfo[0];
			month = activity.DateTimeEndInfo[1];
			day = activity.DateTimeEndInfo[2];
			hour = activity.DateTimeEndInfo[3];
			min = activity.DateTimeEndInfo[4];
		}
	}

	/* 1 代表起始日期 2 代表结束日期* */
	public DateTimeFragment setType(int type) {
		mType = type;
		updateDateInfo();
		return this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_date_time, container, false);
		wv_year = (WheelView) view.findViewById(R.id.wheelview_year);

		wv_year.setOffset(2);
		wv_year.setItems(Arrays.asList(YEARS));
		wv_year.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				System.out.println("selectedIndex: " + selectedIndex + ", item: " + item);
				year = Integer.valueOf(item.replace("年", "").replace(" ", ""));
			}
		});

		wv_month = (WheelView) view.findViewById(R.id.wheelview_month);

		wv_month.setOffset(2);
		wv_month.setItems(Arrays.asList(MONTHS));
		wv_month.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				System.out.println("selectedIndex: " + selectedIndex + ", item: " + item);
				month = Integer.valueOf(item.replace("月", "").replace(" ", ""));
			}
		});

		wv_day = (WheelView) view.findViewById(R.id.wheelview_day);

		wv_day.setOffset(2);
		wv_day.setItems(Arrays.asList(DAYS));
		wv_day.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				System.out.println("selectedIndex: " + selectedIndex + ", item: " + item);
				day = Integer.valueOf(item.replace("日", "").replace(" ", ""));
			}
		});

		wv_hour = (WheelView) view.findViewById(R.id.wheelview_hour);

		wv_hour.setOffset(2);
		wv_hour.setItems(Arrays.asList(HOURS));
		wv_hour.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				System.out.println("selectedIndex: " + selectedIndex + ", item: " + item);
				hour = Integer.valueOf(item.replace("点", "").replace(" ", ""));
			}
		});

		wv_min = (WheelView) view.findViewById(R.id.wheelview_min);

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
		int viewId = view.getId();
		switch (viewId) {
		case R.id.btn_cancel:
			activity.hideDateTimeFragment();
			popPageBackStack();
			break;
		case R.id.btn_ok:
			if (mType == 1) {
				activity.DateTimeStartInfo[0] = year;
				activity.DateTimeStartInfo[1] = month;
				activity.DateTimeStartInfo[2] = day;
				activity.DateTimeStartInfo[3] = hour;
				activity.DateTimeStartInfo[4] = min;
			} else if (mType == 2) {
				activity.DateTimeEndInfo[0] = year;
				activity.DateTimeEndInfo[1] = month;
				activity.DateTimeEndInfo[2] = day;
				activity.DateTimeEndInfo[3] = hour;
				activity.DateTimeEndInfo[4] = min;
			} 
			activity.hideDateTimeFragment();
			popPageBackStack();
			break;
		}
	}

}
