/**
 * 
 */
package net.xinzeling.share;

import net.xinzeling2.R;
import android.content.Intent;

public class ShareAppInfo {
	public static final int Max_Number = 10;
	public final static int 
			Target_QZone = R.drawable.btn_share_qq_dark, 
			Target_Weixin_TimeLine = R.drawable.btn_weixin_friend_timeline_dark,
			Target_Sina_Weibo = R.drawable.btn_weibo_icon_dark,
			Target_Weixin_Friend = R.drawable.btn_weixin_im_dark;
	public String name;
	public int icon;
	public Intent intent;
	/** 埋点名称 */
	public String mUTName;
	public boolean isInstalled = true;
	
	public ShareAppInfo(int icon, String name) {
		this.name = name;
		this.icon = icon;
	}
	
	public ShareAppInfo(int icon, String name,boolean isInstalled) {
		this.name = name;
		this.icon = icon;
		this.isInstalled = isInstalled;
	}


	public ShareAppInfo(int icon, String name, String uTName) {
		this.name = name;
		this.icon = icon;
		this.mUTName = uTName;
	}

	@Override
	public String toString() {
		return name;
	}
}
