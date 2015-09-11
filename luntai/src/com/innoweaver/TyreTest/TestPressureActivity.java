package com.innoweaver.TyreTest;

import java.text.DecimalFormat;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.innoweaver.TyreTest.audioUtil.AudioTestModel;
import com.innoweaver.TyreTest.bgService.AudioRecordingService;
import com.innoweaver.TyreTest.bgService.AudioRecordingService.RecordHelper;
import com.innoweaver.TyreTest.entity.CarEntity;
import com.innoweaver.TyreTest.service.PressureService;
import com.innoweaver.TyreTest.util.UnitTransferUtil;
import com.innoweaver.TyreTest.view.MeterView;

public class TestPressureActivity extends BasicActivity {
	private final static String TAG = TestPressureActivity.class.getSimpleName();
	private final static String UNIT = PressureService.UNIT_BAR;
	// private TyreApp context;

	private MeterView gauge;
	private TextView resultTag;
	private Button testBtn, cmfBtn;

	private long value = 0;
	private float stdPressure;
	private long valueB;
	private long valueC;
	private float testResult;
	// private AudioTestModel mModel;

	private final int maxCount = 3;
	private int count;

	private Handler mHandler;
	private AudioConnection mConn;
	private AudioRecordingService.RecordHelper mHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.test_pressure);
		/**
		 * by kid
		 */
		// context = (TyreApp) getApplicationContext();
		inializeData();

		gauge = (MeterView) findViewById(R.id.gauge);
		// gauge.setMeter(60);
		gauge.setStdPressure(stdPressure);

		setupBtn();

		Intent service = new Intent(this, AudioRecordingService.class);
		bindService(service, mConn = new AudioConnection(), Service.BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mHelper != null) {
			mHelper.resume();
		}
		// startCheck();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mHelper != null) {
			mHelper.pause();
		}
		// mHandler.removeCallbacksAndMessages(null);
		// if (mModel != null) {
		// mModel.turnOff();
		// }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
		unbindService(mConn);
	}

	private void inializeData() {
		// mHandler = new Handler();

		Intent intent = getIntent();
		valueB = intent.getLongExtra(TyreMainActivity.EXTRA_VALUE_B, 0);
		valueC = intent.getLongExtra(TyreMainActivity.EXTRA_VALUE_C, 0);
		Log.e(TAG, "valueB: " + valueB + " valueC: " + valueC);

		CarEntity car = intent.getParcelableExtra(TyreMainActivity.EXTRA_CAR_INDEX);
		int tyreIndex = intent.getIntExtra(TyreMainActivity.EXTRA_TYRE_INDEX, 0);
		// Log.e(TAG, "tyreIndex: " + tyreIndex);
		if (car != null) {
			switch (tyreIndex) {
			case 0:
			case 1:
				// stdPressure = 0;
				stdPressure = car.getfStdPressure();
				break;
			case 2:
			case 3:
				// stdPressure = 7;
				stdPressure = car.getrStdPressure();
				break;
			default:
				break;
			}
		}
	}

	private void setupBtn() {
		testBtn = (Button) findViewById(R.id.test_btn);
		cmfBtn = (Button) findViewById(R.id.confirm_btn);
		testBtn.setOnClickListener(testListener);
		cmfBtn.setOnClickListener(cmfListener);
		resultTag = (TextView) findViewById(R.id.result_text);
	}

	// private void startCheck() {
	// mModel = new AudioTestModel(this);
	// mModel.start();
	// }

	private void setMeasureResult(float value) {
		Log.e(TAG, "value: " + value);
		// value = 5.0f;
		gauge.setMeter(value);
		resultTag.setText(getString(R.string.presure_tag) + " " + new DecimalFormat("0.0").format(value) + " " + UNIT);

		testResult = value;

		cmfBtn.setVisibility(View.VISIBLE);
		testBtn.setText(getString(R.string.re_test));
	}

	private class MeausureTask implements Runnable {
		@Override
		public void run() {
			value = mHelper.getTestValue();

			if (value == AudioTestModel.INVALID_VALUE) {
				if (count == maxCount) {
					Toast.makeText(TestPressureActivity.this, getString(R.string.time_out_notice), Toast.LENGTH_LONG).show();
					return;
				}
				count++;
				mHandler.postDelayed(new MeausureTask(), 1500);
			} else {
				float r = UnitTransferUtil.fromSignalToPressure(TestPressureActivity.this, value, valueB, valueC);
				Log.i(TAG, "TEST");
				setMeasureResult(r);
			}
		}
	}

	private class AudioConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mHandler = new Handler();
			mHelper = (RecordHelper) service;
			mHelper.resume();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	}

	private OnClickListener testListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mHandler.post(new MeausureTask());
		}
	};

	private OnClickListener cmfListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = getIntent();
			intent.putExtra(TyreMainActivity.PRESSURE_VALUE_INDEX, testResult);
			setResult(RESULT_OK, intent);
			finish();
		}
	};

}
