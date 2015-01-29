package net.xinzeling.common.account;

public class QQAccountManager {
	// uid=A8BC19D755E2BDDFBFA2015AFE191E70,
	// gender=男,
	// screen_name=Alex,
	// openid=A8BC19D755E2BDDFBFA2015AFE191E70,
	// profile_image_url=http:
	// //qzapp.qlogo.cn/qzapp/1104084591/A8BC19D755E2BDDFBFA2015AFE191E70/100,
	// access_token=377B62E36F530FB8167640FD3899B5EE,
	// verified=0

	public String uid;
	public String gender;
	public String screen_name;
	public String openid;
	public String profile_image_url;
	public String access_token;
	public String verified;
	
	private static QQAccountManager ins;

	QQAccountManager() {

	}

	public static QQAccountManager getInstance() {
		if (ins == null) {
			ins = new QQAccountManager();
		}
		return ins;
	}
}
