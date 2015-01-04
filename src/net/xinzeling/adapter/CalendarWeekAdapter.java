package net.xinzeling.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.xinzeling2.R;
import net.xinzeling.lib.LunarCalendar;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarWeekAdapter extends BaseAdapter {

	private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
	private Calendar calSelected = Calendar.getInstance(); // 选择的日历
	private Calendar calToday = Calendar.getInstance(); // 今日
	private int iMonthViewCurrentMonth = 0; // 当前视图月
	ArrayList<java.util.Date> titles;
	private Context activity;
	Resources resources;

	public CalendarWeekAdapter(Context a, Calendar cal) {
		calStartDate = cal;
		activity = a;
		resources = activity.getResources();
		titles = getDates();
	}

	public void setSelectedDate(Calendar cal) {
		calSelected = cal;
	}

	// 根据改变的日期更新日历
	// 填充日历控件用
	private void UpdateStartDateForMonth() {
		calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月
		// 星期一是2 星期天是1 填充剩余天数
		int iDay = 0;
		int iFirstDayOfWeek = Calendar.MONDAY;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
		calStartDate.add(Calendar.DAY_OF_MONTH, -1);// 周日第一位
	}

	private ArrayList<java.util.Date> getDates() {
		UpdateStartDateForMonth();
		ArrayList<java.util.Date> alArrayList = new ArrayList<java.util.Date>();
		for (int i = 1; i <= 7; i++) {
			alArrayList.add(calStartDate.getTime());
			calStartDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		return alArrayList;
	}

	@Override
	public int getCount() {
		return titles.size();
	}

	@Override
	public Object getItem(int position) {
		return titles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView gridView = new TextView(activity);
		gridView.setGravity(Gravity.CENTER);
		gridView.setBackgroundColor(resources.getColor(R.color.calendar_grid));
		gridView.setTextColor(resources.getColor(R.color.calendar_txt));
		gridView.setId(position + 5000);
		
		Date myDate = (Date) getItem(position);
		Calendar calCalendar = Calendar.getInstance();
		calCalendar.setTime(myDate);
		final int iMonth = calCalendar.get(Calendar.MONTH);
		LunarCalendar calendarUtil = new LunarCalendar(calCalendar);
		
		if (equalsDate(calToday.getTime(), myDate)) {// 当前日期
			gridView.setBackgroundColor(resources.getColor(R.color.calendar_grid_today));
		}else if (equalsDate(calSelected.getTime(), myDate)) {//选择的
			gridView.setBackgroundColor(resources.getColor(R.color.calendar_grid_seled));
		} 
		
		// 判断是否是当前月
		if (iMonth == iMonthViewCurrentMonth) {
			int day = myDate.getDate(); // 日期
			gridView.setText(Html.fromHtml(String.valueOf(day)+"<br/>"+calendarUtil.toString()));
		} else{
			gridView.setText(Html.fromHtml("<br/>"));
		}
		gridView.setTag(myDate);
		return gridView;
	}

	private Boolean equalsDate(Date date1, Date date2) {
		if (date1.getYear() == date2.getYear()&& date1.getMonth() == date2.getMonth() && date1.getDate() == date2.getDate()) {
			return true;
		} else {
			return false;
		}
	}
}