package net.xinzeling.ui;

import java.io.IOException;
import java.util.HashMap;

import net.xinzeling.MainActivity;
import net.xinzeling.MyApplication;
import net.xinzeling.base.BaseActivity;
import net.xinzeling.lib.DBHelper;
import net.xinzeling.lib.HttpCommon;
import net.xinzeling.push.PushManager;
import net.xinzeling.utils.Utils;
import net.xinzeling2.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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

				//auto login
				MyApplication.sharedPreference = SplashActivity.this.getSharedPreferences("usr", Context.MODE_APPEND);
				MyApplication.usrName = MyApplication.sharedPreference.getString("nick", "");
				MyApplication.gender = MyApplication.sharedPreference.getInt("gender", 2);
				MyApplication.pushSwitch = MyApplication.sharedPreference.getBoolean("pushSwitch", true);
				MyApplication.userToken = MyApplication.sharedPreference.getString("userToken", null);
				MyApplication.userTokenExpireDate = MyApplication.sharedPreference.getString("userTokenExpireDate", "");
				MyApplication.renewalToken = MyApplication.sharedPreference.getString("renewalToken", null);
				MyApplication.renewalTokenExpire = MyApplication.sharedPreference.getString("renewalTokenExpire", "");
				
				checkIfNeedReautoLogin(MyApplication.userTokenExpireDate, MyApplication.renewalToken);
			}
		}, 1500);

	}
	
	private void checkIfNeedReautoLogin(String userTokenExpireDate, String renewalToken) {
		if (!TextUtils.isEmpty(userTokenExpireDate) && !TextUtils.isEmpty(renewalToken)) {
			long timeNow = System.currentTimeMillis();
			if (!Utils.isInCorrectTimeSection(timeNow, timeNow, Utils.getDataByStringyyyyMMdd(userTokenExpireDate).getTime())) {
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
				jsonResp = HttpCommon.getGet(MyApplication.account01Verification, paramsData);
				int resCode = jsonResp.getInt("resCode");
				if (resCode == 0) {
					String userToken = jsonResp.getString("userToken");
					String userTokenExpire = jsonResp.getString("userTokenExpireDate");
					String renewalToken = jsonResp.getString("renewalToken");
					String renewalTokenExpire = jsonResp.getString("renewalTokenExpireDate");
					MyApplication.onSignin(userToken, userTokenExpire, renewalToken, renewalTokenExpire);
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
