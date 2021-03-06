package net.xinzeling.setting;

import java.util.HashMap;
import java.util.Map;

import net.xinzeling.HomeActivity;
import net.xinzeling.MainActivity;
import net.xinzeling.MyApplication;
import net.xinzeling.base.BaseActivity;
import net.xinzeling.common.account.QQAccountManager;
import net.xinzeling.common.account.SinaWeiboAccountManager;
import net.xinzeling.common.account.XZLAccountManager;
import net.xinzeling.net.http.RequestManager;
import net.xinzeling.ui.myxzl.MyXZLActivity;
import net.xinzeling.webview.WebViewActivity;
import net.xinzeling2.R;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

public class SigninActivity extends BaseActivity implements OnClickListener {

	private UMSocialService mController;
	private EditText emailInput;
	private EditText passwdInput;
	private LinearLayout ll_login_container;
	private Button btn_logout;
	private long mFirstime;
	private int FLAG_SETTING = 2;
	private int FLAG_MY = 3;
	private RadioGroup rg_signin_container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);
		
		findViews();
		initView();
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");

		checkLoginState();
	}

	private void checkLoginState() {
		if(!MyApplication.isSignin()){
			ll_login_container.setVisibility(View.VISIBLE);
			btn_logout.setVisibility(View.GONE);
		}else{
			ll_login_container.setVisibility(View.GONE);
			btn_logout.setVisibility(View.VISIBLE);
		}
	}

	private void findViews() {
		TextView forgetPasswd = (TextView) this.findViewById(R.id.btn_forget_passwd);
		forgetPasswd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		emailInput = (EditText) findViewById(R.id.email);
		passwdInput = (EditText) findViewById(R.id.password);
		ll_login_container = (LinearLayout) findViewById(R.id.ll_login_container);
		btn_logout = (Button) findViewById(R.id.btn_logout);
		rg_signin_container = (RadioGroup) findViewById(R.id.rg_signin_container);
	}

	private void initView() {
		setTitle(getAppName());
		findViewById(R.id.rb_setting).setOnClickListener(this);
		findViewById(R.id.rb_my).setOnClickListener(this);
		findViewById(R.id.btn_logout).setOnClickListener(this);
		rg_signin_container.check(R.id.rb_login);
		findViewById(R.id.btn_back).setVisibility(View.GONE);
	}

	public void onClick(View view) {

		Intent intent = new Intent();

		switch (view.getId()) {
		case R.id.btn_back:
			this.startActivity(new Intent(this, HomeActivity.class));
			break;
		case R.id.btn_forward:
			Intent regIntent = new Intent(this, WebViewActivity.class);
			regIntent.putExtra("target", "regist");
			this.startActivity(regIntent);
			break;
		case R.id.btn_forget_passwd:
			Intent passwdIntent = new Intent(this, WebViewActivity.class);
			passwdIntent.putExtra("target", "passwd");
			this.startActivity(passwdIntent);
			break;
		case R.id.btn_signin_qq:
			this.signinQQ();
			break;
		case R.id.btn_signin_weibo:
			this.signinWeibo();
			break;
		case R.id.btn_submit:
			onSubmit();
			break;
		case R.id.rb_setting:
			intent.setClass(this, SettingActivity.class);
			this.startActivityForResult(intent, FLAG_SETTING );
			break;
		case R.id.rb_my:
			gotoMyXzlAct();
			break;
		case R.id.btn_logout:
			MyApplication.logout();
			checkLoginState();
			break;

		}
	}

	private void gotoMyXzlAct() {
		if(!MyApplication.isSignin()){
			toast("请先登录后，再查看个人信息..");
		}else{
			Intent intent = new Intent();
			intent.setClass(this, MyXZLActivity.class);
			this.startActivityForResult(intent, FLAG_MY );
		}
	}

	// QQ登录
	private void signinQQ() {
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(SigninActivity.this, MyApplication.QQ_APP_ID, MyApplication.QQ_APP_KEY);
		qqSsoHandler.addToSocialSDK();
		mController.doOauthVerify(SigninActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
			@Override
			public void onStart(SHARE_MEDIA platform) {
				Toast.makeText(SigninActivity.this, "授权开始", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				Toast.makeText(SigninActivity.this, "授权错误", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				// 获取相关授权信息
				mController.getPlatformInfo(SigninActivity.this, SHARE_MEDIA.QQ, new UMDataListener() {
					@Override
					public void onStart() {
						Toast.makeText(SigninActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(int status, Map<String, Object> info) {
						if (status == 200 && info != null) {
							updateAccountInfoFromQQSso(info);
						} 
					}

					private void updateAccountInfoFromQQSso(Map<String, Object> info) {
						QQAccountManager qqIns = QQAccountManager.getInstance();
						
						qqIns.uid = (String) info.get("uid");
						qqIns.gender = (String) info.get("gender");
						qqIns.screen_name = (String) info.get("screen_name");
						qqIns.openid = (String) info.get("openid");
						qqIns.profile_image_url = (String) info.get("profile_image_url");
						qqIns.access_token = (String) info.get("access_token");
						qqIns.profile_image_url = (String) info.get("profile_image_url");
						qqIns.verified = (String) info.get("verified");
						XZLAccountManager.getInstance().setmAcoutType(2).setQqAccount(qqIns);

						new ThirdSigninTask(qqIns.uid,qqIns.screen_name,qqIns.profile_image_url,qqIns.gender,"2").execute();
					}
					
				});
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				Toast.makeText(SigninActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
			}
		});

	}

	// 微博登录
	private void signinWeibo() {
		int flag = SocializeConstants.FLAG_USER_CENTER_LOGIN_VERIFY | SocializeConstants.FLAG_USER_CENTER_HIDE_LOGININFO;
		mController.openUserCenter(this.getApplicationContext(), flag);
		// 设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.doOauthVerify(SigninActivity.this, SHARE_MEDIA.SINA, new UMAuthListener() {

			@Override
			public void onCancel(SHARE_MEDIA arg0) {
				System.out.println("cancel");
			}
			

			@Override
			public void onComplete(Bundle arg0, SHARE_MEDIA arg1) {
				System.out.println("complete");
				mController.getPlatformInfo(SigninActivity.this, SHARE_MEDIA.SINA, new UMDataListener() {
					
					@Override
					public void onStart() {
					}

					@Override
					public void onComplete(int status, Map<String, Object> info) {
						if (status == 200 && info != null) {
							updateAccountInfoFromSinaSso(info);
						} else {
						}
					}

					private void updateAccountInfoFromSinaSso(Map<String, Object> info) {
						SinaWeiboAccountManager sinaIns = SinaWeiboAccountManager.getInstance();
						sinaIns.uid = String.valueOf((int) info.get("uid"));
						sinaIns.favourites_count = (int) info.get("favourites_count");
						sinaIns.location = (String) info.get("location");
						sinaIns.description = (String) info.get("description");
						sinaIns.verified = (boolean) info.get("verified");
						sinaIns.friends_count = (int) info.get("friends_count");
						sinaIns.gender = (int) info.get("gender");
						sinaIns.screen_name = (String) info.get("screen_name");
						sinaIns.statuses_count = (int) info.get("statuses_count");
						sinaIns.followers_count = (int) info.get("followers_count");
						sinaIns.profile_image_url = (String) info.get("profile_image_url");
						sinaIns.access_token = (String) info.get("access_token");

						XZLAccountManager.getInstance().setmAcoutType(1).setSinaAccount(sinaIns);

						new ThirdSigninTask(sinaIns.access_token,sinaIns.screen_name,sinaIns.profile_image_url,String.valueOf(sinaIns.gender),"1").execute();
						
					}
				});
			}

			@Override
			public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
				System.out.println("error");
			}

			@Override
			public void onStart(SHARE_MEDIA arg0) {
				Toast.makeText(SigninActivity.this, "正在获取微博sso信息。。。", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void onSubmit() {
			String email = emailInput.getText().toString();
			String password = passwdInput.getText().toString();
			if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
				new SigninTask(email, password,0).execute();
			}
	}

	private final class SigninTask extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog progress;
		private String errorDesc;
		private HashMap<String, Object> params = new HashMap<String, Object>();
		private int signinType = 0;
		/**
		 * auth 0 api 方式登录
		 * auth 1 微博
		 * auth 2 qq
		 * */
		public SigninTask(String username, String passwd,int auth) {
			params.put("username", username);
			params.put("password", passwd);
			params.put("auth", auth);
			
			signinType = auth;
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(SigninActivity.this, null, "请求中……");
		}

		@Override
		protected Boolean doInBackground(Void... args) {
			try {
				JSONObject jsonResp = RequestManager.getGet(MyApplication.account01Verification, params);
				int resCode = jsonResp.getInt("resCode");
				if (resCode == 0) {
					String userToken = jsonResp.getString("userToken");
					String userTokenExpire = jsonResp.getString("userTokenExpireDate");
					String renewalToken = jsonResp.getString("renewalToken");
					String renewalTokenExpire = jsonResp.getString("renewalTokenExpireDate");
					MyApplication.onSignin(userToken, userTokenExpire, renewalToken, renewalTokenExpire,signinType);
					
					return true;
				} else {
					errorDesc = jsonResp.getString("resMsg");
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorDesc = "服务器错误";
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (progress != null) {
				progress.dismiss();
			}
			if (result) {
				toast("登录成功");
				Intent intent = new Intent(SigninActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), errorDesc, Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
			if (ssoHandler != null) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		rg_signin_container.check(R.id.rb_login);

	}

	// 第三方登陆，每次离开后记得清理登陆信息<退出>
	// 第三方登录 首先判断该用户是否已经注册，如果没有就注册，如果注册了就不用进行注册这一步了
	// 参数分别是：用户名 昵称 头像地址 第三方标识
	private class ThirdSigninTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;
		private int resCode;
		private String errorDesc;
		private HashMap usrinfoMap;
		private HashMap<String, Object> params = new HashMap<String, Object>();

		public ThirdSigninTask(String name, String nick_name, String profile_url, String gender, String type) {
			usrinfoMap = new HashMap<String, String>();
			usrinfoMap.put("nick", nick_name);
			if ("1".equals(type)) {
				params.put("username", name);
				params.put("nickname", nick_name);
				params.put("imageurl", profile_url);
				params.put("auth", type);
				usrinfoMap.put("gender", Integer.valueOf(gender) == 1 ? "0" : "1");
			} else if ("2".equals(type)) {
				params.put("username", "qq_" + name);
				params.put("nickname", "qq_" + nick_name);
				params.put("imageurl", profile_url);
				params.put("auth", type);
				usrinfoMap.put("gender", "男".equals(gender) ? "0" : "1");
			}
			MyApplication.editUserInfo(usrinfoMap);
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(SigninActivity.this, null, "请求中……");
		}

		@Override
		protected Void doInBackground(Void... args) {
			// 先验证是否已经注册过了，注册过了就注册过了，不用重新注册了
			try {
				JSONObject jsonResp = RequestManager.getGet(MyApplication.check_usrname_isused + params.get("username"), null);
				resCode = jsonResp.getInt("resCode");
				if (resCode == 0) {
					//第三方sns 注册接口
					jsonResp = RequestManager.getGet(MyApplication.third_regist_url, this.params);
					resCode = jsonResp.getInt("resCode");
					if (resCode != 0) {
						errorDesc = jsonResp.getString("resMsg");
					}
				} else {
					 errorDesc = jsonResp.getString("resMsg");
				}
			} catch (Exception e) {
				errorDesc = "服务器错误";
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (progress != null) {
				progress.dismiss();
			}
			if (resCode == 0|| resCode == 107) {
				new SigninTask((String)params.get("username"),"", Integer.valueOf((String)params.get("auth"))).execute();
			} else {
				Toast.makeText(getApplicationContext(), errorDesc, Toast.LENGTH_LONG).show();
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondtime = System.currentTimeMillis();
			if (secondtime - mFirstime   > 3000) {
				Toast.makeText(this, "再按一次返回键退出信则聆", Toast.LENGTH_SHORT).show();
				mFirstime = System.currentTimeMillis();
				return true;
			} else {
				finish();
				System.exit(0);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
