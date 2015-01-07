package net.xinzeling;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import net.xinzeling.fragment.LunarFragment;
import net.xinzeling.fragment.MonthFragment;
import net.xinzeling.fragment.WeekFragment;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.BlurMaskTask;
import net.xinzeling.lib.DateTime;
import net.xinzeling.lib.DateTitleView;
import net.xinzeling.lib.HttpCommon;
import net.xinzeling.lib.ImageViewWithCount;
import net.xinzeling.model.LunarModel;
import net.xinzeling.model.LunarModel.Lunar;
import net.xinzeling.news.GuaNewsActivity;
import net.xinzeling2.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;

public class HomeActivity extends Activity  implements OnClickListener{
//	public Typeface tf;
	
	private RadioButton modeMonth;
	private FragmentManager fManager =getFragmentManager();
	private DateTitleView titleTxt;
	private ImageViewWithCount notificationIcon;
	private Handler myHandler;
	public Lunar lunar;
	private View lunarView;
	private Animation scaleYupAnim; 
	private Animation scaleYdownAnim;
	public LunarFragment lunarFragment;
	private UserStatusBroadcastReceiver usrBReceiver;
	private ReceiverNewDateBDcast receiverNDBD;
	private WeekFragment weekfragment;
	private MonthFragment monthfragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		tf = FontManager.getTypeface(HomeActivity.this);
		//System.out.println(AppBase.getDeviceInfo(this.getApplicationContext()));
		//finish();
		setContentView(R.layout.activity_home);
		lunar = LunarModel.fetchByDate(new Date());
		notificationIcon = (ImageViewWithCount)this.findViewById(R.id.msgnotification);	
		notificationIcon.setOnClickListener(this);

		titleTxt = (DateTitleView)this.findViewById(R.id.home_title);

		modeMonth = (RadioButton)findViewById(R.id.mode_month);
		modeMonth.setChecked(true);
		findViewById(R.id.lunar_frame).setOnClickListener(this);
		lunarView =findViewById(R.id.lunar_fragment);
		lunarView.setOnClickListener(null);
		scaleYupAnim =AnimationUtils.loadAnimation(this, R.anim.scale_yup);
		scaleYdownAnim = AnimationUtils.loadAnimation(this, R.anim.scale_ydown);
		scaleYdownAnim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationEnd(Animation arg0) {
				findViewById(R.id.lunar_frame).setVisibility(View.GONE);
			}
			public void onAnimationRepeat(Animation arg0) {}
			public void onAnimationStart(Animation arg0) {}}
		);
		
		myHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 1:
					notificationIcon.reDrawCount(msg.arg1);
					break;
				}
				super.handleMessage(msg);
			}
		};
		//(new UpdateNotificationMsgCnt(this.getApplicationContext())).start();
		weekfragment = new WeekFragment();
		monthfragment = new MonthFragment();
		usrBReceiver = new UserStatusBroadcastReceiver();
		registerReceiver(usrBReceiver, new IntentFilter(AppBase.USER_STATUS_CHANGE_BROADCAST));
		receiverNDBD = new ReceiverNewDateBDcast();
		registerReceiver(receiverNDBD, new IntentFilter(AppBase.SELECT_NEW_DATE_BROADCAST));

		this.onClick(modeMonth);
//		FontManager.changeFonts((ViewGroup)AppBase.getRootView(this),HomeActivity.this);
	}

	public void onResume(){
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(usrBReceiver);
		unregisterReceiver(receiverNDBD);
		super.onDestroy();
	}

	public void onClick(View view) {

		switch (view.getId()){
		case R.id.msgnotification:
			Intent intent = new Intent(HomeActivity.this, GuaNewsActivity.class);
			startActivity(intent);
			notificationIcon.reDrawCount(0);
			break;
//		case R.id.tab_note:
//			this.startActivity(new Intent(this, NoteActivity.class));
//			break;
//		case R.id.tab_usr:
//			this.startActivity(new Intent(this,MainSettingActivity.class));
//			break;
//		case R.id.tab_usr:
//			new BlurMaskTask(this, findViewById(R.id.frame_main),findViewById(R.id.frame_nav), 
//					new NavRunner(view.getId())).execute();
//			break;
		case R.id.mode_day:
			fManager.beginTransaction().replace(R.id.fragment_content, weekfragment).commit();
			break;
		case R.id.mode_month:
			fManager.beginTransaction().replace(R.id.fragment_content, monthfragment).commit();
			break;
////		case R.id.nav_gua_num:
////			AppBase.goGuaActivity(this, AppBase.nav_gua_num);
////			break;
////		case R.id.nav_gua_photo:
////			AppBase.goGuaActivity(this, AppBase.nav_gua_photo);
////			break;
////		case R.id.nav_gua_time:
////			AppBase.goGuaActivity(this, AppBase.nav_gua_time);
////			break;
////
////		case R.id.nav_note_index:
////			this.startActivity(new Intent(this, NoteActivity.class));
////			break;
////
////		case R.id.nav_usr_feed:
////			new FeedFragment().show(this.getFragmentManager(),"");
////			break;
////		case R.id.nav_usr_index:
////			this.startActivity(new Intent(this, UsrActivity.class));
////			break;
////		case R.id.nav_usr_gua:
////			this.startActivity(new Intent(this,GuaListActivity.class));
////			break;
////		case R.id.nav_usr_signin:
////			this.startActivity(new Intent(this, SigninActivity.class));
////			break;
////		case R.id.nav_usr_exit:
////			new LogoutFragment().show(this.getFragmentManager(),"");
////			//AppBase.logout();
////			break;

		case R.id.lunar_frame:
			this.hideLunarFragment();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
//		if(keyCode == KeyEvent.KEYCODE_BACK  && !tabHome.isChecked()){
//			navNote.setVisibility(View.INVISIBLE);
//			navUsr.setVisibility(View.INVISIBLE);
//			navGua.setVisibility(View.INVISIBLE);
//			tabHome.setChecked(true);
//			return false;
//		}
		return super.onKeyDown(keyCode, event);
	}

	public void showLunarFragment(){
		new BlurMaskTask(this, findViewById(R.id.frame_main),findViewById(R.id.lunar_frame), 
				new Runnable(){
			@Override
			public void run() {
				lunarView.startAnimation(scaleYupAnim);
				lunarFragment.onShow();
			}}).execute();
	}

	public void hideLunarFragment(){
		lunarView.startAnimation(scaleYdownAnim);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		titleTxt.onActivityResult(requestCode, resultCode, data);
	}

	public void refreshTitleTxt(boolean useToday) {
		if(useToday){
			lunar = LunarModel.fetchByDate(new Date());		
		}
		titleTxt.setViewDateTitle(lunar);
	}
	
	public class NavRunner implements Runnable{
		private int navId;
		public NavRunner(int viewId){
			navId = viewId;
		}

		@SuppressWarnings("unused")
		@Override
		public void run() {
			setUserStatus(AppBase.isSignin());
		}
	}
	
	public void setUserStatus(boolean islogin) {
		if(islogin){
//			findViewById(R.id.nav_usr_signin).setVisibility(View.GONE);
//			findViewById(R.id.nav_usr_exit).setVisibility(View.VISIBLE);			
		}else{
//			findViewById(R.id.nav_usr_exit).setVisibility(View.GONE);
//			findViewById(R.id.nav_usr_signin).setVisibility(View.VISIBLE);
		}
	}
	
	private class UpdateNotificationMsgCnt extends Thread {
		private Context context;
		public UpdateNotificationMsgCnt(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			String today_ymd = DateTime.getTodayYmd(null);
			String last_ymd = AppBase.getLastUpdateMsgCnt();
			if(last_ymd!=null&&today_ymd.equals(last_ymd))return;
			try {
				JSONObject res = HttpCommon.getGet(AppBase.update_notification_cnt);
				if(res!=null){
					int update_cnt = Integer.valueOf(res.getString("badge"));
					//notificationIcon.reDrawCount(update_cnt);
					Message message = new Message();
					message.what = 1;
					message.arg1 = update_cnt;
					myHandler.sendMessage(message);
					AppBase.setLastUpdateMsgCnt();
				}
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.run();
		}
	}
	
	private class UserStatusBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			int cmd = arg1.getIntExtra("cmd", -1);
			if(cmd!=-1){
				switch(cmd){
				case AppBase.USER_STATUS_CHANGE:
					if(!arg1.getBooleanExtra("isLogin",false)){
						//未登录
						((HomeActivity)arg0).setUserStatus(false);
					}else{
						//已登录
						((HomeActivity)arg0).setUserStatus(true);
					}
					break;
				}
			}
		}	
	}
	
	private class ReceiverNewDateBDcast extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String newdate = arg1.getStringExtra("newDateString");
			if(newdate!=null){
				try {
					if(monthfragment!=null&&monthfragment.calendarView!=null){
						monthfragment.calendarView.refreshByDate(newdate);
					}
					if(weekfragment!=null&&weekfragment.calendarView!=null){
						weekfragment.calendarView.refreshByDate(newdate);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}