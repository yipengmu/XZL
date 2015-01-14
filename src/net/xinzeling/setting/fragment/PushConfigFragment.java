package net.xinzeling.setting.fragment;

import net.xinzeling.push.PushManager;
import net.xinzeling2.R;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/** push 开关控制页 */
public class PushConfigFragment extends Fragment {

	public boolean push_Open;
	private View view;
	private Editor editor;
	public static SharedPreferences sharedPreference;
	private RadioButton radio_push_open;
	private RadioButton radio_push_close;
	private RadioGroup mRadioGroup;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_push_config, container, false);
		sharedPreference = getActivity().getSharedPreferences("usr", Context.MODE_PRIVATE);

		push_Open = sharedPreference.getBoolean(PushManager.pushSwitcher, true);
		editor = sharedPreference.edit();

		radio_push_open = (RadioButton) view.findViewById(R.id.radio_push_open);
		radio_push_close = (RadioButton) view.findViewById(R.id.radio_push_close);
		
		mRadioGroup = (RadioGroup) view.findViewById(R.id.radiogroups_push);

		
		updateRadioSatate();

		// 绑定一个匿名监听器
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();

				if (radioButtonId == R.id.radio_push_open) {
					push_Open = true;
					editor.putBoolean(PushManager.pushSwitcher, true);
					editor.apply();
				} else if (radioButtonId == R.id.radio_push_close) {
					push_Open = false;
					editor.putBoolean(PushManager.pushSwitcher, false);
					editor.apply();
				}
				updateRadioSatate();
			}
		});
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	private void updateRadioSatate() {
		if (push_Open) {
			mRadioGroup.check(radio_push_open.getId()); 
		} else {
			mRadioGroup.check(radio_push_close.getId()); 
		}
	}
}
