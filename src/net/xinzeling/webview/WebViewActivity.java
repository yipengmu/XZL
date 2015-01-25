package net.xinzeling.webview;

import net.xinzeling.MyApplication;
import net.xinzeling2.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebViewActivity extends Activity {
	private TextView txtTitle;
	private WebView webView;
	private String intentTitle;
	private String intentUrl;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		txtTitle = (TextView)this.findViewById(R.id.txt_title);
		intentTitle = getIntent().getStringExtra("title")	;
		intentUrl = getIntent().getStringExtra("url")	;
		if(!TextUtils.isEmpty(intentTitle)){
			txtTitle.setText(intentTitle);
		}
		
		webView = (WebView) findViewById(R.id.webview);       
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() 
        {        
			@Override
			public boolean shouldOverrideUrlLoading(WebView view,String url){  
		        if(url.indexOf("tel:")<0){
		            view.loadUrl(url);  
		        }
		        return true;            
		    }  
			
        });
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onReceivedTitle(WebView view, String title) {
				if(!TextUtils.isEmpty(title) && !title.equals(intentTitle)){
					txtTitle.setText(title);
				}
				super.onReceivedTitle(view, title);
			}
			
		});
		if(!TextUtils.isEmpty(intentUrl)){
			webView.loadUrl(intentUrl); 
		}else{
			String target = this.getIntent().getStringExtra("target");
			if(target.equals("regist")){
				webView.loadUrl(MyApplication.webview_regist_url); 
				txtTitle.setText("注册");
			}else{
				webView.loadUrl(MyApplication.webview_passwd_url);
				txtTitle.setText("忘记密码");
			}
		}
		
	}

	public void onClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else {
			webView.reload();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}