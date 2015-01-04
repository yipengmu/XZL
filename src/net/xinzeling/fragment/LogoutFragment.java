package net.xinzeling.fragment;

import net.xinzeling2.R;
import net.xinzeling.lib.AppBase;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public  class LogoutFragment extends DialogFragment implements OnClickListener{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setStyle(STYLE_NO_TITLE, 0);
	}
	
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {  
    	 View view = inflater.inflate(R.layout.fragment_logout, container, false);  
    	 view.findViewById(R.id.btn_ok).setOnClickListener(this);
    	 view.findViewById(R.id.btn_cancel).setOnClickListener(this);
    	 if(true){
    		 ((ImageView)view.findViewById(R.id.header_left_btn)).setImageResource(R.drawable.tab_usr_s);
    	 }
    	 ((TextView)view.findViewById(R.id.header_main_title)).setText("确定要退出当前“"+AppBase.usrName+"”的账号？");;
    	 return view;
    }
    
    public void onClick(View view){
    	if(view.getId()==R.id.btn_ok){
    		AppBase.logout();
    	}
    	this.dismiss();
    }
 
}
