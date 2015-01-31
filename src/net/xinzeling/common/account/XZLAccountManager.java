package net.xinzeling.common.account;

import android.util.Log;

public class XZLAccountManager {

	private static XZLAccountManager ins = null;

	/** 0 代表xzl api帐号 1代表微博，2代表qq */
	public int mAcoutType = 0;

	public static QQAccountManager qqAccount = new QQAccountManager();
	public static SinaWeiboAccountManager SinaAccount = new SinaWeiboAccountManager();
	public static XZLCommonAccountManager commonAccount = new XZLCommonAccountManager();

	
	public static XZLAccountManager getInstance(){
		if(ins == null){
			ins = new XZLAccountManager();
		}
		return ins;
	}


	public int getmAcoutType() {
		return mAcoutType;
	}


	/** 0 代表xzl api帐号 1代表微博，2代表qq */
	public XZLAccountManager setmAcoutType(int type) {
		this.mAcoutType = type;
		Log.d("type", "type " + mAcoutType);
		return this;
	}


	public static QQAccountManager getQqAccount() {
		return qqAccount;
	}


	public static void setQqAccount(QQAccountManager qqAccount) {
		XZLAccountManager.qqAccount = qqAccount;
	}


	public static SinaWeiboAccountManager getSinaAccount() {
		return SinaAccount;
	}


	public static void setSinaAccount(SinaWeiboAccountManager SinaAccount) {
		XZLAccountManager.SinaAccount = SinaAccount;
	}


	public static XZLCommonAccountManager getCommonAccount() {
		return commonAccount;
	}


	public static void setCommonAccount(XZLCommonAccountManager commonAccount) {
		XZLAccountManager.commonAccount = commonAccount;
	}
	
	
	
}
