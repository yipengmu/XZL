package net.xinzeling.setting;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.xinzeling.HomeActivity;
import net.xinzeling.WebViewActivity;
import net.xinzeling.base.BaseActivity;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.HttpCommon;
import net.xinzeling2.R;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class SigninActivity extends BaseActivity implements OnClickListener {

	private SigninTask signinTask = null;
	private ThirdSigninTask thirdsignintask = null;
	private UMSocialService mController;
	private EditText emailInput;
	private EditText passwdInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);
		TextView forgetPasswd = (TextView) this.findViewById(R.id.btn_forget_passwd);
		forgetPasswd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		emailInput = (EditText) findViewById(R.id.email);
		passwdInput = (EditText) findViewById(R.id.password);
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");

		initView();

	}

	private void initView() {
		setTitle(getAppName());
		findViewById(R.id.rb_setting).setOnClickListener(this);
		findViewById(R.id.rb_my).setOnClickListener(this);
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
		// case R.id.btn_signin_weixin:
		// this.signinWeixin();
		// break;
		case R.id.btn_submit:
			onSubmit();
			break;
		case R.id.rb_setting:
			intent.setClass(this, SettingActivity.class);
			this.startActivity(intent);
			break;
		case R.id.rb_my:
			intent.setClass(this, UsrEditActivity.class);
			this.startActivity(intent);
			break;
			
		}
	}

	// QQ登录
	private void signinQQ() {
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(SigninActivity.this, AppBase.QQ_APP_ID, AppBase.QQ_APP_KEY);
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
				Toast.makeText(SigninActivity.this, "授权完成", Toast.LENGTH_SHORT).show();
				// 获取相关授权信息
				mController.getPlatformInfo(SigninActivity.this, SHARE_MEDIA.QQ, new UMDataListener() {
					@Override
					public void onStart() {
						Toast.makeText(SigninActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(int status, Map<String, Object> info) {
						if (status == 200 && info != null) {
							StringBuilder sb = new StringBuilder();
							Set<String> keys = info.keySet();
							for (String key : keys) {
								sb.append(key + "=" + info.get(key).toString() + "\r\n");
							}
							Log.d("TestData", sb.toString());
						} else {
							Log.d("TestData", "发生错误：" + status);
						}
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
				// TODO Auto-generated method stub
				System.out.println("cancel");
			}

			@Override
			public void onComplete(Bundle arg0, SHARE_MEDIA arg1) {
				// TODO Auto-generated method stub
				System.out.println("complete");
				mController.getPlatformInfo(SigninActivity.this, SHARE_MEDIA.SINA, new UMDataListener() {
					@Override
					public void onStart() {
						Toast.makeText(SigninActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(int status, Map<String, Object> info) {
						if (status == 200 && info != null) {
							StringBuilder sb = new StringBuilder();
							Set<String> keys = info.keySet();
							for (String key : keys) {
								sb.append(key + "=" + info.get(key).toString() + "\r\n");
							}
							Log.d("TestData", sb.toString());
						} else {
							Log.d("TestData", "发生错误：" + status);
						}
					}
				});
			}

			@Override
			public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
				// TODO Auto-generated method stub
				System.out.println("error");
			}

			@Override
			public void onStart(SHARE_MEDIA arg0) {
				// TODO Auto-generated method stub
				System.out.println("onstart");
			}
		});
	}

	// 微信登录
	private void signinWeixin() {
		UMWXHandler wxHandler = new UMWXHandler(SigninActivity.this, AppBase.WEIXIN_APP_ID, AppBase.WEIXIN_APP_KEY);
		wxHandler.addToSocialSDK();

		int flag = SocializeConstants.FLAG_USER_CENTER_LOGIN_VERIFY | SocializeConstants.FLAG_USER_CENTER_HIDE_LOGININFO;
		mController.openUserCenter(this.getApplicationContext(), flag);
		// 设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.doOauthVerify(SigninActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
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
				Toast.makeText(SigninActivity.this, "授权完成", Toast.LENGTH_SHORT).show();
				// 获取相关授权信息
				mController.getPlatformInfo(SigninActivity.this, SHARE_MEDIA.WEIXIN, new UMDataListener() {
					@Override
					public void onStart() {
						Toast.makeText(SigninActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(int status, Map<String, Object> info) {
						if (status == 200 && info != null) {
							StringBuilder sb = new StringBuilder();
							Set<String> keys = info.keySet();
							for (String key : keys) {
								sb.append(key + "=" + info.get(key).toString() + "\r\n");
							}
							Log.d("TestData", sb.toString());
						} else {
							Log.d("TestData", "发生错误：" + status);
						}
					}
				});
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				Toast.makeText(SigninActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void onSubmit() {
		if (signinTask == null) {
			String email = emailInput.getText().toString();
			String password = passwdInput.getText().toString();
			if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
				signinTask = new SigninTask(email, password);
				signinTask.execute();
			}
		}
	}

	private final class SigninTask extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog progress;
		private String errorDesc;
		private HashMap<String, Object> params = new HashMap<String, Object>();

		public SigninTask(String email, String passwd) {
			params.put("username", email);
			params.put("password", passwd);
			params.put("auth", 0);
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(SigninActivity.this, null, "请求中……");
		}

		@Override
		protected Boolean doInBackground(Void... args) {
			try {
				JSONObject jsonResp = HttpCommon.getGet(AppBase.account01Verification, params);
				int resCode = jsonResp.getInt("resCode");
				if (resCode == 0) {
					String userToken = jsonResp.getString("userToken");
					String userTokenExpire = jsonResp.getString("userTokenExpireDate");
					String renewalToken = jsonResp.getString("renewalToken");
					String renewalTokenExpire = jsonResp.getString("renewalTokenExpireDate");
					AppBase.onSignin(userToken, userTokenExpire, renewalToken, renewalTokenExpire);
					SigninActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							toast("登录成功");
						}
					});
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
				Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), errorDesc, Toast.LENGTH_LONG).show();
			}
			signinTask = null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		try {
			UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
			if (ssoHandler != null) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			}
		} catch (Exception e) {
		}
		
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
				params.put("username", "weibo_" + name);
				params.put("nickname", "weibo_" + nick_name);
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
			AppBase.editUserInfo(usrinfoMap);
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(SigninActivity.this, null, "请求中……");
		}

		@Override
		protected Void doInBackground(Void... args) {
			// 先验证是否已经注册过了，注册过了就注册过了，不用重新注册了

			try {
				JSONObject jsonResp = HttpCommon.getGet(AppBase.check_usrname_isused + params.get("username"), null);
				resCode = jsonResp.getInt("resCode");
				if (resCode == 0) {
					jsonResp = HttpCommon.getGet(AppBase.third_regist_url, this.params);
					resCode = jsonResp.getInt("resCode");
					if (resCode == 0) {
						// AppBase.onSignin(userToken,userTokenExpire,renewalToken,renewalTokenExpire);
					} else {
						errorDesc = jsonResp.getString("resMsg");
					}
				} else {
					// errorDesc = jsonResp.getString("resMsg");
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
			if (resCode == 0) {
				Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), errorDesc, Toast.LENGTH_LONG).show();
			}
			thirdsignintask = null;
		}

	}

}
