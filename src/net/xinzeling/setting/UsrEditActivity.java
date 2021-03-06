package net.xinzeling.setting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.xinzeling.MyApplication;
import net.xinzeling.adapter.BirthAdapter;
import net.xinzeling.common.account.QQAccountManager;
import net.xinzeling.common.account.SinaWeiboAccountManager;
import net.xinzeling.common.account.XZLAccountManager;
import net.xinzeling.net.http.RequestManager;
import net.xinzeling2.R;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class UsrEditActivity extends Activity implements OnClickListener {
	private View avataChose;
	private ImageView avataView;
	private EditText inputNick,inputName,inputSurname,inputBirthday,inputPhone,inputEmail;
	private EditText inputBirthAddr,inputNowAddr,inputJob;
//	private EditText inputHunyin;
	

	private RadioButton radioWeihun;
	private RadioButton radiohun;
	
	private RadioButton radioMale;
	private RadioButton radioFemale;
	private Bitmap avataImg;
	private Spinner spinnerBirth;
	private int mAccountType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usr_edit);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		((TextView) this.findViewById(R.id.btn_avata)).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

		avataView=(ImageView)this.findViewById(R.id.img_avata);
		avataChose = this.findViewById(R.id.layout_avata_chose);
		avataChose.setOnClickListener(this);
		inputNick =(EditText)findViewById(R.id.input_nick);
		inputName =(EditText)findViewById(R.id.input_name);
		inputSurname=(EditText)findViewById(R.id.input_firstName);
		inputBirthday =(EditText)findViewById(R.id.input_birthday);
	
		inputPhone = (EditText)findViewById(R.id.input_phone);
		inputEmail = (EditText)findViewById(R.id.input_email);
		radioMale = (RadioButton)findViewById(R.id.radio_male);
		radioFemale =(RadioButton)findViewById(R.id.radio_female);
		spinnerBirth = (Spinner)findViewById(R.id.spinner_birth);
		spinnerBirth.setAdapter(new BirthAdapter(this));
		
		SharedPreferences usr = MyApplication.sharedPreference;
		inputNick.setText(usr.getString("nick", ""));
		inputName.setText(usr.getString("name", ""));
		inputSurname.setText(usr.getString("firstName", ""));
		inputBirthday.setText(usr.getString("birthday", ""));

		inputPhone.setText(usr.getString("phone", ""));
		inputEmail.setText(usr.getString("email", ""));
		spinnerBirth.setSelection(usr.getInt("birth_spinner", 0));
		Picasso.with(this).load(usr.getString("faceLogo", "http://tp3.sinaimg.cn/5319290870/180/40069366120/1")).into((ImageView)findViewById(R.id.img_avata));
	
		int gender = usr.getInt("gender", 2);
		
		spinnerBirth.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				spinnerBirth.setSelection(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		if(gender==0){
			radioMale.setChecked(true);
		}else if(gender==1){
			radioFemale.setChecked(true);
		}
	
		inputBirthAddr = (EditText)findViewById(R.id.birth_addr);
		inputNowAddr = (EditText)findViewById(R.id.now_addr);
		inputJob = (EditText)findViewById(R.id.job);
//		inputHunyin = (EditText)findViewById(R.id.hunyin);
		
		inputBirthAddr.setText(usr.getString("birthAddress", ""));
		inputNowAddr.setText(usr.getString("nowaddr", ""));
		inputJob.setText(usr.getString("career", ""));
//		inputHunyin.setText(usr.getString("marriage", ""));
		
//		Bitmap avata =BitmapFactory.decodeFile("/data/data/net.xinzeling/avata.png");
//		ImageView avataImg = (ImageView)findViewById(R.id.img_avata);
//		if(avata !=null){
//			avataImg.setImageBitmap(avata);
//			Picasso.with(this).load(usr.getString("faceLogo", "http://tp3.sinaimg.cn/5319290870/180/40069366120/1")).into(avataImg);
//		}
		initViewByData();
	}

	private void initViewByData() {
		mAccountType = XZLAccountManager.getInstance().getmAcoutType();
		Log.d("getmAcoutType : ","getmAcoutType " + mAccountType);
		
		SinaWeiboAccountManager sinaAc = XZLAccountManager.getInstance().getSinaAccount();
		QQAccountManager qqAc = XZLAccountManager.getInstance().getQqAccount();

		Log.d("getmAcoutType : ","sinaAc " + sinaAc + "  qqAc "+qqAc);

	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_avata:
			avataChose.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_avata_camera:// 相机
			Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(camera, R.id.btn_avata_camera);
			break;
		case R.id.btn_avata_album:// 相册
			Intent photoIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(photoIntent, R.id.btn_avata_album);
			break;
		case R.id.btn_avata_cancel:
			avataChose.setVisibility(View.GONE);
			break;
		case R.id.btn_forward:
			onSubmit();
			break;
		case R.id.layout_avata_chose:
			avataChose.setVisibility(View.GONE);
			break;
		}
	}

	private void onSubmit() {
		Editor editor = MyApplication.sharedPreference.edit();
		editor.putString("nick", inputNick.getText().toString());
		editor.putString("name", inputName.getText().toString());
		editor.putString("firstName", inputSurname.getText().toString());
		editor.putString("birthday", inputBirthday.getText().toString());
		int birth=spinnerBirth.getSelectedItemPosition();
		editor.putString("birthAddress", BirthAdapter.timeSet[birth]);
		editor.putInt("birth_spinner", birth);
		
		editor.putString("phone", inputPhone.getText().toString());
		editor.putString("email", inputEmail.getText().toString());
		
		if(radioMale.isChecked()){
			editor.putInt("gender", 0);
			MyApplication.mCommonAccountManager.gender=0;
		}else if(radioFemale.isChecked()){
			editor.putInt("gender", 1);
			MyApplication.mCommonAccountManager.gender=1;
		}
		
		editor.putString("birthAddress", inputBirthAddr.getText().toString());
		editor.putString("nowaddr", inputNowAddr.getText().toString());
		editor.putString("career", inputJob.getText().toString());
//		editor.putString("marriage", inputHunyin.getText().toString());
		
		editor.apply();

		if(avataImg !=null){
			try {
				 File file = new File("/data/data/net.xinzeling/", "avata.png");
				 FileOutputStream os = new FileOutputStream(file);
				 avataImg.compress(Bitmap.CompressFormat.PNG, 100, os);
				 os.close();
			} catch (Exception e) {
			}
		}
		//保存到服务器
		new UsrTask(UsrEditActivity.this).execute();
		Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == R.id.btn_avata_album) {
			avataChose.setVisibility(View.GONE);
			 Uri originalUri = data.getData();
	         try {
				avataImg = MediaStore.Images.Media.getBitmap(getContentResolver(), originalUri);
				avataView.setImageBitmap(avataImg);
			} catch (IOException e) {
			}  
		} else if (resultCode == RESULT_OK && requestCode ==R.id.btn_avata_camera) {
			avataChose.setVisibility(View.GONE);
			avataImg = (Bitmap) data.getExtras().get("data");
			avataView.setImageBitmap(avataImg);
		}
	}
	
	//更新账户的信息
	private class UsrTask extends AsyncTask<Void,Void,Boolean>{
		private ProgressDialog progress;
		private String resMsg;
		private HashMap<String,Object> params = new HashMap<String,Object>();
		private Context context;
		
		public UsrTask(Context context){
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(context, null, "请求中……");
		}
		
		@Override
		protected Boolean doInBackground(Void... args) {
			SharedPreferences usr = MyApplication.sharedPreference;
			params.put("usertoken", MyApplication.mCommonAccountManager.userToken);//授权令牌
			params.put("NickName", usr.getString("nick", ""));//昵称
			params.put("firstName", usr.getString("firstName", ""));//姓
			params.put("secondName", usr.getString("name", ""));//名
			params.put("gender", usr.getInt("gender", 0));//性别
			params.put("birthday", usr.getString("birthday", ""));//生日
			params.put("birthTime", usr.getString("birthTime", ""));//时辰选项	0子时~11亥时
			params.put("phone", usr.getString("phone", ""));//电话
			params.put("email", usr.getString("email", ""));//邮箱
			params.put("birthAddress", usr.getString("birthAddress", ""));//出生地址
			params.put("address", usr.getString("nowaddr", ""));//地址
			params.put("career", usr.getString("career", ""));//事业
			params.put("marriage", 0);//婚姻  0未婚1结婚
			
			try {
				JSONObject jsonResp = RequestManager.getPost(MyApplication.update_usr_profile_url, params);
				int resCode = jsonResp.getInt("resCode");
				if(resCode ==0){
					return true;
				}else{
					resMsg = jsonResp.getString("resMsg");
				}
			} catch (Exception e) {
				resMsg ="服务器错误";
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (progress != null) {
				progress.dismiss();
			}
			if(result){
				finish();
			}else{
				Toast.makeText(context, resMsg, Toast.LENGTH_LONG).show();
			}
		}
	}
}
