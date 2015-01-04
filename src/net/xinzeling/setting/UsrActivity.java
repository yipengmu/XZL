package net.xinzeling.setting;

import net.xinzeling.HomeActivity.NavRunner;
import net.xinzeling2.R.id;
import net.xinzeling2.R.layout;
import net.xinzeling.fragment.FeedFragment;
import net.xinzeling.fragment.PushSWFragment;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.BlurMaskTask;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import net.xinzeling2.R;

public class UsrActivity extends Activity implements OnClickListener {
	private TextView btn_about,btn_pushswitch;
	
	private ImageView avataImg ;
	private TextView inputNick ;
	private TextView inputName;
	private TextView inputGender;
	private TextView inputBirth;
	private TextView inputPhone;
	private TextView inputEmail;
	
	private TextView inputBirthAddr,inputNowAddr,inputJob,inputHunyin;
	
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usr);
		
		btn_about = (TextView)findViewById(R.id.btn_About);
		btn_pushswitch = (TextView)findViewById(R.id.btn_PushSwitch);
		btn_about.setOnClickListener(this);
		btn_pushswitch.setOnClickListener(this);
		
		avataImg = (ImageView)findViewById(R.id.img_avata);
		inputNick=(TextView)findViewById(R.id.usr_nick);
		inputName=(TextView)findViewById(R.id.usr_name);
		inputGender=(TextView)findViewById(R.id.usr_gender);
		inputBirth=(TextView)findViewById(R.id.usr_birthtime);
		inputPhone=(TextView)findViewById(R.id.usr_phone);
		inputEmail=(TextView)findViewById(R.id.usr_email);
		
		inputBirthAddr = (TextView)findViewById(R.id.usr_birth_addr);
		inputNowAddr = (TextView)findViewById(R.id.usr_now_addr);
		inputJob = (TextView)findViewById(R.id.usr_job);
		inputHunyin = (TextView)findViewById(R.id.usr_hunyin);
	}
	
	protected void onStart(){
		super.onStart();
		SharedPreferences usr = AppBase.sharedPreference;
		inputNick.setText(usr.getString("nick", ""));
		inputName.setText(usr.getString("firstName", "")+" "+usr.getString("name", ""));
		inputBirth.setText(usr.getString("birthday", "")+usr.getString("birthAddress", ""));
		inputPhone.setText(usr.getString("phone", ""));
		inputEmail.setText(usr.getString("email", ""));
	
		inputBirthAddr.setText(usr.getString("birthAddress", ""));
		inputNowAddr.setText(usr.getString("nowaddr", ""));
		inputJob.setText(usr.getString("career", ""));
		inputHunyin.setText(usr.getString("marriage", ""));
		
		int gender = usr.getInt("gender", 0);
		if(gender==0){
			inputGender.setText("男");
		}else if(gender==1){
			inputGender.setText("女");
		}
		
		Bitmap avata =BitmapFactory.decodeFile("/data/data/net.xinzeling/avata.png");
		if(avata !=null){
			avataImg.setImageBitmap(avata);
		}
	}

	public void onClick(View view) {
		switch(view.getId()){
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_forward:
			this.startActivity(new Intent(this, UsrEditActivity.class));
			break;
		case R.id.btn_About:
			intent = new Intent(UsrActivity.this,AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_PushSwitch:
//			intent = new Intent(UsrActivity.this,PushSwitchActivity.class);
//			this.startActivityForResult(intent, REQUEST_PUSHSWITCH_CODE);
			new PushSWFragment().show(this.getFragmentManager(),"");
//			new BlurMaskTask(this, findViewById(R.id.user_main_frame),findViewById(R.id.frame_nav), 
//					new NavRunner(view.getId())).execute();
			break;
		}
	}
	
}
