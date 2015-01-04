package net.xinzeling.gua;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import android.webkit.WebSettings;
import android.webkit.WebView;
import net.xinzeling2.R;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.AppBase.share_id;
import net.xinzeling.lib.BlurBehind;
import net.xinzeling.model.GuaModel;
import net.xinzeling.model.GuaModel.Gua;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShareActivity extends Activity {
	private UMSocialService mController;
	private share_id share_type;
	private Gua gua;
	
	private WebView webview;
	private TextView gua_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BlurBehind.getInstance().withAlpha(240).setBackground(this);
		setContentView(R.layout.activity_share_gua);
		int guaid = this.getIntent().getIntExtra("guaid", 0);
		share_type = (share_id)this.getIntent().getSerializableExtra("share_type");
		gua = GuaModel.fetch(guaid);
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
		mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		//只有微博是弹出页面的,其他的直接调接口就可以了
		if(share_type==share_id.SHARE_WEIBO){
			webview = ((WebView)findViewById(R.id.webview_share));
			//gua_content = (TextView)findViewById(R.id.txt_share_content);
			//gua_content.setText(gua.inference);
			readHtmlFormAssets();
		}else{
			;
		}
		
		// 设置分享图片, 参数2为图片的url地址
		//mController.setShareMedia(new UMImage(getActivity(),"http://www.baidu.com/img/bdlogo.png"));
		// 传递本地图片绝对路径方法
		//mController.setShareMedia(new UMImage(getActivity(),BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));
		//传递本地图片资源引用方法
		//mController.setShareMedia(new UMImage(getActivity(), R.drawable.icon));
	}

	private void readHtmlFormAssets(){
        WebSettings webSettings = webview.getSettings();
        
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        
        webview.setBackgroundColor(Color.TRANSPARENT);  //  WebView 背景透明效果 
        webview.loadUrl("file:///android_asset/html/tips.htm");
    }
	
	public void onClick(View view){
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(ShareActivity.this,AppBase.WEIXIN_APP_ID,AppBase.WEIXIN_APP_KEY);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(ShareActivity.this,AppBase.WEIXIN_APP_ID,AppBase.WEIXIN_APP_KEY);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(ShareActivity.this, AppBase.QQ_APP_ID,
				AppBase.QQ_APP_KEY);
		qqSsoHandler.addToSocialSDK();  

		//设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		//设置腾讯微博SSO handler
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(ShareActivity.this, AppBase.QQ_APP_ID,
				AppBase.QQ_APP_KEY);
		qZoneSsoHandler.addToSocialSDK();
		//mController.openShare(ShareActivity.this, false);
		//微信朋友圈
		mController.postShare(ShareActivity.this,SHARE_MEDIA.WEIXIN_CIRCLE, 
				new SnsPostListener(){
			@Override
			public void onStart() {
				Toast.makeText(ShareActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {
				if (arg1 == 200) {
					Toast.makeText(ShareActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
				} else {
					String eMsg = "";
					if (arg1 == -101){
						eMsg = "没有授权";
					}
					Toast.makeText(ShareActivity.this, "分享失败[" + arg1 + "] " + 
							eMsg,Toast.LENGTH_SHORT).show();
				}
			}
		});
		//this.finish();
	}

	private void share_weixin(){
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(ShareActivity.this,AppBase.WEIXIN_APP_ID,AppBase.WEIXIN_APP_KEY);
		wxHandler.addToSocialSDK();
		//mController.openShare(ShareActivity.this, false);
		//微信
		mController.postShare(ShareActivity.this,SHARE_MEDIA.WEIXIN, 
				new SnsPostListener(){
			@Override
			public void onStart() {
				Toast.makeText(ShareActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {
				if (arg1 == 200) {
					Toast.makeText(ShareActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
				} else {
					String eMsg = "";
					if (arg1 == -101){
						eMsg = "没有授权";
					}
					Toast.makeText(ShareActivity.this, "分享失败[" + arg1 + "] " + 
							eMsg,Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void share_weibo(){
		//设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		//mController.openShare(ShareActivity.this, false);
		//微博
		mController.postShare(ShareActivity.this,SHARE_MEDIA.SINA, 
				new SnsPostListener(){
			@Override
			public void onStart() {
				Toast.makeText(ShareActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {
				if (arg1 == 200) {
					Toast.makeText(ShareActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
				} else {
					String eMsg = "";
					if (arg1 == -101){
						eMsg = "没有授权";
					}
					Toast.makeText(ShareActivity.this, "分享失败[" + arg1 + "] " + 
							eMsg,Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void share_pengyouquan(){
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(ShareActivity.this,AppBase.WEIXIN_APP_ID,AppBase.WEIXIN_APP_KEY);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		//mController.openShare(ShareActivity.this, false);
		//微信朋友圈
		mController.postShare(ShareActivity.this,SHARE_MEDIA.WEIXIN_CIRCLE, 
				new SnsPostListener(){
			@Override
			public void onStart() {
				Toast.makeText(ShareActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {
				if (arg1 == 200) {
					Toast.makeText(ShareActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
				} else {
					String eMsg = "";
					if (arg1 == -101){
						eMsg = "没有授权";
					}
					Toast.makeText(ShareActivity.this, "分享失败[" + arg1 + "] " + 
							eMsg,Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	private void share_qq(){
		//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(ShareActivity.this, AppBase.QQ_APP_ID,
				AppBase.QQ_APP_KEY);
		qqSsoHandler.addToSocialSDK();  
		//mController.openShare(ShareActivity.this, false);
		//qq
		mController.postShare(ShareActivity.this,SHARE_MEDIA.QQ, 
				new SnsPostListener(){
			@Override
			public void onStart() {
				Toast.makeText(ShareActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {
				if (arg1 == 200) {
					Toast.makeText(ShareActivity.this, "分享成功.", Toast.LENGTH_SHORT).show();
				} else {
					String eMsg = "";
					if (arg1 == -101){
						eMsg = "没有授权";
					}
					Toast.makeText(ShareActivity.this, "分享失败[" + arg1 + "] " + 
							eMsg,Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/**使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
		if(ssoHandler != null){
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}