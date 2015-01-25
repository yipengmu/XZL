package net.xinzeling.news;

import net.xinzeling.MyApplication;
import net.xinzeling.share.CommonShareActivity;
import net.xinzeling.share.WeiboShareActivity;
import net.xinzeling2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GuaNewsDetailActivity extends Activity implements OnClickListener{
	public final static int REQ_WEIBO_CMD = 1;

	private WebView mWebview;
	private LinearLayout btn_share_line;
	private TextView header_main_title;
	private ImageView header_left_btn,header_right_btn;
	private boolean isShowShare;

	private final static int FLAG_GUA_DETAIL_SHARE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		mWebview = (WebView)findViewById(R.id.showNews);
		btn_share_line = (LinearLayout)findViewById(R.id.btn_share_line);
		isShowShare = false;
		//showShareLine(isShowShare);
		header_main_title = (TextView)findViewById(R.id.header_main_title);
		header_main_title.setText("大师看法");
		header_main_title.setVisibility(View.VISIBLE);
		header_left_btn = (ImageView)findViewById(R.id.header_left_btn);
		header_right_btn = (ImageView)findViewById(R.id.header_right_btn);
		header_right_btn.setImageDrawable(getResources().getDrawable(R.drawable.my_title_share_icon));
		header_left_btn.setOnClickListener(this);
		header_right_btn.setOnClickListener(this);

		Intent intent = getIntent();
		int news_id = intent.getIntExtra("news_id", 0);
		if(news_id>0)MyApplication.setNewsReaded(news_id);
		mWebview.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				header_main_title.setText(title);
			}
			
		});
		mWebview.loadUrl(MyApplication.kanfa_detail_url + news_id);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.header_left_btn:
			finish();
			break;
		case R.id.header_right_btn:
			showShareLine();
			break;
		case R.id.btn_share_qq:
			break;
		case R.id.btn_share_pengyouquan:
			break;
		case R.id.btn_share_weibo:
			Intent intent = new Intent(GuaNewsDetailActivity.this,WeiboShareActivity.class);
			this.startActivityForResult(intent, REQ_WEIBO_CMD);
			break;
		case R.id.btn_share_weixin:
			break;
		}
	}

	private void showShareLine() {
//		btn_share_line.setVisibility(isShowShare?View.VISIBLE:View.INVISIBLE);
//		isShowShare = !isShowShare;
		Intent intent = new Intent(GuaNewsDetailActivity.this,CommonShareActivity.class);
		intent.putExtra(CommonShareActivity.SHARE_TEXT_CONTENT, "分享内容 xzl");
		intent.putExtra(CommonShareActivity.SHARE_TITLE, "分享title-xzl");
		startActivityForResult(intent, FLAG_GUA_DETAIL_SHARE );
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			switch(requestCode){
			case REQ_WEIBO_CMD:
				doOldWeibo(data);
				break;
			case 2:
				saveZhuanfaDatalogic();
				break;
			}
			
		}
	}

	private void saveZhuanfaDatalogic() {
		
	}

	private void doOldWeibo(Intent data) {
		int result = data.getIntExtra("result", -1);
		if(result==-1){
			//错误
			Toast.makeText(GuaNewsDetailActivity.this, 
					"微博分享失败", 
					Toast.LENGTH_SHORT).show();
		}else if(result==0){
			//成功
			Toast.makeText(GuaNewsDetailActivity.this, 
					"微博分享成功", 
					Toast.LENGTH_SHORT).show();
		}else if(result==1){
			//失败
			Toast.makeText(GuaNewsDetailActivity.this, 
					"取消了微博分享", 
					Toast.LENGTH_SHORT).show();
		}
	}

}
