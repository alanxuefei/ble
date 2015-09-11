package com.innoweaver.TyreTest;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor.TURQUOISE;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.innoweaver.TyreTest.audioUtil.AudioTestModel;
import com.innoweaver.TyreTest.bgService.AudioRecordingService;
import com.innoweaver.TyreTest.bgService.AudioRecordingService.RecordHelper;
import com.innoweaver.TyreTest.util.HeadSetMonitor;
import com.innoweaver.TyreTest.util.HeadsetStatusReceiver;

public class Launcher extends Activity implements HeadSetMonitor {
	private Context context;
	private TextSwitcher notice;
	private HeadsetStatusReceiver receiver;

	public static final int LAUNCHER_REQUEST = 700;

	private ImageView[] imgs;
	private Animation[] anims;
	private Animation[] animRs;
	private int[] counts;

	private AnimHandler mHandler = new AnimHandler();
	private long animDuration = 300;
	private long animInterval = 200;
	private long rythm = 4000;
	private boolean off = false;

	// AudioTestModel model;

	List<Long> constants = new ArrayList<Long>();

	// Animation anim;
	// Animation animR;
	private AudioConnection mConn;
	private AudioRecordingService.RecordHelper mHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tyre_laucher);

		context = getApplicationContext();
		setupTextSwither();

		checkIfHeadsetPlugged();

		// int[] imgIds = { R.id.img0 };
		setupAnimation();
	}

	@Override
	protected void onResume() {
		startLoadingAnimation();
		if (mHelper != null) {
			mHelper.resume();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (mHelper != null) {
			mHelper.pause();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		// if (model != null) {
		// model.turnOff();
		// }
		if (mConn != null) {
			// unbindService(mConn);
		}
		mHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	private void setupAnimation() {
		int[] imgIds = { R.id.img0, R.id.img1, R.id.img2, R.id.img3, R.id.img4 };
		imgs = new ImageView[imgIds.length];

		// anim = AnimationUtils.loadAnimation(this, R.anim.sparkle_tyre2);
		// animR = AnimationUtils.loadAnimation(this, R.anim.sparkle_tyre_r);
		anims = new AnimationSet[imgIds.length];
		animRs = new AnimationSet[imgIds.length];

		counts = new int[imgIds.length];

		for (int i = 0; i < imgIds.length; i++) {
			imgs[i] = (ImageView) findViewById(imgIds[i]);
			anims[i] = AnimationUtils.loadAnimation(this, R.anim.sparkle_tyre2);
			// animRs[i] = AnimationUtils.loadAnimation(this, R.anim.sparkle_r);
			animRs[i] = AnimationUtils.loadAnimation(this,
					R.anim.sparkle_tyre_r);

		}
	}

	private void setupTextSwither() {
		notice = (TextSwitcher) findViewById(R.id.notice_txt);
		notice.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				TextView txt = (TextView) getLayoutInflater().inflate(
						R.layout.waiting_msg_txt, notice, false);

				return txt;
			}
		});
		notice.setCurrentText(getString(R.string.connect_headset_notice));
	}

	private void startLoadingAnimation() {
		for (int i = 0; i < imgs.length; i++) {
			final int index = i;
			// anim.setDuration(duration * (i + 1));
			final ImageView img = imgs[i];
			final Animation animR = animRs[i];

			anims[i].setAnimationListener(new RepeatAnimationModel(img, animR));

			animRs[i]
					.setAnimationListener(new RepeatAnimationModel(img, animR));

			Message msg = Message.obtain();
			msg.arg1 = index;
			mHandler.sendMessageDelayed(msg, 600 * i);

		}
	}

	@Override
	public void onHeadSetPluggedin() {
		((TyreApp) context).stopAnimation(notice);
		notice.setText(getString(R.string.waiting_msg));

		Intent service = new Intent(this, AudioRecordingService.class);
		mConn = new AudioConnection();
		bindService(service, mConn, Service.BIND_AUTO_CREATE);
		// if (model != null && !off) {
		// model.turnOff();
		// }
		// model = new AudioTestModel(this);
		// model.start();

		Log.i("0000000000000000000000000000000000000", "检测到耳机插入时间");

		mHandler.postDelayed(new MeasureRunnable(), 2000);
	}

	private void checkIfHeadsetPlugged() {
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		if (!am.isWiredHeadsetOn()) {
			((TyreApp) context).startAnimation(notice);
			receiver = new HeadsetStatusReceiver();
			receiver.setMonitor(this);
			registerReceiver(receiver, new IntentFilter(
					Intent.ACTION_HEADSET_PLUG));
		} else {
			onHeadSetPluggedin();
		}
	}

	private class RepeatAnimationModel implements AnimationListener {
		View target;
		Animation anim;

		public RepeatAnimationModel(View target, Animation anim) {
			this.target = target;
			this.anim = anim;
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			target.startAnimation(anim);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}

	private class AnimHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			int index = msg.arg1;
			imgs[index].startAnimation(anims[index]);

		}
	}

	private class MeasureRunnable implements Runnable {
		@Override
		public void run() {
			// Log.e("launcher", "RESULT: " + model.getTestValue());
			// // TODO try & catch
			long value = mHelper.getTestValue();
			if (value != AudioTestModel.INVALID_VALUE) {
				constants.add(value);
			}
			if (constants.size() < 2) {
				mHandler.postDelayed(new MeasureRunnable(), 2000);
			} else {
				// if (model != null) {
				// model.turnOff();
				// off = true;
				// }
				mHandler.removeCallbacksAndMessages(null);
				Intent intent = new Intent();

				Log.i("信号一", constants.get(0) + "");
				Log.i("信号二", constants.get(1) + "");

				intent.putExtra(TyreMainActivity.EXTRA_VALUE_B,
						constants.get(0));
				intent.putExtra(TyreMainActivity.EXTRA_VALUE_C,
						constants.get(1));
				intent.setAction(getString(R.string.tyre_main_action));

				Log.i("0000000000000000000000000000000000000", "初始化完毕时间");

				// startActivityForResult(intent, LAUNCHER_REQUEST);
				unbindService(mConn);
				Launcher.this.startActivity(intent);
				Launcher.this.finish();
			}
		}
	}

	private class AudioConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mHelper = (RecordHelper) service;
			// mHandler.post(new MeasureRunnable());
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

	}

}
