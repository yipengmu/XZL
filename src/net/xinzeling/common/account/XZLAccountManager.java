package net.xinzeling.common.account;

public class XZLAccountManager {

	/** 1 代表xzl api帐号 2代表微博，3代表qq */
	public static int mAcoutType = 1;

	public static QQAccountManager qqAccount = new QQAccountManager();
	public static SinaWeiboAccountManager qqAcount = new SinaWeiboAccountManager();
	public static XZLCommonAccountManager commonAccount = new XZLCommonAccountManager();

}
