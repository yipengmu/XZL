package net.xinzeling.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
}

