package net.xinzeling.fragment;

import java.io.IOException;
import java.util.HashMap;

import net.xinzeling2.R;
import net.xinzeling.MyApplication;
import net.xinzeling.net.http.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public  class FeedFragment extends DialogFragment implements OnClickListener{
	private EditText inputFeed;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setStyle(STYLE_NO_TITLE, 0);
	}
	
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {  
    	 View view = inflater.inflate(R.layout.fragment_feed, container, false);  
    	 inputFeed = (EditText)view.findViewById(R.id.input_feed);
    	 view.findViewById(R.id.btn_ok).setOnClickListener(this);
    	 view.findViewById(R.id.btn_cancel).setOnClickListener(this);
    	 return view;
    }
    
    public void onClick(View view){
    	if(view.getId()==R.id.btn_ok){
    		String feedContent = this.inputFeed.getText().toString();
    		if(!TextUtils.isEmpty(feedContent)){
    			new FeedTask(feedContent).execute();
    		}else{
    			Toast.makeText(getActivity(), "请输入内容", Toast.LENGTH_LONG).show();
    		}
    	}else{
    		this.dismiss();
    	}
    }
        
    //发送反馈
  	private class FeedTask extends AsyncTask<Void, Void, Boolean> {
  		private ProgressDialog progress;
  		String resMsg="";
  		private HashMap<String,Object> params= new HashMap<String,Object>();
  		
  		public FeedTask(String content){
  			params.put("content", content);
  		}
  		@Override
  		protected void onPreExecute(){
  			progress = ProgressDialog.show(getActivity(), null, "请求中……");
  		}
  		@Override
  		protected Boolean doInBackground(Void... args) {  			
  			try {
				JSONObject jsonResp = RequestManager.getGet(MyApplication.system_feed_url,params);
				int resCode = jsonResp.getInt("resCode");
				if(resCode !=0){
					resMsg = jsonResp.getString("resMsg");
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				resMsg ="请求失败，请重试";
				return false;
			}
  			return true;
  		}
  		
  		@Override
  		protected void onPostExecute(Boolean result){
  			if (progress != null) {
				progress.dismiss();
			}
  			if(!result){
  				Toast.makeText(getActivity(), resMsg,Toast.LENGTH_LONG).show();
  			}else{
  				dismiss();
  			}
  		}
  	}
}
