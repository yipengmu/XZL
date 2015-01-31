package net.xinzeling;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import net.xinzeling.common.CommonDefine;
import net.xinzeling.common.PreferenceManager;
import net.xinzeling.fragment.LunarFragment;
import net.xinzeling.fragment.MonthFragment;
import net.xinzeling.fragment.WeekFragment;
import net.xinzeling.lib.DateTime;
import net.xinzeling.lib.DateTitleView;
import net.xinzeling.lib.ImageViewWithCount;
import net.xinzeling.model.LunarModel;
import net.xinzeling.model.LunarModel.Lunar;
import net.xinzeling.net.http.RequestManager;
import net.xinzeling.news.GuaNewsActivity;
import net.xinzeling.utils.Utils;
import net.xinzeling.widget.BlurMask.BlurMaskTask;
import net.xinzeling2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

/** 首页 */
public class HomeActivity extends Activity implements OnClickListener {

	private RadioButton modeMonth;
	private DateTitleView titleTxt;
	private ImageViewWithCount notificationIcon;
	private Handler myHandler;
	public Lunar lunar;
	private View lunarView;
	private Animation scaleYupAnim;
	private Animation scaleYdownAnim;

	private UserStatusBroadcastReceiver usrBReceiver;
	private ReceiverNewDateBDcast receiverNDBD;

	// 老黄历新旧解释，弹出浮层
	public LunarFragment lunarFragment;
	private WeekFragment weekfragment;
	private MonthFragment monthfragment;
	private FragmentManager fManager;
	

	private long mFirstime = 0L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		lunar = LunarModel.fetchByDate(new Date());
		notificationIcon = (ImageViewWithCount) this.findViewById(R.id.msgnotification);
		notificationIcon.setOnClickListener(this);
		fManager = getFragmentManager();
		titleTxt = (DateTitleView) this.findViewById(R.id.home_title);
		modeMonth = (RadioButton) findViewById(R.id.mode_month);
		modeMonth.setChecked(true);
		findViewById(R.id.lunar_frame).setOnClickListener(this);
		lunarView = findViewById(R.id.lunar_fragment);
		lunarView.setOnClickListener(null);
		scaleYupAnim = AnimationUtils.loadAnimation(this, R.anim.scale_yup);
		scaleYdownAnim = AnimationUtils.loadAnimation(this, R.anim.scale_ydown);
		scaleYdownAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				findViewById(R.id.lunar_frame).setVisibility(View.GONE);
			}

			public void onAnimationRepeat(Animation arg0) {
			}

			public void onAnimationStart(Animation arg0) {
			}
		});

		myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					notificationIcon.reDrawCount(msg.arg1);
					break;
				}
				super.handleMessage(msg);
			}
		};
		weekfragment = new WeekFragment();
		monthfragment = new MonthFragment();
		usrBReceiver = new UserStatusBroadcastReceiver();
		receiverNDBD = new ReceiverNewDateBDcast();
		
		this.onClick(modeMonth);
	}

	public void onResume() {
		super.onResume();
		new Handler(getMainLooper()).postDelayed(new Runnable() {

			@Override
			public void run() {
				registerReceiver(receiverNDBD, new IntentFilter(MyApplication.SELECT_NEW_DATE_BROADCAST));
				registerReceiver(usrBReceiver, new IntentFilter(MyApplication.USER_STATUS_CHANGE_BROADCAST));

				// 启动左上角消息计数
				new UpdateNotificationMsgCnt(HomeActivity.this).start();
				new GetRecentDashiKanfaTask().execute();
			}
		}, 1500);
	}
	
	class GetRecentDashiKanfaTask extends AsyncTask{
		JSONObject recentlyObj = null;
		@Override
		protected Object doInBackground(Object... params) {
			try {
				JSONObject res = RequestManager.getGet(MyApplication.dashikanfa_url);
				JSONArray  jarr= res.getJSONArray("pushList");
				recentlyObj = (JSONObject) jarr.get(jarr.length() -1);
			} catch (JSONException | IOException e) {
				e.printStackTrace();
			}
			return null;
		} 
		
		@Override
		protected void onPostExecute(Object result) {

			if(recentlyObj == null || TextUtils.isEmpty(recentlyObj.optString("vc_Title"))){
				return;
			}
			String recentTitle = recentlyObj.optString("vc_Title");
			if(updateNewDashiKanfa(recentlyObj)){
				PreferenceManager.getInstance().setPreference(CommonDefine.PREF_RECNET_DASHI_KANFA, recentlyObj.toString());
				Intent intent=new Intent();
		        intent.setAction(CommonDefine.BROAD_CAST_RECENT_DASHI_KANFA);
		        intent.putExtra("recentTitle", recentTitle);
		        intent.putExtra("pushId", Integer.valueOf(recentlyObj.optString("i_PushID")));
		        
		        HomeActivity.this.sendBroadcast(intent);
			}
			
		}

		private boolean updateNewDashiKanfa(JSONObject recentlyObj2) {
			try {
				String lastedDashiKanfa = PreferenceManager.getInstance().getPreferenceString(CommonDefine.PREF_RECNET_DASHI_KANFA);
				if(TextUtils.isEmpty(lastedDashiKanfa)){
					return true;
				}
				com.alibaba.fastjson.JSONObject jo = null;
				try {
					com.alibaba.fastjson.JSONArray jarr = (com.alibaba.fastjson.JSONArray) JSON.parse(lastedDashiKanfa);
					jo = (com.alibaba.fastjson.JSONObject) jarr.get(0);
				} catch (Exception e) {
					e.printStackTrace();
					jo = (com.alibaba.fastjson.JSONObject) JSON.parse(lastedDashiKanfa);
				}
				
				//"vc_RealDate":"2014-11-02 16:52:00"
				String prefLastedDate = jo.getString("vc_RealDate");
				Date datePrf = Utils.getDateByStringFormat(prefLastedDate,"yyyy-MM-dd HH:mm:ss");
				Date dateReq = Utils.getDateByStringFormat(recentlyObj.optString("vc_RealDate"),"yyyy-MM-dd HH:mm:ss");
				if(dateReq.getTime() >= datePrf.getTime()){
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		}
	}

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(usrBReceiver);
			unregisterReceiver(receiverNDBD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.msgnotification:
			Intent intent = new Intent(HomeActivity.this, GuaNewsActivity.class);
			startActivity(intent);
			notificationIcon.reDrawCount(0);
			break;
		case R.id.mode_day:
			fManager.beginTransaction().replace(R.id.fragment_content, weekfragment).commit();
			break;
		case R.id.mode_month:
			fManager.beginTransaction().replace(R.id.fragment_content, monthfragment).commit();
			break;
		case R.id.lunar_frame:
			this.hideLunarFragment();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondtime = System.currentTimeMillis();
			if (secondtime - mFirstime > 3000) {
				Toast.makeText(HomeActivity.this, "再按一次返回键退出信则聆", Toast.LENGTH_SHORT).show();
				mFirstime = System.currentTimeMillis();
				return true;
			} else {
				finish();
				System.exit(0);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showLunarFragment() {
		new BlurMaskTask(this, findViewById(R.id.frame_main), findViewById(R.id.lunar_frame), new Runnable() {
			@Override
			public void run() {
				lunarView.startAnimation(scaleYupAnim);
				lunarFragment.onShow();
			}
		}).execute();
	}

	public void hideLunarFragment() {
		lunarView.startAnimation(scaleYdownAnim);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		titleTxt.onActivityResult(requestCode, resultCode, data);
	}

	public void refreshTitleTxt(boolean useToday) {
		if (useToday) {
			lunar = LunarModel.fetchByDate(new Date());
		}
		titleTxt.setViewDateTitle(lunar);
	}

	public class NavRunner implements Runnable {
		private int navId;

		public NavRunner(int viewId) {
			navId = viewId;
		}

		@SuppressWarnings("unused")
		@Override
		public void run() {
			setUserStatus(MyApplication.isSignin());
		}
	}

	public void setUserStatus(boolean islogin) {
		if (islogin) {
			// findViewById(R.id.nav_usr_signin).setVisibility(View.GONE);
			// findViewById(R.id.nav_usr_exit).setVisibility(View.VISIBLE);
		} else {
			// findViewById(R.id.nav_usr_exit).setVisibility(View.GONE);
			// findViewById(R.id.nav_usr_signin).setVisibility(View.VISIBLE);
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
			String last_ymd = MyApplication.getLastUpdateMsgCnt();
//			if (last_ymd != null && today_ymd.equals(last_ymd))
//				return;
			try {
				JSONObject res = RequestManager.getGet(MyApplication.update_notification_cnt);
				if (res != null) {
					final int update_cnt = Integer.valueOf(res.getString("badge"));
					HomeActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							notificationIcon.reDrawCount(update_cnt);
						}
					});
					Message message = new Message();
					message.what = 1;
					message.arg1 = update_cnt;
					myHandler.sendMessage(message);
					MyApplication.setLastUpdateMsgCnt();
				}
			} catch (IOException | JSONException e) {
				e.printStackTrace();
			}
			super.run();
		}
	}

	private class UserStatusBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			int cmd = arg1.getIntExtra("cmd", -1);
			if (cmd != -1) {
				switch (cmd) {
				case MyApplication.USER_STATUS_CHANGE:
					if (!arg1.getBooleanExtra("isLogin", false)) {
						// 未登录
						((HomeActivity) arg0).setUserStatus(false);
					} else {
						// 已登录
						((HomeActivity) arg0).setUserStatus(true);
					}
					break;
				}
			}
		}
	}

	private class ReceiverNewDateBDcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String newdate = arg1.getStringExtra("newDateString");
			if (newdate != null) {
				try {
					if (monthfragment != null && monthfragment.calendarView != null) {
						monthfragment.calendarView.refreshByDate(newdate);
					}
					if (weekfragment != null && weekfragment.calendarView != null) {
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