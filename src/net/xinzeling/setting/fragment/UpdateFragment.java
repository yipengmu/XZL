package net.xinzeling.setting.fragment;

import java.util.HashMap;

import net.xinzeling.MyApplication;
import net.xinzeling.lib.HttpCommon;
import net.xinzeling.utils.Utils;
import net.xinzeling2.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateFragment extends Fragment implements OnClickListener {

	private Button btn_check_msg;
	private TextView tv_app_update_info_readme;
	private TextView txt_version;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about_update, container, false);
		btn_check_msg = (Button) view.findViewById(R.id.btn_check_msg);
		tv_app_update_info_readme = (TextView) view.findViewById(R.id.tv_app_update_info_readme);
		txt_version = (TextView) view.findViewById(R.id.txt_version);

		btn_check_msg.setOnClickListener(this);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		checkVersionUpdate();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_check_msg:
			getVersionUpdate();
			break;

		default:
			break;
		}

	}

	private void getVersionUpdate() {
		MyApplication.mAppUpdateBean.downloadUrl = "http://gdown.baidu.com/data/wisegame/0ef8e7b345561cbd/shoujibaidu_16786444.apk";
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("\n是否更新最新版本?\n").setCancelable(false).setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// 成功 ，启动下载
				Utils.downloadApk(MyApplication.mAppUpdateBean.downloadUrl);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}

	private void checkVersionUpdate() {
		new CheckVersionUpdateTask().execute();
	}

	public class CheckVersionUpdateTask extends AsyncTask<Void, Void, Boolean> {

		public JSONObject jsonResp;
		private ProgressDialog progress;
		String resMsg = "";
		private HashMap<String, Object> params = new HashMap<String, Object>();

		public CheckVersionUpdateTask() {
			params.put("type", 0);
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(getActivity(), null, "请求中……");
		}

		@Override
		protected Boolean doInBackground(Void... args) {
			try {
				jsonResp = HttpCommon.getGet(MyApplication.check_app_update, params);

				int resCode = jsonResp.getInt("resCode");
				if (resCode != 0) {
					resMsg = jsonResp.getString("resMsg");
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				resMsg = "请求失败，请重试";
				return false;
			}
			MyApplication.mAppUpdateBean.parseJson(jsonResp);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (progress != null) {
				progress.dismiss();
			}
			
			if (!result) {
				// 失败
				Toast.makeText(getActivity(), resMsg, Toast.LENGTH_LONG).show();
			} else {
				progress.dismiss();
				if(MyApplication.mAppUpdateBean.version > Utils.getAppVersion(getActivity())){
					btn_check_msg.setText("已检测到最新版本，点击更新");
					tv_app_update_info_readme.setText(jsonResp.optString("description"));
					txt_version.setText("xzl " + jsonResp.optString("version"));
				}else{
					
				}
			}
		}

	}

}