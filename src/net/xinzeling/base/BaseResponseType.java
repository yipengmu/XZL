package net.xinzeling.base;

import org.json.JSONObject;

public abstract class BaseResponseType {

	public String resCode ;
	public String msg ;
	
	public abstract void parseJson(JSONObject json);
}
