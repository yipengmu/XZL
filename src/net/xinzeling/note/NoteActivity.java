package net.xinzeling.note;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import net.xinzeling.MainActivity;
import net.xinzeling.MyApplication;
import net.xinzeling.base.BaseActivity;
import net.xinzeling.fragment.DateTimeFragment;
import net.xinzeling.fragment.TopicFragment;
import net.xinzeling.lib.DateTime;
import net.xinzeling.model.NoteModel;
import net.xinzeling.model.NoteModel.Note;
import net.xinzeling.utils.Utils;
import net.xinzeling.widget.LineEditText;
import net.xinzeling.widget.BlurMask.BlurMaskTask;
import net.xinzeling2.R;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class NoteActivity extends BaseActivity implements OnClickListener {
	private EditText inputTopic,inputContact,stimeYear,stimeMonth,stimeDay,stimeHour,stimeMin,etimeYear;
	private EditText etimeMonth,etimeDay,etimeHour,etimeMin;
	private EditText earlyMonth, earlyDay, earlyHour, earlyMin;
	private LineEditText inputContent;

	private RadioButton radioRepeatYear,radioRepeatMonth,radioRepeatWeek,radioRepeatDay,radioRepeatNo;

	private int REQUEST_CODE;
	private int noteid = -1;
	private Animation scaleYupAnim;
	private Animation scaleYdownAnim;
	private View earlydatetimeView;

	private int repeat_type = 0;

	public int[] DateTimeStartInfo = new int[] { 0, 0, 0, 0, 0 };// 年 月 日 时 分
	public int[] DateTimeEndInfo = new int[] { 0, 0, 0, 0, 0 };// 年 月 日 时 分
	private int DateTimeSelectId;

	public int[] EarlyDateTimeValue = new int[] { 0, 0, 0, 0 };// 月日时分
	public SparseIntArray mSelected = new SparseIntArray();// 主题选项
	public ArrayList<Integer> mSelectedList = new ArrayList<>();
	private long mFirstime;
	private TextView tv_topic_icon_themes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);

		inputTopic = (EditText) findViewById(R.id.input_note_topic);
		inputContact = (EditText) findViewById(R.id.input_note_contact);
		inputContent = (LineEditText) findViewById(R.id.input_note_content);
		tv_topic_icon_themes = (TextView) findViewById(R.id.tv_topic_icon_themes);

		stimeYear = (EditText) findViewById(R.id.input_stime_year);
		stimeMonth = (EditText) findViewById(R.id.input_stime_month);
		stimeDay = (EditText) findViewById(R.id.input_stime_day);
		stimeHour = (EditText) findViewById(R.id.input_stime_hour);
		stimeMin = (EditText) findViewById(R.id.input_stime_min);

		etimeYear = (EditText) findViewById(R.id.input_etime_year);
		etimeMonth = (EditText) findViewById(R.id.input_etime_month);
		etimeDay = (EditText) findViewById(R.id.input_etime_day);
		etimeHour = (EditText) findViewById(R.id.input_etime_hour);
		etimeMin = (EditText) findViewById(R.id.input_etime_min);

		earlyMonth = (EditText) findViewById(R.id.earlier_month);
		earlyDay = (EditText) findViewById(R.id.earlier_day);
		earlyHour = (EditText) findViewById(R.id.earlier_hour);
		earlyMin = (EditText) findViewById(R.id.earlier_min);

		radioRepeatYear = (RadioButton) findViewById(R.id.radio_repeat_year);
		radioRepeatMonth = (RadioButton) findViewById(R.id.radio_repeat_month);
		radioRepeatWeek = (RadioButton) findViewById(R.id.radio_repeat_week);
		radioRepeatDay = (RadioButton) findViewById(R.id.radio_repeat_day);
		radioRepeatNo = (RadioButton) findViewById(R.id.radio_repeat_no);
		noteid = this.getIntent().getIntExtra("noteid", -1);
		if (noteid > 0) {
			this.loadNote(noteid);
		} else {
			Calendar calendar = getNoteCalendarTime();
			stimeYear.setText(calendar.get(Calendar.YEAR) + "年");
			stimeMonth.setText(calendar.get(Calendar.MONTH) + 1 + "月");
			stimeDay.setText(calendar.get(Calendar.DAY_OF_MONTH) + "日");
			stimeHour.setText(calendar.get(Calendar.HOUR) + "点");
			stimeMin.setText(calendar.get(Calendar.MINUTE) + "分");

			calendar.add(Calendar.HOUR, 2);
			etimeYear.setText(calendar.get(Calendar.YEAR) + "年");
			etimeMonth.setText(calendar.get(Calendar.MONTH) + 1 + "月");
			etimeDay.setText(calendar.get(Calendar.DAY_OF_MONTH) + "日");
			etimeHour.setText(calendar.get(Calendar.HOUR) + "点");
			etimeMin.setText(calendar.get(Calendar.MINUTE) + "分");
		}
		findViewById(R.id.topic_frame).setOnClickListener(this);

		earlydatetimeView = findViewById(R.id.early_datetime_fragment);
		findViewById(R.id.early_datetime_frame).setOnClickListener(this);
		findViewById(R.id.btn_save).setOnClickListener(this);
		scaleYupAnim = AnimationUtils.loadAnimation(this, R.anim.scale_yup);
		scaleYdownAnim = AnimationUtils.loadAnimation(this, R.anim.scale_ydown);
		scaleYdownAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				findViewById(R.id.topic_frame).setVisibility(View.GONE);
				findViewById(R.id.early_datetime_frame).setVisibility(View.GONE);
			}

			public void onAnimationRepeat(Animation arg0) {
			}

			public void onAnimationStart(Animation arg0) {
			}
		});

		RadioGroup group = (RadioGroup) this.findViewById(R.id.radio_note_repeat);
		// 绑定一个匿名监听器
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				switch (radioButtonId) {
				case R.id.radio_repeat_year:
					repeat_type = MyApplication.REPEAT_TYPE_YEAR;
					break;
				case R.id.radio_repeat_month:
					repeat_type = MyApplication.REPEAT_TYPE_MONTH;
					break;
				case R.id.radio_repeat_week:
					repeat_type = MyApplication.REPEAT_TYPE_WEEK;
					break;
				case R.id.radio_repeat_day:
					repeat_type = MyApplication.REPEAT_TYPE_DAY;
					break;
				case R.id.radio_repeat_no:
					repeat_type = MyApplication.REPEAT_EMPTY;
					break;
				}
			}
		});

	}

	private Calendar getNoteCalendarTime() {
		Calendar targetCalendar = Calendar.getInstance();
		Calendar intentCalendar = null;
		try {
			Object intentObj = getIntent().getSerializableExtra("calendar");
			if(intentObj != null){
				intentCalendar = (Calendar) intentObj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		if(intentCalendar != null){
			return intentCalendar;
		}else{
			return targetCalendar;
		}
	}

	private void loadNote(int noteid) {
		Note note = NoteModel.fetch(noteid);
		if (note != null) {
			inputTopic.setText(note.topic);
			inputContact.setText(note.contact);
			inputContent.setText(note.content);
			String[] sdates = DateTime.Timestamp2String((long) note.started * 1000, "yyyy-MM-dd-HH-mm").split("-");
			if (sdates != null && sdates.length == 5) {
				stimeYear.setText(sdates[0] + "年");
				stimeMonth.setText(sdates[1] + "月");
				stimeDay.setText(sdates[2] + "日");
				stimeHour.setText(sdates[3] + "点");
				stimeMin.setText(sdates[4] + "分");
			}
			String[] edates = DateTime.Timestamp2String((long) note.ended * 1000, "yyyy-MM-dd-HH-mm").split("-");
			if (edates != null && edates.length == 5) {
				etimeYear.setText(edates[0] + "年");
				etimeMonth.setText(edates[1] + "月");
				etimeDay.setText(edates[2] + "日");
				etimeHour.setText(edates[3] + "点");
				etimeMin.setText(edates[4] + "分");
			}
			repeat_type = note.repeat_type;
			radioRepeatMonth = (RadioButton) findViewById(R.id.radio_repeat_month);
			radioRepeatWeek = (RadioButton) findViewById(R.id.radio_repeat_week);
			radioRepeatDay = (RadioButton) findViewById(R.id.radio_repeat_day);
			radioRepeatNo = (RadioButton) findViewById(R.id.radio_repeat_no);
			switch (note.repeat_type) {
			case MyApplication.REPEAT_EMPTY:
				radioRepeatNo.setChecked(true);
				break;
			case MyApplication.REPEAT_TYPE_DAY:
				radioRepeatDay.setChecked(true);
				break;
			case MyApplication.REPEAT_TYPE_WEEK:
				radioRepeatWeek.setChecked(true);
				break;
			case MyApplication.REPEAT_TYPE_MONTH:
				radioRepeatMonth.setChecked(true);
				break;
			case MyApplication.REPEAT_TYPE_YEAR:
				radioRepeatYear.setChecked(true);
				break;
			}

			// 提前的时间信息也的显示出来
			int before_sec = note.befored;
			int mouth = before_sec / 1000000;
			int day = (before_sec / 10000) % 100;
			int hour = (before_sec % 10000) / 100;
			int min = before_sec % 100;

			earlyMonth.setText(mouth + "");
			earlyDay.setText(day + "");
			earlyHour.setText(hour + "");
			earlyMin.setText(min + "");
		}
	}

	public void showTopicFragment() {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

		fragmentTransaction.replace(R.id.fl_fragment, new TopicFragment());
		fragmentTransaction.addToBackStack("topic_fragment");
		fragmentTransaction.commit();
	}

	public void saveTopicFragment() {
		ArrayList<SpannableString> spanStrArr = new ArrayList<SpannableString>();

		for (int i = 0; i < mSelected.size(); i++) {
			int drawable_id = mSelected.get(mSelected.keyAt(i));
			String sp_pre = "_x_icon_y_" + drawable_id + "_";
			ImageSpan span = new ImageSpan(this, getResources().getIdentifier("small_note_topic_" + drawable_id, "drawable", "net.xinzeling2"));
			SpannableString spanStr = new SpannableString(sp_pre);
			spanStr.setSpan(span, 0, sp_pre.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

			spanStrArr.add(spanStr);
			if (inputTopic != null) {
				updateTopicThemes(spanStrArr);
			}
		}

		updateTopicThemes(spanStrArr);

	}

	public void updateTopicThemes(ArrayList<SpannableString> spanStrArr) {
		Log.d("mSelected  : ", "spanStrArr:" + spanStrArr.toString());
		tv_topic_icon_themes.setText("");
		for (int i = 0; i < spanStrArr.size(); i++) {
			if (i < 3) {
				// 只显示3个 icon 的topic
				tv_topic_icon_themes.append(spanStrArr.get(i));
			}
		}
	}

	public class TopicEditTextHolder {
		public String imageTopicThemes;
		public String strTopicThemes;
	}

	public void onClick(View view) {
		REQUEST_CODE = view.getId();
		switch (REQUEST_CODE) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_topic_add:
			// 显示主题fragment
			this.showTopicFragment();
			break;
		case R.id.btn_contact_add:
			startActivityForResult(new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI), REQUEST_CODE);
			break;
		case R.id.alert_begin:
		case R.id.input_stime_month:
		case R.id.input_stime_day:
		case R.id.input_stime_hour:
		case R.id.input_stime_min:
			this.showDateTimeFragment(R.id.input_stime_day);
			break;
		case R.id.alert_end:
		case R.id.input_etime_month:
		case R.id.input_etime_day:
		case R.id.input_etime_hour:
		case R.id.input_etime_min:
			this.showDateTimeFragment(R.id.input_etime_day);
			break;
		case R.id.early_setting:
		case R.id.earlier_month:
		case R.id.earlier_day:
		case R.id.earlier_hour:
		case R.id.earlier_min:
			REQUEST_CODE = R.id.earlier_day;
			this.showEarlyDateTimeFragment();
			break;
		case R.id.btn_save:
		case R.id.btn_forward:
//			Utils.showNotifycation(this);
			new SubmitTask().execute();
			break;
		case R.id.topic_frame:
			// 关闭主题
			this.saveTopicFragment();
			break;
		case R.id.early_datetime_frame:
			this.hideEarlyDateTimeFragment();
			break;
		}
	}

	public void showEarlyDateTimeFragment() {
		new BlurMaskTask(this, findViewById(R.id.frame_main), findViewById(R.id.early_datetime_frame), new Runnable() {
			@Override
			public void run() {
				earlydatetimeView.startAnimation(scaleYupAnim);
			}
		}).execute();
	}

	public void hideEarlyDateTimeFragment() {
		earlyMonth.setText(EarlyDateTimeValue[0] + "");
		earlyDay.setText(EarlyDateTimeValue[1] + "");
		earlyHour.setText(EarlyDateTimeValue[2] + "");
		earlyMin.setText(EarlyDateTimeValue[3] + "");
		earlydatetimeView.startAnimation(scaleYdownAnim);
	}

	public void showDateTimeFragment(int selectid) {
		this.DateTimeSelectId = selectid;
		int iDateType = 1;
		switch (this.DateTimeSelectId) {
		case R.id.input_stime_day:
			DateTimeStartInfo[0] = Integer.valueOf(stimeYear.getText().toString().replace("年", ""));
			DateTimeStartInfo[1] = Integer.valueOf(stimeMonth.getText().toString().replace("月", ""));
			DateTimeStartInfo[2] = Integer.valueOf(stimeDay.getText().toString().replace("日", ""));
			DateTimeStartInfo[3] = Integer.valueOf(stimeHour.getText().toString().replace("点", ""));
			DateTimeStartInfo[4] = Integer.valueOf(stimeMin.getText().toString().replace("分", ""));
			break;
		case R.id.input_etime_day:
			iDateType = 2;
			DateTimeEndInfo[0] = Integer.valueOf(etimeYear.getText().toString().replace("年", ""));
			DateTimeEndInfo[1] = Integer.valueOf(etimeMonth.getText().toString().replace("月", ""));
			DateTimeEndInfo[2] = Integer.valueOf(etimeDay.getText().toString().replace("日", ""));
			DateTimeEndInfo[3] = Integer.valueOf(etimeHour.getText().toString().replace("点", ""));
			DateTimeEndInfo[4] = Integer.valueOf(etimeMin.getText().toString().replace("分", ""));
			break;
		}

		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.fl_fragment, new DateTimeFragment(this).setType(iDateType));
		fragmentTransaction.addToBackStack("dateTime");
		fragmentTransaction.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			/*
			 * 已经是最后一个fragment getSupportFragmentManager()或者getFragmentManager()
			 * 具体要看你add to back stack 是用哪个
			 */
			// if no more history in stack
			if (getFragmentManager().getBackStackEntryCount() > 0) {
				// 显示退出框业务逻辑
				getFragmentManager().popBackStack();
				return true;
			}

			long secondtime = System.currentTimeMillis();
			if (secondtime - mFirstime > 3000) {
				Toast.makeText(this, "再按一次返回键退出信则聆", Toast.LENGTH_SHORT).show();
				mFirstime = System.currentTimeMillis();
				return true;
			} else {
				finish();
				System.exit(0);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void hideDateTimeFragment() {
		switch (DateTimeSelectId) {
		case R.id.input_stime_day:
			stimeYear.setText(DateTimeStartInfo[0] + "年");
			stimeMonth.setText(DateTimeStartInfo[1] + "月");
			stimeDay.setText(DateTimeStartInfo[2] + "日");
			stimeHour.setText(DateTimeStartInfo[3] + "点");
			stimeMin.setText(DateTimeStartInfo[4] + "分");
			break;
		case R.id.input_etime_day:
			etimeYear.setText(DateTimeEndInfo[0] + "年");
			etimeMonth.setText(DateTimeEndInfo[1] + "月");
			etimeDay.setText(DateTimeEndInfo[2] + "日");
			etimeHour.setText(DateTimeEndInfo[3] + "点");
			etimeMin.setText(DateTimeEndInfo[4] + "分");
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK && intent != null) {
			switch (REQUEST_CODE) {
			case R.id.input_note_topic:
				inputTopic.setText(intent.getExtras().getString("input", ""));
				break;
			case R.id.input_note_contact:
				inputContact.setText(intent.getExtras().getString("input", ""));
				break;
			case R.id.input_note_content:
				inputContent.setText(intent.getExtras().getString("input", ""));
				break;
			case R.id.btn_contact_add:
				Cursor cursor = getContentResolver().query(intent.getData(), new String[] { Contacts.DISPLAY_NAME }, null, null, null);
				if (cursor.moveToFirst()) {
					inputContact.append(cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME)));
				}
				break;
			}
		}
	}

	private class SubmitTask extends AsyncTask<Void, Void, Void> {
		private boolean isValidate;
		private MyApplication.note_error_code error_code;

		@Override
		protected Void doInBackground(Void... params) {
			Calendar calendar;
			isValidate = false;
			String title = inputTopic.getText().toString();
			String iconTheme = tv_topic_icon_themes.getText().toString();
			
			if (TextUtils.isEmpty(title) && TextUtils.isEmpty(iconTheme)) {
				error_code = MyApplication.note_error_code.EMPTY_TITLE;
				return null;
			}
			String contact = inputContact.getText().toString();
			if ("".equals(contact) || contact.isEmpty()) {
				error_code = MyApplication.note_error_code.EMPTY_CONTACT;
				return null;
			}
			String content = inputContent.getText().toString();
			if ("".equals(content) || content.isEmpty()) {
				error_code = MyApplication.note_error_code.EMPTY_CONTENT;
				return null;
			}
			String syear = stimeYear.getText().toString().replace("年", "");
			String smonth = stimeMonth.getText().toString().replace("月", "");
			String sday = stimeDay.getText().toString().replace("日", "");
			String shour = stimeHour.getText().toString().replace("点", "");
			String smin = stimeMin.getText().toString().replace("分", "");

			String eyear = etimeYear.getText().toString().replace("年", "");
			String emonth = etimeMonth.getText().toString().replace("月", "");
			String eday = etimeDay.getText().toString().replace("日", "");
			String ehour = etimeHour.getText().toString().replace("点", "");
			String emin = etimeMin.getText().toString().replace("分", "");

			if ((syear + smonth + sday + shour + smin).compareTo(eyear + emonth + sday + shour + smin) > 0) {
				error_code = MyApplication.note_error_code.ERROR_ENDTIME;
				return null;
			}
			isValidate = true;

			String started = syear + "-" + smonth + "-" + sday + " " + shour + ":" + smin;
			String ended = eyear + "-" + emonth + "-" + eday + " " + ehour + ":" + emin;

				
			/**
			 * sendAlarmBroadCast 发送闹铃广播
			 * 
			 * @param context
			 * @param isRepeat
			 *            定时器是否重复
			 * @param firsttime
			 *            第一次触发时间离现在的秒数 ，默认十分钟后再提醒 60 * 10
			 * @param repeat_time
			 *            定时器重复的间隔秒数
			 * @param alarmId
			 *            定时器自定义id
			 */
			MyApplication.sendAlarmBroadCast(NoteActivity.this, false, 60 * 10 , 0, 1001);
			
			String StringFormatWithoutHour = "yyyy-MM-dd";
			String StringFormatWithHour = "yyyy-MM-dd HH:mm";
		
			try {
				// 这个％02d%02d%02d%02d 月 天 时 分 ,其他的时间
				String before_str = String.format("%02d%02d%02d%02d", Integer.valueOf(earlyMonth.getText().toString()), Integer.valueOf(earlyDay.getText().toString()),
						Integer.valueOf(earlyHour.getText().toString()), Integer.valueOf(earlyMin.getText().toString()));
				int before_second = Integer.valueOf(before_str);
				int iscancel = 0;
				if (noteid > 0) {
					NoteModel.edit(noteid, title, contact, content, DateTime.String2Timestamp(started, StringFormatWithHour), DateTime.String2Timestamp(ended,StringFormatWithHour), before_second,
							iscancel, repeat_type);
				} else {
					// 判断起始与终止时间是否在同一天，如果不是同一天，需要每天每天的进行保存
					long betweenDays = DateTime.diffDayNumString1String2(ended, started, StringFormatWithHour);
					if (betweenDays == 0) {
						// 同一天
						NoteModel
								.add(title, contact, content, DateTime.String2Timestamp(started, StringFormatWithHour), DateTime.String2Timestamp(ended, StringFormatWithHour), before_second, repeat_type);
					} else {
						calendar = Calendar.getInstance();
						for (long i = 0; i < betweenDays; i++) {
							if (i == 0) {
								NoteModel.add(title, contact, content, Utils.getDateByStringFormat(started, StringFormatWithHour).getTime(),
										 Utils.getDateByStringFormat(syear + "-" + smonth + "-" + sday + " 23:59", StringFormatWithHour).getTime()
										, before_second, repeat_type);
								// -23:59:59
							} 
//							else if (i == betweenDays - 1) {
//								NoteModel.add(title, contact, content,Utils.getDateByStringFormat( eyear + "-" + emonth + "-" + eday + " 00:00", StringFormatWithHour).getTime(),
//										Utils.getDateByStringFormat(ended, StringFormatWithHour).getTime(), before_second, repeat_type);
//								// 00:00:00-
//							}
							else {
								calendar.add(Calendar.DAY_OF_YEAR, 1);
								NoteModel.add(title, contact, content, calendar.getTimeInMillis(), calendar.getTimeInMillis() / 1000 + 86400, before_second, repeat_type);
								// 00:00:00-
							}
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (isValidate) {
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(NoteActivity.this, MainActivity.class);
				intent.putExtra("tabIndex", MainActivity.Maintab_Index_Home);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NoteActivity.this.startActivity(intent);
				finish();
			} else {
				switch (error_code) {
				case EMPTY_TITLE:
					Toast.makeText(getApplicationContext(), "主题不能为空", Toast.LENGTH_LONG).show();
					break;
				case EMPTY_CONTACT:
					Toast.makeText(getApplicationContext(), "联系人不能为空", Toast.LENGTH_LONG).show();
					break;
				case EMPTY_CONTENT:
					Toast.makeText(getApplicationContext(), "内容不能为空", Toast.LENGTH_LONG).show();
					break;
				case EMPTY_STARTTIME:
					break;
				case EMPTY_ENDTIME:
					break;
				case ERROR_ENDTIME:
					Toast.makeText(getApplicationContext(), "终止时间早于起始时间", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
			}
		}
	}

}