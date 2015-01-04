package net.xinzeling.lib;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpCommon {
	
	public static JSONObject getGet(String surl) throws IOException, JSONException{
		return getGet(surl, null);
	}
	
	public static JSONObject getGet(String surl,HashMap<String,Object> params) throws IOException, JSONException{
		String response="" ;
		if(params !=null){
			surl =surl+"?"+paramsEncode(params);
		}
		HttpURLConnection conn = null;
		try {
			URL url = new URL(surl);
			conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(4000);
			conn.setDoInput(true);
	
			if(conn.getResponseCode()==200){
				response = inputStreamToString(conn.getInputStream());
			}
		}finally{
			if(conn !=null){
				conn.disconnect();
			}
		}
		Log.i("http", response);
		return new JSONObject(response);
	}
	
	public static String paramsEncode(HashMap<String,Object> params){
		StringBuffer sb = new StringBuffer();
		if(params !=null && !params.isEmpty()){
			try {
				for(Map.Entry<String, Object> entry:params.entrySet()){
					sb.append(entry.getKey()).append("=");
					if(entry.getValue() !=null){
						sb.append(URLEncoder.encode(entry.getValue().toString(),"utf-8"));
					}else{
						sb.append("null");
					}
					sb.append("&");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sb.deleteCharAt(sb.length()-1);
		} 
		return sb.toString();
	}
	
	public static JSONObject getPost(String surl, HashMap<String,Object> params) throws IOException, JSONException{
		return getPost(surl,params,null);
	}
	
	public static JSONObject getPost(String surl, HashMap<String,Object> params,File file) throws IOException, JSONException{
		HttpPost httpPost = new HttpPost(surl);  
		// 设置字符
		MultipartEntity mpEntity = new MultipartEntity(); //文件传输
		if(file!=null){
			ContentBody cbFile = new FileBody(file);
			mpEntity.addPart("photo", cbFile);
		}
		if(params!=null){
			for(String key:params.keySet())
			{
				if(params.get(key)!=null)
				{
					StringBody stringBody = new StringBody(params.get(key).toString(), Charset.forName("utf-8"));
					mpEntity.addPart(key,stringBody);
				}
			}
		}
		// 设置参数实体  
		httpPost.setEntity(mpEntity);
		if(params!=null){
			System.out.println("request params:--->>" + params.toString());
		}
		// 获取HttpClient对象  
		HttpClient httpClient = new DefaultHttpClient();  
		//连接超时  
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);  
		//请求超时  
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);  

		HttpResponse httpResp = httpClient.execute(httpPost);  
		String json = EntityUtils.toString(httpResp.getEntity(), "utf-8");
		System.out.println(json);
		return new JSONObject(json);
	}
	
	private static String inputStreamToString(InputStream inputStream) throws IOException{
		String result="";
		if(inputStream !=null){
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			int len = 0;
			while((len=inputStream.read(data)) !=-1){
				ba.write(data, 0, len);
			}
			result= new String(ba.toByteArray(),"utf-8");
			inputStream.close();
		}
		return result;
	}
}