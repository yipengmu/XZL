package net.xinzeling.lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import net.xinzeling.gua.GuaActivity;
import net.xinzeling.receiver.AlarmReceiver;
import net.xinzeling2.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import cn.jpush.android.api.JPushInterface;

public class AppBase extends Application{
	public final static String Weibo_APP_ID = "2045436852";//"3665317294";
	public final static String Weibo_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
	public static final String Weibo_SCOPE =  
			"email,direct_messages_read,direct_messages_write,"
					+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
					+ "follow_app_official_microblog," + "invitation_write";

	public final static String QQ_APP_ID = "1103520665";
	public final static String QQ_APP_KEY = "4aiyBe5G7H20CSs0";
	public final static String QQ_SCOPE = "";

	public final static String WEIXIN_APP_ID = "wx103dfd6ac8f00857";
	public final static String WEIXIN_APP_KEY = "a1933c4de22b6466e6edfccb93c0df7c";

	public final static int USER_STATUS_CHANGE = 1;
	public final static String USER_STATUS_CHANGE_BROADCAST = "user_status_change_broadcast";

	public final static String GUA_BACK_HOME_BROADCAST = "back_home_from_thread_broadcast";

	public final static String SELECT_NEW_DATE_BROADCAST = "select_new_date_broadcast";

	public final static String photo_path = "/sdcard/photo_tmp.bmp";
	private final static String server = "http://115.29.5.162/";
	public final static String refresh_token_url = server + "webservice/XZLService.asmx/accoun02tRenewal?renewalToken=[renewalToken]";
	public final static String webview_regist_url = server + "register/phoneregister.aspx?theme=1";
	public final static String webview_passwd_url = server + "register/rescuepassword.aspx?theme=1";
	public final static String third_regist_url = server + "webservice/XZLService.asmx/account06RegisterUserByAuth";
	public final static String account01Verification = server + "webservice/XZLService.asmx/account01Verification";
	public final static String dashijianjie_info_url = server + "management/master.aspx";
	public final static String dashikanfa_url = server + "webservice/XZLService.asmx/push01GetList?timestamp=0";
	public final static String kanfa_detail_url = server + "management/news.aspx?id=";
	public final static String update_notification_cnt = server + "webservice/XZLService.asmx/system03CheckBadgeAndVersion?timestamp=0&type=0";
	public final static String system_feed_url = server + "webservice/XZLService.asmx/system02AddFeedback";
	public final static String update_usr_profile_url = server + "webservice/XZLService.asmx/account09UpdateAccountDetailToNet";
	public final static String gua_double_num_url = server + "webservice/XZLService.asmx/divine11DoubleNum";
	public final static String gua_photo_url = server + "webservice/XZLService.asmx/divine12Photo";
	public final static String gua_time_url = server + "webservice/XZLService.asmx/divine14Time";
	public final static String check_usrname_isused = server + "webservice/XZLService.asmx/account04CheckUserName?username=";

	public final static int[] note_topics = {
		R.id.note_topic_1,R.id.note_topic_2,R.id.note_topic_3,R.id.note_topic_4,R.id.note_topic_5,R.id.note_topic_6,
		R.id.note_topic_7,R.id.note_topic_8,R.id.note_topic_9,R.id.note_topic_10,R.id.note_topic_11,R.id.note_topic_12,
		R.id.note_topic_13,R.id.note_topic_14,R.id.note_topic_15,R.id.note_topic_16,R.id.note_topic_17,R.id.note_topic_18,
		R.id.note_topic_19,R.id.note_topic_20,R.id.note_topic_21,R.id.note_topic_22,R.id.note_topic_23,R.id.note_topic_24
	};

	public final static int REPEAT_EMPTY = 0;
	public final static int REPEAT_TYPE_YEAR = 1;
	public final static int REPEAT_TYPE_MONTH = 2;
	public final static int REPEAT_TYPE_WEEK = 3;
	public final static int REPEAT_TYPE_DAY = 4;

	public static enum note_error_code{
		EMPTY_TITLE,
		EMPTY_CONTACT,
		EMPTY_CONTENT,
		EMPTY_STARTTIME,
		EMPTY_ENDTIME,
		ERROR_ENDTIME,
	};

	public static enum share_id{
		SHARE_QQ_PENGYOUQUAN,
		SHARE_QQZONE,
		SHARE_WEIBO,
		SHARE_WEIXIN,
	};
	public final static int xz_id_day = 1;
	public final static int xz_id_month = 2;
	public final static int xz_id_year = 3;

	public final static int[] double_type_int = {
		2,201,202,203,27,2801,2802
	};
	public final static int nav_gua_num = 1;
	public final static int nav_gua_photo = 2;
	public final static int nav_gua_time = 3;
	protected static Context context;
	protected static SQLiteDatabase dbh;
	private static DBHelper dbHelper;
	public static SharedPreferences sharedPreference;

	public static int usrId =-1;
	public static String usrName;
	public static int gender=2;

	public static String userToken;
	public static String userTokenExpire;
	public static String renewalToken;
	public static String renewalTokenExpire;

	public static List<String> userinfo_params_key = Arrays.asList(
			new String[]{"nick","firstName","name","birthday","birthTime","phone","email","birthAddress",
					"nowaddr","career","marriage","gender"});
	
	public void onCreate(){
		context = getApplicationContext();

		sharedPreference = context.getSharedPreferences("usr", Context.MODE_APPEND);

		///test begin
		Editor editor=sharedPreference.edit();
		editor.putString("nick", "小白");
		editor.putString("firstName", "白");//姓
		editor.putString("name", "明江");//名
		editor.putString("birthday", "1982-1-21");
		editor.putString("birthTime", "08:21:00");
		editor.putString("phone", "13776104530");
		editor.putString("email", "351035557@qq.com");

		editor.putString("birthAddress", "山东济宁");
		editor.putString("nowaddr", "苏州");
		editor.putString("career", "IT");
		editor.putString("marriage", "已婚");//已婚 单身 未婚 离异

		editor.putInt("gender", 0);//0 男 1女
		editor.commit();
		///test end

		usrName = sharedPreference.getString("nick", "");
		gender = sharedPreference.getInt("gender", 2);

		AppBase.userToken=sharedPreference.getString("userToken", null);
		AppBase.userTokenExpire = sharedPreference.getString("userTokenExpire", "");
		AppBase.renewalToken = sharedPreference.getString("renewalToken", null);
		AppBase.renewalTokenExpire=sharedPreference.getString("renewalTokenExpire", "");

		dbHelper =  new DBHelper(context, "xinzeling.db");
		dbh =dbHelper.getWritableDatabase();
		
		// push
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	}		
	
	public static Context getContext() {
		return context;
	}

	public static String sdcardPath(){
		File sdDir = null; 
		boolean sdCardExist = Environment.getExternalStorageState()   
				.equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
		if   (sdCardExist)   
		{                               
			sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
			return sdDir.toString();        
		}else{
			return null;
		}
	}

	public static void editUserInfo(HashMap<String,String> params){
		if(params.size()>0){

			Editor editor=sharedPreference.edit();
			for(String key:userinfo_params_key){
				if(params.containsKey(key)){
					if("gender".equals(key)){
						editor.putInt(key, Integer.valueOf(params.get(key)));
					}else{
						editor.putString(key, params.get(key));						
					}
				}
			}
			editor.commit();

		}
	}

	/**
	 * sendAlarmBroadCast 发送闹铃广播
	 * @param context
	 * @param isRepeat 定时器是否重复
	 * @param firsttime 第一次触发时间离现在的秒数
	 * @param repeat_time 定时器重复的间隔秒数
	 * @param alarmId 定时器自定义id
	 */
	public static void sendAlarmBroadCast(Context context,boolean isRepeat,long firsttime,long repeat_time,int alarmId) {
		Intent intent =new Intent(context, AlarmReceiver.class);
		intent.setAction("xinzeling_alarm");
		intent.putExtra("alarm_id", alarmId);
		PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, 0);
		AlarmManager alarm=(AlarmManager)context.getSystemService(ALARM_SERVICE);
		if(!isRepeat){
			alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+firsttime*1000, sender);
		}else{
			alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis()+firsttime, repeat_time, sender);
		}
	}

	public static long NowTime(){
		long ret = 0;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		//获取系统当前时间
		ret = now.getTime();
		ret = ret / 1000;
		return ret;
	}

	/**
	 * cancelAlarm 取消定时器
	 * @param context
	 * @param alarmId 定时器自定义id
	 */
	public static void cancelAlarm(Context context,int alarmId) {
		Intent intent =new Intent(context, AlarmReceiver.class);
		intent.setAction("xinzeling_alarm");
		intent.putExtra("alarm_id", alarmId);
		PendingIntent sender=PendingIntent.getBroadcast(context, alarmId, intent, 0);
		AlarmManager alarm=(AlarmManager)context.getSystemService(ALARM_SERVICE);
		alarm.cancel(sender);
	}

	public static View getRootView(Activity context)  
	{  
		return ((ViewGroup)context.findViewById(android.R.id.content)).getChildAt(0);  
	}  

	public static String getDeviceInfo(Context context) {
		try{
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if( TextUtils.isEmpty(device_id) ){
				device_id = mac;
			}

			if( TextUtils.isEmpty(device_id) ){
				device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static void saveBitmap(Bitmap bm) {
		File f = new File(photo_path);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean getPushSW() {
		return sharedPreference.getBoolean("isPush", true);
	}

	public static void setPushSW(boolean isPush) {
		Editor editor=sharedPreference.edit();
		editor.putBoolean("isPush", isPush);
		editor.commit();
	}

	@SuppressWarnings("deprecation")
	public static float[] getWidthHeight() {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display db = wm.getDefaultDisplay();
		float[] result = new float[2];
		result[0] = db.getWidth();
		result[1] = db.getHeight();
		return result;
	}
	public static float[] getWidthHeightPixels(){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		float[] result = new float[2];
		result[0] = dm.widthPixels;
		result[1] = dm.heightPixels;
		return result;
	}

	public static void goGuaActivity(Context context,int mode){
		Intent guaIntent = new Intent(context, GuaActivity.class);
		guaIntent.putExtra("mode", mode);
		context.startActivity(guaIntent);
	}

	public static void onSignin(String userToken, String userTokenExpire,String renewalToken,String renewalTokenExpire){
		Editor editor=sharedPreference.edit();
		editor.putString("userToken", userToken);
		editor.putString("userTokeExpire", userTokenExpire);
		editor.putString("renewalToken", renewalToken);
		editor.putString("renewalTokenExpire", renewalTokenExpire);
		editor.apply();
		AppBase.userToken=userToken;
		AppBase.userTokenExpire=userTokenExpire;
		AppBase.renewalToken=renewalToken;
		AppBase.renewalTokenExpire=renewalTokenExpire;
		sendBroadcastAboutUsrStatus(true);
	}

	public static void sendBroadcastBackHomeFromThread() {
		Intent it = new Intent(AppBase.GUA_BACK_HOME_BROADCAST);
		context.sendBroadcast(it);
	}

	public static void sendBroadcastNewSelectDate(Date d){
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		String date = sdFormat.format(d);
		sendBroadcastNewSelectDate(date);
	}

	public static void sendBroadcastNewSelectDate(String newYYYYmmdd) {
		Intent it = new Intent(AppBase.SELECT_NEW_DATE_BROADCAST);
		it.putExtra("newDateString", newYYYYmmdd);
		context.sendBroadcast(it);
	}

	public static void sendBroadcastAboutUsrStatus(boolean isLogin) {
		Intent it = new Intent(AppBase.USER_STATUS_CHANGE_BROADCAST);
		it.putExtra("cmd", AppBase.USER_STATUS_CHANGE);
		it.putExtra("isLogin", isLogin);
		context.sendBroadcast(it);
	}

	public static String getLastUpdateMsgCnt() {
		return sharedPreference.getString("last_updatemsgcnt", null);
	}

	public static void setLastUpdateMsgCnt() {
		Editor editor = sharedPreference.edit();
		editor.putString("last_updatemsgcnt", DateTime.getTodayYmd(null));
		editor.commit();
	}

	public static int getunreadmsg_cnt(){
		return sharedPreference.getInt("unreadmsgcnt", 0);
	}

	public static void setunreadmsg_cnt(int cnt) {
		Editor editor = sharedPreference.edit();
		editor.putInt("unreadmsgcnt", cnt);
		editor.commit();
	}

	public static boolean isNewsReaded(int id){
		return sharedPreference.getBoolean("news_"+id, false);
	}

	public static void setNewsReaded(int id){
		Editor editor = sharedPreference.edit();
		editor.putBoolean("news_"+id, true);
		editor.commit();
	}

	public static void logout(){
		Editor editor=sharedPreference.edit();
		editor.remove("userToken");
		editor.remove("renewalToken");
		editor.commit();
		AppBase.userToken = null;
		AppBase.usrId = -1;
		AppBase.usrName = null;
		sendBroadcastAboutUsrStatus(false);
	}

	public static boolean isSignin(){
		if(AppBase.userToken !=null){
			return true;
		}
		return false;
	}

	//刷新token
	public static void renewalToken() throws IOException, JSONException{
		JSONObject jsonResp = HttpCommon.getGet(refresh_token_url);
		int resCode = jsonResp.getInt("resCode");

		if (resCode == 0) {
			String userToken = jsonResp.getString("userToken");
			String userTokenExpire = jsonResp.getString("userTokenExpireDate");
			String renewalToken = jsonResp.getString("renewalToken");
			String renewalTokenExpire =jsonResp.getString("renewalTokenExpire");
			AppBase.onSignin(userToken,userTokenExpire,renewalToken,renewalTokenExpire);
		}
	}

	public static Bitmap getResIcon(int resId){
		return getResIcon(context.getResources(),resId);
	}

	public static Bitmap getResIcon(Resources res,int resId){  
		Drawable icon=res.getDrawable(resId);  
		if(icon instanceof BitmapDrawable){  
			BitmapDrawable bd=(BitmapDrawable)icon;  
			return bd.getBitmap();  
		}else{  
			return null;  
		}  
	}  

	public static Bitmap generatorContactCountIcon(Bitmap icon, String cnt,int font_size,int font_color,int circle_color){ 
		//初始化画布  
		Bitmap contactIcon=Bitmap.createBitmap(icon.getWidth(), icon.getHeight(), Config.ARGB_8888);  
		Canvas canvas=new Canvas(contactIcon);  

		//拷贝图片  
		Paint iconPaint=new Paint();  
		iconPaint.setDither(true);//防抖动  
		iconPaint.setFilterBitmap(true);//用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果  
		Rect src=new Rect(0, 0, icon.getWidth(), icon.getHeight());  
		canvas.drawBitmap(icon, src, src, iconPaint);  

		if(!"".equals(cnt)){
			//背景圆
			float radius = (float)0.28*icon.getHeight();
			iconPaint.setColor(circle_color);
			iconPaint.setAntiAlias(true);
			canvas.drawCircle((float)(icon.getWidth()*0.83), (float)1.1*radius, radius, iconPaint);
			//启用抗锯齿和使用设备的文本字距  
			Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);  
//			countPaint.setTypeface(tf);
			countPaint.setColor(font_color);  
			countPaint.setTextSize(font_size);  
			//再加个圆形就可以了
			canvas.drawText(cnt, (float)(icon.getWidth()*0.83 - 9), (float)(1.2*radius+4), countPaint); 
		}
		return contactIcon;  
	}

	public static class usr{

	}
}