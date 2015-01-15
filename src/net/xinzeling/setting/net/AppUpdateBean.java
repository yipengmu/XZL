package net.xinzeling.setting.net;

import org.json.JSONObject;

import net.xinzeling.base.BaseResponseType;

public class AppUpdateBean extends BaseResponseType{

	public float version ;
	public String downloadUrl ;
	public String description ;
	
	@Override
	public void parseJson(JSONObject json) {
		try {
			version = Float.valueOf(json.optString("version"));
		} catch (Exception e) {
		}
		downloadUrl = json.optString("downloadUrl");
		description = json.optString("description");
	}
	
}
