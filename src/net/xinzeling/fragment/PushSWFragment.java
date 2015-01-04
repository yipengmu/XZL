package net.xinzeling.fragment;

import net.xinzeling2.R;
import net.xinzeling.lib.AppBase;
import net.xinzeling.setting.UsrActivity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public  class PushSWFragment extends DialogFragment implements OnClickListener{
	private ImageView btn_yes,btn_no;
	private UsrActivity activity;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setStyle(STYLE_NO_TITLE, 0);
		activity = (UsrActivity)this.getActivity();
	}

	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {  
		View view = inflater.inflate(R.layout.fragment_pushswitch, container, false);  
		btn_yes = (ImageView)view.findViewById(R.id.btn_yes);
		btn_no = (ImageView)view.findViewById(R.id.btn_no);

		//AppBase.getPushSW()
		btn_yes.setOnClickListener(this);
		btn_no.setOnClickListener(this);
		return view;
	}

	public void onClick(View view){
		
		switch(view.getId()){
		case R.id.btn_yes:
			AppBase.setPushSW(true);
			break;
		case R.id.btn_no:
			AppBase.setPushSW(false);
			break;
		}
    	this.dismiss();
	}

}
