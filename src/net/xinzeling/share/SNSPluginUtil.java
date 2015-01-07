package net.xinzeling.share;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class SNSPluginUtil {
	/**transaction字段用于唯一标识一个请求*/
	public static final String TRANSACTION = "PluginTransaction";
	/** WeiBo, WeiXin, QZone */
	public enum PluginName {
		WeiBo, WeiXin, QZone;
	}
	
	/**获得当前时间戳*/
	public static String getCharacterAndNumber() {
		String rel = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());
		rel = formatter.format(curDate);
		return rel;
	}
	
	public static ArrayList<ShareAppInfo> getShareAppList(Context context, Intent shareIntent) {
		List<ResolveInfo> list = new ArrayList<ResolveInfo>();
		PackageManager pm = context.getPackageManager();
		list = pm.queryIntentActivities(shareIntent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		ShareAppInfo[] array = new ShareAppInfo[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ShareAppInfo item = getAppName(list.get(i).activityInfo.packageName, list.get(i).activityInfo.name);
			if (item != null) {
				item.intent = new Intent(shareIntent);
				item.intent.setComponent(new ComponentName(list.get(i).activityInfo.packageName,
						list.get(i).activityInfo.name));
				array[i] = item;
			}
		}
		ShareAppInfo[] sortArray = new ShareAppInfo[ShareAppInfo.Max_Number];
		
		ArrayList<ShareAppInfo> nameList = new ArrayList<ShareAppInfo>();
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				if (array[i].icon == ShareAppInfo.Target_Sina_Weibo) {
					sortArray[2] = array[i];
				}else if (array[i].icon == ShareAppInfo.Target_QZone) {
					sortArray[4] = array[i];
				}
			}
		}
		for (int i = 0; i < sortArray.length; i++) {
			if (sortArray[i] != null) {
				nameList.add(sortArray[i]);
			}
		}
		return nameList;
	}

	public static ShareAppInfo getAppName(String packageName, String activityName) {
		if (packageName == null || activityName == null) {
			return null;
		}
		packageName = packageName.toLowerCase();
		activityName = activityName.toLowerCase();
		if (packageName.equals("com.sina.weibo")) {
			return new ShareAppInfo(ShareAppInfo.Target_Sina_Weibo,"新浪微博","Share_Xlwb");
		}
		if (packageName.equals("com.qzone")) {
			return new ShareAppInfo(ShareAppInfo.Target_QZone, "QQ空间","Share_Qqkj");
		}
		
		return null;
	}
	
	/**获得 一个 LinearLayout.LayoutParams (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 * 宽度 ：包含内部控件
	 * 高度 ：包含内部控件
	 * WRAP_CONTENT,WRAP_CONTENT
	 *  */
	public static  LinearLayout.LayoutParams getWapConentAndWapConentParams(){
		return new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
	/**获得 一个 LinearLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	 * 宽度 ：撑满父控件
	 * 高度 ：撑满父控件
	 * MATCH_PARENT,MATCH_PARENT
	 * */
	public static  LinearLayout.LayoutParams getMatchParentAndMatchParentParams(){
		return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
	
	/**获得 一个 LinearLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	 * 宽度 ：撑满父控件
	 * 高度 ：包含内部控件
	 * MATCH_PARENT,WRAP_CONTENT
	 * */
	public static  LinearLayout.LayoutParams getMatchParentAndWapConentParams(){
		return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

}
