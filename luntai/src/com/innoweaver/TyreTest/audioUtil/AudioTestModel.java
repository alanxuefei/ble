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

public class AudioTestModel {
	private final static String TAG = AudioTestModel.class.getSimpleName();

	private Context context;

	ClsOscilloscope clsOscilloscope = new ClsOscilloscope();

	static final int frequency = 8000;// �ֱ���
	static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;

	static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	// static final int audioEncoding = AudioFormat.ENCODING_PCM_8BIT;

	public SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	int recBufSize;// ¼����Сbuffer��С
	AudioRecord audioRecord;
	Paint mPaint;

	AudioTrackManager audio;
	boolean isPlaySound = true;
	boolean flag;
	Thread thread;
	AudioManager am;
	private WaveOutF woF;// 方波类

	private byte[] byteDate1;
	private int len1;
	
	public static final long INVALID_VALUE = -1;

	public AudioTestModel(Context context) {
		this.context = context;
		init();
	}

	private void init() {
		am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

		// 录音组件
		recBufSize = AudioRecord.getMinBufferSize(frequency,
				channelConfiguration, audioEncoding);
		Log.e("xiu", "recBufSize:" + recBufSize + "");
		/**
		 * ��ʼ��¼���Ķ���
		 */
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
				channelConfiguration, audioEncoding, recBufSize * 10);
		// 画板和画笔
		// sfv = (SurfaceView) this.findViewById(R.id.SurfaceView01);
		// sfv.setOnTouchListener(new TouchEvent());
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);// 画笔为绿色
		mPaint.setStrokeWidth(2);// 设置画笔粗细
		// 示波器类库
		// clsOscilloscope.initOscilloscope(sfv.getHeight() / 2);
		clsOscilloscope.initOscilloscope(10);
		
		byteDate1 = new byte[] { 0x11, 0x22, 0x42, 0x1A, 0x24, 0x13, 0x0a };
		len1 = byteDate1.length;
	}

	public void start() {
		// Log.e(TAG, "start");
		clsOscilloscope.Start(audioRecord, recBufSize * 10, null, mPaint);
		
		if (woF == null) {
			woF = new WaveOutF();
		}
		woF.sendByteDate(byteDate1, len1);
		flag = true;
	}

	public void pause() {
		flag = false;
		clsOscilloscope.run_record = false;
	}

	public void resume() {
		flag = true;
		clsOscilloscope.run_record = true;
	}

	public void turnOff() {
		isPlaySound = false;
		flag = false;
		// Log.e(TAG, "turn off");
		audioRecord.release();
		clsOscilloscope.Stop();
	}

	public String getTestValue() {
		// Log.e("model", "getValue");
		return clsOscilloscope.getValue();
	}

	public long getTestValueByLong() {
		long value = INVALID_VALUE;
		String r = getTestValue();
		if (r != null) {
			value = Long.valueOf(r);
		}

		return value;
	}

}
