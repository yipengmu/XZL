package net.xinzeling.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.xinzeling.MainActivity;
import net.xinzeling.MyApplication;
import net.xinzeling.common.database.DBHelper;
import net.xinzeling.share.CommonShareActivity;
import net.xinzeling.ui.SplashActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.WindowManager;

public class Utils {

	/** 分享的url */
	public static String SNS_SHARE_BUNDLE_WEIXIN_TITLE = "信则聆";
	/** 分享的内容文案 */
	public static String SNS_SHARE_BUNDLE_MSG = "信则聆";
	/** 分享的url */
	public static String SNS_SHARE_BUNDLE_URL = "http://weibo.com/5319290870/BBYP6pM2L?sudaref=www.baidu.com&type=comment#_rnd1420610216560";
	/** 分享的图片 */
	public static String SNS_SHARE_BUNDLE_BITMAP_URL = "http://tp3.sinaimg.cn/5319290870/180/40069366120/1";
	/** 微信 仅分享的文字 */
	public static String SNS_SHARE_BUNDLE_WEIXIN_ONLY_TEXT = "false";

	/** 根据image url 获取bitmap */
	public static Bitmap getBitmapFromURL(String urlSrc) {
		try {
			if (TextUtils.isEmpty(urlSrc)) {
				return null;
			}
			URL url = new URL(urlSrc);
			if (url.getProtocol().equals("file")) { // 本地文件
				String file_path = url.getPath();
				if (TextUtils.isEmpty(file_path)) {
					return null;
				}
				File file = new File(file_path);
				FileInputStream io = new FileInputStream(file);
				Bitmap myBitmap = BitmapFactory.decodeStream(io);
				io.close();
				return myBitmap;
			} else {
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 检查 当前时间 是否在规定时间内 */
	public static boolean isInCorrectTimeSection(long now, long start, long end) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		long limitDaySecond = 1000 * 60 * 60 * 24;
		return now <= end + limitDaySecond && now >= start ? true : false;
	}

	public static Date getDateByStringyyyyMMdd(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return new Date();
	}

	/** fomatter yyyyMMdd yyyy年MM月dd日 HH时mm分ss秒 */
	public static Date getDateByStringFormat(String str, String fomatter) {
		SimpleDateFormat sdf = new SimpleDateFormat(fomatter);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	/**
	 * "yyyyMMdd" format yyyy年MM月dd日 HH时mm分ss秒
	 * 
	 * */
	public static String getStringByDate(String format, long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(time));
	}

	// 打开APK程序代码
	private void openFile(File file) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		MyApplication.getContext().startActivity(intent);
	}

	public static void downloadApk(String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		if (url.endsWith(".apk")) {
			Uri uri = Uri.parse(url);
			Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
			viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MyApplication.getContext().startActivity(viewIntent);
		}
	}

	public static float getAppVersion(Context c) {
		float localVersion = 0;
		try {
			PackageManager nPackageManager = c.getPackageManager();// 得到包管理器
			PackageInfo nPackageInfo = nPackageManager.getPackageInfo(c.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			localVersion = Float.valueOf(nPackageInfo.versionName);// 得到现在app的版本号
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		return localVersion;
	}

	public static int getScreenWidth(Context c) {
		WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	public static int getScreenHeight(Context c) {
		WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}

	/**
	 * 
	 intent.putExtra("tabIndex", MainActivity.Maintab_Index_Home);
	 * 
	 * public static int Maintab_Index_Home = 0; public static int
	 * Maintab_Index_Gua = 1; public static int Maintab_Index_Note = 2; public
	 * static int Maintab_Index_My = 3;
	 * 
	 * */
	public static Intent getMaintabIndexIntent(Context c, int index) {
		Intent intent = new Intent(c, MainActivity.class);
		intent.putExtra("tabIndex", index);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	public static Intent getShareIntent(Context c, String shareText, String shareTitle) {
		Intent intent = new Intent(c, CommonShareActivity.class);
		intent.putExtra(CommonShareActivity.SHARE_TEXT_CONTENT, shareText);
		intent.putExtra(CommonShareActivity.SHARE_TITLE, shareTitle);
		return intent;
	}

	public static String getGuaTypeFromId(int type) {
		String t = "";
		// 类型说明
		switch (type) {
		case 0:
			t = "出行";
			break;
		case 1:
			t = "交易";
			break;
		case 2:
			t = "婚姻";
			break;
		case 3:
			t = "恋爱·恋人";
			break;
		case 4:
			t = "求财·财运·讨债";
			break;
		case 5:
			t = "事业·工作";
			break;
		case 6:
			t = "健康·疾病";
			break;
		case 7:
			t = "家宅·房屋";
			break;
		case 8:
			t = "官司诉讼·申诉投诉";
			break;
		case 9:
			t = "其他·难分类·谋事";
			break;
		case 10:
			t = "近期财运";
			break;
		case 11:
			t = "求财之事";
			break;
		case 12:
			t = "借贷";
			break;
		case 13:
			t = "讨债";
			break;
		case 14:
			t = "学业";
			break;
		case 15:
			t = "考试";
			break;
		case 16:
			t = "今日吃啥";
			break;
		case 17:
			t = "今日穿啥";
			break;
		case 18:
			t = "公务出差";
			break;
		case 19:
			t = "旅行出游";
			break;
		case 20:
			t = "每日出行";
			break;
		case 21:
			t = "单身 ' 今日运程";
			break;
		case 22:
			t = "单身 ' 近期桃花";
			break;
		case 23:
			t = "有恋人 ' 今日运程";
			break;
		case 24:
			t = "有恋人' 恋爱状况";
			break;
		case 25:
			t = "有恋人'近期桃花";
			break;
		case 26:
			t = "婚姻状况";
			break;
		case 27:
			t = "将来婚姻状况";
			break;
		case 28:
			t = "婚事情况";
			break;
		case 29:
			t = "年内事业运";
			break;
		case 30:
			t = "创业选择";
			break;
		case 31:
			t = "合作项目";
			break;
		case 32:
			t = "谈判成败";
			break;
		case 33:
			t = "另谋高就";
			break;
		case 34:
			t = "今日运程";
			break;
		case 35:
			t = "去逛街";
			break;
		case 36:
			t = "网购";
			break;
		case 37:
			t = "买房";
			break;
		case 38:
			t = "买车";
			break;
		case 39:
			t = "装修";
			break;
		case 40:
			t = "今日运程";
			break;
		case 41:
			t = "近期状况";
			break;
		case 42:
			t = "病症状况";
			break;
		case 43:
			t = "家宅房屋";
			break;
		case 44:
			t = "出租房屋";
			break;
		case 45:
			t = "求租房屋";
			break;
		case 46:
			t = "投诉申诉";
			break;
		case 47:
			t = "其他·难分类";
			break;
		}

		return t;

	}

	public static void checkDBHInited(Context c) {
		if(MyApplication.dbh == null){
			MyApplication.dbHelper = new DBHelper(c, "xinzeling.db");
			MyApplication.dbh = MyApplication.dbHelper.getWritableDatabase();
		
		}
	}
}
