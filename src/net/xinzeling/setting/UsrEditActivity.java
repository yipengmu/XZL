package net.xinzeling.setting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.xinzeling2.R.id;
import net.xinzeling2.R.layout;
import net.xinzeling.adapter.BirthAdapter;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.FontManager;
import net.xinzeling.lib.HttpCommon;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import net.xinzeling2.R;

public class UsrEditActivity extends Activity {
	private View avataChose;
	private ImageView avataView;
	private EditText inputNick,inputName,inputSurname,inputBirthday,inputPhone,inputEmail;
	private EditText inputBirthAddr,inputNowAddr,inputJob,inputHunyin;
	
	private RadioButton radioMale;
	private RadioButton radioFemale;
	private Bitmap avataImg;
	private Spinner spinnerBirth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usr_edit);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		((TextView) this.findViewById(R.id.btn_avata)).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

		avataView=(ImageView)this.findViewById(R.id.img_avata);
		avataChose = this.findViewById(R.id.layout_avata_chose);
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
		
		SharedPreferences usr = AppBase.sharedPreference;
		inputNick.setText(usr.getString("nick", ""));
		inputName.setText(usr.getString("name", ""));
		inputSurname.setText(usr.getString("firstName", ""));
		inputBirthday.setText(usr.getString("birthday", ""));
		
		inputPhone.setText(usr.getString("phone", ""));
		inputEmail.setText(usr.getString("email", ""));
		spinnerBirth.setSelection(usr.getInt("birth_spinner", 0));
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
		int gender = usr.getInt("gender", 2);
		if(gender==0){
			radioMale.setChecked(true);
		}else if(gender==1){
			radioFemale.setChecked(true);
		}
	
		inputBirthAddr = (EditText)findViewById(R.id.birth_addr);
		inputNowAddr = (EditText)findViewById(R.id.now_addr);
		inputJob = (EditText)findViewById(R.id.job);
		inputHunyin = (EditText)findViewById(R.id.hunyin);
		
		inputBirthAddr.setText(usr.getString("birthAddress", ""));
		inputNowAddr.setText(usr.getString("nowaddr", ""));
		inputJob.setText(usr.getString("career", ""));
		inputHunyin.setText(usr.getString("marriage", ""));
		
		Bitmap avata =BitmapFactory.decodeFile("/data/data/net.xinzeling/avata.png");
		ImageView avataImg = (ImageView)findViewById(R.id.img_avata);
		if(avata !=null){
			avataImg.setImageBitmap(avata);
		}
		FontManager.changeFonts((ViewGroup)AppBase.getRootView(UsrEditActivity.this), UsrEditActivity.this);
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
			this.onSubmit();
			break;
		}
	}

	private void onSubmit() {
		Editor editor = AppBase.sharedPreference.edit();
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
			AppBase.gender=0;
		}else if(radioFemale.isChecked()){
			editor.putInt("gender", 1);
			AppBase.gender=1;
		}
		
		editor.putString("birthAddress", inputBirthAddr.getText().toString());
		editor.putString("nowaddr", inputNowAddr.getText().toString());
		editor.putString("career", inputJob.getText().toString());
		editor.putString("marriage", inputHunyin.getText().toString());
		
		editor.commit();

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
			SharedPreferences usr = AppBase.sharedPreference;
			params.put("usertoken", AppBase.userToken);//授权令牌
			params.put("nickName", usr.getString("nick", ""));//昵称
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
				JSONObject jsonResp = HttpCommon.getPost(AppBase.update_usr_profile_url, params);
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
