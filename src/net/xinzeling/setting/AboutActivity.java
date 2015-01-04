package net.xinzeling.setting;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.FontManager;
import net.xinzeling2.R;
import net.xinzeling2.R.id;
import net.xinzeling2.R.layout;

public class AboutActivity extends Activity {

	FragmentManager fManager =this.getFragmentManager();
	RadioButton radioIntro;
	
	private InstructionFragment frag_instruct;
	private IntroFragment frag_intro;
	private UpdateFragment frag_update;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		((TextView)findViewById(R.id.txt_title)).setText("关于我们");
		
		frag_intro = new IntroFragment();
		frag_instruct = new InstructionFragment(); 
		frag_update = new UpdateFragment();

		FontManager.changeFonts((ViewGroup)AppBase.getRootView(AboutActivity.this), AboutActivity.this);

		radioIntro = (RadioButton) this.findViewById(R.id.radio_intro);
		this.onClick(radioIntro);
	}
	
	public void onClick(View view){
		switch(view.getId()){
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.radio_intro:
			fManager.beginTransaction().replace(R.id.fragment_content, frag_intro).commit();
			break;
		case R.id.radio_instruction:
			fManager.beginTransaction().replace(R.id.fragment_content, frag_instruct).commit();
			break;
		case R.id.radio_update:
			fManager.beginTransaction().replace(R.id.fragment_content, frag_update).commit();
			break;
		case R.id.radio_dafen:
			break;
		}
	}
	
	public static class InstructionFragment extends Fragment{
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.fragment_gua_instruction, container, false);
			return view;
		}
	}
	
	public static class IntroFragment extends Fragment{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.fragment_about_intro, container, false);
			return view;
		}
	}
	
	public static class UpdateFragment extends Fragment{
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.fragment_about_update, container, false);
			return view;
		}
	}
	
	
	
}
