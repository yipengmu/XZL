package net.xinzeling.setting;

import net.xinzeling.base.BaseActivity;
import net.xinzeling.setting.fragment.AdviceOrderFragment;
import net.xinzeling.setting.fragment.InstructionFragment;
import net.xinzeling.setting.fragment.IntroFragment;
import net.xinzeling.setting.fragment.PushConfigFragment;
import net.xinzeling.setting.fragment.UpdateFragment;
import net.xinzeling2.R;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class SettingActivity extends BaseActivity {

	FragmentManager fManager =this.getFragmentManager();
	RadioButton radioIntro;

	private AdviceOrderFragment frag_advice_order;
	private PushConfigFragment frag_push_config;
	private InstructionFragment frag_instruct;
	private IntroFragment frag_intro;
	private UpdateFragment frag_update;
	
	private View rgs_about_us_container;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		
		setTitle(getResources().getString(R.string.setting));

		frag_advice_order = new AdviceOrderFragment();
		frag_push_config = new PushConfigFragment();
		frag_intro = new IntroFragment();
		frag_instruct = new InstructionFragment(); 
		frag_update = new UpdateFragment();
		rgs_about_us_container = findViewById(R.id.rgs_about_us_container);
		radioIntro = (RadioButton) this.findViewById(R.id.radio_intro);
		this.onClick(radioIntro);
		
		findViewById(R.id.rb_about_us).setOnClickListener(this);
		findViewById(R.id.rb_advice_order).setOnClickListener(this);
		findViewById(R.id.rb_push_config).setOnClickListener(this);
		
	}
	
	public void onClick(View view){
		switch(view.getId()){
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.rb_about_us:
			rgs_about_us_container.setVisibility(View.VISIBLE);
			fManager.beginTransaction().replace(R.id.fragment_content, frag_intro).commit();
			break;
		case R.id.rb_advice_order:
			rgs_about_us_container.setVisibility(View.GONE);
			fManager.beginTransaction().replace(R.id.fragment_content, frag_advice_order).commit();
			break;
		case R.id.rb_push_config:
			rgs_about_us_container.setVisibility(View.GONE);
			fManager.beginTransaction().replace(R.id.fragment_content, frag_push_config).commit();
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
	
	
	
	
	
}
