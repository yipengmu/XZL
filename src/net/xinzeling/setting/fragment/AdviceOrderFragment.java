package net.xinzeling.setting.fragment;

import java.util.HashMap;

import net.xinzeling.MyApplication;
import net.xinzeling.base.BaseFragment;
import net.xinzeling.net.http.RequestManager;
import net.xinzeling.widget.LineEditText;
import net.xinzeling2.R;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**意见反馈页*/
public class AdviceOrderFragment extends BaseFragment{
	
	private View view;
	private Button btn_submit;
	private net.xinzeling.widget.LineEditText mLineEditText;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view = inflater.inflate(R.layout.fragment_advice_order, container, false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	
		btn_submit = (Button) view.findViewById(R.id.btn_submit);
		mLineEditText = (LineEditText) view.findViewById(R.id.input_advice_order_content);
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(mLineEditText.getText())){
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(getActivity(), "请填写意见反馈后再提交", Toast.LENGTH_SHORT).show();
						}
					});
				}else{
					
					new AdviceOrderTask(mLineEditText.getText().toString()).execute();
				}
				
			}
		});
	}
	
	public class AdviceOrderTask extends AsyncTask<Void, Void, Boolean> {

		public JSONObject jsonResp;
		private ProgressDialog progress;
		String resMsg = "";
		private HashMap<String, Object> params = new HashMap<String, Object>();

		public AdviceOrderTask(String content) {
			params.put("content", content);
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(getActivity(), null, "请求中……");
		}

		@Override
		protected Boolean doInBackground(Void... args) {
			try {
				jsonResp = RequestManager.getGet(MyApplication.check_advice_order, params);

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
				Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_LONG).show();
				mLineEditText.setText("");
			}
		}

	}
}