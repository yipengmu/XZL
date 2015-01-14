package net.xinzeling.push;

import net.xinzeling.MyApplication;
import net.xinzeling.common.PreferenceManager;
import cn.jpush.android.api.JPushInterface;

public class PushManager {
	public static PushManager ins;
	public final static String pushSwitcher = "PUSH_SWITCHER";
	private PushManager() {
	}

	public static PushManager getInstance() {
		if (ins == null) {
			ins = new PushManager();
		}
		return ins;
	}

	public void init(){
		if(PreferenceManager.getInstance().getPreferenceBoolean(pushSwitcher,true)){
			JPushInterface.setDebugMode(true);
			JPushInterface.init(MyApplication.getContext());
		}else{
			JPushInterface.stopPush(MyApplication.getContext());
		}
	}
	
}
