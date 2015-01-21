package net.xinzeling.common;

public class SinaWeiboAccountManager {
	// sina sso login ok data
	// uid=1310602621
	// favourites_count=9
	// location=浙江 杭州
	// description=纸上得来终觉浅，绝知此事要躬行
	// verified=true
	// friends_count=782
	// gender=1
	// screen_name=老穆_boom
	// statuses_count=1968
	// followers_count=641
	// profile_image_url=http://tp2.sinaimg.cn/1310602621/180/5635546650/1
	// access_token=2.00xbJh7BVGmUoBf2a29e98b103Dj_B

	// sina sso login ok data
	public int uid;
	public int favourites_count;
	public String location;
	public String description;
	public boolean verified;
	public int friends_count;
	public int gender;
	public String screen_name;
	public int statuses_count;
	public int followers_count;
	public String profile_image_url;
	public String access_token;

	private static SinaWeiboAccountManager ins;

	SinaWeiboAccountManager() {

	}

	public static SinaWeiboAccountManager getInstance() {
		if (ins == null) {
			ins = new SinaWeiboAccountManager();
		}
		return ins;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
