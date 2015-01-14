package net.xinzeling.lib;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import net.xinzeling.DateActivity;
import net.xinzeling.MyApplication;
import net.xinzeling2.R;
import net.xinzeling.model.LunarModel;
import net.xinzeling.model.LunarModel.Lunar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DateTitleView extends LinearLayout implements OnClickListener {

	public Lunar lunar;
	private TextView view_date_title_1,view_date_title_2;
	private ImageView view_date_title_down;
	private final int REQUEST_ID = 0;
	private Context mContext;
	private static String str_date;
	public DateTitleView(Context context) {
		super(context);
		init(context);
	}

	public DateTitleView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public DateTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.view_date_title, this,true);
		//Typeface tf = FontManager.getTypeface(context);
		view_date_title_1 = (TextView)layout.findViewById(R.id.view_date_title_1);
		//view_date_title_1.setTypeface(tf);
		view_date_title_2 = (TextView)layout.findViewById(R.id.view_date_title_2);
		//view_date_title_2.setTypeface(tf);
		view_date_title_down = (ImageView)layout.findViewById(R.id.view_date_title_down);
		
		lunar = LunarModel.fetchByDate(new Date());
		view_date_title_down.setOnClickListener(this);
		view_date_title_1.setOnClickListener(this);
		view_date_title_2.setOnClickListener(this);
		setViewDateTitle(lunar);
	}
	
	public void setViewDateTitle(Lunar lunar) {
		view_date_title_1.setText(lunar.date);
		view_date_title_2.setText(lunar.lunarCalendar);
	}

	@Override
	public void onClick(View arg0) {
//		Calendar calendar = Calendar.getInstance();
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH);
//		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String date = view_date_title_1.getText().toString();
		Date d;
		try {
			d = DateTime.String2Date(date, "yyyy-MM-dd");
			int year = d.getYear()+1900;
			int month = d.getMonth();
			int day = d.getDate();
			
			Intent intent = new Intent(mContext, DateActivity.class);
			intent.putExtra("year", year);
			intent.putExtra("month", month);
			intent.putExtra("day", day);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			((Activity)mContext).startActivityForResult(intent, REQUEST_ID);		
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == Activity.RESULT_OK) {
			Bundle data = intent.getExtras();
			switch (requestCode) {
			case REQUEST_ID:
				String dstr = String.format(Locale.CHINA, "%d-%02d-%02d",data.getInt("year"),
						(data.getInt("month")+1),
						data.getInt("day"));
				boolean isneedrefresh = false;
				if(str_date!=null){
					if(!dstr.equals(str_date)){
						str_date = dstr;
						isneedrefresh = true;
					}
				}else{
					str_date = dstr;
					isneedrefresh = true;
				}
				if(isneedrefresh){
					lunar = LunarModel.fetchByDate(dstr);
					setViewDateTitle(lunar);
					//发广播
					MyApplication.sendBroadcastNewSelectDate(dstr);
				}
				break;
			}
		}
	}
}
