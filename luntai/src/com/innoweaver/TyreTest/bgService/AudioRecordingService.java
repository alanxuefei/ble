package com.innoweaver.TyreTest.bgService;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.innoweaver.TyreTest.audioUtil.AudioTestModel;

public class AudioRecordingService extends Service {
	static final String TAG = "service";

	AudioTestModel mModel;
	RecordHelper mHelper;

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "bind");
		return mHelper;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "service created");
		mModel = new AudioTestModel(this);
		mModel.start();
		mHelper = new RecordHelper();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "unbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		if (mModel != null) {
			mModel.turnOff();
		}
		Log.i(TAG, "destroy");
		super.onDestroy();
	}

	public class RecordHelper extends Binder {

		public long getTestValue() {
			long r = 0;
			if (mModel != null) {
				r = mModel.getTestValueByLong();
			}
			return r;
		}

		public void turnOff() {
			mModel.turnOff();
		}

		public void pause() {
			mModel.pause();
		}

		public void resume() {
			mModel.resume();
		}

	}
}
