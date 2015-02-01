package net.xinzeling.ui;

import java.io.IOException;
import java.util.HashMap;

import net.xinzeling.MainActivity;
import net.xinzeling.MyApplication;
import net.xinzeling.base.BaseActivity;
import net.xinzeling.common.database.DBHelper;
import net.xinzeling.net.http.RequestManager;
import net.xinzeling.push.PushManager;
import net.xinzeling.utils.Utils;
import net.xinzeling2.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);

		new Handler(getMainLooper()).postDelayed(new Runnable() {

			@Override
			public void run() {
				// push
				PushManager.getInstance().init();
				
				//db
				MyApplication.dbHelper = new DBHelper(SplashActivity.this, "xinzeling.db");
				MyApplication.dbh = MyApplication.dbHelper.getWritableDatabase();
			
				MyApplication.pushSwitch = MyApplication.sharedPreference.getBoolean("pushSwitch", true);
				
				//auto login
				MyApplication.mCommonAccountManager.usrName = MyApplication.sharedPreference.getString("nick", "");
				MyApplication.mCommonAccountManager.gender = MyApplication.sharedPreference.getInt("gender", 2);
				MyApplication.mCommonAccountManager.userToken = MyApplication.sharedPreference.getString("userToken", null);
				MyApplication.mCommonAccountManager.userTokenExpireDate = MyApplication.sharedPreference.getString("userTokenExpireDate", "");
				MyApplication.mCommonAccountManager.renewalToken = MyApplication.sharedPreference.getString("renewalToken", null);
				MyApplication.mCommonAccountManager.renewalTokenExpire = MyApplication.sharedPreference.getString("renewalTokenExpire", "");
				
				checkIfNeedReautoLogin(MyApplication.mCommonAccountManager.userTokenExpireDate, MyApplication.mCommonAccountManager.renewalToken);
			}
		}, 1500);

	}
	
	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
	
	private void checkIfNeedReautoLogin(String userTokenExpireDate, String renewalToken) {
		if (!TextUtils.isEmpty(userTokenExpireDate) && !TextUtils.isEmpty(renewalToken)) {
			long timeNow = System.currentTimeMillis();
			if (!Utils.isInCorrectTimeSection(timeNow, timeNow, Utils.getDateByStringyyyyMMdd(userTokenExpireDate).getTime())) {
				new AutoLoginTask(renewalToken).equals(null);
			}
		}else{
			// 非首次登录
			jumpToMainTabActivity();
		}

	}

	public class AutoLoginTask extends AsyncTask {
		private HashMap<String, Object> paramsData = new HashMap<String, Object>();

		public AutoLoginTask(String renewalTokenString) {
			paramsData.put("renewalToken", renewalTokenString);
		}

		@Override
		protected Object doInBackground(Object... params) {
			JSONObject jsonResp;
			try {
				jsonResp = RequestManager.getGet(MyApplication.account01Verification, paramsData);
				int resCode = jsonResp.getInt("resCode");
				if (resCode == 0) {
					String userToken = jsonResp.getString("userToken");
					String userTokenExpire = jsonResp.getString("userTokenExpireDate");
					String renewalToken = jsonResp.getString("renewalToken");
					String renewalTokenExpire = jsonResp.getString("renewalTokenExpireDate");
					MyApplication.onSignin(userToken, userTokenExpire, renewalToken, renewalTokenExpire,0);
					return true;
				} else {
					// 自动登录失败
					jsonResp.getString("resMsg");
				}
			} catch (IOException | JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			jumpToMainTabActivity();
		}

	}

	public void jumpToMainTabActivity() {

		startActivity(new Intent(this, MainActivity.class));

		finish();
	}
}
