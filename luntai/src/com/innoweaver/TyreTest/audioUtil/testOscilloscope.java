package com.innoweaver.TyreTest.audioUtil;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.innoweaver.TyreTest.R;

public class testOscilloscope extends Activity {
	/** Called when the activity is first created. */
	Button btnStart, btnExit;
	SurfaceView sfv;

	ClsOscilloscope clsOscilloscope = new ClsOscilloscope();

	static final int frequency = 8000;// 分辨率
	static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;

	static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	// static final int audioEncoding = AudioFormat.ENCODING_PCM_8BIT;

	public SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	int recBufSize;// 录音最小buffer大小
	AudioRecord audioRecord;
	Paint mPaint;

	AudioTrackManager audio;
	boolean isPlaySound = true;
	boolean flag;
	Thread thread;
	AudioManager am;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		// 录音组件
		recBufSize = AudioRecord.getMinBufferSize(frequency,
				channelConfiguration, audioEncoding);
		Log.i("xiu", "recBufSize:" + recBufSize + "");
		/**
		 * ��ʼ��¼���Ķ���
		 */
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
				channelConfiguration, audioEncoding, recBufSize * 10);
		// 按键
		btnStart = (Button) this.findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new ClickEvent());
		btnExit = (Button) this.findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new ClickEvent());
		// 画板和画笔
		sfv = (SurfaceView) this.findViewById(R.id.SurfaceView01);
		sfv.setOnTouchListener(new TouchEvent());
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);// ����Ϊ��ɫ
		mPaint.setStrokeWidth(2);// ���û��ʴ�ϸ
		// 示波器类库
		clsOscilloscope.initOscilloscope(sfv.getHeight() / 2);

		// 缩放控件，X轴的数据缩小的比率高些
		/**
		 * instantiate an AudioTrackManger object
		 */
		// audio = new AudioTrackManager();
		// thread = new Thread(runable);
		// isPlaySound = true;
		// flag = true;
		/**
		 * starts a new thread where the volume index is set, then the
		 * AudioTrackManger's play() method is called which means a set of data
		 * will be pushed to audio hardware to play if AudioTrack isn't null. <br/>
		 * but at this moment, the AudioTrack hasn't been instantiated, so no
		 * data will be pushed.
		 */
		// thread.start();

		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.e("ACT", "test value: " + clsOscilloscope.getValue());
			}
		});

	}

	protected void onDestroy() {
		super.onDestroy();
		// android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * 按键事件处理
	 * 
	 * @author GV
	 * 
	 */
	class ClickEvent implements View.OnClickListener {

		public void onClick(View v) {
			if (v == btnStart) {
				clsOscilloscope
						.Start(audioRecord, recBufSize * 10, sfv, mPaint);

				// audio.start(440);
				/**
				 * turns on an audioTrackManager object, obtain an instance of
				 * AudioTrack, ��������PCM��Ƶ? ���ҵ���AudioTrack play() then starts
				 * playing the AudioTrack;
				 */
				// audio.start(10000);
				// flag = true;

			} else if (v == btnExit) {
				clsOscilloscope.Stop();
				// flag = false;
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == event.KEYCODE_BACK) {

			AlertDialog.Builder normalDia = new AlertDialog.Builder(this);
			// normalDia.setIcon(R.drawable.ic_launcher);
			normalDia.setTitle("退出程序");
			normalDia.setMessage("是否退出程序");

			normalDia.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							isPlaySound = false;
							flag = false;
							clsOscilloscope.Stop();
							// exit();
						}
					});
			// normalDia.setNegativeButton("ȡ��",
			// new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog, int which) {
			// // TODO Auto-generated method stub
			// }
			// });
			// normalDia.create().show();
			return false;
		}
		return false;
	}

	public void exit() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
		System.exit(0);// �˳�����
	}

	/**
	 * 触摸屏动态设置波形图基线
	 * 
	 * @author GV
	 * 
	 */

	class TouchEvent implements OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {
			clsOscilloscope.baseLine = (int) event.getY();
			return true;
		}

	}

	// Runnable runable = new Runnable() {
	//
	// public void run() {
	// // TODO Auto-generated method stub
	// while (isPlaySound) {
	// while (flag) {
	// // audio.setChannel(3,50.0f);
	// // am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 50,
	// // 0);
	// am.setStreamVolume(AudioManager.STREAM_MUSIC,
	// am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
	// audio.play();
	// }
	// try {
	// Thread.sleep(1);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// };

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		flag = false;
		clsOscilloscope.run_record = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		flag = true;
		clsOscilloscope.run_record = true;
	}
	

}
