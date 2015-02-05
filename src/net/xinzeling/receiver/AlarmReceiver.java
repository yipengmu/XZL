package net.xinzeling.receiver;

import java.io.IOException;

import net.xinzeling.ui.SplashActivity;
import net.xinzeling.utils.Utils;
import net.xinzeling2.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;

public class AlarmReceiver extends BroadcastReceiver {
	Context mContext;

	@Override
	public void onReceive(Context c, Intent arg1) {
		mContext = c;
		String action = arg1.getAction();
		long alarmId = arg1.getIntExtra("alarm_id", 0);

		if ("xinzeling_alarm".equals(action)) {
			// 调用提醒信息内容显示＋闹铃＋振动
			// 暂时只进行震动提示

			// AudioManager audiomanage =
			// (AudioManager)arg0.getSystemService(Context.AUDIO_SERVICE);
			// int mode = audiomanage.getRingerMode();
			// //RINGER_MODE_NORMAL（普通）、RINGER_MODE_SILENT（静音）、RINGER_MODE_VIBRATE（震动）
			// if((mode & AudioManager.RINGER_MODE_NORMAL)>0){
			// //普通
			// Uri alert =
			// RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			// ring(arg0,alert,audiomanage);
			// }
			// if((mode & AudioManager.RINGER_MODE_SILENT)>0){
			// //静音
			// }
			// if((mode & AudioManager.RINGER_MODE_VIBRATE)>0){
			// //震动
			// vibrator(arg0);
			// }
			vibrator(c);
			Utils.showNotifycation(c);
		}
	}



	private void vibrator(Context context) {
		Vibrator mVibrator01; // 声明一个振动器对象
		mVibrator01 = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		mVibrator01.vibrate(new long[] { 100, 100, 100, 100, 500, 500 }, -1);
	}

	private void ring(Context context, Uri alert, AudioManager audiomanage) {
		MediaPlayer mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, alert);
			if (audiomanage.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.setLooping(false);
				mMediaPlayer.prepare();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mMediaPlayer.start();
	}
}
