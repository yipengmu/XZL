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

import net.xinzeling.MyApplication;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

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

	public static Date getDataByStringyyyyMMdd(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Date();
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

}
