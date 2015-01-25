package net.xinzeling.ui.myzhuanfa;

import java.util.ArrayList;

import net.xinzeling.common.CommonDefine;
import net.xinzeling.common.PreferenceManager;

import com.alibaba.fastjson.JSON;

public class ZhuanfaManager {

	private static ZhuanfaManager ins;
	private static ArrayList<ZhuanfaBean> mList = new ArrayList<ZhuanfaBean>();

	public static ZhuanfaManager getInstance() {
		if (ins == null) {
			ins = new ZhuanfaManager();
		}
		return ins;
	}

	private ZhuanfaManager() {
	}

	public static void addZhuanfaItem(ZhuanfaBean obj) {
		mList.add(obj);
		PreferenceManager.getInstance().setPreference(CommonDefine.PREF_MY_ZHUANFA_KEY,JSON.toJSONString(mList));
	}

	public static String getAllZhuanfaString() {

		return JSON.toJSONString(mList);
	}

}
