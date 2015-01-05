package net.xinzeling.news;

import net.xinzeling2.R;
import net.xinzeling.gua.JieGuaActivity;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.FontManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GuaNewsShareActivity extends Activity implements OnClickListener{

	private WebView showNews;
	private LinearLayout btn_share_line;
	private TextView header_main_title;
	private ImageView header_left_btn,header_right_btn;
	private boolean isShowShare;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		showNews = (WebView)findViewById(R.id.showNews);
		btn_share_line = (LinearLayout)findViewById(R.id.btn_share_line);
		btn_share_line.setVisibility(View.INVISIBLE);
		isShowShare = false;
		header_main_title = (TextView)findViewById(R.id.header_main_title);
		header_main_title.setText("大师看法");
		
		header_left_btn = (ImageView)findViewById(R.id.header_left_btn);
		header_right_btn = (ImageView)findViewById(R.id.header_right_btn);
		header_left_btn.setOnClickListener(this);
		header_right_btn.setOnClickListener(this);
		
		Intent intent = getIntent();
		int news_id = intent.getIntExtra("news_id", 0);
		if(news_id>0)AppBase.setNewsReaded(news_id);
		showNews.loadUrl(AppBase.kanfa_detail_url + news_id);
//		FontManager.changeFonts((ViewGroup)AppBase.getRootView(GuaNewsShareActivity.this),GuaNewsShareActivity.this);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.header_left_btn:
			break;
		case R.id.header_right_btn:
			isShowShare = !isShowShare;
			btn_share_line.setVisibility(isShowShare?View.VISIBLE:View.INVISIBLE);
			break;
		case R.id.btn_share_qq:
			break;
		case R.id.btn_share_pengyouquan:
			break;
		case R.id.btn_share_weibo:
			break;
		case R.id.btn_share_weixin:
			break;
		}
	}

}
